package com.grofers.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 209217327957540032L;

	public NotFoundException(String message) {
		super(message);
		
	}
	
	
}
