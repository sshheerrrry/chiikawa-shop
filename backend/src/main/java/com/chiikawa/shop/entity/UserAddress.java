package com.chiikawa.shop.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_addresses")
public class UserAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// =========================
	// 所屬會員
	// user_addresses.user_id
	// 對應 users.id
	// =========================
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// 收件人姓名
	@Column(name = "recipient_name", nullable = false, length = 100)
	private String recipientName;

	// 收件人電話
	@Column(nullable = false, length = 20)
	private String phone;

	// 郵遞區號
	@Column(name = "postal_code", length = 10)
	private String postalCode;

	// 縣市
	@Column(nullable = false, length = 50)
	private String city;

	// 區 / 鄉 / 鎮 / 市
	@Column(nullable = false, length = 50)
	private String district;

	// 詳細地址
	@Column(nullable = false, length = 255)
	private String address;

	// 是否為預設地址
	@Column(name = "is_default", nullable = false)
	private Boolean isDefault = false;

	// =========================
	// Getter / Setter
	// =========================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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