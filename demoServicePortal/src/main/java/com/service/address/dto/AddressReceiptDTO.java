package com.service.address.dto;

import com.service.address.model.Address;
import com.service.address.model.AddressReceipt;
import com.service.model.ProfessionalDocument;

public class AddressReceiptDTO {

	private Long addressId;
	private Long professionalDocumentId;
	
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getProfessionalDocumentId() {
		return professionalDocumentId;
	}
	public void setProfessionalDocumentId(Long professionalDocumentId) {
		this.professionalDocumentId = professionalDocumentId;
	}
	
	public AddressReceipt toEntity() {
		AddressReceipt ar = new AddressReceipt();
		
		Address a = new Address();
		a.setAddressId(this.addressId);
		
		ProfessionalDocument pd = new ProfessionalDocument();
		pd.setProfessionalDocumentId(this.professionalDocumentId);
		
		ar.setAddress(a);
		ar.setDocument(pd);
		
		return ar;
	}
}
