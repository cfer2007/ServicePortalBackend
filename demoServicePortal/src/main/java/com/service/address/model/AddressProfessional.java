package com.service.address.model;

import com.service.model.Professional;
import com.service.model.ProfessionalDocument;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "address_professional",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"addressId", "professionalId"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressProfessional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK simple

    @ManyToOne(optional = false)
    @JoinColumn(name = "addressId", referencedColumnName = "addressId", nullable = false)
    private Address address;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professionalId", referencedColumnName = "professionalId", nullable = false)
    private Professional professional;
    
 // 🔹 Documento (recibo o evidencia)
    @ManyToOne(optional = false)
    @JoinColumn(name = "professionalDocumentId", referencedColumnName = "professionalDocumentId", nullable = false)
    private ProfessionalDocument document;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Professional getProfessional() {
        return professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

	public ProfessionalDocument getDocument() {
		return document;
	}

	public void setDocument(ProfessionalDocument document) {
		this.document = document;
	}
}
