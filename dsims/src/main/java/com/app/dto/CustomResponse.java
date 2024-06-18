package com.app.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
public class CustomResponse {
	private String msg;
	private LocalDateTime stamp;

	public CustomResponse(String msg) {
		this.msg = msg;
		this.stamp = LocalDateTime.now();
	}
}
