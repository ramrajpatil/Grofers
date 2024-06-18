package com.app.dto;

import com.app.pojos.Inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManDashboard extends Dashboard {
	private int placedOrders;
	private int placedOrdersQty;
	private double placedOrdersPrice;
	private int deliveredOrders;
	private int deliveredOrdersQty;
	private double deliveredOrdersPrice;
	private int productsCnt;

	public ManDashboard(Long userId, int totalOrders, int totalOrdersQty, double totalOrdersPrice, Inventory inventory,
			int placedOrders, int placedOrdersQty, double placedOrdersPrice, int deliveredOrders,
			int deliveredOrdersQty, double deliveredOrdersPrice, int productsCnt) {
		super(userId, totalOrders, totalOrdersQty, totalOrdersPrice, inventory);
		this.placedOrders = placedOrders;
		this.placedOrdersQty = placedOrdersQty;
		this.placedOrdersPrice = placedOrdersPrice;
		this.deliveredOrders = deliveredOrders;
		this.deliveredOrdersQty = deliveredOrdersQty;
		this.deliveredOrdersPrice = deliveredOrdersPrice;
		this.productsCnt = productsCnt;
	}

}