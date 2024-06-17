package com.grofers.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.grofers.config.AppConstants;
import com.grofers.dtos.ResponseDTO;
import com.grofers.dtos.SupplierDto;
import com.grofers.dtos.SupplierResponseDto;
import com.grofers.services.ISupplierService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/suppliers")
public class SupplierRestController {

    @Autowired
    private ISupplierService supService;

    private final Logger logger = LoggerFactory.getLogger(SupplierRestController.class);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<SupplierResponseDto> getAllSuppliers(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SUPPLIER_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

        logger.info("Fetching all suppliers: "+getClass().getName());

        SupplierResponseDto dto = supService.fetchAllSuppliers(pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{supId}")
    public ResponseEntity<SupplierDto> getSingleSupplier(@PathVariable Integer supId) {
        logger.info("Fetching supplier with ID: {}", supId);

        SupplierDto supplierDto = supService.fetchSupplierById(supId);

        return ResponseEntity.ok(supplierDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SupplierDto> createNewSupplier(@Valid @RequestBody SupplierDto supDto) {
        logger.info("Creating a new supplier");

        SupplierDto newSupplier = supService.addNewSupplier(supDto);

        return new ResponseEntity<>(newSupplier, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{supId}")
    public ResponseEntity<SupplierDto> updateSupplier(@PathVariable Integer supId, @Valid @RequestBody SupplierDto supDto) {
        logger.info("Updating supplier with ID: {}", supId);

        SupplierDto updatedSupplierDto = supService.updateSupplier(supDto, supId);

        return ResponseEntity.ok(updatedSupplierDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{supId}")
    public ResponseEntity<ResponseDTO> deleteSupplier(@PathVariable Integer supId) {
        logger.info("Deleting supplier with ID: {}", supId);

        String message = supService.deleteSupplier(supId);

        return ResponseEntity.ok(new ResponseDTO(message, true));
    }

}
