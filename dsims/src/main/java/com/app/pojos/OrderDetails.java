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
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = { "product", "order" })
@Table(name = "order_details")
public class OrderDetails extends BaseEntity {

	@Min(value = 0, message = "The total quantity of OrderDetails must be greater than or equal to zero")
	@Column(nullable = false)
	private int quantity;

	@Min(value = 0, message = "The total price of OrderDetails must be greater than or equal to zero")
	@Column(name = "total_price", nullable = false)
	private double price;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@OneToOne
	@JoinColumn(name = "inventoryItem_id")
	InventoryItem inventoryItem;
}