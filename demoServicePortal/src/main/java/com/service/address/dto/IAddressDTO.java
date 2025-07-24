package com.service.address.dto;

public interface IAddressDTO {

	String getName();
	Long getAddressId();
	Long getRegionId();
	String getRegionName();
	Long getCityId();
	String getCityName();
	String getStreetAddress();
}
