package com.grofers.services;

import com.grofers.dtos.UserDto;
import com.grofers.dtos.UserResponseDto;


public interface IUserService {

//	List<UserDto> fetchAllUsers();
	// Implementing paging and sorting.
	UserResponseDto fetchAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	
	UserDto fetchSingleUser(Integer userId);
	
	
	UserDto addNewUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto, Integer userId);
	
	String deleteUser(Integer userId);
	
	UserDto registerUser(UserDto userDto);
}
