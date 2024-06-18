package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.pojos.OrderDetails;

public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Long> {

}
