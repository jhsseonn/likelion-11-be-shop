package com.likelion.beshop.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

public interface WebMvcConfigurer {


    void addResourceHandlers(ResourceHandlerRegistry registry);
}
