package com.app.service;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.centralexception.CustomCentralException;
import com.app.pojos.Cart;
import com.app.pojos.CartItem;
import com.app.pojos.InventoryItem;
import com.app.pojos.User;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.InventoryItemRepo;
import com.app.repositories.UserRepo;

@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Autowired
	CartRepo cartRepo;

	@Autowired
	CartItemRepo cartItemRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	InventoryItemRepo inventoryItemRepo;

	@Override
	public String addToCart(Long inventoryItemId, Long cartId) {
		// Getting InventoryItem using inventoryItemId
		InventoryItem inventoryItem = inventoryItemRepo.findById(inventoryItemId)
				.orElseThrow(() -> new CustomCentralException("Invalid InventoryItem id"));

		// Getting User i.e. Wholesaler in this case using user id
		// User wholesaler = userRepo.findById(userId).orElseThrow(() -> new
		// CustomCentralException("User Id Invalid!"));

		// We're adding CartItem into cart of this Wholesaler
		Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new CustomCentralException("Invalid Id"));
		// Creating empty CartItem
		CartItem cartItem = new CartItem();

		// Mapping CartItem according to InventoryItem
		cartItem.setQuantity(inventoryItem.getQuantity());
		cartItem.setPrice(inventoryItem.getPrice());
		cartItem.setCart(cart);
		cartItem.setProduct(inventoryItem.getProduct());
		// edit v.2
		cartItem.setInventoryItem(inventoryItem);
		// Populating Cart according to newly added CartItem
		// Setting List of CartItem of this Cart
		List<CartItem> list = cart.getCartItems();
		list.add(cartItem);
		cart.setCartItems(list);

		cart.setTotal(cart.getTotal() + cartItem.getPrice());
		cart.setTotalItems(cart.getTotalItems() + 1);

		// Finally adding this CartItem to the Database
		cartItemRepo.save(cartItem);

		// Updated cart mapped with database
		cartRepo.save(cart);

		return "Added to cart succesfully";
	}

	@Override
	public Cart fetchCart(Long userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new CustomCentralException("User Id Invalid!"));
		Cart cart = user.getCart();
		cart.getCartItems().size();
		return cart;
	}

	@Override
	public String removeFromCart(Long cartItemId) {
		// Getting CartItem using cartItemId
		CartItem cartItem = cartItemRepo.findById(cartItemId)
				.orElseThrow(() -> new CustomCentralException("Invalid CartItemId!"));

		// Getting cart from this CartItem
		Cart cart = cartItem.getCart();
		// Updating Cart's total items
		cart.setTotalItems(cart.getTotalItems() - 1);

		// Updating Cart's totalPrice
		cart.setTotal(cart.getTotal() - cartItem.getPrice());

		// Updating Cart's List of CartItems
		List<CartItem> list = cart.getCartItems();
		list.remove(cartItem);
		cart.setCartItems(list);

		// Updated cart mapped with database
		cartRepo.save(cart);

		// Finally removing this CartItem from Database
		cartItemRepo.delete(cartItem);

		return "Product removed from cart successfully!";
	}

	@Override
	public String emptyCart(Long cartId) {
		Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new CustomCentralException("Invalid Cart Id"));

		for (CartItem cartItem : cart.getCartItems()) {
			cartItemRepo.delete(cartItem);
		}
		List<CartItem> list = cart.getCartItems();
		list.clear();
		cart.setCartItems(list);
		cart.setTotal(0.0);
		cart.setTotalItems(0);
		cart.setUpdatedOn(LocalDate.now());
		cartRepo.save(cart);
		return "Cart emptied successfully!";
	}

	@Override
	public String addAllToCart(List<Long> inventoryItemIds, Long cartId) {
		for (Long inventoryItemId : inventoryItemIds) {
			// Getting InventoryItem using inventoryItemId
			InventoryItem inventoryItem = inventoryItemRepo.findById(inventoryItemId)
					.orElseThrow(() -> new CustomCentralException("Invalid InventoryItem id"));

			// Getting User i.e. Wholesaler in this case using user id
			// User wholesaler = userRepo.findById(userId).orElseThrow(() -> new
			// CustomCentralException("User Id Invalid!"));

			// We're adding CartItem into cart of this Wholesaler
			Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new CustomCentralException("Invalid Id"));
			// Creating empty CartItem
			CartItem cartItem = new CartItem();

			// Mapping CartItem according to InventoryItem
			cartItem.setQuantity(inventoryItem.getQuantity());
			cartItem.setPrice(inventoryItem.getPrice());
			cartItem.setCart(cart);
			cartItem.setProduct(inventoryItem.getProduct());
			// edit v.2
			cartItem.setInventoryItem(inventoryItem);
			// Populating Cart according to newly added CartItem
			// Setting List of CartItem of this Cart
			List<CartItem> list = cart.getCartItems();
			list.add(cartItem);
			cart.setCartItems(list);

			cart.setTotal(cart.getTotal() + cartItem.getPrice());
			cart.setTotalItems(cart.getTotalItems() + 1);

			// Finally adding this CartItem to the Database
			cartItemRepo.save(cartItem);

			// Updated cart mapped with database
			cartRepo.save(cart);
		}
		return "Added all InventoryItems to the cart succesfully";
	}

}
