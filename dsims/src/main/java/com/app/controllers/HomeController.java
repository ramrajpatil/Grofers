package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.service.HomeService;
import com.app.service.ProductService;

@CrossOrigin
@RestController
@RequestMapping("/")
public class HomeController {
	@Autowired
	private ProductService prodService;

	@Autowired
	private HomeService homeService;

	@GetMapping
	public ResponseEntity<?> fetchAllProducts() {
		return new ResponseEntity<>(prodService.fetchAllProduct(), HttpStatus.OK);
	}

	@GetMapping("/dashboard/{email}")
	public ResponseEntity<?> fetchDashboard(@PathVariable String email) {
		return new ResponseEntity<>(homeService.fetchDashboardDetails(email), HttpStatus.OK);
	}
}