package com.grofers.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartHandlingException extends RuntimeException {

	public CartHandlingException(String message) {
		super(message);
		
	}
	
	
}
