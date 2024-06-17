package com.grofers.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

	private Integer productId;

	@NotEmpty(message = "Product name cannot be empty.")
	private String productName;

	@NotEmpty(message = "Product price cannot be empty.")
	private String price;
	
	private CategoryDto category;

	private SupplierDto supplier;

}
