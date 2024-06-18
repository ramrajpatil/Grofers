package com.app.service;

import java.util.List;

import com.app.pojos.Product;

public interface ProductService {

	Product fetchProductById(Long id);

	Product fetchProductByName(String name);

	List<Product> fetchProductsByContent(String name);

	String deleteProduct(Long id);

	String updateProduct(Long id, Product product);

	String addProduct(Product product);

	List<Product> fetchAllProductById();

	List<Product> fetchAllProduct();

	String addAllProducts(List<Product> products);

	List<Product> fetchAllProductBelongingToThisUser(Long userId);
}
