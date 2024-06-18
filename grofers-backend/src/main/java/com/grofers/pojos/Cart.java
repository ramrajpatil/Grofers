package com.grofers.pojos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;
    
    @CreationTimestamp
	@Column(nullable = false)
    private LocalDate createdOn;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDate updatedOn;
    
	@Min(value = 0, message = "The Cart Total must be greater than or equal to zero")
    private double totalAmount;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = {"role","cart", "orders", "password","enabled","authorities","accountNonExpired","credentialsNonExpired","accountNonLocked" })
    private User user;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = "cart")
    private List<CartItem> cartItems = new ArrayList<>();

    public double getTotalAmount() {
    	
    	this.totalAmount = cartItems.stream()
        .mapToDouble(CartItem::getTotalPrice)
        .sum();
    	
        return this.totalAmount;
    }
    
    // To create cart when user is created.
    public Cart(User user) {
    	super();
    	this.user=user;
    }

}
