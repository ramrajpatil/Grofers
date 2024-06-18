
package com.app.pojos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@ToString(exclude = { "orderDetails", "to", "from" })
@Table(name = "orders")
public class Order extends BaseEntity {

	@CreationTimestamp
	@Column(name = "order_date", nullable = false)
	private LocalDate orderDate;

	@CreationTimestamp
	@Column(name = "delivery_date", nullable = false)
	private LocalDate deliveryDate;

	@Min(value = 0, message = "Total Order Items must be greater than or equal to zero")
	@Column(name = "total_order_items", nullable = false)
	private int totalOrderItems;

	@Min(value = 0, message = "The Order Total must be greater than or equal to zero")
	@Column(name = "order_total", nullable = false)
	private double orderTotal;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status", length = 10)
	private Status status;

	@Enumerated(EnumType.STRING)
	@Column(name = "pay_status", length = 6)
	private Status payment;

	@JsonIgnoreProperties(value = { "order","inventoryItem"})
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();

	@ManyToOne
	@JsonIgnoreProperties(value = { "cart", "inventory", "products" })
	@JoinColumn(name = "man_id")
	private User to;

	@ManyToOne
	@JsonIgnoreProperties(value = { "cart", "inventory", "products" })
	@JoinColumn(name = "ws_id")
	private User from;

}
