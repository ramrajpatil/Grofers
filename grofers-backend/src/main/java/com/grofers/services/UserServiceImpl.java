package com.grofers.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.grofers.dtos.UserDto;
import com.grofers.exceptions.UserHandlingException;
import com.grofers.pojos.User;
import com.grofers.pojos.UserRole;
import com.grofers.repos.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder encoder;
	
	
	@Override
	public List<UserDto> fetchAllUsers() {
		List<User> users = this.uRepo.findAll();
		
		List<UserDto> userDtos = users
				.stream()
				.map((u) -> 
				this.mapper.map(u, UserDto.class))
				.collect(Collectors.toList());
		
		return userDtos;
	}

	@Override
	public UserDto fetchSingleUser(Integer userId) {
		
		User user = this.uRepo.findById(userId)
		.orElseThrow(() -> new UserHandlingException("User with given id: "+userId+" does not exist."));
		
		return this.mapper.map(user, UserDto.class);
	}


	@Override
	public UserDto addNewUser(UserDto userDto) {
		
		User transientUser = this.mapper.map(userDto, User.class);
		
		User persistentUser = this.uRepo.save(transientUser);
		
		return this.mapper.map(persistentUser, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = uRepo.findById(userId)
				.orElseThrow(() -> new UserHandlingException("User with given id: "+userId+" does not exist."));
				
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		
		User updatedUser = this.uRepo.save(user);
		
		return this.mapper.map(updatedUser, UserDto.class);
	}

	@Override
	public String deleteUser(Integer userId) {
		User user = uRepo.findById(userId)
				.orElseThrow(() -> new UserHandlingException("User with given id: "+userId+" does not exist."));
		
		uRepo.delete(user);
		
		return "User with userdId: "+userId+" deleted successfully !!!";
	}

	@Override
	public UserDto registerUser(UserDto userDto) {
		User user = this.mapper.map(userDto, User.class);
		user.setPassword(this.encoder.encode(user.getPassword()));
		
		
		// Setting the new registering user as customer.
		user.setRole(UserRole.ROLE_CUSTOMER);
		
		User registeredUser = this.uRepo.save(user);
		
		
		return this.mapper.map(registeredUser, UserDto.class);
	}

}
