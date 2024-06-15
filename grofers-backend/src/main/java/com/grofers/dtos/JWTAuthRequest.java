package com.grofers.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JWTAuthRequest {

	@NotEmpty(message = "username must not be empty.")
	@Email
	private String username;
	
	@Size(min = 3, max = 20, message = "password must be between 3 to 20 characters.")
	private String password;
}
