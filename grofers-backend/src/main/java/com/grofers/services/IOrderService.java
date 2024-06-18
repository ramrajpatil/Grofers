package com.grofers.services;

import java.time.LocalDate;
import java.util.List;

import com.grofers.dtos.OrderDto;
import com.grofers.dtos.OrderResponseDto;

public interface IOrderService {

	// Get orders by a user
	OrderResponseDto fetchAllOrdersByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	OrderResponseDto fetchAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	List<OrderDto> findByDeliveryDateBetween(LocalDate strtDate, LocalDate endDate);
	
	OrderDto fetchSingleOrder(Integer orderId);
	
	OrderDto placeOrder(Integer userId);
	
	OrderDto updateOrder(OrderDto orderDto, Integer orderId);
	
	String deleteOrder(Integer orderId);
	
}
