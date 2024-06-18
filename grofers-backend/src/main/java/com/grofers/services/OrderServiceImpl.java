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
import com.grofers.exceptions.EmptyCartException;
import com.grofers.exceptions.NotFoundException;
import com.grofers.exceptions.UserHandlingException;
import com.grofers.pojos.Cart;
import com.grofers.pojos.CartItem;
import com.grofers.pojos.Order;
import com.grofers.pojos.OrderDetail;
import com.grofers.pojos.User;
import com.grofers.repos.OrderDetailRepository;
import com.grofers.repos.OrderRepository;
import com.grofers.repos.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private OrderDetailRepository oDRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ICartService cartService;

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
	public OrderDto placeOrder(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new UserHandlingException("User with given id: " + userId + " does not exist."));

		Order newOrder = new Order();
		
		
		Cart cart = user.getCart();
		
		if(cart.getCartItems().size() <= 0) {
			throw new EmptyCartException("Please add items in you cart first before placing an order.");
		}
		else {
			List<CartItem> cartItems = cart.getCartItems();
			Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
			cartItems.forEach((c) ->{
				OrderDetail od = new OrderDetail();
				od.setOrder(newOrder);
				od.setProduct(c.getProduct());
				od.setQuantity(c.getQuantity());
				
				oDRepo.save(od);
			});
			
			newOrder.setOrderDetails(orderDetails);
			newOrder.setOrderDate(LocalDate.now());
			newOrder.setDeliveryDate(LocalDate.now().plusDays(5));// Delivery date is set to 2 days after the order.
			newOrder.setTotalAmount(cart.getTotalAmount());
			newOrder.setUser(user);
			
			Order savedOrder = this.orderRepo.save(newOrder);
			
			this.cartService.emptyCart(cart.getCartId());
			
			return this.mapper.map(savedOrder, OrderDto.class);
		}
		
	}

	@Override
	public OrderDto updateOrder(OrderDto orderDto, Integer orderId) {
		Order order = this.orderRepo.findById(orderId)
				.orElseThrow(() -> new NotFoundException("Order with given id: " + orderId + " does not exist."));

		// Updating delivery data and products from the order.
		order.setDeliveryDate(orderDto.getDeliveryDate());
		
		if (orderDto.getOrderDetails() != null) {
            Set<OrderDetail> orders = orderDto.getOrderDetails().stream()
                    .map(o -> this.mapper.map(o, OrderDetail.class))
                    .collect(Collectors.toSet());
            order.setOrderDetails(orders);
            order.setTotalAmount(order.getTotalAmount());
        }
		
		Order updatedOrder = this.orderRepo.save(order);
		
		return this.mapper.map(updatedOrder, OrderDto.class);
	}

	@Override
	public String deleteOrder(Integer orderId) {

		Order order = this.orderRepo.findById(orderId)
				.orElseThrow(() -> new NotFoundException("Order with given id: " + orderId + " does not exist."));

		this.orderRepo.delete(order);

		return "Order with orderId: " + orderId + " was deleted successfully !!!";
	}

	@Override
	public List<OrderDto> findByDeliveryDateBetween(LocalDate strtDate, LocalDate endDate) {
		List<Order> list = this.orderRepo.findByDeliveryDateBetween(strtDate, endDate);
		
		List<OrderDto> dtos = list.stream().map((o) -> this.mapper.map(o, OrderDto.class))
		.collect(Collectors.toList());
		return dtos;
	}

}
