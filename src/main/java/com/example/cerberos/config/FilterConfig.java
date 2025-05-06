package com.example.cerberos.config;

import com.example.cerberos.config.filter.CookieFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CookieFilter> cookieFilter() {
        FilterRegistrationBean<CookieFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CookieFilter());
        registrationBean.addUrlPatterns("/graphql"); // Asegúrate de que coincida con tus rutas de GraphQL
        return registrationBean;
    }
}