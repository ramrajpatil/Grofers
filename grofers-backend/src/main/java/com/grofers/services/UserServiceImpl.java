package com.grofers.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.grofers.dtos.UserDto;
import com.grofers.dtos.UserResponseDto;
import com.grofers.exceptions.DuplicateEntryException;
import com.grofers.exceptions.UserHandlingException;
import com.grofers.pojos.Cart;
import com.grofers.pojos.User;
import com.grofers.pojos.UserRole;
import com.grofers.repos.CartRepo;
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
	private CartRepo cartRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	
	@Override
	public UserResponseDto fetchAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		// Paging and sorting 
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<User> pagedUsers = this.uRepo.findAll(pageable);
		List<User> users = pagedUsers.getContent();
				
		List<UserDto> userDtos = users
				.stream()
				.map((u) -> 
				this.mapper.map(u, UserDto.class))
				.collect(Collectors.toList());
		
		UserResponseDto userResponseDto = new UserResponseDto();
		userResponseDto.setUserList(userDtos);
		userResponseDto.setPageNumber(pagedUsers.getNumber());
		userResponseDto.setPageSize(pagedUsers.getSize());
		userResponseDto.setTotalElements(pagedUsers.getTotalElements());
		userResponseDto.setTotalPages(pagedUsers.getTotalPages());
		userResponseDto.setLastPage(pagedUsers.isLast());
		
		
		return userResponseDto;
	}

	@Override
	public UserDto fetchSingleUser(Integer userId) {
		
		User user = this.uRepo.findById(userId)
		.orElseThrow(() -> new UserHandlingException("User with given id: "+userId+" does not exist."));
		
		return this.mapper.map(user, UserDto.class);
	}


	@Override
	public UserDto addNewUser(UserDto userDto, String choice) {
		if (uRepo.existsByEmail(userDto.getEmail())) {
			throw new DuplicateEntryException("User email must be unique.");
		} else {

			User user = this.mapper.map(userDto, User.class);
			user.setPassword(this.encoder.encode(user.getPassword()));
			
			// Setting the new registering user as customer.

			if (choice.equalsIgnoreCase("admin"))
				user.setRole(UserRole.ROLE_ADMIN);
			else
				user.setRole(UserRole.ROLE_CUSTOMER);

			User newUser = this.uRepo.save(user);
			
			if(newUser.getRole() == UserRole.ROLE_CUSTOMER) {
				Cart cart = new Cart(newUser);
				this.cartRepo.save(cart);
			}
			

			return this.mapper.map(newUser, UserDto.class);
		}
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = uRepo.findById(userId)
				.orElseThrow(() -> new UserHandlingException("User with given id: "+userId+" does not exist."));
				
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(encoder.encode(userDto.getPassword()));
		
		User updatedUser = this.uRepo.save(user);
		
		return this.mapper.map(updatedUser, UserDto.class);
	}

	@Override
	public String deleteUser(Integer userId) {
		User user = uRepo.findById(userId)
				.orElseThrow(() -> new UserHandlingException("User with given id: "+userId+" does not exist."));
		
		this.uRepo.delete(user);
		
		return "User with userdId: "+userId+" deleted successfully !!!";
	}

	@Override
	public UserDto registerUser(UserDto userDto) {
		User user = this.mapper.map(userDto, User.class);
		user.setPassword(this.encoder.encode(user.getPassword()));
		
		
		// Setting the new registering user as customer.
		user.setRole(UserRole.ROLE_CUSTOMER);
		
		User registeredUser = this.uRepo.save(user);
		Cart cart = new Cart(registeredUser);
		this.cartRepo.save(cart);
		
		return this.mapper.map(registeredUser, UserDto.class);
	}

}
