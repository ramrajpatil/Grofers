package com.app.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.pojos.Inventory;
import com.app.pojos.Product;
import com.app.pojos.User;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
	@Query("SELECT i FROM Inventory i JOIN i.items ii WHERE ii.expDate BETWEEN :startDate AND :endDate AND i.user = :user")
	List<Inventory> findExpiringStocksForUserAndPeriod(@Param("user") User user,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query("SELECT i FROM Inventory i JOIN i.items ii WHERE ii.product = :product AND i.user = :user")
	Inventory findByUserAndItemsWithGivenProduct(@Param("user") User user, @Param("product") Product product);

}