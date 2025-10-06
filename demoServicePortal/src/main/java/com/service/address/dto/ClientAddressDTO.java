package com.service.address.dto;

import com.service.address.model.Address;
import com.service.address.model.City;
import com.service.model.Client;

public class ClientAddressDTO {

	private String name;
	private String StreetAddress;
	private Double latitude;
	private Double longitude;
	private Long clientId;
	private Long cityId;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStreetAddress() {
		return StreetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		StreetAddress = streetAddress;
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
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}	
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	
	public Address toEntity() {
		City c = new City();
		c.setCityId(this.cityId);
		
		Client cli = new Client();
		cli.setClientId(this.clientId);
		
		Address a = new Address();
		a.setCity(c);
		a.setClient(cli);
		a.setLatitude(this.latitude);
		a.setLongitude(this.longitude);
		a.setName(this.name);
		a.setStreetAddress(this.StreetAddress);
		
		return a;
	}
	
	public void updateEntity(Address entity) {
		City city = new City();
		city.setCityId(this.cityId);
		
		Client client = new Client();
		client.setClientId(this.clientId);
		
		entity.setCity(city);
		entity.setClient(client);
		entity.setName(this.name);
		entity.setStreetAddress(this.StreetAddress);
		entity.setLatitude(this.latitude);
		entity.setLongitude(this.longitude);
	}
}
