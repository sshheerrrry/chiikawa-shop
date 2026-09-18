package com.chiikawa.shop.dto;

import com.chiikawa.shop.entity.PaymentMethod;

public class CheckoutRequest {
	private Long addressId;
	private PaymentMethod paymentMethod;
	
	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}