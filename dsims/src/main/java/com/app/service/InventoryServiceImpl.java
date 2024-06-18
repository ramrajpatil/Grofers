package com.app.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.centralexception.CustomCentralException;
import com.app.pojos.Inventory;
import com.app.pojos.InventoryItem;
import com.app.pojos.Product;
import com.app.pojos.User;
import com.app.repositories.InventoryItemRepo;
import com.app.repositories.InventoryRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.UserRepo;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {
	@Autowired
	InventoryRepo inventoryRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	ProductRepo prodRepo;

	@Autowired
	InventoryItemRepo inventoryItemRepo;

	@Override
	public String addToInventory(InventoryItem inventoryItem) {
		Inventory inventory = inventoryRepo.findById(inventoryItem.getInventory().getId())
				.orElseThrow(() -> new CustomCentralException("Invalid Inventory id, try again!!!"));
		Product product = prodRepo.findById(inventoryItem.getProduct().getId())
				.orElseThrow(() -> new CustomCentralException("Product cannot be updated as it is not persistent!!!"));
		inventoryItem.setPrice(inventoryItem.getQuantity() * product.getPrice());
		List<InventoryItem> list = inventory.getItems();
		list.add(inventoryItem);
		inventory.setItems(list);
		inventory.setTotalPrice(inventory.getTotalPrice() + inventoryItem.getPrice());
		inventory.setTotalQuantity(inventory.getTotalQuantity() + inventoryItem.getQuantity());

		inventoryItemRepo.save(inventoryItem);
		inventoryRepo.save(inventory);
		return "Resources credidted to the Inventory with id: " + inventoryItem.getInventory().getId()
				+ " and it is been updated successfully!";
	}

	@Override
	public Inventory fetchInventory(Long id) {
		User user = userRepo.findById(id).orElseThrow(() -> new CustomCentralException("User Id Invalid!"));
		return user.getInventory();
	}

	@Override
	public String removeFromInventory(Long inventoryId, Long inventoryItemId) {

		Inventory inventory = inventoryRepo.findById(inventoryId)
				.orElseThrow(() -> new CustomCentralException("Invalid Inventory id, try again!!!"));

		InventoryItem inventoryItem = inventoryItemRepo.findById(inventoryItemId)
				.orElseThrow(() -> new CustomCentralException("Invalid InventoryItem id"));

		List<InventoryItem> list = inventory.getItems();
		list.remove(inventoryItem);
		inventory.setItems(list);

		inventory.setTotalPrice(inventory.getTotalPrice() - inventoryItem.getPrice());
		inventory.setTotalQuantity(inventory.getTotalQuantity() - inventoryItem.getQuantity());

		inventoryItemRepo.delete(inventoryItem);
		inventoryRepo.save(inventory);

		return "Resources deducted from Inventory with id: " + inventoryId + " and it is updated successfully!";
	}

	@Override
	public String addAllToInventory(List<InventoryItem> inventoryItems) {
		for (InventoryItem inventoryItem : inventoryItems) {
			Inventory inventory = inventoryRepo.findById(inventoryItem.getInventory().getId())
					.orElseThrow(() -> new CustomCentralException("Invalid Inventory id, try again!!!"));

			Product product = prodRepo.findById(inventoryItem.getProduct().getId()).orElseThrow(
					() -> new CustomCentralException("Product cannot be updated as it is not persistent!!!"));

			inventoryItem.setPrice(inventoryItem.getQuantity() * product.getPrice());
			List<InventoryItem> list = inventory.getItems();
			list.add(inventoryItem);
			inventory.setItems(list);
			inventory.setTotalPrice(inventory.getTotalPrice() + inventoryItem.getPrice());
			inventory.setTotalQuantity(inventory.getTotalQuantity() + inventoryItem.getQuantity());

			inventoryItemRepo.save(inventoryItem);
			inventoryRepo.save(inventory);
		}
		return "Resources credidted to respective Inventories successfully!";
	}

	@Override
	public List<InventoryItem> fetchInventoryItemsByProductId(Long productId) {
		Product product = prodRepo.findById(productId)
				.orElseThrow(() -> new CustomCentralException("Invalid Product Id"));

		Inventory manufacturerInventory = product.getUser().getInventory();

		return manufacturerInventory.getItems().stream().filter(invItem -> invItem.getProduct().equals(product))
				.collect(Collectors.toList());
	}
}
