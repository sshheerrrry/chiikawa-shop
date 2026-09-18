package com.chiikawa.shop.repository;

import com.chiikawa.shop.entity.ProductReview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

	// 查詢某商品的所有評價
	List<ProductReview> findByProductIdOrderByCreatedAtDesc(Long productId);

	// 查詢某會員對某商品的評價
	Optional<ProductReview> findByUserIdAndProductId(Long userId, Long productId);

	// 判斷是否已經評價過
	boolean existsByUserIdAndProductId(Long userId, Long productId);

	// 商品平均星等
	@Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.product.id = :productId")
	Double findAverageRating(@Param("productId") Long productId);

	// 商品評價數
	long countByProductId(Long productId);
}