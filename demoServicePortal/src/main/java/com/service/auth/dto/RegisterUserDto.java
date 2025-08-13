package com.service.auth.dto;

import com.service.auth.enums.Role;

public class RegisterUserDto {
    private String email;
    
    private String password;
    
    private String name;
    
    private Role role;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public static class Builder {
		 private String email;
		 private String password;
		 private String name;
		 private Role role;
		
		public Builder setEmail(String email) {
			this.email = email;
			return this;
		}
		
		public Builder setPassword(String password) {
			this.password = password;
			return this;			
		}
		
		public Builder setName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder setRoles(Role role) {
			this.role = role;
			return this;
		}
		 
		public RegisterUserDto build() {
			return new RegisterUserDto(email,password,name, role);
		}
		 
	}

	public RegisterUserDto(String email, String password, String name, Role role) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.role = role;
	}
	
   
}