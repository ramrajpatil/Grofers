package com.grofers.dtos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grofers.pojos.Order;
import com.grofers.pojos.Product;
import com.grofers.pojos.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class UserDto {

	private Integer userId;

	@NotEmpty
	@Size(min = 4, message = "Name cannot be empty or must be min of 4 characters.")
	private String name;

	@NotEmpty(message = "Email address cannot be empty or is not valid.")
	@Email
	private String email;

	@NotEmpty
	@Size(min = 3, max = 20, message = "Password cannot be empty or must be between 3 to 20 characters.")
	private String password;

	private UserRole role;

	private List<Product> products = new ArrayList<>();

	private Set<Order> orders = new HashSet<>();// to avoid duplication of order.

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	// As we don't want to send the user password to the frontend.
	@JsonIgnore
	public String getPassword() {
		return password;
	}
}
