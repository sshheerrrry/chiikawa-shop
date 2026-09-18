package com.chiikawa.shop.dto;

import com.chiikawa.shop.entity.ProductReview;

import java.time.LocalDateTime;

public class ProductReviewResponse {
	private Long id;
	private Long userId;
	private String username;
	private Long productId;
	private Integer rating;
	private String comment;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ProductReviewResponse from(ProductReview review) {
		ProductReviewResponse response = new ProductReviewResponse();
		response.setId(review.getId());
		response.setUserId(review.getUser().getId());
		response.setUsername(review.getUser().getUsername());
		response.setProductId(review.getProduct().getId());
		response.setRating(review.getRating());
		response.setComment(review.getComment());
		response.setCreatedAt(review.getCreatedAt());
		response.setUpdatedAt(review.getUpdatedAt());
		return response;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}