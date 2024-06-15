package com.grofers.pojos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

	// unique userId, name, email, password, and role.

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(length = 20, nullable = false)
	private String name;

	@Column(length = 30, nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Product> products = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Order> orders = new HashSet<>();// to avoid duplication of order.

	public User(String name, String email, String password, UserRole role) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<SimpleGrantedAuthority> grantedAuths = new ArrayList<>();

		grantedAuths.add(new SimpleGrantedAuthority(UserRole.ROLE_ADMIN.name()));
		grantedAuths.add(new SimpleGrantedAuthority(UserRole.ROLE_CUSTOMER.name()));

		return grantedAuths;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	// Method to retrieve the password associated with the user
	// Already implemented using getters.

	// Method to determine if the user's account is not expired
	@Override
	public boolean isAccountNonExpired() {
		return true; // Assuming no account expiration logic for this example
	}

	// Method to determine if the user's account is not locked
	@Override
	public boolean isAccountNonLocked() {
		return true; // Assuming no account locking logic for this example
	}

	// Method to determine if the user's credentials are not expired
	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Assuming no credential expiration logic for this example
	}

	// Method to determine if the user is enabled
	@Override
	public boolean isEnabled() {
		return true; // Assuming all users are enabled for this example
	}



	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", email=" + email + ", password=" + password + ", role="
				+ role + "]";
	}

}
