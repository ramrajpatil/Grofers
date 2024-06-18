package com.app.pojos;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
@Table(name = "inventory_items")
public class InventoryItem extends BaseEntity {

	@NotNull(message = "Manufactured Date cannot be blank")
	@Column(name = "prod_mfgDate")
	private LocalDate mfgDate;

	@NotNull(message = "Expiry Date cannot be blank")
	@Column(name = "prod_expDate")
	private LocalDate expDate;

	@Min(value = 0, message = "The quantity of InventoryItem must be greater than or equal to zero")
	@Column(nullable = false)
	private int quantity;

	@Min(value = 0, message = "The Price of InventoryItem must be greater than or equal to zero")
	@Column(nullable = false)
	private double price;

	@OneToOne
	private Product product;

	@ManyToOne
	@JoinColumn(name = "inventory_id", nullable = false)
	@JsonIgnoreProperties(value = "items")
	private Inventory inventory;
}
