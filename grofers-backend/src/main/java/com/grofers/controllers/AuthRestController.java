package com.grofers.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grofers.dtos.JWTAuthRequest;
import com.grofers.dtos.JWTAuthResponse;
import com.grofers.securitys.JWTTokenHelper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthRestController {

	@Autowired
	private JWTTokenHelper helper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/login")
	public ResponseEntity<JWTAuthResponse> createToken(@Valid @RequestBody JWTAuthRequest request) {
		logger.info("In create token / login");

		this.autheticate(request.getUsername(), request.getPassword());

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

		String token = this.helper.generateToken(userDetails.getUsername());

		JWTAuthResponse response = new JWTAuthResponse();
		response.setToken(token);
		
		logger.info("In JWTAuthResponse: "+response.getToken());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void autheticate(String username, String password) {
		logger.info("Username from authenticate() : " + username );

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		
		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (Exception e) {
			System.out.println(
					"In error of authenticate() of " + getClass().getName() + " error message : " + e.getMessage());
			throw new BadCredentialsException("Invalid username or password");
		}
	}


}
