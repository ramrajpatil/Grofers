package com.grofers.securitys;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
// This class will get called when any API request is hit.
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTTokenHelper jwtTokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1: Get token
		String requestToken = request.getHeader("Authorization");
		// Bearer ufucuwy8237yr
		
		System.out.println(requestToken);// to check the token
		String username = null;

		String token = null;
		if (requestToken != null && requestToken.startsWith("Bearer")) {
			// Main token
			token = requestToken.substring(7);
			try {

				username = this.jwtTokenHelper.getUsernameFromToken(token);
				
				System.out.println("In JWTAuthenticationFilter line no 50... "+username);
				
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get Jwt token " + e.getMessage());

			} catch (ExpiredJwtException e) {
				System.out.println("Jwt token has exprired " + e.getMessage());
			} catch (MalformedJwtException e) {
				System.out.println("Invalid Jwt " + e.getMessage());
			}

		} else {
			System.out.println("Jwt token does not begin with Bearer");
		}

		// Once we get the token, validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			
			if (this.jwtTokenHelper.validateToken(token, userDetails)) {

				// All going well, and authenticate it.

				UsernamePasswordAuthenticationToken authToken = 
						new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());

				
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);

			} else
				System.out.println("invalid jwt token.");

		} else {

			System.out.println("Username is null or context is not null.");
		}

		filterChain.doFilter(request, response);

	}

}
