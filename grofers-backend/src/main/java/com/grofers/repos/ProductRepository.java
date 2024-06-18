package com.grofers.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grofers.pojos.Category;
import com.grofers.pojos.Product;
import com.grofers.pojos.Supplier;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findByCategory( Category category);
	
	List<Product> findBySupplier(Supplier supplier);
	
	List<Product> findByProductNameContaining(String prodName);
	
	boolean existsByProductName(String productName);
	
	List<Product> findTop2ByCategory(Category catList);
}
