package com.grofers.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grofers.config.AppConstants;
import com.grofers.dtos.ProductDto;
import com.grofers.dtos.ProductResponseDto;
import com.grofers.dtos.ResponseDTO;
import com.grofers.services.IProductService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductRestController {

	@Autowired
	private IProductService prodService;
	
	private final Logger logger = LoggerFactory.getLogger(ProductRestController.class);
	
	@GetMapping("/")
	public ResponseEntity<ProductResponseDto> getAllProducts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.PRODUCT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
			){
		this.logger.info("In get all categories of : "+getClass().getName());
		ProductResponseDto dto = this.prodService.fetchAllProducts(pageNumber, pageSize, sortBy, sortDir);
		
		return ResponseEntity.ok(dto);
	}
	
	
	@GetMapping("/{prodId}")
	public ResponseEntity<ProductDto> getSingleProduct(@PathVariable Integer prodId){
		ProductDto dto = this.prodService.fetchSingleProduct(prodId);
	
		return ResponseEntity.ok(dto);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/category/{catId}/supplier/{supId}")
	public ResponseEntity<ProductDto> createProduct(
			@PathVariable Integer catId, @PathVariable Integer supId,
			@Valid @RequestBody ProductDto productDto){
		
		ProductDto newProduct = this.prodService.addNewProduct(productDto, catId, supId);
		
		return ResponseEntity.ok(newProduct);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/supplier/{supId}")
	public ResponseEntity<List<ProductDto>> getProductsBySupplier(@PathVariable Integer supId){
		
		List<ProductDto> prodList = this.prodService.fetchProductsBySupplier(supId);
		
		return ResponseEntity.ok(prodList);
	}
	
	// Getting products by category.
	@GetMapping("/category/{catId}")
	public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable Integer catId){
		
		List<ProductDto> prodList = this.prodService.fetchProductsByCategory(catId);
		
		return ResponseEntity.ok(prodList);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{prodId}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable Integer prodId, @Valid @RequestBody ProductDto productDto){
		
		ProductDto dto = this.prodService.updateProduct(productDto, prodId);
		
		return ResponseEntity.ok(dto);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{prodId}")
	public ResponseEntity<ResponseDTO> deleteProduct(@PathVariable Integer prodId){
		
		return ResponseEntity
				.ok(new ResponseDTO(this.prodService.deleteProduct(prodId), true));
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<ProductDto>> getProductsByKeyword(@RequestParam(value = "keyword", required = true) String keyword){
		this.logger.info(keyword);
		
		List<ProductDto> prodList = this.prodService.getByNameContaining(keyword);
		
		return ResponseEntity.ok(prodList);
	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/recommend/{userId}")
	public ResponseEntity<List<ProductDto>> recommendProducts(@PathVariable Integer userId){
		
		List<ProductDto> dtos = this.prodService.recommendProducts(userId);
		
		return ResponseEntity.ok(dtos);
	}
	
	
}
