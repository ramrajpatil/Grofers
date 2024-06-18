package com.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.centralexception.CustomCentralException;
import com.app.dto.GetOrder;
import com.app.pojos.Cart;
import com.app.pojos.CartItem;
import com.app.pojos.Inventory;
import com.app.pojos.InventoryItem;
import com.app.pojos.Order;
import com.app.pojos.OrderDetails;
import com.app.pojos.Product;
import com.app.pojos.Role;
import com.app.pojos.Status;
import com.app.pojos.User;
import com.app.repositories.InventoryItemRepo;
import com.app.repositories.InventoryRepo;
import com.app.repositories.OrderDetailsRepo;
import com.app.repositories.OrderRepo;
import com.app.repositories.UserRepo;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepo orderRepo;

	@Autowired
	InventoryRepo inventoryRepo;

	@Autowired
	InventoryService inventoryService;

	@Autowired
	InventoryItemRepo inventoryItemRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	CartService cartService;
	@Autowired
	OrderDetailsRepo orderDetailsRepo;

	@Override
	public String placeOrder(Long userId) {
		int orderCount = 0;

		User ws = userRepo.findById(userId).orElseThrow(() -> new CustomCentralException("User Id Invalid!"));
		Cart cart = ws.getCart();

		HashSet<User> manufacturers = new HashSet<User>();
		for (CartItem cartItem : cart.getCartItems()) {
			// Finding unique manufacturers for all Products
			manufacturers.add(cartItem.getProduct().getUser());
		}
		// Rehearse
		orderCount = manufacturers.size();
		String response = orderCount + " order(s) placed!!!";

		for (User to : manufacturers) {
			// Separate Order of wholesaler for each distinct manufacturer
			Order order = new Order();
			orderRepo.save(order);
			order.setFrom(ws);
			order.setTo(to);

			List<CartItem> cartItemsBelongingToThisManuf = cart.getCartItems().stream()
					.filter((cartitem) -> cartitem.getProduct().getUser().equals(to)).collect(Collectors.toList());

			List<OrderDetails> allOrderDetails = new ArrayList<OrderDetails>();
			int totalItems = 0;
			double totalPrice = 0.0;
			for (CartItem cartItem : cartItemsBelongingToThisManuf) {
				// Getting required Product
				Product requiredProduct = cartItem.getProduct();

				// 2. Already placed orders quantity for this particular product
				// from inventory
				List<Order> oldOrdersForThisProduct = orderRepo.findByStatusAndProduct(requiredProduct);
				List<OrderDetails> orderDetailsHavingThisProduct = new ArrayList<OrderDetails>();

				for (Order o : oldOrdersForThisProduct) {
					// we've already filtered out orderDetails having requiredProduct only
					orderDetailsHavingThisProduct.addAll(o.getOrderDetails());
				}
				orderDetailsHavingThisProduct.toString();
				boolean isAvailable = true;
				for (OrderDetails od : orderDetailsHavingThisProduct) {
					if (cartItem.getInventoryItem().equals(od.getInventoryItem()))
						isAvailable = false;
				}

				if (!isAvailable) {
					response = (to.getFirstName() + " from " + to.getUserDetails().getOrgName()
							+ " does not any stock for " + requiredProduct.getName());
					// Setting true if some Ordered quantity cannot be processed
					System.out.println("I'm here: if (cartItem.getQuantity() < availableQty) {");
					break;
				} else {

					System.out.println("I'm here:} else if (cartItem.getQuantity() < availableQty) {");
					OrderDetails orderDetails = new OrderDetails();
					orderDetails.setQuantity(cartItem.getQuantity());
					orderDetails.setPrice(cartItem.getPrice());
					orderDetails.setProduct(cartItem.getProduct());
					orderDetails.setOrder(order);
					// edit v.2
					orderDetails.setInventoryItem(cartItem.getInventoryItem());
					allOrderDetails.add(orderDetails);
					totalItems++;
					totalPrice = totalPrice + cartItem.getPrice();
					orderDetailsRepo.save(orderDetails);
					// response.add("Order for " + requiredProduct.getName() + " is placed");
					System.out.println(orderDetails.toString());
				}
			}

			List<OrderDetails> ods = order.getOrderDetails();
			ods.addAll(allOrderDetails);
			order.setOrderDetails(ods);

			order.setTotalOrderItems(totalItems);

			order.setOrderTotal(totalPrice);

			order.setStatus(Status.PLACED);

			order.setPayment(Status.UNPAID);

			order.setDeliveryDate(LocalDate.now().plusDays(5));
			// Not saving order if there are no Items in the order
			if (order.getTotalOrderItems() <= 0)
				orderRepo.delete(order);
			else
				orderRepo.save(order);
		}
		// Emptying Cart of this Wholesaler
		cartService.emptyCart(cart.getId());
		return response;
	}

	@Override
	public String confirmOrder(Long orderId) {
		// Particular single order for **single wholesaler for **single manufacturer
		Order order = orderRepo.findById(orderId)
				.orElseThrow(() -> new CustomCentralException("Invalid Order id, try again!"));
		orderRepo.save(order);
		List<OrderDetails> orderDetails = order.getOrderDetails();
		User man = order.getTo();
		User ws = order.getFrom();

		// For each product in order detail
		for (OrderDetails od : orderDetails) {
			InventoryItem item = inventoryItemRepo.findById(od.getInventoryItem().getId())
					.orElseThrow(() -> new CustomCentralException("Invalid InventoryItem id"));

			// Replaced Inventory of this InvItem from Man to WS, by doing this we're
			// Subtracting this InventoryItem as whole and adding it to the WS's inventory
			// as whole
			InventoryItem wsInvItem = new InventoryItem();
			wsInvItem.setExpDate(item.getExpDate());
			wsInvItem.setMfgDate(item.getMfgDate());
			wsInvItem.setInventory(ws.getInventory());
			wsInvItem.setPrice(item.getPrice());
			wsInvItem.setProduct(item.getProduct());
			wsInvItem.setQuantity(item.getQuantity());
			InventoryItem persistentwsInvItem = inventoryItemRepo.save(wsInvItem);
			od.setInventoryItem(persistentwsInvItem);

			Inventory wsInventory = ws.getInventory();
			List<InventoryItem> listForUpdatedWSInventoryItem = wsInventory.getItems();
			listForUpdatedWSInventoryItem.add(wsInvItem);
			// Populated inventory->List<InventoryItem> of Wholesaler and updated
			wsInventory.setItems(listForUpdatedWSInventoryItem);

			wsInventory.setTotalQuantity(wsInventory.getTotalQuantity() + item.getQuantity());
			wsInventory.setTotalPrice(wsInventory.getTotalPrice() + item.getPrice());

			inventoryRepo.save(wsInventory);

			inventoryItemRepo.delete(item);
			Inventory manInventory = man.getInventory();

			List<InventoryItem> listForUpdatedManInventoryItem = manInventory.getItems();
			listForUpdatedManInventoryItem.remove(item);

			// Populated Manufacturer inventory -> List<InventoryItem> and updated
			manInventory.setItems(listForUpdatedManInventoryItem);
			manInventory.setTotalQuantity(manInventory.getTotalQuantity() - item.getQuantity());
			manInventory.setTotalPrice(manInventory.getTotalPrice() - item.getPrice());
			inventoryRepo.save(manInventory);
		}

		// As Inventories of both parties is mapped, order's other details are set
		order.setDeliveryDate(LocalDate.now());
		order.setStatus(Status.DELIVERED);
		order.setPayment(Status.PAID);
		orderRepo.save(order);
		return "Order success, Inventories updated!";

	}

	@Override
	public List<Order> getOrdersByIdAndDatesBetweenAndTo(GetOrder orderInfo) {
		User user = userRepo.findById(orderInfo.getUserId())
				.orElseThrow(() -> new CustomCentralException("Invalid user id!"));

		if (user.getRole() == Role.MANUFACTURER)
			return orderRepo.findByDeliveryDateBetweenAndTo(orderInfo.getStartDate(), orderInfo.getEndDate(), user);
		else
			return orderRepo.findByDeliveryDateBetweenAndFrom(orderInfo.getStartDate(), orderInfo.getEndDate(), user);
	}

	@Override
	public List<Order> getOrdersByIdAndStatus(GetOrder orderInfo) {
		User user = userRepo.findById(orderInfo.getUserId())
				.orElseThrow(() -> new CustomCentralException("Invalid user id!"));
//		Status status = Status.valueOf(orderInfo.getStatus());
		if (user.getRole() == Role.MANUFACTURER)
			return orderRepo.findByStatusAndTo(orderInfo.getStatus(), user);
		else
			return orderRepo.findByStatusAndFrom(orderInfo.getStatus(), user);
	}

	@Override
	public Order getOrderByOrderId(Long orderId) {
		// Custom query written + added Join for fetching OrderDetails list
		return orderRepo.findByOrderIdWithAllOrderDetails(orderId);
	}

	@Override
	public List<Order> getOrdersByUserId(Long userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new CustomCentralException("User Id Invalid!"));
		if (user.getRole() == Role.MANUFACTURER)
			return orderRepo.findByTo(user);
		else
			return orderRepo.findByFrom(user);
	}
}
