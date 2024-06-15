package com.grofers.services;

import java.util.List;

import com.grofers.dtos.UserDto;
import com.grofers.pojos.User;


public interface IUserService {

	List<UserDto> fetchAllUsers();
	
	UserDto fetchSingleUser(Integer userId);
	
	
	UserDto addNewUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto, Integer userId);
	
	String deleteUser(Integer userId);
	
	UserDto registerUser(UserDto userDto);
}
