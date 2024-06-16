package com.grofers.services;

import java.util.List;

import com.grofers.dtos.CategoryDto;

public interface ICategoryService {

	
	List<CategoryDto> fetchAllCategories();
	
	CategoryDto createCategory(CategoryDto catDto);
	
	CategoryDto updateCategory(CategoryDto catDto, Integer catId);
	
	String deleteCategory(Integer catId);
	
	CategoryDto fetchCategoryById(Integer catId);
	

}
