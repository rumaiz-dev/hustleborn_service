package com.hustleborn.service.service.jwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
public class JWTAuthentication extends AbstractAuthenticationToken {

	private String principal;
	private JWTTokenDetails jwtTokenDetails;

	public JWTAuthentication(String principal, JWTTokenDetails jwtTokenDetails,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.jwtTokenDetails = jwtTokenDetails;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return "";
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public JWTTokenDetails getJwtTokenDetails() {
		return jwtTokenDetails;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public void setJwtTokenDetails(JWTTokenDetails jwtTokenDetails) {
		this.jwtTokenDetails = jwtTokenDetails;
	}
}