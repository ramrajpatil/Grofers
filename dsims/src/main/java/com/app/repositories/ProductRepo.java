package com.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.pojos.Product;
import com.app.pojos.User;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

	Optional<Product> findById(Long id);

	Product findByName(String name);

	List<Product> findByContent(String content);

	List<Product> findByUser(User user);

	void delete(Product entity);
}
