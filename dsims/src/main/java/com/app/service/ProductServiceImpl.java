package com.app.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.centralexception.CustomCentralException;
import com.app.pojos.Product;
import com.app.pojos.User;
import com.app.repositories.ProductRepo;
import com.app.repositories.UserRepo;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepo prodRepo;

	@Autowired
	UserRepo userRepo;

	@Override
	public Product fetchProductById(Long id) {
		return prodRepo.findById(id).orElseThrow(() -> new CustomCentralException("Invalid Product Id!!!"));
	}

	@Override
	public Product fetchProductByName(String name) {
		return prodRepo.findByName(name);
	}

	@Override
	public List<Product> fetchProductsByContent(String name) {
		Product product = prodRepo.findByName(name);
		return prodRepo.findByContent(product.getContent());
	}

	@Override
	public String deleteProduct(Long id) {
		Product product = prodRepo.findById(id)
				.orElseThrow(() -> new CustomCentralException("Cannot delete product as invalid Id!!!"));
		prodRepo.delete(product);
		User user = product.getUser();
		List<Product> products = user.getProducts();
		products.remove(product);
		user.setProducts(products);
		return "Product with id: " + id + " deleted successfully!";
	}

	@Override
	public String updateProduct(Long id, Product product) {
		Product persistentProduct = prodRepo.findById(id)
				.orElseThrow(() -> new CustomCentralException("Product cannot be updated as it is not persistent!!!"));
		User user = persistentProduct.getUser();
		List<Product> products = user.getProducts();
		Product updatedProduct = null;
		for (Product listProduct : products) {
			if (listProduct.getId() == product.getId()) {
				listProduct.setContent(product.getContent());
				listProduct.setImageURL(product.getImageURL());
				listProduct.setMargin(product.getMargin());
				listProduct.setName(product.getName());
				listProduct.setPrice(product.getPrice());
				listProduct.setUser(product.getUser());
				updatedProduct = listProduct;
			}
		}
		user.setProducts(products);
		prodRepo.save(updatedProduct);

		return "Product with id: " + updatedProduct.getId() + " updated successfully!";
	}

	@Override
	public String addProduct(Product product) {
		product.setMargin(0.20f);
		Product savedProduct = prodRepo.save(product);
		return "Product updated successfully with id: " + savedProduct.getId();
	}

	@Override
	public List<Product> fetchAllProductById() {
		return prodRepo.findAll();
	}

	@Override
	public String addAllProducts(List<Product> products) {
		for (Product product : products) {
			product.setMargin(0.20f);
			User user = userRepo.findById(product.getUser().getId())
					.orElseThrow(() -> new CustomCentralException("User Id Invalid!"));
			product.setUser(user);
			prodRepo.save(product);
		}
		return "All products saved successflly!";
	}

	@Override
	public List<Product> fetchAllProductBelongingToThisUser(Long userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new CustomCentralException("User Id Invalid!"));
		return prodRepo.findByUser(user);
	}

	@Override
	public List<Product> fetchAllProduct() {
		return prodRepo.findAll();
	}
}