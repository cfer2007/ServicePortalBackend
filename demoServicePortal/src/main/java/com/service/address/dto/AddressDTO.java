package com.service.address.dto;

import com.service.address.model.Address;
import com.service.address.model.Area;
import com.service.model.Client;
import com.service.model.Professional;

public class AddressDTO {

	private String StreetAddress;
	private Double latitude;
	private Double longitude;
	private Long clientId;
	private Long ProfessionalId;
	private Long areaId;
	
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
	public Long getProfessionalId() {
		return ProfessionalId;
	}
	public void setProfessionalId(Long professionalId) {
		ProfessionalId = professionalId;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	
	public void updateEntity(Address entity) {
		Area area = new Area();
		area.setAreaId(this.areaId);
		
		Client client = new Client();
		client.setClientId(this.clientId);
		
		Professional professional = new Professional();
		professional.setProfessionalId(this.ProfessionalId);
		
		entity.setArea(area);
		entity.setClient(client);
		entity.setProfessional(professional);
		entity.setStreetAddress(this.StreetAddress);
		entity.setLatitude(this.latitude);
		entity.setLongitude(this.longitude);
	}
}
