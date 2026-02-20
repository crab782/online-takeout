package com.test.takeout.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.entity.Orders;
import com.test.takeout.service.OrdersService;
import com.test.takeout.service.OrderDetailService;
import com.test.takeout.entity.OrderDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 订单WebSocket处理器
 * 用于实时推送新订单和订单状态更新
 */
@Slf4j
@Component
public class OrderWebSocketHandler extends TextWebSocketHandler {

    @Resource
    private OrdersService ordersService;

    @Resource
    private OrderDetailService orderDetailService;

    // 存储店铺ID与WebSocket会话的映射，一个店铺可能有多个客户端连接
    private final Map<Long, List<WebSocketSession>> sessionMap = new ConcurrentHashMap<>();

    // 存储上次检查订单的时间，用于增量更新
    private final Map<Long, LocalDateTime> lastCheckTimeMap = new ConcurrentHashMap<>();

    // 定时任务线程池，用于定期检查新订单
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * 构造方法，初始化定时任务
     */
    public OrderWebSocketHandler() {
        // 定时检查新订单，每2秒执行一次
        executorService.scheduleAtFixedRate(this::checkNewOrders, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * 连接建立时调用
     * @param session WebSocket会话
     * @throws Exception 异常
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket连接建立: {}", session.getId());

        // 从会话参数中获取店铺ID
        Long storeId = getStoreIdFromSession(session);
        if (storeId != null) {
            // 将会话添加到对应店铺的会话列表中
            sessionMap.computeIfAbsent(storeId, k -> Collections.synchronizedList(new ArrayList<>())).add(session);
            // 初始化上次检查时间为当前时间
            lastCheckTimeMap.putIfAbsent(storeId, LocalDateTime.now());
            log.info("店铺 {} 建立WebSocket连接，当前连接数: {}", storeId, sessionMap.get(storeId).size());
        } else {
            log.error("WebSocket连接失败：缺少店铺ID参数");
            session.close(CloseStatus.BAD_DATA);
        }
    }

    /**
     * 连接关闭时调用
     * @param session WebSocket会话
     * @param status 关闭状态
     * @throws Exception 异常
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket连接关闭: {}, 状态: {}", session.getId(), status);

        // 从会话参数中获取店铺ID
        Long storeId = getStoreIdFromSession(session);
        if (storeId != null && sessionMap.containsKey(storeId)) {
            // 从店铺的会话列表中移除当前会话
            sessionMap.get(storeId).remove(session);
            // 如果店铺没有连接了，清理相关数据
            if (sessionMap.get(storeId).isEmpty()) {
                sessionMap.remove(storeId);
                lastCheckTimeMap.remove(storeId);
            }
            log.info("店铺 {} 关闭WebSocket连接，当前连接数: {}", storeId, sessionMap.getOrDefault(storeId, Collections.emptyList()).size());
        }
    }

    /**
     * 处理文本消息
     * @param session WebSocket会话
     * @param message 文本消息
     * @throws Exception 异常
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("收到WebSocket消息: {}", message.getPayload());
        // 这里可以处理客户端发送的消息，例如心跳检测等
        session.sendMessage(new TextMessage("pong"));
    }

    /**
     * 处理传输错误
     * @param session WebSocket会话
     * @param exception 异常
     * @throws Exception 异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误: {}", exception.getMessage(), exception);
        // 传输错误时关闭连接
        session.close(CloseStatus.SERVER_ERROR);
    }

    /**
     * 检查新订单并推送到客户端
     */
    private void checkNewOrders() {
        try {
            // 遍历所有有连接的店铺
            for (Map.Entry<Long, List<WebSocketSession>> entry : sessionMap.entrySet()) {
                Long storeId = entry.getKey();
                List<WebSocketSession> sessions = entry.getValue();

                if (sessions.isEmpty()) {
                    continue;
                }

                // 获取上次检查时间
                LocalDateTime lastCheckTime = lastCheckTimeMap.getOrDefault(storeId, LocalDateTime.now().minusMinutes(1));
                LocalDateTime currentTime = LocalDateTime.now();

                // 查询该店铺在最近时间内的待处理订单（状态为1-4）
                LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Orders::getStoreId, storeId);
                queryWrapper.ge(Orders::getCreateTime, lastCheckTime);
                queryWrapper.ge(Orders::getStatus, 1); // 商家已接单状态（1-4）
                queryWrapper.lt(Orders::getStatus, 5);
                queryWrapper.orderByDesc(Orders::getCreateTime);

                List<Orders> newOrders = ordersService.list(queryWrapper);

                if (!newOrders.isEmpty()) {
                    log.info("店铺 {} 发现 {} 个新订单", storeId, newOrders.size());

                    // 为每个订单加载详情
                    for (Orders order : newOrders) {
                        LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
                        detailQueryWrapper.eq(OrderDetail::getOrderId, order.getId());
                        List<OrderDetail> orderDetails = orderDetailService.list(detailQueryWrapper);
                        order.setOrderDetails(orderDetails);
                    }

                    // 构建消息
                    Map<String, Object> messageMap = new HashMap<>();
                    messageMap.put("type", "new_orders");
                    messageMap.put("orders", newOrders);
                    messageMap.put("timestamp", System.currentTimeMillis());

                    String messageJson = com.alibaba.fastjson.JSON.toJSONString(messageMap);
                    TextMessage message = new TextMessage(messageJson);

                    // 发送消息到所有会话
                    for (WebSocketSession session : sessions) {
                        if (session.isOpen()) {
                            try {
                                session.sendMessage(message);
                                log.info("向店铺 {} 的客户端 {} 推送新订单消息", storeId, session.getId());
                            } catch (IOException e) {
                                log.error("向店铺 {} 的客户端 {} 推送消息失败: {}", storeId, session.getId(), e.getMessage());
                                // 推送失败时关闭连接
                                session.close(CloseStatus.SERVER_ERROR);
                            }
                        }
                    }
                }

                // 更新上次检查时间
                lastCheckTimeMap.put(storeId, currentTime);
            }
        } catch (Exception e) {
            log.error("检查新订单失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 从WebSocket会话中获取店铺ID
     * @param session WebSocket会话
     * @return 店铺ID
     */
    private Long getStoreIdFromSession(WebSocketSession session) {
        try {
            // 从会话的URI参数中获取storeId
            String queryString = session.getUri().getQuery();
            if (queryString != null) {
                String[] params = queryString.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && "storeId".equals(keyValue[0])) {
                        return Long.parseLong(keyValue[1]);
                    }
                }
            }
        } catch (Exception e) {
            log.error("从会话中获取店铺ID失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 推送消息到指定店铺的所有客户端
     * @param storeId 店铺ID
     * @param message 消息内容
     */
    public void pushMessageToStore(Long storeId, String message) {
        List<WebSocketSession> sessions = sessionMap.get(storeId);
        if (sessions != null && !sessions.isEmpty()) {
            TextMessage textMessage = new TextMessage(message);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(textMessage);
                    } catch (IOException e) {
                        log.error("推送消息失败: {}", e.getMessage());
                    }
                }
            }
        }
    }
}
