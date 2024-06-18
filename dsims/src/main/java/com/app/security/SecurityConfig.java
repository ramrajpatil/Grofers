package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	JwtRequestFilter jwtRequestFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
		.and()
		.csrf()
		.disable()
		.authorizeRequests()
				.antMatchers("/**").permitAll()
				.antMatchers("/product/**").hasRole("MANUFACTURER")
				.antMatchers("/product/getproducts").hasRole("WHOLESALER")
				.antMatchers("/inventory/**").hasRole("MANUFACTURER").antMatchers("/cart/**").hasRole("WHOLESALER").antMatchers("/inventory/show")
				.hasRole("WHOLESALER").antMatchers("/order/confirm").hasRole("MANUFACTURER")
				.antMatchers("/order/bystatus").hasRole("MANUFACTURER").antMatchers("/order/bydates")
				.hasRole("MANUFACTURER").antMatchers("/order/place").hasRole("WHOLESALER").antMatchers("/order/bydates")
				.hasRole("WHOLESALER").antMatchers("/order/bystatus").hasRole("WHOLESALER")
				.antMatchers("/", "/authenticate").permitAll().antMatchers("/user/**").permitAll()
				// .antMatchers("/product/**").permitAll()
				.and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

}