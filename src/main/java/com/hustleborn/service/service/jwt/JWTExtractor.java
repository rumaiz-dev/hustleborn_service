package com.hustleborn.service.service.jwt;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JWTExtractor {

	public static JWTTokenDetails extraction(Authentication authentication) {
		if (authentication instanceof JWTAuthentication jwtAuthentication) {
			return jwtAuthentication.getJwtTokenDetails();
		}
		throw new InsufficientAuthenticationException("User not authenticated");
	}

}
