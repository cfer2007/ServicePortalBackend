package com.service.address.model;

import com.service.auth.model.UserRole;
import com.service.model.ProfessionalDocument;

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
	@JoinColumn(name = "cityId", referencedColumnName = "cityId", nullable = false)
	private City city;
	
	@ManyToOne
	@JoinColumn(name = "userRoleId", referencedColumnName = "userRoleId", nullable = false)
	private UserRole userRole;
	
	@ManyToOne
	@JoinColumn(name = "documentId", referencedColumnName = "professionalDocumentId", nullable = true)
	private ProfessionalDocument document;

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

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public ProfessionalDocument getDocument() {
		return document;
	}

	public void setDocument(ProfessionalDocument document) {
		this.document = document;
	}
}
