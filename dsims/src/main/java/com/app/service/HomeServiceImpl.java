package com.app.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.Dashboard;
import com.app.dto.ManDashboard;
import com.app.pojos.Cart;
import com.app.pojos.Inventory;
import com.app.pojos.Order;
import com.app.pojos.Role;
import com.app.pojos.Status;
import com.app.pojos.User;
import com.app.repositories.CartRepo;
import com.app.repositories.InventoryRepo;
import com.app.repositories.OrderRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.UserRepo;

@Service
//@Transactional
public class HomeServiceImpl implements HomeService {

	@Autowired
	UserRepo userRepo;

	@Autowired
	ProductRepo prodRepo;

	@Autowired
	InventoryRepo inventoryRepo;

	@Autowired
	CartRepo cartRepo;

	@Autowired
	OrderRepo orderRepo;
	
	@Transactional()
	@Override
	public Dashboard fetchDashboardDetails(String email) {
		User user = userRepo.findByEmail(email);
		Inventory inventory = user.getInventory();

		if (user.getRole() == Role.MANUFACTURER) {

			List<Order> allOrdersOfManufacturer = orderRepo.findByTo(user);
			int totalOrders = 0;
			int totalOrdersQty = 0;
			double totalOrdersPrice = 0;
			int placedOrders = 0;
			int placedOrdersQty = 0;
			double placedOrdersPrice = 0;
			int deliveredOrders = 0;
			int deliveredOrdersQty = 0;
			double deliveredOrdersPrice = 0;

			for (Order o : allOrdersOfManufacturer) {
				totalOrders++;
				totalOrdersQty += o.getTotalOrderItems();
				totalOrdersPrice += o.getOrderTotal();
				if (o.getStatus() == Status.PLACED) {
					placedOrders++;
					placedOrdersQty += o.getTotalOrderItems();
					placedOrdersPrice += o.getOrderTotal();
				} else {
					deliveredOrders++;
					deliveredOrdersQty += o.getTotalOrderItems();
					deliveredOrdersPrice += o.getOrderTotal();
				}
			}
			int totalProducts = prodRepo.findByUser(user).size();

//			public ManDashboard(Long userId, int totalOrders, int totalOrdersQty, double totalOrdersPrice, Inventory inventory,
//					int placedOrders, int placedOrdersQty, double placedOrdersPrice, int deliveredOrders,
//					int deliveredOrdersQty, double deliveredOrdersPrice, int productsCnt) {
			Dashboard manDashboard = new ManDashboard(
														user.getId(), 
														totalOrders, 
														totalOrdersQty, 
														totalOrdersPrice,
														inventory, 
														placedOrders, 
														placedOrdersQty, 
														placedOrdersPrice, 
														deliveredOrders, 
														deliveredOrdersQty,
														deliveredOrdersPrice, 
														totalProducts
														);

			return manDashboard;
		} else {

			Cart cart = user.getCart();
			List<Order> allOrdersOfWs = orderRepo.findByFrom(user);

			int totalOrders = 0;
			int totalOrdersQty = 0;
			double totalOrdersPrice = 0;

			for (Order o : allOrdersOfWs) {
				totalOrders++;
				totalOrdersQty += o.getTotalOrderItems();
				totalOrdersPrice += o.getOrderTotal();
			}

			Dashboard wsDashboard = new Dashboard(
													user.getId(), 
													totalOrders, 
													totalOrdersQty, 
													totalOrdersPrice,
													inventory, 
													cart
													);
			return wsDashboard;
		}

	}

}
