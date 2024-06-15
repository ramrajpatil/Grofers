package com.grofers.securitys;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


//@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

	
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		// Set the message to send user when he's accessing some api that he doesn't have permission to.
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access denied !!");

	}

}
