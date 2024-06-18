package com.grofers.services;

import java.util.List;

import com.grofers.dtos.CartItemDto;
import com.grofers.pojos.Cart;

public interface ICartService {
	

	Cart fetchCart(Integer userId);
	
	Cart addToCart(CartItemDto cartItemDto, Integer cartId);
	
	Cart removeFromCart(Integer cartItemId);
	
	String emptyCart(Integer cartId);
	
	Cart addAllToCart(List<CartItemDto> cartItemDtos, Integer cartId);

}
