package com.app.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.pojos.Credentails;

@RestController
@CrossOrigin
public class AuthController {

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/authenticate")
	public ResponseEntity<String> authenticate(@RequestBody Credentails cred) {

		try {
			Authentication auth = new UsernamePasswordAuthenticationToken(cred.getEmail(), cred.getPassword());
			auth = authManager.authenticate(auth);
			User user = (User) auth.getPrincipal();
			String Role = null;
			Collection<GrantedAuthority> authorities = user.getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {
				Role = grantedAuthority.getAuthority();
				System.out.println(Role.toString());
			}
			String token = jwtUtils.generateToken(user.getUsername(), Role);
			return ResponseEntity.ok(token);
		} catch (BadCredentialsException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
