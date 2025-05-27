package com.service.auth.dto;


public class RegisterUserDto {
    private String email;
    
    private String password;
    
    private String fullName;

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
	
	public static class Builder {
		 private String email;
		 private String password;
		 private String fullName;
		
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
		 
		public RegisterUserDto build() {
			return new RegisterUserDto(email,password,fullName);
		}
		 
	}

	public RegisterUserDto(String email, String password, String fullName) {
		super();
		this.email = email;
		this.password = password;
		this.fullName = fullName;
	}
	
   
}