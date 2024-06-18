package com.grofers.dtos;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grofers.pojos.OrderDetail;

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

	private LocalDate deliveryDate;

	private double totalAmount;

	@JsonIgnoreProperties(value = {"cart", "orders", "password","enabled","authorities","accountNonExpired","credentialsNonExpired","accountNonLocked" })
	private UserDto user;

	// We will be fetching order details along with order always.
	private Set<OrderDetail> orderDetails = new HashSet<>();
	
	
}
