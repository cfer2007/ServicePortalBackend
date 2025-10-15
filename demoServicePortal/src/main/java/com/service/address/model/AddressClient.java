package com.service.address.model;

import com.service.model.Client;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "address_client",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"addressId", "clientId"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK simple

    @ManyToOne(optional = false)
    @JoinColumn(name = "addressId", referencedColumnName = "addressId", nullable = false)
    private Address address;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clientId", referencedColumnName = "clientId", nullable = false)
    private Client client;

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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
