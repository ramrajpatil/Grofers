package com.grofers.pojos;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "suppliers")
public class Supplier {

//	supplierId, name, and email.
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer supplierId;
	
	@Column(name = "supplier_name", nullable = false)
	private String name;
	
	@Column(name = "supplier_email", nullable = false, unique = true)
	private String email;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "supplier", orphanRemoval = true)
	private List<Product> products = new ArrayList<>();
	
}
