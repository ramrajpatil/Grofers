package com.app.pojos;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name = "inventories")
public class Inventory extends BaseEntity {

	@Min(value = 0, message = "Total quantity of Inventory must be greater than or equal to zero")
	@Column(name = "total_quantity")
	private int totalQuantity;

	@Min(value = 0, message = "Total Price of Inventory must be greater than or equal to zero")
	@Column(name = "total_price")
	private double totalPrice;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnoreProperties(value = { "cart", "inventory", "products" })
	private User user;

	@OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties(value = "inventory")
	private List<InventoryItem> items = new ArrayList<InventoryItem>();

	public Inventory(User user) {
		super();
		this.user = user;
	}
}
