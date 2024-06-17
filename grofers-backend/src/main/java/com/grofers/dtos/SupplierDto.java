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
public class SupplierDto {
	
	private Integer supplierId;

	@NotEmpty(message = "Supplier name cannot be empty.")
	private String name;

	@NotEmpty(message = "Supplier email cannot be empty.")
	private String email;
}
