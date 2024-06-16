package com.grofers.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.grofers.dtos.CategoryDto;
import com.grofers.dtos.CategoryResponseDto;
import com.grofers.exceptions.NotFoundException;
import com.grofers.pojos.Category;
import com.grofers.repos.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	private CategoryRepository catRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CategoryResponseDto fetchAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		// Paging and sorting
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Category> pagedCategories = this.catRepo.findAll(pageable);

		List<Category> categories = pagedCategories.getContent();

		List<CategoryDto> catDtos = categories
				.stream()
				.map(c-> 
				this.mapper.map(c, CategoryDto.class))
				.collect(Collectors.toList());
		
		
		CategoryResponseDto resp = new CategoryResponseDto();
		resp.setCategory_list(catDtos);
		resp.setPageNumber(pagedCategories.getNumber());
		resp.setPageSize(pagedCategories.getSize());
		resp.setTotalElements(pagedCategories.getTotalElements());
		resp.setTotalPages(pagedCategories.getTotalPages());
		resp.setLastPage(pagedCategories.isLast());
		

		return resp;
	}
	
	@Override
	public CategoryDto fetchCategoryById(Integer catId) {
		
		Category category = this.catRepo.findById(catId)
		.orElseThrow(() -> 
		new NotFoundException("Category with id: "+catId+" does not exist"));

		return this.mapper.map(category, CategoryDto.class);
	}
	

	@Override
	public CategoryDto addNewCategory(CategoryDto catDto) {
		Category category = this.mapper.map(catDto, Category.class);
		
		Category savedCategory = this.catRepo.save(category);
		
		
		return this.mapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto catDto, Integer catId) {
		Category category = this.catRepo.findById(catId)
				.orElseThrow(() -> 
				new NotFoundException("Category with id: "+catId+" does not exist"));

		category.setName(catDto.getName());
		
		
		return this.mapper.map(category, CategoryDto.class);
	}

	@Override
	public String deleteCategory(Integer catId) {
		Category category = this.catRepo.findById(catId)
				.orElseThrow(() -> 
				new NotFoundException("Category with id: "+catId+" does not exist"));

		String catName = category.getName();
		this.catRepo.delete(category);
		
		
		return "Category with name: "+catName+" deleted successfully !!!";
	}

	

}
