package com.grofers.dtos;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.grofers.pojos.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

	private Integer orderId;
	
	private LocalDate orderDate;

	@NotEmpty(message = "Delivery date cannot be empty.")
	private LocalDate deliveryDate;

	private double totalAmount;

	private User user;

	// We will be fetching order details along with order always.
	private Set<OrderDetailDto> orderDetails = new HashSet<>();
}
