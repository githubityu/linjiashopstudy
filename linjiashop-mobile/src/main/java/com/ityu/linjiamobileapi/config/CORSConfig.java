package com.ityu.linjiamobileapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.web.cors.CorsConfiguration.ALL;

/**
 * CORS configuration
 */
@Configuration
public class CORSConfig {
    @Autowired
    FileConfig fileConfig;
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(ALL)
                        .allowedMethods(ALL)
                        .allowedHeaders(ALL)
                        .allowCredentials(true);
            }
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler(fileConfig.getStaticAccessPath()).addResourceLocations("file:" + fileConfig.getUploadFolder());
            }
        };
    }

}