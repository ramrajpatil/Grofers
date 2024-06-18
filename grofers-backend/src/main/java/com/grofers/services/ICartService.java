package com.grofers.services;

import java.util.List;

import com.grofers.dtos.CartItemDto;
import com.grofers.pojos.Cart;

public interface ICartService {
	

	Cart fetchCart(Integer userId);
	
	String addToCart(CartItemDto cartItemDto, Integer cartId);
	
	String removeFromCart(Integer cartItemId);
	
	String emptyCart(Integer cartId);
	
	String addAllToCart(List<CartItemDto> cartItemDtos, Integer cartId);
	
}
