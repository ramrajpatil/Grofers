package com.grofers.glo_exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.grofers.dtos.ResponseDTO;
import com.grofers.exceptions.NotFoundException;
import com.grofers.exceptions.UserHandlingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ResponseDTO> handleResourceNotFoundExceptionHandler(NotFoundException ex) {

		String message = ex.getMessage();

		ResponseDTO response = new ResponseDTO(message, false);

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserHandlingException.class)
	public ResponseEntity<ResponseDTO> handleUserHandlingException(UserHandlingException ex) {

		String message = ex.getMessage();

		ResponseDTO response = new ResponseDTO(message, false);

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ResponseDTO> handleBadCredentialsException(BadCredentialsException ex) {

		String message = ex.getMessage();

		ResponseDTO response = new ResponseDTO(message, false);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	
}
