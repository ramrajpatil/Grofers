package com.grofers.services;

import java.util.List;

import com.grofers.dtos.ProductDto;
import com.grofers.dtos.ProductResponseDto;

public interface IProductService {

	ProductResponseDto fetchAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	ProductDto fetchSingleProduct(Integer prodId);
	
	ProductDto addNewProduct(ProductDto prodDto, Integer catId, Integer supId);
	
	ProductDto updateProduct(ProductDto prodDto, Integer prodId);
	
	String deleteProduct(Integer prodId);
	
	List<ProductDto> fetchProductsByCategory(Integer catId);
	
	List<ProductDto> fetchProductsBySupplier(Integer supId);
	
	List<ProductDto> getByNameContaining(String prodName);
	
	List<ProductDto> recommendProducts(Integer userId);
}
