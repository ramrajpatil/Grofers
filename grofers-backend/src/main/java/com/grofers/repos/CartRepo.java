package com.grofers.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grofers.pojos.Cart;
import com.grofers.pojos.User;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {

	Cart findByUser(User user);
}
