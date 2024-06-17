package com.grofers.dtos;

import com.grofers.pojos.Order;
import com.grofers.pojos.Product;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {

	private Integer id;

    private Order order;
    
    private Product product;

    @NotEmpty(message = "please select the quantity first.")
    private int quantity;
    
}
