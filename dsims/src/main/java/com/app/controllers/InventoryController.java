package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.pojos.InventoryItem;
import com.app.service.InventoryService;

@CrossOrigin
@RestController
@RequestMapping("/inventory")
public class InventoryController {
	@Autowired
	InventoryService iService;

	@GetMapping("/show/{id}")
	public ResponseEntity<?> showInventory(@PathVariable Long id) {
		return new ResponseEntity<>(iService.fetchInventory(id), HttpStatus.OK);
	}

	@GetMapping("/getstock/{productId}")
	public ResponseEntity<?> getInventoryItems(@PathVariable Long productId) {
		return new ResponseEntity<>(iService.fetchInventoryItemsByProductId(productId), HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<?> addToInventory(@RequestBody InventoryItem inventoryItem) {
		return new ResponseEntity<>(iService.addToInventory(inventoryItem), HttpStatus.OK);
	}

	@PostMapping("/addall")
	public ResponseEntity<?> addAllToInventory(@RequestBody List<InventoryItem> inventoryItems) {
		return new ResponseEntity<>(iService.addAllToInventory(inventoryItems), HttpStatus.OK);
	}

	@DeleteMapping("/remove/{inventoryId}/{inventoryItemId}")
	public ResponseEntity<?> removeFromInventory(@PathVariable Long inventoryId, @PathVariable Long inventoryItemId) {
		return new ResponseEntity<>(iService.removeFromInventory(inventoryId, inventoryItemId), HttpStatus.OK);
	}

}