package com.service.address.model;

import com.service.model.Client;
import com.service.model.Professional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "address")
@Entity(name = "address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long addressId;
	
	@Column
	private String name;
	
	@Column
	private String streetAddress;
	
	@Column
	private Double latitude;
	
	@Column
	private Double longitude;
	
	@ManyToOne
	@JoinColumn(name = "clientId", referencedColumnName = "clientId", nullable = true)
	private Client client;
	
	@ManyToOne
	@JoinColumn(name = "professionalId", referencedColumnName = "professionalId", nullable = true)
	private Professional professional;
	
	@ManyToOne
	@JoinColumn(name = "cityId", referencedColumnName = "cityId", nullable = false)
	private City city;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Professional getProfessional() {
		return professional;
	}

	public void setProfessional(Professional professional) {
		this.professional = professional;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}
