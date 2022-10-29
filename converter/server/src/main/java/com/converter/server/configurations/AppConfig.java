package com.converter.server.configurations;

import com.converter.server.interceptors.SpotifyTokenInterceptor;
import com.converter.server.services.ClientIDService;
import com.converter.server.services.SpotifyTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    SpotifyTokenService tokenService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SpotifyTokenInterceptor(tokenService)).addPathPatterns("/spotify/**");
    }
}
