package com.chiikawa.shop.dto;

import com.chiikawa.shop.entity.CustomerOrder;
import com.chiikawa.shop.entity.OrderItem;
import com.chiikawa.shop.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
	private Long id;
	private BigDecimal totalAmount;
	private LocalDateTime createdAt;
	private String recipientName;
	private String phone;
	private String shippingAddress;
	private PaymentMethod paymentMethod;
	private List<OrderItem> items;

	// Entity -> DTO
	public static OrderResponse from(CustomerOrder order) {
		OrderResponse response = new OrderResponse();
		response.setId(order.getId());
		response.setTotalAmount(order.getTotalAmount());
		response.setCreatedAt(order.getCreatedAt());
		response.setRecipientName(order.getRecipientName());
		response.setPhone(order.getPhone());
		response.setShippingAddress(order.getShippingAddress());
		response.setPaymentMethod(order.getPaymentMethod());
		response.setItems(order.getItems());
		return response;
	}

	// Getter / Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
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

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
}