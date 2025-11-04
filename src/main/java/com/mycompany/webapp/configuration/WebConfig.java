package com.mycompany.webapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/*
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // ✅ Giữ ảnh trong resources/static/images/banner/
        registry.addResourceHandler("/images/banner/**")
                .addResourceLocations("classpath:/static/images/banner/");

        // ✅ Ảnh trong thư mục ngoài D:/imgProjectWeb/
        registry.addResourceHandler("/imagesFolder/**")
                .addResourceLocations("file:/D:/imgProjectWeb/");

    }
}
*/