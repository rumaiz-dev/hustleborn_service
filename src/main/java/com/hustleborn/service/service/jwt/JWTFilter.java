package com.hustleborn.service.service.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hustleborn.service.config.CustomUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	JWTService jwtService;

	@Autowired
	ApplicationContext context;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = jwtService.extractUsername(token);

		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = context.getBean(CustomUserDetailService.class).loadUserByUsername(username);

			if (jwtService.validateToken(token, userDetails)) {
				Long userId = jwtService.extractUserId(token);
				String email = jwtService.extractEmail(token);
				JWTTokenDetails jwtTokenDetails = new JWTTokenDetails();
				jwtTokenDetails.setUserId(userId);
				jwtTokenDetails.setUsername(username);
				jwtTokenDetails.setEmail(email);
				jwtTokenDetails.setSubject(username);

				JWTAuthentication authToken = new JWTAuthentication(username, jwtTokenDetails,
						userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request, response);

	}
}
