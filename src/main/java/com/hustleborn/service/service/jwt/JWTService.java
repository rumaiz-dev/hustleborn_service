package com.hustleborn.service.service.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTService {

	private static final String SECRET_KEY = "your-super-secure-secret-key-change-this";

	private final long EXPIRATION_TIME = 15_552_000_000L;

	public String generateToken(String email, String username, Long userId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userId);
		claims.put("email", email);

		return Jwts.builder().claims(claims).subject(username).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(getSigningKey()).compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Long extractUserId(String token) {
		return extractClaim(token, claims -> {
			Object userId = claims.get("userId");
			return userId != null ? ((Number) userId).longValue() : null;
		});
	}

	public String extractEmail(String token) {
		return extractClaim(token, claims -> {
			Object email = claims.get("email");
			return email != null ? ((String) email).toString() : null;
		});
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}

	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64
				.decode(Base64.getEncoder().encodeToString(SECRET_KEY.getBytes(StandardCharsets.UTF_8)));
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
