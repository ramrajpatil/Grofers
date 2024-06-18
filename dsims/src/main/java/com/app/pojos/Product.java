package com.app.pojos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
@Table(name = "products")
public class Product extends BaseEntity {

	@NotNull(message = "Product Name cannot be blank")
	@Length(max = 100)
	@Column(length = 100, name = "prod_name", unique = true)
	private String name;

	@NotNull(message = "Product price cannot be 0 or blank")
	@Column(name = "prod_price")
	private float price;

	@NotNull(message = "Product margin price cannot be 0 or blank")
	@Column(name = "margin")
	private float margin;

	@Column(length = 1000, name = "url")
	private String imageURL;

	@NotNull(message = "Contents cannot be blank")
	@Column(name = "contents")
	private String content;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties(value = { "cart", "inventory", "products" })
	private User user;
}