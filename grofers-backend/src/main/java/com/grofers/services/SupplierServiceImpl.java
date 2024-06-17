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

import com.grofers.dtos.SupplierDto;
import com.grofers.dtos.SupplierResponseDto;
import com.grofers.exceptions.DuplicateEntryException;
import com.grofers.exceptions.NotFoundException;
import com.grofers.pojos.Supplier;
import com.grofers.repos.SupplierRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SupplierServiceImpl implements ISupplierService {

	
	@Autowired
	private SupplierRepository supRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public SupplierResponseDto fetchAllSuppliers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		// Paging and sorting
		
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Supplier> pagedSuppliers = this.supRepo.findAll(pageable);
		
		List<Supplier> suppliers = pagedSuppliers.getContent();
		
		List<SupplierDto> supplierDtos = suppliers.stream().map(s -> 
		this.mapper.map(s, SupplierDto.class))
		.collect(Collectors.toList());
		
		SupplierResponseDto resp = new SupplierResponseDto();
		resp.setSupplier_list(supplierDtos);
		resp.setPageNumber(pagedSuppliers.getNumber());
		resp.setPageSize(pagedSuppliers.getSize());
		resp.setTotalElements(pagedSuppliers.getTotalElements());
		resp.setTotalPages(pagedSuppliers.getTotalPages());
		resp.setLastPage(pagedSuppliers.isLast());
		
		return resp;
	}

	@Override
	public SupplierDto fetchSupplierById(Integer supId) {
		Supplier supplier = this.supRepo.findById(supId)
		.orElseThrow(() -> 
		new NotFoundException("Supplier with id: "+supId+" does not exist"));
		
		return this.mapper.map(supplier, SupplierDto.class);
	}
	
	@Override
	public SupplierDto addNewSupplier(SupplierDto supDto) {
		if (supRepo.existsByEmail(supDto.getEmail())) {
			throw new DuplicateEntryException("Supplier email must be unique.");
		} else {

			Supplier supplier = this.mapper.map(supDto, Supplier.class);
			Supplier savedSupplier = this.supRepo.save(supplier);

			return this.mapper.map(savedSupplier, SupplierDto.class);
		}
	}


	@Override
	public SupplierDto updateSupplier(SupplierDto supDto, Integer supId) {

		Supplier supplier = this.supRepo.findById(supId)
				.orElseThrow(() -> 
				new NotFoundException("Supplier with id: "+supId+" does not exist"));
		supplier.setName(supDto.getName());
		supplier.setEmail(supDto.getEmail());
		
		return this.mapper.map(supplier, SupplierDto.class);
	}

	@Override
	public String deleteSupplier(Integer supId) {

		Supplier supplier = this.supRepo.findById(supId)
				.orElseThrow(() -> 
				new NotFoundException("Supplier with id: "+supId+" does not exist"));
		String supName = supplier.getName();
		this.supRepo.delete(supplier);
		
		return "Supplier with name: "+supName+" was deleted successfully !!!";
	}

}
