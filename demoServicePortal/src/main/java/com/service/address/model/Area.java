package com.service.address.model;

import com.service.enums.AreaType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name="area")
@Entity(name="area")
public class Area {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long areaId;
	
	@Column
	private String name;
	
	@Enumerated(EnumType.STRING)
	private AreaType type;

	@ManyToOne
	@JoinColumn(name = "cityId", referencedColumnName = "cityId", nullable = false)
	private City city;	

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AreaType getType() {
		return type;
	}

	public void setType(AreaType type) {
		this.type = type;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}
