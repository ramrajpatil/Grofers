package com.grofers.dtos;

import com.grofers.pojos.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTAuthResponse {

	private String token;
	
	private User user;
	
}
