package com.londonhydro.config;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		if(authException instanceof BadCredentialsException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
		} else if(request.getAttribute("TOKEN_EXPIRED") != null) {
			RequestDispatcher requestDispatcher = request.getServletContext().getRequestDispatcher("/login");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,request.getAttribute("TOKEN_EXPIRED").toString());
			requestDispatcher.forward(request, response);
			
		}else if(authException instanceof InsufficientAuthenticationException) {
			RequestDispatcher requestDispatcher = request.getServletContext().getRequestDispatcher("/error");
			requestDispatcher.forward(request, response);
		}
		
	}

}
