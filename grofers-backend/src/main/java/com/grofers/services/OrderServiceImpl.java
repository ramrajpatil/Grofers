package com.grofers.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.grofers.dtos.OrderDto;
import com.grofers.dtos.OrderResponseDto;
import com.grofers.exceptions.DuplicateEntryException;
import com.grofers.exceptions.UserHandlingException;
import com.grofers.pojos.Order;
import com.grofers.pojos.OrderDetail;
import com.grofers.pojos.User;
import com.grofers.repos.OrderRepository;
import com.grofers.repos.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private UserRepository userRepo;

	@Override
	public OrderResponseDto fetchAllOrdersByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new UserHandlingException("User with given id: " + userId + " does not exist."));

		// Paging and sorting
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Order> pagedOrders = this.orderRepo.findByUser(user, pageable);

		List<Order> orders = pagedOrders.getContent();

		List<OrderDto> orderDtos = orders.stream().map(o -> this.mapper.map(o, OrderDto.class))
				.collect(Collectors.toList());

		OrderResponseDto resp = new OrderResponseDto();
		resp.setOrderList(orderDtos);
		resp.setPageNumber(pagedOrders.getNumber());
		resp.setPageSize(pagedOrders.getSize());
		resp.setTotalElements(pagedOrders.getTotalElements());
		resp.setTotalPages(pagedOrders.getTotalPages());
		resp.setLastPage(pagedOrders.isLast());

		return resp;
	}

	@Override
	public OrderResponseDto fetchAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		// Paging and sorting
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Order> pagedOrders = this.orderRepo.findAll(pageable);

		List<Order> orders = pagedOrders.getContent();

		List<OrderDto> orderDtos = orders.stream().map(o -> this.mapper.map(o, OrderDto.class))
				.collect(Collectors.toList());

		OrderResponseDto resp = new OrderResponseDto();
		resp.setOrderList(orderDtos);
		resp.setPageNumber(pagedOrders.getNumber());
		resp.setPageSize(pagedOrders.getSize());
		resp.setTotalElements(pagedOrders.getTotalElements());
		resp.setTotalPages(pagedOrders.getTotalPages());
		resp.setLastPage(pagedOrders.isLast());

		return resp;
	}

	@Override
	public OrderDto fetchSingleOrder(Integer orderId) {

		Order order = this.orderRepo.findById(orderId)
				.orElseThrow(() -> new UserHandlingException("Order with given id: " + orderId + " does not exist."));

		return this.mapper.map(order, OrderDto.class);
	}

	@Override
	public OrderDto addNewOrder(OrderDto orderDto, Integer userId) {
		if (this.orderRepo.existsById(orderDto.getOrderId())) {
			throw new DuplicateEntryException("This order already exists, please place new order.");
		} else {

			User user = this.userRepo.findById(userId)
					.orElseThrow(() -> new UserHandlingException("User with given id: " + userId + " does not exist."));

			Order order = this.mapper.map(orderDto, Order.class);
			
			order.setOrderDate(LocalDate.now());
			order.setUser(user);
			
			Order savedOrder = this.orderRepo.save(order);
			
			
			return this.mapper.map(savedOrder, OrderDto.class);
		}
	}

	@Override
	public OrderDto updateOrder(OrderDto orderDto, Integer orderId) {
		Order order = this.orderRepo.findById(orderId)
				.orElseThrow(() -> new UserHandlingException("Order with given id: " + orderId + " does not exist."));

		// Updating delivery data and products from the order.
		order.setDeliveryDate(orderDto.getDeliveryDate());
		
		if (orderDto.getOrderDetails() != null) {
            Set<OrderDetail> orders = orderDto.getOrderDetails().stream()
                    .map(o -> this.mapper.map(o, OrderDetail.class))
                    .collect(Collectors.toSet());
            order.setOrderDetails(orders);
        }
		
		Order updatedOrder = this.orderRepo.save(order);
		
		return this.mapper.map(updatedOrder, OrderDto.class);
	}

	@Override
	public String deleteOrder(Integer orderId) {

		Order order = this.orderRepo.findById(orderId)
				.orElseThrow(() -> new UserHandlingException("Order with given id: " + orderId + " does not exist."));

		this.orderRepo.delete(order);

		return "Order with orderId: " + orderId + " was deleted successfully !!!";
	}

}
