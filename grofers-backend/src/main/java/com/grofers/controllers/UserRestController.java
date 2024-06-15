package com.grofers.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grofers.dtos.ResponseDTO;
import com.grofers.dtos.UserDto;
import com.grofers.services.IUserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserRestController {

	
	private Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	private IUserService uService;
	
	// GET - getting all users
	@GetMapping("/")
	public ResponseEntity<?> getAllUsers(){
		this.logger.info("In get all Users request.");
		return ResponseEntity.ok(this.uService.fetchAllUsers());
	}
	
	// GET - get single user
	@GetMapping("/{userId}")
	public ResponseEntity<?> getSingleUser(@PathVariable Integer userId){
		return ResponseEntity.ok(this.uService.fetchSingleUser(userId));
		
	}
	
	// POST - create user
	@PostMapping("/")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto){
		UserDto createdUserDto = this.uService.addNewUser(userDto);
		
		return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
	}
	
	// PUT- update user
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUserDetails(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto) {
		this.logger.info("in updateUserDetails() of "+userId);
		this.logger.info("Name",userDto.getName());
		this.logger.info("Email",userDto.getEmail());
		this.logger.info("Password",userDto.getPassword());
		return new ResponseEntity<>(this.uService.updateUser(userDto, userId), HttpStatus.OK);
	}
	
	// ADMIN
	// DELETE - delete user
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUserDetails(@PathVariable Integer userId){
		this.uService.deleteUser(userId);
//		return new ResponseEntity<>(Map.of("message","User deleted successfully"), HttpStatus.OK);
		return new ResponseEntity<>(new ResponseDTO("User deleted successfully", true), HttpStatus.OK);
	}
	
}
