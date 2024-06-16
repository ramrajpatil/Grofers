package com.grofers.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.grofers.securitys.CustomBasicAuthenticationEntryPoint;
import com.grofers.securitys.CustomUserDetailService;
import com.grofers.securitys.JWTAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebMvc
public class SecurityConfig {

    public static final String[] PUBLIC_URLS = { 
        "/auth/login", 
        "/users", 
        "/v3/api-docs",
        "/v2/api-docs", 
        "/swagger-resources/**", 
        "/swagger-ui*/**", 
        "/webjars/**"
    };
    
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JWTAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())
            .authorizeHttpRequests(auths -> auths
                .requestMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(exceptionHandling -> 
                exceptionHandling
                    .accessDeniedHandler(customAccessDeniedHandler())
                    .authenticationEntryPoint(customAuthenticationEntryPoint())
            )
            .authenticationProvider(daoAuthenticationProvider())
            .addFilterBefore(this.filter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setExposedHeaders(Arrays.asList("Authorization"));
            return configuration;
        };
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"You are not authorized to access this resource.\", \"success\": false}");
        };
    }

    @Bean
    public CustomBasicAuthenticationEntryPoint customAuthenticationEntryPoint() {
        CustomBasicAuthenticationEntryPoint entryPoint = new CustomBasicAuthenticationEntryPoint();
        entryPoint.afterPropertiesSet();
        return entryPoint;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.customUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
