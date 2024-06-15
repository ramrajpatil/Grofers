package com.grofers.securitys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.grofers.exceptions.UserHandlingException;
import com.grofers.pojos.User;
import com.grofers.repos.UserRepository;


@Service
public class CustomUserDetailService implements UserDetailsService {

	
	@Autowired
	private UserRepository uRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// Loading user from database by username (Email)
		User user = this.uRepo.findByEmail(username)
		.orElseThrow(() -> new UserHandlingException("Invalid username or it doesn't exist !!! "));
		
		
		return user;
	}

}
