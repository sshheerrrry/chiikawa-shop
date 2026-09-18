package com.chiikawa.shop.service;

import com.chiikawa.shop.dto.ProductReviewSummaryResponse;
import com.chiikawa.shop.entity.ProductReview;

import java.util.List;

public interface ProductReviewService {

	// 新增評價
	ProductReview createReview(Long userId, Long productId, Integer rating, String comment);

	// 查詢某商品所有評價
	List<ProductReview> getReviews(Long productId);

	// 判斷會員是否有購買過
	boolean hasPurchased(Long userId, Long productId);

	// 判斷會員是否已評價過
	boolean hasReviewed(Long userId, Long productId);

	// 商品評價統計
	ProductReviewSummaryResponse getReviewSummary(Long productId);
}