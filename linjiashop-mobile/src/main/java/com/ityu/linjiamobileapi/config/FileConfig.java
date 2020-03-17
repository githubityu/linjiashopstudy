package com.ityu.linjiamobileapi.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//读取classpath配置信息
@Data
@Component
@PropertySource(value = {"classpath:config.yml"},encoding = "utf-8")
//@PropertySource(value ={"file:${user.dir}/config/config.yml"}, ignoreResourceNotFound = true)
public class FileConfig {
    @Value("${staticAccessPath}")
    private String staticAccessPath;
    @Value("${uploadFolder}")
    private String uploadFolder;
    @Value("${min}")
    private int min;
    @Value("${max}")
    private int max;

    @Override
    public String toString() {
        return "FileConfig{" +
                "staticAccessPath='" + staticAccessPath + '\'' +
                ", uploadFolder='" + uploadFolder + '\'' +
                ", min='" + min + '\'' +
                ", max='" + max + '\'' +
                '}';
    }
}
