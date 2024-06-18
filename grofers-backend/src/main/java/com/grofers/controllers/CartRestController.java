package com.grofers.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grofers.dtos.CartItemDto;
import com.grofers.dtos.ResponseDTO;
import com.grofers.pojos.Cart;
import com.grofers.services.ICartService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/cart")
public class CartRestController {

	@Autowired
	private ICartService cartService;
	
	private final Logger logger = LoggerFactory.getLogger(CartRestController.class);
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/user/{userId}")
	public ResponseEntity<Cart> fetchCart(@PathVariable Integer userId){
		this.logger.info("In fetch cart of: "+getClass().getName());
		Cart cart = this.cartService.fetchCart(userId);
		
		return ResponseEntity.ok(cart);
	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/add/{userId}")
	public ResponseEntity<Cart> addToCart(@PathVariable Integer userId, @Valid @RequestBody CartItemDto cartItemDto){
		
		Cart cart = this.cartService.addToCart(cartItemDto, userId);
		
		return ResponseEntity.ok(cart);
	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/addAll/{userId}")
	public ResponseEntity<Cart> addAllToCart(@PathVariable Integer userId, @RequestBody List<CartItemDto> cartItemDtos){
		
		Cart cart = this.cartService.addAllToCart(cartItemDtos, userId);
		
		return ResponseEntity.ok(cart);
	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<Cart> removeFromCart(@PathVariable Integer cartItemId){
		Cart cart = this.cartService.removeFromCart(cartItemId);
		
		return ResponseEntity.ok(cart);
	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@DeleteMapping("/emptyCart/{cartId}")
	public ResponseEntity<ResponseDTO> emptyCart(@PathVariable Integer cartId){
		String message = this.cartService.emptyCart(cartId);
		
		return ResponseEntity.ok(new ResponseDTO(message, true));
	}
	
	
	
}
