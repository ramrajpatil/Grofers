package com.grofers.services;

import com.grofers.dtos.SupplierDto;
import com.grofers.dtos.SupplierResponseDto;

public interface ISupplierService {

	SupplierResponseDto fetchAllSuppliers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	SupplierDto addNewSupplier(SupplierDto supDto);
	
	SupplierDto fetchSupplierById(Integer supId);
	
	SupplierDto updateSupplier(SupplierDto supDto, Integer supId);
	
	String deleteSupplier(Integer supId);
}
