package com.service.dto;

import com.service.model.Client;

public class ClientDTO {
	private String name;
	private String lastName;
	private String phone;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void updateEntity(Client entity) {
		entity.setName(this.name);
		entity.setLastName(this.lastName);
		entity.setPhone(this.phone);
	}
}
