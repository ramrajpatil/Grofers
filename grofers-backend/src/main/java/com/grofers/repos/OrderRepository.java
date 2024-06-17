package com.grofers.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grofers.pojos.Order;
import com.grofers.pojos.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	// For getting order details of a particular user.
	
	Page<Order> findByUser(User user, Pageable pageable);
	
	
	
}
