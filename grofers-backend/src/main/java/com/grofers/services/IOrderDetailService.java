package com.grofers.services;

import java.util.List;

import com.grofers.dtos.OrderDetailDto;

public interface IOrderDetailService {

	List<OrderDetailDto> fetchOrderDetailsByOrder(Integer orderId);
	
	List<OrderDetailDto> fetchAllOrderDetails();
	
	OrderDetailDto fetchSingleOrderDetail(Integer id);
	
	OrderDetailDto addProductToOrder(OrderDetailDto orderDetailDto, Integer orderId, Integer productId);
	
	OrderDetailDto updateProductFromOrderDetail(OrderDetailDto orderDetailDto, Integer id);
	
	String removeProductFromOrder(Integer id);
	
	
}
