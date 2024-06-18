package com.app.pojos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = { "inventory", "cart", "products" })
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonInclude(Include.NON_NULL)
public class User extends BaseEntity {

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

	@NotNull(message = "Password cannot be null")
	@Length(max = 20)
	// @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[#@$*]).{5,20})", message =
	// "Invalid Password !")
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(length = 30, nullable = false)
	private String password;

	@NotNull(message = "Details cannot be null")
	@Embedded
	@JsonInclude
	private UserDetails userDetails;

	@NotNull(message = "Phone Number must be provided")
	@Length(max = 10)
	@Column(length = 10, nullable = false, unique = true)
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role", length = 30)
	private Role role;

	@JsonIgnoreProperties(value = { "user", "cartItems" })
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Cart cart;

	@JsonIgnoreProperties(value = { "user", "items" })
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Inventory inventory;

	@JsonIgnoreProperties(value = { "user" })
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> products = new ArrayList<Product>();

	public org.springframework.security.core.userdetails.User toUser() {

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());

		org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
				email, password, Collections.singletonList(authority));
		return user;
	}

}