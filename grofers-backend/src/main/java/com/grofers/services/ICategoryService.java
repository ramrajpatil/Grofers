package com.grofers.services;

import com.grofers.dtos.CategoryDto;
import com.grofers.dtos.CategoryResponseDto;

public interface ICategoryService {

	
	CategoryResponseDto fetchAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	CategoryDto addNewCategory(CategoryDto catDto);
	
	CategoryDto updateCategory(CategoryDto catDto, Integer catId);
	
	String deleteCategory(Integer catId);
	
	CategoryDto fetchCategoryById(Integer catId);
	

}
