package com.grofers.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grofers.config.AppConstants;
import com.grofers.dtos.OrderDto;
import com.grofers.dtos.OrderResponseDto;
import com.grofers.dtos.ResponseDTO;
import com.grofers.services.IOrderService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/orders")
public class OrderRestController {

	@Autowired
	private IOrderService orderService;
	
	private final Logger logger = LoggerFactory.getLogger(OrderRestController.class);

	// Fetching all orders.
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	public ResponseEntity<OrderResponseDto> getAllOrders(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.ORDER_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		OrderResponseDto dto = this.orderService.fetchAllOrders(pageNumber, pageSize, sortBy, sortDir);

		this.logger.info("In get all order of: "+getClass().getName());
		
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<OrderResponseDto> getAllOrdersByUser(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.ORDER_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@PathVariable Integer userId) {
		OrderResponseDto dto = this.orderService.fetchAllOrdersByUser(userId, pageNumber, pageSize, sortBy, sortDir);

		return ResponseEntity.ok(dto);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDto> getSingleOrder(@PathVariable Integer orderId) {

		OrderDto orderDto = this.orderService.fetchSingleOrder(orderId);

		return ResponseEntity.ok(orderDto);
	}

	@PostMapping("/{userId}")
	public ResponseEntity<OrderDto> createOrder(@PathVariable Integer userId, @Valid @RequestBody OrderDto orderDto) {

		OrderDto order = this.orderService.addNewOrder(orderDto, userId);

		return ResponseEntity.ok(order);
	}
	
	@PutMapping("/{orderId}/products")
	public ResponseEntity<OrderDto> addProductsToOrder(@PathVariable Integer orderId, @Valid @RequestBody OrderDto orderDto){
		
		
		return null;
	}
	
	@PutMapping("/{orderId}")
	public ResponseEntity<OrderDto> updateOrder(@PathVariable Integer orderId, @Valid @RequestBody OrderDto orderDto) {

		OrderDto order = this.orderService.updateOrder(orderDto, orderId);

		return ResponseEntity.ok(order);
	}
	
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ResponseDTO> deleteOrder(@PathVariable Integer orderId){
		
		String message = this.orderService.deleteOrder(orderId);
		
		return ResponseEntity.ok(new ResponseDTO(message, true));
	}
	
	
}
