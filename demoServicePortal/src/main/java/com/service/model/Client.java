package com.service.model;

import com.service.auth.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name="client")
@Entity(name="client")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long clientId;
	
	@Column
	private String name;
	
	@Column
	private String lastName;
	
	@ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
	private User user;

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
