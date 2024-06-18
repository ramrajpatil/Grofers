package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.GetOrder;
import com.app.service.OrderService;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	OrderService orderService;

	@GetMapping("/{orderId}")
	public ResponseEntity<?> getOrderByOrderId(@PathVariable Long orderId) {
		return new ResponseEntity<>(orderService.getOrderByOrderId(orderId), HttpStatus.OK);
	}

	@GetMapping("/myorders/{userId}")
	public ResponseEntity<?> getOrderByUserId(@PathVariable Long userId) {
		return new ResponseEntity<>(orderService.getOrdersByUserId(userId), HttpStatus.OK);
	}

	@GetMapping("/bydates")
	public ResponseEntity<?> getOrdersByUserId(@RequestBody GetOrder orderInfo) {
		return new ResponseEntity<>(orderService.getOrdersByIdAndDatesBetweenAndTo(orderInfo), HttpStatus.OK);
	}

	@GetMapping("/bystatus")
	public ResponseEntity<?> getOrdersByIdAndStatus(@RequestBody GetOrder orderInfo) {
		return new ResponseEntity<>(orderService.getOrdersByIdAndStatus(orderInfo), HttpStatus.OK);
	}

	@PostMapping("/place/{userId}")
	public ResponseEntity<?> placeMyOrder(@PathVariable Long userId) {
		return new ResponseEntity<>(orderService.placeOrder(userId), HttpStatus.CREATED);
	}

	@GetMapping("/confirm/{orderId}")
	public ResponseEntity<?> confirMyOrder(@PathVariable Long orderId) {
		return new ResponseEntity<>(orderService.confirmOrder(orderId), HttpStatus.OK);
	}

}
