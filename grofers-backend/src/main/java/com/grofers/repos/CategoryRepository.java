package com.grofers.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grofers.pojos.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
