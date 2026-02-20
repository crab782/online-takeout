package com.test.takeout;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoginTest {
    public static void main(String[] args) {
        try {
            // 创建URL对象
            URL url = new URL("http://localhost:8081/employee/login");
            
            // 打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // 设置请求方法为POST
            conn.setRequestMethod("POST");
            
            // 设置请求头
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            // 构建请求体
            String requestBody = "{\"username\": \"shop_admin2\", \"password\": \"123\"}";
            
            // 写入请求体
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // 获取响应状态码
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            
            // 读取响应
            try (Scanner scanner = new Scanner(conn.getInputStream(), "utf-8")) {
                String response = scanner.useDelimiter("\\A").next();
                System.out.println("Response: " + response);
            }
            
            // 关闭连接
            conn.disconnect();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}