package com.grofers.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grofers.pojos.CartItem;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

}
