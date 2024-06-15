package com.grofers.dtos;

import lombok.Data;

@Data
public class JWTAuthRequest {

	private String username;
	
	private String password;
}
