package com.app.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.centralexception.CustomCentralException;
import com.app.dto.UserDto;
import com.app.pojos.Cart;
import com.app.pojos.Inventory;
import com.app.pojos.Role;
import com.app.pojos.User;
import com.app.repositories.CartRepo;
import com.app.repositories.InventoryRepo;
import com.app.repositories.UserRepo;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepo userRepo;

	@Autowired
	CartRepo cartRepo;

	@Autowired
	InventoryRepo iRepo;

	@Autowired
	ModelMapper mapper;

	@Override
	public String saveUser(User user) {
		User persistentUser = userRepo.save(user);

		Inventory inventory = new Inventory(persistentUser);
		iRepo.save(inventory);

		if (user.getRole() == Role.WHOLESALER) {
			Cart cart = new Cart(persistentUser);
			cartRepo.save(cart);
		}

		return "User registration success!";
	}

	@Override
	public UserDto findUserById(Long id) {
		User user = userRepo.findById(id).orElseThrow(() -> new CustomCentralException("User Id Invalid!"));
		UserDto userDto = mapper.map(user, UserDto.class);
		return userDto;
	}

	@Override
	public User findFullUserById(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new CustomCentralException("User Id Invalid!"));
	}

	@Override
	public List<User> findAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public String saveAllUsers(@Valid List<User> users) {
		for (User user : users) {
			User persistentUser = userRepo.save(user);

			Inventory inventory = new Inventory(persistentUser);
			iRepo.save(inventory);

			if (user.getRole() == Role.WHOLESALER) {
				Cart cart = new Cart(persistentUser);
				cartRepo.save(cart);
			}
		}
		return "User registration success!";
	}

	@Override
	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}
}