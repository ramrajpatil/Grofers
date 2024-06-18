package com.grofers.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grofers.dtos.CartItemDto;
import com.grofers.exceptions.NotFoundException;
import com.grofers.exceptions.UserHandlingException;
import com.grofers.pojos.Cart;
import com.grofers.pojos.CartItem;
import com.grofers.pojos.Product;
import com.grofers.pojos.User;
import com.grofers.repos.CartItemRepo;
import com.grofers.repos.CartRepo;
import com.grofers.repos.ProductRepository;
import com.grofers.repos.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements ICartService {

	@Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private CartItemRepo cartItemRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProductRepository prodRepo;
	

	@Override
	public Cart fetchCart(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new UserHandlingException("User with given id: " + userId + " does not exist."));
		
		Cart cart = user.getCart();
		cart.getCartItems().size();
		return cart;
	}

	@Override
	public Cart addToCart(CartItemDto cartItemDto, Integer userId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new UserHandlingException("User with given id: " + userId + " does not exist."));
		
		Cart cart = user.getCart();
		
		
		Product product = this.prodRepo.findById(cartItemDto.getProductId())
				.orElseThrow(() -> 
				new NotFoundException("Product with id: "+cartItemDto.getProductId()+" does not exist."));
				
		// Create new CartItem object
		CartItem cartItem = new CartItem();
		
		cartItem.setQuantity(cartItemDto.getQuantity());
		cartItem.setProduct(product);
		cartItem.setCart(cart);
		
		cart.getCartItems().add(cartItem);
		
		cart.setTotalAmount(cart.getTotalAmount());
		
		// Saving new cart item to the database.
		this.cartItemRepo.save(cartItem);
		// Updating the cart with newly added CartItem
		Cart updatedCart = this.cartRepo.save(cart);
		
		return updatedCart;
	}
	

	@Override
	public Cart removeFromCart(Integer cartItemId) {
		
		CartItem cartItem = this.cartItemRepo.findById(cartItemId)
		.orElseThrow(() -> 
		new NotFoundException("CartItem with id: "+cartItemId+" does not exists."));
		
		Cart cart = cartItem.getCart();
		List<CartItem> cartItems = cart.getCartItems();
		cartItems.remove(cartItem);
		cart.setCartItems(cartItems);
		cart.setTotalAmount(cart.getTotalAmount());
		// updating the cart
		Cart updatedCart = this.cartRepo.save(cart);
		
		// Removing CartItem from the database
		this.cartItemRepo.delete(cartItem);
		
		return updatedCart;
	}

	@Override
	public String emptyCart(Integer userId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new UserHandlingException("User with given id: " + userId + " does not exist."));
		
		Cart cart = user.getCart();
		
		// Removing all items from the cart.
		List<CartItem> list = cart.getCartItems();
		list.clear();
		
		cart.setCartItems(list);
		cart.setTotalAmount(0.0);
		
		// Updating the cart in the database.
		
		this.cartRepo.save(cart);
		return "Cart emptied successfully !!!";
	}

	@Override
	public Cart addAllToCart(List<CartItemDto> cartItemDtos, Integer userId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new UserHandlingException("User with given id: " + userId + " does not exist."));
		
		Cart cart = user.getCart();
		
		for(CartItemDto dto : cartItemDtos) {
			
			Product product = this.prodRepo.findById(dto.getProductId())
					.orElseThrow(() -> 
					new NotFoundException("Product with id: "+dto.getProductId()+" does not exist."));
					
			// Create new CartItem object
			CartItem cartItem = new CartItem();
			
			cartItem.setQuantity(dto.getQuantity());
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			
			cart.getCartItems().add(cartItem);
			cart.setTotalAmount(cart.getTotalAmount());
			// Saving new cart item to the database.
			this.cartItemRepo.save(cartItem);
			// Updating the cart with newly added cartitem
			
		}
		Cart updatedCart = this.cartRepo.save(cart);
		
		return updatedCart;
	}

	

}
