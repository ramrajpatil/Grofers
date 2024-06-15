package com.grofers.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ResponseDTO {

	private String message;
	private boolean success;
	public ResponseDTO(String message, boolean success) {
		super();
		this.message = message;
		this.success = success;
	}
	
}
