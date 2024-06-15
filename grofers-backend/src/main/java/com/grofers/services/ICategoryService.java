package com.grofers.services;

import java.util.List;

import com.grofers.dtos.CategoryDto;

public interface ICategoryService {

	
	List<CategoryDto> fetchAllCategories();
}
