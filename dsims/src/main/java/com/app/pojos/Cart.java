package com.app.pojos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(exclude = { "user", "cartItems" })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {

	@CreationTimestamp
	@Column(name = "created_on", nullable = false)
	private LocalDate createdOn;

	@UpdateTimestamp
	@Column(name = "updated_on", nullable = false)
	private LocalDate updatedOn;

	@Min(value = 0, message = "Total Cart Items must be greater than or equal to zero")
	@Column(name = "total_items")
	private int totalItems;

	@Min(value = 0, message = "The Cart Total must be greater than or equal to zero")
	@Column(name = "total")
	private double total;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnoreProperties(value = { "cart", "inventory", "products" })
	private User user;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties(value = "cart")
	private List<CartItem> cartItems = new ArrayList<CartItem>();

	public Cart(User user) {
		super();
		this.user = user;
	}

}
