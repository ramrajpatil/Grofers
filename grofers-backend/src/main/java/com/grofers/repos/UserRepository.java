package com.grofers.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grofers.pojos.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	// For spring security 
	// Treating email as username.
	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);
}
