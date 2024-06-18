package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.service.CartService;
import com.app.service.UserService;
@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	CartService cartservice;

	@Autowired
	UserService userservice;

//	@PostMapping("/add")
//	public ResponseEntity<?> addToCart(@RequestParam Long inventoryItemId, @RequestParam Long userId,
//			@RequestParam int quantity) {

	//v.2
	@PostMapping("/add/{inventoryItemId}/{cartId}")
	public ResponseEntity<?> addToCart(@PathVariable Long inventoryItemId, @PathVariable Long cartId) {

		return new ResponseEntity<>(cartservice.addToCart(inventoryItemId, cartId), HttpStatus.CREATED);
	}

	@PostMapping("/addall/{cartId}")
	public ResponseEntity<?> addAllToCart(@RequestBody List<Long> inventoryItemIds, @PathVariable Long cartId) {
		
		return new ResponseEntity<>(cartservice.addAllToCart(inventoryItemIds, cartId), HttpStatus.CREATED);
	}

	@GetMapping("/fetchcart/{userId}")
	public ResponseEntity<?> fetchCart(@PathVariable Long userId) {
		return new ResponseEntity<>(cartservice.fetchCart(userId), HttpStatus.OK);
	}

	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<?> removeFromCart(@PathVariable Long cartItemId) {
		return new ResponseEntity<>(cartservice.removeFromCart(cartItemId), HttpStatus.OK);
	}

	@DeleteMapping("/emptycart/{cartId}")
	public ResponseEntity<?> emptyCart(@PathVariable Long cartId) {
		return new ResponseEntity<>(cartservice.emptyCart(cartId), HttpStatus.OK);
	}

}
