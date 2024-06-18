package com.app.service;

import java.util.List;

import com.app.pojos.Cart;

public interface CartService {
	String addToCart(Long inventoryItemId, Long userId);

	Cart fetchCart(Long id);

	String removeFromCart(Long cartItemId);

	String emptyCart(Long cartId);

	String addAllToCart(List<Long> inventoryItemIds, Long cartId);
}
