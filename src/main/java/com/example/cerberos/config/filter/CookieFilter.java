package com.example.cerberos.config.filter;

import com.example.cerberos.util.TokenHolder;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

public class CookieFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    
        HttpServletResponse httpResponse = (HttpServletResponse) response;
    
        chain.doFilter(request, response);

        String token = TokenHolder.getToken();
        System.out.println("Token obtenido en CookieFilter: " + token);
    
        if (token != null) {
            Cookie cookie = new Cookie("ticket", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
    
            httpResponse.addCookie(cookie);
            TokenHolder.clear();
            System.out.println("Cookie configurada en CookieFilter: " + cookie.getValue());
        } else {
            System.out.println("No se encontró token en TokenHolder");
        }
    }

    @Override
    public void destroy() {
    }
}