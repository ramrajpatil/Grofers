package com.grofers.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.grofers.dtos.CategoryDto;
import com.grofers.dtos.CategoryResponseDto;
import com.grofers.dtos.ResponseDTO;
import com.grofers.services.ICategoryService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/categories")
public class CategoryRestController {

	@Autowired
	private ICategoryService catService;
	
	private final Logger logger = LoggerFactory.getLogger(CategoryRestController.class);
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	public ResponseEntity<CategoryResponseDto> getAllCategories(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.CATEGORY_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
			){
		
		this.logger.info("In get all categories of : "+getClass().getName());
		CategoryResponseDto dto = this.catService.fetchAllCategories(pageNumber, pageSize, sortBy, sortDir);
		
		
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/{catId}")
	public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable Integer catId){
		
		CategoryDto categoryDto = this.catService.fetchCategoryById(catId);
		
		return ResponseEntity.ok(categoryDto);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryDto> createNewCategory(@Valid @RequestBody CategoryDto catDto){
		
		CategoryDto categoryDto = this.catService.addNewCategory(catDto);
		
		return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{catId}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer catId,@Valid @RequestBody CategoryDto catDto ){
		
		CategoryDto categoryDto = this.catService.updateCategory(catDto, catId);
		
		return ResponseEntity.ok(categoryDto);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{catId}")
	public ResponseEntity<ResponseDTO> deleteCategory(@PathVariable Integer catId){
		String message = this.catService.deleteCategory(catId);
		
		return ResponseEntity.ok(new ResponseDTO(message, true));
	}
	
}
