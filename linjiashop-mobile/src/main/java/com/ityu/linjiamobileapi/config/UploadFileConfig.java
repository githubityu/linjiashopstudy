package com.ityu.linjiamobileapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
public class UploadFileConfig {

    @Autowired
    FileConfig config;

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(config.getUploadFolder());
        //文件最大
        factory.setMaxFileSize(DataSize.ofMegabytes(config.getMin()));
        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(config.getMin()));
        return factory.createMultipartConfig();
    }
}
