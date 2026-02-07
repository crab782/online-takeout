package com.test.takeout.controller;

import com.test.takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 通用控制器，处理文件下载等通用功能
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    /**
     * 文件下载预览
     * @param name 文件名
     * @param response HttpServletResponse
     */
    @GetMapping("/download")
    public void download(@RequestParam("name") String name, HttpServletResponse response) {
        log.info("文件下载预览：name={}", name);

        // 这里需要根据实际情况修改文件存储路径
        // 示例路径：D:/upload/
        String filePath = "D:/upload/" + name;

        try {
            // 设置响应头
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(name, "UTF-8"));
            response.setContentType("application/octet-stream");

            // 读取文件并写入响应流
            FileInputStream fis = new FileInputStream(filePath);
            OutputStream os = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

            // 关闭流
            os.close();
            fis.close();
        } catch (FileNotFoundException e) {
            log.error("文件不存在：{}", filePath, e);
            // 如果文件不存在，可以返回一个错误信息
            try {
                response.setContentType("application/json; charset=UTF-8");
                OutputStream os = response.getOutputStream();
                os.write("{\"code\": 0, \"msg\": \"文件不存在\", \"data\": null}".getBytes("UTF-8"));
                os.close();
            } catch (IOException ex) {
                log.error("返回错误信息失败", ex);
            }
        } catch (IOException e) {
            log.error("文件下载失败", e);
            // 如果下载失败，可以返回一个错误信息
            try {
                response.setContentType("application/json; charset=UTF-8");
                OutputStream os = response.getOutputStream();
                os.write("{\"code\": 0, \"msg\": \"文件下载失败\", \"data\": null}".getBytes("UTF-8"));
                os.close();
            } catch (IOException ex) {
                log.error("返回错误信息失败", ex);
            }
        }
    }

}