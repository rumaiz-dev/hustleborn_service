package com.hustleborn.service.service.auth;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import com.hustleborn.service.model.auth.LoginRequest;
import com.hustleborn.service.model.auth.RegisterRequest;
import com.hustleborn.service.model.userprofiles.UserProfiles;
import com.hustleborn.service.model.users.Users;
import com.hustleborn.service.repository.users.UsersRepository;
import com.hustleborn.service.service.jwt.JWTService;
import com.hustleborn.service.utils.responses.ApiResponse;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTService jwtService;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public ApiResponse login(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		Users user = usersRepository.findByUsername(loginRequest.getUsername()).orElseThrow();

		String token = jwtService.generateToken(user.getUserProfile().getEmail(), user.getUsername(), user.getId());

		Map<String, Object> data = new HashMap<>();
		data.put("token", token);
		return new ApiResponse(true, "Login successful", data);
	}

	public ApiResponse register(RegisterRequest registerRequest) {
		if (usersRepository.existsByUsernameAndEmail(registerRequest.getUsername(), registerRequest.getEmail())) {
			return new ApiResponse(false, "Username or Email already exists", null);
		}
		Users user = new Users();
		user.setUsername(registerRequest.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setDisplayName(registerRequest.getDisplayName());
		user.setEnabled(true);
		user.setCreatedAt(LocalDate.now());
		user.setUpdatedAt(LocalDate.now());

		UserProfiles profile = new UserProfiles();
		profile.setEmail(registerRequest.getEmail());
		profile.setUser(user);
		user.setUserProfile(profile);

		Users savedUser = usersRepository.save(user);

		Map<String, Object> data = new HashMap<>();
		data.put("username", savedUser.getUsername());
		data.put("userId", savedUser.getId());

		return new ApiResponse(true, "User registered successfully", data);
	}
}