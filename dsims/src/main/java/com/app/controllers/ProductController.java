package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.pojos.Product;
import com.app.service.ProductService;
@CrossOrigin
@RestController
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductService prodService;

	@GetMapping("/getproducts")
	public ResponseEntity<?> fetchAllProductToManufacturer() {
		return new ResponseEntity<>(prodService.fetchAllProductById(), HttpStatus.OK);
	}

	@GetMapping("/myproducts/{userId}")
	public ResponseEntity<?> fetchAllProductBelongingToThisUser(@PathVariable Long userId) {
		return new ResponseEntity<>(prodService.fetchAllProductBelongingToThisUser(userId), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> fetchProductById(@PathVariable Long id) {
		return new ResponseEntity<>(prodService.fetchProductById(id), HttpStatus.OK);
	}

	@GetMapping("/byname/{name}")
	public ResponseEntity<?> fetchProductByName(@PathVariable String name) {
		return new ResponseEntity<>(prodService.fetchProductByName(name), HttpStatus.OK);
	}

	@GetMapping("/bycontent/{name}")
	public ResponseEntity<?> fetchProductByContent(@PathVariable String name) {
		return new ResponseEntity<>(prodService.fetchProductsByContent(name), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		return new ResponseEntity<>(prodService.addProduct(product), HttpStatus.CREATED);
	}

	@PostMapping("/saveallproducts")
	public ResponseEntity<?> addAllProducts(@RequestBody List<Product> products) {
		return new ResponseEntity<>(prodService.addAllProducts(products), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
		return new ResponseEntity<>(prodService.updateProduct(id, product), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		return new ResponseEntity<>(prodService.deleteProduct(id), HttpStatus.OK);
	}

}