package com.service.address.dto;

import com.service.address.model.Address;
import com.service.address.model.City;
import com.service.auth.model.UserRole;
import com.service.model.ProfessionalDocument;

import lombok.Data;

@Data
public class AddressDTO {

	private String name;
    private Long userRoleId; 
    private Long cityId;
    private String streetAddress;
    private Double latitude;
    private Double longitude;
    private Long documentId; 
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
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

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Address toEntity() {
    	City c = new City();
		c.setCityId(this.cityId);
		
		UserRole ur = new UserRole();
		ur.setUserRoleId(userRoleId);
		
		ProfessionalDocument pd = new ProfessionalDocument();		
    	
        Address a = new Address();
        a.setName(name);
        a.setStreetAddress(streetAddress);
        a.setLatitude(latitude);
        a.setLongitude(longitude);
        a.setCity(c);
        a.setUserRole(ur);
        if (documentId != null) {
        	pd.setProfessionalDocumentId(documentId);
        	a.setDocument(pd);
        	
        }
        return a;
    }

    public void updateEntity(Address e) {
    	City c = new City();
		c.setCityId(this.cityId);		
		ProfessionalDocument d = new ProfessionalDocument();	
		
		e.setName(name);
        e.setStreetAddress(streetAddress);
        e.setLatitude(latitude);
        e.setLongitude(longitude);
        e.setCity(c);
        if (documentId != null) {
        	d.setProfessionalDocumentId(documentId);
            e.setDocument(d);
        }
    }
}

