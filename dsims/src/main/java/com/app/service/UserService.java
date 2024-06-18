package com.app.service;

import java.util.List;

import javax.validation.Valid;

import com.app.dto.UserDto;
import com.app.pojos.User;

public interface UserService {

	String saveUser(User user);

	UserDto findUserById(Long id);

	User findFullUserById(Long id);

	User findByEmail(String email);

	List<User> findAllUsers();

	String saveAllUsers(@Valid List<User> users);
}