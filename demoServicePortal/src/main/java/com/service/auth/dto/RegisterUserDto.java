package com.service.auth.dto;

import java.util.List;

import com.service.auth.enums.Role;

public class RegisterUserDto {
    private String email;
    
    private String password;
    
    private String fullName;
    
    private List<Role> roles;

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
	
	public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }



	public static class Builder {
		 private String email;
		 private String password;
		 private String fullName;
		 private List<Role> roles;
		
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
		
		public Builder setRoles(List<Role> roles) {
			this.roles = roles;
			return this;
		}
		 
		public RegisterUserDto build() {
			return new RegisterUserDto(email,password,fullName, roles);
		}
		 
	}

	public RegisterUserDto(String email, String password, String fullName, List<Role> roles) {
		super();
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.roles = roles;
	}
	
   
}