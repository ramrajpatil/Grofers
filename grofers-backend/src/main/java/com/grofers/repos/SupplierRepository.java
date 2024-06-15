package com.grofers.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grofers.pojos.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

}
