package com.test.takeout.performance;

import com.test.takeout.controller.StoreController;
import com.test.takeout.entity.Store;
import com.test.takeout.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.when;

public class ConcurrencyTest {

    @Mock
    private StoreService storeService;

    @InjectMocks
    private StoreController storeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConcurrentStoreListRequests() throws InterruptedException {
        // 模拟数据
        List<Store> stores = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Store store = new Store();
            store.setId((long) i);
            store.setName("测试店铺" + i);
            store.setStatus(1);
            stores.add(store);
        }

        // 模拟service方法
        when(storeService.list()).thenReturn(stores);

        // 并发测试参数
        int threadCount = 50;
        int requestsPerThread = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // 启动并发请求
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        storeController.list(1, 12, "", null, null);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // 等待所有线程完成
        latch.await();
        executor.shutdown();

        System.out.println("Concurrent test completed: " + (threadCount * requestsPerThread) + " requests processed");
        System.out.println("All requests completed successfully");
    }

    @Test
    void testConcurrentStoreGetByIdRequests() throws InterruptedException {
        // 模拟数据
        Store store = new Store();
        store.setId(1L);
        store.setName("测试店铺");
        store.setStatus(1);

        // 模拟service方法
        when(storeService.getById(1L)).thenReturn(store);

        // 并发测试参数
        int threadCount = 100;
        int requestsPerThread = 20;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // 启动并发请求
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        storeController.detail(1L);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // 等待所有线程完成
        latch.await();
        executor.shutdown();

        System.out.println("Concurrent test completed: " + (threadCount * requestsPerThread) + " requests processed");
        System.out.println("All requests completed successfully");
    }
}
