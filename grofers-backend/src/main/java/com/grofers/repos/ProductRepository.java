package com.grofers.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grofers.pojos.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
