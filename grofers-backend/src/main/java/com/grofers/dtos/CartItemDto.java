package com.grofers.dtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

	private Integer productId;
	
    @Min(value = 0, message = "The quantity of CartItem must be greater than or equal to zero")
	private int quantity;
	
}
