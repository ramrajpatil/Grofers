package com.grofers.glo_exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.grofers.dtos.ResponseDTO;
import com.grofers.exceptions.DuplicateEntryException;
import com.grofers.exceptions.CartHandlingException;
import com.grofers.exceptions.NotFoundException;
import com.grofers.exceptions.UserHandlingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ResponseDTO> handleNotFoundExceptionHandler(NotFoundException ex) {

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
	
	@ExceptionHandler(CartHandlingException.class)
	public ResponseEntity<ResponseDTO> handleEmptyCartException(CartHandlingException ex) {
		
		String message = ex.getMessage();
		
		ResponseDTO response = new ResponseDTO(message, false);
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
	
	@ExceptionHandler(DuplicateEntryException.class)
	public ResponseEntity<ResponseDTO> handleDuplicateCategoryNameException(DuplicateEntryException ex) {
		
		String message = ex.getMessage();
		
		ResponseDTO response = new ResponseDTO(message, false);
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	// To catch all forbidden exceptions
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseDTO> handleAccessDeniedException(AccessDeniedException ex) {
		
		String message = "You are not authorized for access.";
		
		ResponseDTO response = new ResponseDTO(message, false);
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseDTO> handleAuthenticationException(AuthenticationException ex) {
        String message = "Authentication failed. Please provide valid credentials.";
        ResponseDTO response = new ResponseDTO(message, false);
        

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
	
	// To catch all other exceptions
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<ResponseDTO> handleException(Exception ex) {
//
//		String message = ex.getMessage();
//
//		ResponseDTO response = new ResponseDTO(message, false);
//
//		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	
}
