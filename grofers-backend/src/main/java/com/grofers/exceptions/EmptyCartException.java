package com.grofers.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmptyCartException extends RuntimeException {

	public EmptyCartException(String message) {
		super(message);
		
	}
	
	
}
