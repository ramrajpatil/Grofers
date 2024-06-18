package com.app.dto;

import com.app.pojos.Cart;
import com.app.pojos.Inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard {

	protected Long userId;
	protected int totalOrders;
	protected int totalOrdersQty;
	protected double totalOrdersPrice;
	protected Inventory inventory;
	private Cart cart;

	public Dashboard(Long userId, int totalOrders, int totalOrdersQty, double totalOrdersPrice, Inventory inventory) {
		this.userId = userId;
		this.totalOrders = totalOrders;
		this.totalOrdersQty = totalOrdersQty;
		this.totalOrdersPrice = totalOrdersPrice;
		this.inventory = inventory;
	}

}