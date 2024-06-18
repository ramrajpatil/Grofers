package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.pojos.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {


}
