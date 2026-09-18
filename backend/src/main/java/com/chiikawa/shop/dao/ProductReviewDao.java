package com.chiikawa.shop.dao;

import com.chiikawa.shop.entity.ProductReview;

import java.util.List;
import java.util.Optional;

public interface ProductReviewDao {

	ProductReview save(ProductReview review);

	List<ProductReview> findByProductId(Long productId);

	Optional<ProductReview> findByUserIdAndProductId(Long userId, Long productId);

	boolean existsByUserIdAndProductId(Long userId, Long productId);

	// 商品平均星等
	Double findAverageRating(Long productId);

	// 商品評價數
	long countByProductId(Long productId);
}