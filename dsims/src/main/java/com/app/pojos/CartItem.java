package com.app.pojos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {

	@Min(value = 0, message = "The quantity of CartItem must be greater than or equal to zero")
	@Column(nullable = false)
	private int quantity;

	@Min(value = 0, message = "The total price of CartItem must be greater than or equal to zero")
	@Column(name = "total_price", nullable = false)
	private double price;

	@ManyToOne
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@OneToOne
	@JoinColumn(name = "inventoryItem_id")
	InventoryItem inventoryItem;
}
