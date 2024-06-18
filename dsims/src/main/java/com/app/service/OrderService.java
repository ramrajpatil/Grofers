package com.app.service;

import java.util.List;

import com.app.dto.GetOrder;
import com.app.pojos.Order;

public interface OrderService {

	String placeOrder(Long userId);

	String confirmOrder(Long orderId);

	List<Order> getOrdersByIdAndDatesBetweenAndTo(GetOrder orderInfo);

	List<Order> getOrdersByIdAndStatus(GetOrder orderInfo);

	Order getOrderByOrderId(Long orderId);

	List<Order> getOrdersByUserId(Long userId);
}
