package com.app.service;

import java.util.List;

import com.app.pojos.Inventory;
import com.app.pojos.InventoryItem;

public interface InventoryService {

	String addToInventory(InventoryItem inventoryItem);

	Inventory fetchInventory(Long id);

	String removeFromInventory(Long inventoryId, Long inventoryItemId);

	String addAllToInventory(List<InventoryItem> inventoryItems);

	List<InventoryItem> fetchInventoryItemsByProductId(Long productId);
}
