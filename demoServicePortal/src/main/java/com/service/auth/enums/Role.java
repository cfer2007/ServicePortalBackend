package com.service.auth.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
	ADMIN,
	PROFESSIONAL,
	USER;

	@Override
	public String getAuthority() {
		return this.name();
	}
}
