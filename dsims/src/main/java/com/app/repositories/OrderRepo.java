package com.app.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.pojos.Order;
import com.app.pojos.Product;
import com.app.pojos.Status;
import com.app.pojos.User;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

	List<Order> findByDeliveryDateBetweenAndFrom(LocalDate startDate, LocalDate endDate, User wholesaler);

	List<Order> findByDeliveryDateBetweenAndTo(LocalDate startDate, LocalDate endDate, User manufacturer);

	List<Order> findByStatusAndTo(Status status, User manufacturer);

	List<Order> findByStatusAndFrom(Status status, User manufacturer);

	List<Order> findByTo(User user);

	List<Order> findByFrom(User user);

	@Query("SELECT o FROM Order o JOIN o.orderDetails od WHERE o.id = :id")
	Order findByOrderIdWithAllOrderDetails(@Param("id") Long id);

	@Query("SELECT o FROM Order o JOIN o.orderDetails od WHERE o.status = 'PLACED' AND od.product = :product")
	List<Order> findByStatusAndProduct(@Param("product") Product product);
}
