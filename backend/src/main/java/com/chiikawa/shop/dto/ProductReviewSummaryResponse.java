package com.chiikawa.shop.dto;

public class ProductReviewSummaryResponse {
	private Double averageRating;//平均星等
	private long reviewCount;//評價數

	public ProductReviewSummaryResponse() {
	}

	public ProductReviewSummaryResponse(Double averageRating, long reviewCount) {
		this.averageRating = averageRating;
		this.reviewCount = reviewCount;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	public long getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(long reviewCount) {
		this.reviewCount = reviewCount;
	}
}