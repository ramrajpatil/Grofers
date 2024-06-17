package com.grofers.services;

import org.springframework.stereotype.Service;

import com.grofers.dtos.OrderDto;
import com.grofers.dtos.OrderResponseDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public interface IOrderService {

	// Get orders by a user
	OrderResponseDto fetchAllOrdersByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	OrderResponseDto fetchAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	OrderDto fetchSingleOrder(Integer orderId);
	
	OrderDto addNewOrder(OrderDto orderDto, Integer userId);
	
	OrderDto updateOrder(OrderDto orderDto, Integer orderId);
	
	String deleteOrder(Integer orderId);
	
}
