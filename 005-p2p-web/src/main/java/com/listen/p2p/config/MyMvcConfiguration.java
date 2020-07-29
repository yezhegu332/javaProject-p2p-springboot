package com.listen.p2p.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Listen
 * @Date: 2020/7/24
 */
@Configuration
public class MyMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/loan/page/register").setViewName("register");
        registry.addViewController("/loan/page/realName").setViewName("realName");
    }
}
