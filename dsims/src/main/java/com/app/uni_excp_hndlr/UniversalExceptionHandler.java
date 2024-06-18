package com.app.uni_excp_hndlr;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.app.dto.CustomResponse;

@ControllerAdvice
public class UniversalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> list = ex.getFieldErrors();
		Map<String, String> map = list.stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
		return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleRTExcp(RuntimeException e) {
//		System.out.println("Inside Custome RTExcp method!");
//		e.printStackTrace();
		return new ResponseEntity<>(new CustomResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
