package com.chiikawa.shop.dto;

import com.chiikawa.shop.entity.UserAddress;

public class UserAddressResponse {
	private Long id;
	private String recipientName;
	private String phone;
	private String postalCode;
	private String city;
	private String district;
	private String address;
	private Boolean isDefault;

	// Entity -> DTO
	public static UserAddressResponse from(UserAddress address) {
		UserAddressResponse response = new UserAddressResponse();

		response.setId(address.getId());
		response.setRecipientName(address.getRecipientName());
		response.setPhone(address.getPhone());
		response.setPostalCode(address.getPostalCode());
		response.setCity(address.getCity());
		response.setDistrict(address.getDistrict());
		response.setAddress(address.getAddress());
		response.setIsDefault(address.getIsDefault());
		return response;
	}

	// Getter / Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
}