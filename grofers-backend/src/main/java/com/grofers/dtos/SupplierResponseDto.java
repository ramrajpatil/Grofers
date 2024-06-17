package com.grofers.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponseDto {

	private List<SupplierDto> supplier_list;
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean isLastPage;
}
