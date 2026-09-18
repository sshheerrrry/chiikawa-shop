package com.chiikawa.shop.controller;

import com.chiikawa.shop.dto.ProductReviewRequest;
import com.chiikawa.shop.dto.ProductReviewResponse;
import com.chiikawa.shop.dto.ProductReviewSummaryResponse;
import com.chiikawa.shop.entity.ProductReview;
import com.chiikawa.shop.entity.User;

import com.chiikawa.shop.service.ProductReviewService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductReviewController {
	private final ProductReviewService productReviewService;
	private final JwtAuthHelper jwtAuthHelper;

	public ProductReviewController(ProductReviewService productReviewService, JwtAuthHelper jwtAuthHelper) {
		this.productReviewService = productReviewService;
		this.jwtAuthHelper = jwtAuthHelper;
	}

	// =====================================
	// 查詢商品所有評價
	// 不需要登入
	// =====================================
	@GetMapping("/{productId}/reviews")
	public List<ProductReviewResponse> getReviews(@PathVariable Long productId) {
		return productReviewService.getReviews(productId).stream().map(ProductReviewResponse::from).toList();
	}

	// =====================================
	// 新增商品評價
	// 必須登入 + 必須購買過
	// =====================================
	@PostMapping("/{productId}/reviews")
	public ResponseEntity<ProductReviewResponse> createReview(@PathVariable Long productId,
			@RequestBody ProductReviewRequest request,
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
		User user = jwtAuthHelper.requireUser(authorizationHeader);
		ProductReview review = productReviewService.createReview(user.getId(), productId, request.getRating(),
				request.getComment());
		return ResponseEntity.ok(ProductReviewResponse.from(review));
	}

	// =====================================
	// 查詢目前會員是否可以評價
	// =====================================
	@GetMapping("/{productId}/reviews/eligibility")
	public Map<String, Boolean> getReviewEligibility(@PathVariable Long productId,
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
		User user = jwtAuthHelper.requireUser(authorizationHeader);
		// 是否購買過
		boolean purchased = productReviewService.hasPurchased(user.getId(), productId);
		// 是否已經評價過
		boolean reviewed = productReviewService.hasReviewed(user.getId(), productId);
		return Map.of("purchased", purchased, "reviewed", reviewed, "canReview", purchased && !reviewed);// canReview現在是否可以評價
	}

	// 商品平均星等 + 評價數
	@GetMapping("/{productId}/reviews/summary")
	public ProductReviewSummaryResponse getReviewSummary(@PathVariable Long productId) {
		return productReviewService.getReviewSummary(productId);
	}
}