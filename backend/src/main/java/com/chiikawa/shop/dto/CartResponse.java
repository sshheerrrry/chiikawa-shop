package com.chiikawa.shop.dto;

import com.chiikawa.shop.entity.CartItem;

import java.math.BigDecimal;

public class CartResponse {
	private Long id;
	private Long productId;
	private String series;
	private String characterName;
	private BigDecimal price;
	private Integer quantity;
	private String imageUrl;

	// Entity -> DTO
	public static CartResponse from(CartItem cartItem) {
		CartResponse response = new CartResponse();
		response.setId(cartItem.getId());
		response.setProductId(cartItem.getProduct().getId());
		response.setSeries(cartItem.getProduct().getSeries());
		response.setCharacterName(cartItem.getProduct().getCharacterName());
		response.setPrice(cartItem.getProduct().getPrice());
		response.setQuantity(cartItem.getQuantity());
		response.setImageUrl(cartItem.getProduct().getImageUrl());
		return response;
	}

	// Getter / Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}