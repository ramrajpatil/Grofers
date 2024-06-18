package com.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.pojos.User;
import com.app.service.UserService;
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> registerNewUser( @Valid @RequestBody User user) {
		return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
	}

	@PostMapping("/registerall")
	public ResponseEntity<?> registerAllUsers(@Valid @RequestBody List<User> users) {
		return new ResponseEntity<>(userService.saveAllUsers(users), HttpStatus.CREATED);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<?> fetchUser(@PathVariable Long id) {
		return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
	}

	@GetMapping("/getallusers")
	public ResponseEntity<?> fetchAllUsers() {
		return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
	}

}