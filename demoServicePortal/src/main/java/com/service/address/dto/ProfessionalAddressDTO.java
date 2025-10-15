package com.service.address.dto;

import com.service.address.model.Address;
import com.service.address.model.AddressProfessional;
import com.service.address.model.City;
import com.service.model.Professional;
import com.service.model.ProfessionalDocument;

public class ProfessionalAddressDTO {

	private String name;
	private String StreetAddress;
	private Double latitude;
	private Double longitude;
	private Long professionalId;
	private Long cityId;
	private Long professionalDocumentId;
		
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
	public Long getProfessionalId() {
		return professionalId;
	}
	public void setProfessionalId(Long professionalId) {
		this.professionalId = professionalId;
	}	
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public Long getProfessionalDocumentId() { 
		return professionalDocumentId; 
	}
    public void setProfessionalDocumentId(Long professionalDocumentId) { 
    	this.professionalDocumentId = professionalDocumentId; 
    }
	
	public Address toEntity() {
		City c = new City();
		c.setCityId(this.cityId);
		
		Address a = new Address();
		a.setCity(c);
		a.setLatitude(this.latitude);
		a.setLongitude(this.longitude);
		a.setName(this.name);
		a.setStreetAddress(this.StreetAddress);
		
		return a;
	}
	
	public void updateEntity(Address entity) {
		City city = new City();
		city.setCityId(this.cityId);
		
		entity.setCity(city);
		entity.setName(this.name);
		entity.setStreetAddress(this.StreetAddress);
		entity.setLatitude(this.latitude);
		entity.setLongitude(this.longitude);
	}
	
	public AddressProfessional toAddressProfessional(Address address) {
	    Professional profesional = new Professional();
	    profesional.setProfessionalId(this.professionalId);
	    
	    ProfessionalDocument document = new ProfessionalDocument();
        document.setProfessionalDocumentId(this.professionalDocumentId);

	    AddressProfessional link = new AddressProfessional();
	    link.setAddress(address);
	    link.setProfessional(profesional);
	    link.setDocument(document);
	    return link;
	}
}
