package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.pojos.InventoryItem;
import com.app.pojos.Product;
import java.util.List;

public interface InventoryItemRepo extends JpaRepository<InventoryItem, Long> {
	
	List<InventoryItem> findByProduct(Product product);
}
