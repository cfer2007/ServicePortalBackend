package com.service.auth.dto;

import com.service.auth.enums.Role;

public class RegisterUserDto {
    private String email;
    
    private String password;
    
    private String fullName;
    
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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
		 private String fullName;
		 private Role role;
		
		public Builder setEmail(String email) {
			this.email = email;
			return this;
		}
		
		public Builder setPassword(String password) {
			this.password = password;
			return this;			
		}
		
		public Builder setFullName(String fullName) {
			this.fullName = fullName;
			return this;
		}
		
		public Builder setRole(Role role) {
			this.role = role;
			return this;
		}
		 
		public RegisterUserDto build() {
			return new RegisterUserDto(email,password,fullName, role);
		}
		 
	}

	public RegisterUserDto(String email, String password, String fullName, Role role) {
		super();
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.role = role;
	}
	
   
}