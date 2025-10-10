package com.service.address.model;

import com.service.model.ProfessionalDocument;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "address_receipt")
public class AddressReceipt {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressReceiptId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_document_id", nullable = false)
    private ProfessionalDocument document;

	public Long getAddressReceiptId() {
		return addressReceiptId;
	}

	public void setAddressReceiptId(Long addressReceiptId) {
		this.addressReceiptId = addressReceiptId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ProfessionalDocument getDocument() {
		return document;
	}

	public void setDocument(ProfessionalDocument document) {
		this.document = document;
	}
}
