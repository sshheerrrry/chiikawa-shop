package com.chiikawa.shop.dao.impl;

import com.chiikawa.shop.dao.ProductReviewDao;
import com.chiikawa.shop.entity.ProductReview;
import com.chiikawa.shop.repository.ProductReviewRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductReviewDaoImpl implements ProductReviewDao {

	private final ProductReviewRepository productReviewRepository;

	public ProductReviewDaoImpl(ProductReviewRepository productReviewRepository) {
		this.productReviewRepository = productReviewRepository;
	}

	// 新增 / 修改評價
	@Override
	public ProductReview save(ProductReview review) {
		return productReviewRepository.save(review);
	}

	// 查詢商品所有評價
	@Override
	public List<ProductReview> findByProductId(Long productId) {
		return productReviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
	}

	// 查詢會員對商品的評價
	@Override
	public Optional<ProductReview> findByUserIdAndProductId(Long userId, Long productId) {
		return productReviewRepository.findByUserIdAndProductId(userId, productId);
	}

	// 是否已經評價
	@Override
	public boolean existsByUserIdAndProductId(Long userId, Long productId) {
		return productReviewRepository.existsByUserIdAndProductId(userId, productId);
	}

	// 商品平均星等
	@Override
	public Double findAverageRating(Long productId) {
		return productReviewRepository.findAverageRating(productId);
	}

	// 商品評價數
	@Override
	public long countByProductId(Long productId) {
		return productReviewRepository.countByProductId(productId);
	}
}