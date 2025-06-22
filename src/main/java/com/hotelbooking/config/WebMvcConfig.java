package com.hotelbooking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/product-image/**")
                .addResourceLocations("file:product-image/");

        registry.addResourceHandler("/profile-image/**")
                .addResourceLocations("file:profile-image/");

    }
}
