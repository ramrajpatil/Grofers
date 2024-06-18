package com.app.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.app.pojos.Cart;
import com.app.pojos.Inventory;
import com.app.pojos.Role;
import com.app.pojos.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class UserDto {
	
	@NotNull(message = "First Name cannot be blank")
	@Length(max = 20)
	@Column(name = "first_name", length = 20)
	private String firstName;

	@NotNull(message = "Last Name cannot be blank")
	@Length(max = 25)
	@Column(name = "last_name", length = 20)
	private String lastName;

	@NotNull(message = "Email cannot be blank")
	@Length(max = 30)
	@Email(message = "Invalid email format!")
	@Column(length = 30, unique = true, nullable = false)
	private String email;

	@Embedded
	private UserDetails userDetails;

	@NotNull(message = "Phone Number must be provided")
	@Length(max = 10)
	@Column(length = 10, nullable = false, unique = true)
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role", length = 30)
	private Role role;
	
	@JsonIgnoreProperties(value = {"user","cartItems"})
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Cart cart;
	
	@JsonIgnoreProperties(value = {"user","items"})
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Inventory inventory;
	
}
