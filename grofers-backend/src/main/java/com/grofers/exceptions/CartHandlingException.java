package com.grofers.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartHandlingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5253869428911564974L;

	public CartHandlingException(String message) {
		super(message);
		
	}
	
	
}
