package com.grofers.securitys;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, 
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Authentication failed. Please provide valid credentials.\", \"success\": false}");
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("http://localhost:8888");
        super.afterPropertiesSet();
    }
}
