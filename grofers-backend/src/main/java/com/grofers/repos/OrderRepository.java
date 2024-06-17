package com.grofers.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grofers.pojos.Order;
import com.grofers.pojos.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	// For getting order details of a particular user.
	
	List<Order> findByUser(User user);
	
	
	
}
