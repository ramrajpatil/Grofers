package com.app.dto;

import java.time.LocalDate;

import com.app.pojos.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetOrder {

	private Long userId;
	private LocalDate startDate;
	private LocalDate endDate;
	private Status status;

}
