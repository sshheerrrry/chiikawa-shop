package com.chiikawa.shop.service.impl;

import com.chiikawa.shop.dao.OrderDao;
import com.chiikawa.shop.dao.ProductDao;
import com.chiikawa.shop.dao.ProductReviewDao;
import com.chiikawa.shop.dao.UserDao;
import com.chiikawa.shop.dto.ProductReviewSummaryResponse;
import com.chiikawa.shop.entity.CustomerOrder;
import com.chiikawa.shop.entity.Product;
import com.chiikawa.shop.entity.ProductReview;
import com.chiikawa.shop.entity.User;

import com.chiikawa.shop.service.ProductReviewService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {
	private final ProductReviewDao productReviewDao;
	private final OrderDao orderDao;
	private final UserDao userDao;
	private final ProductDao productDao;

	public ProductReviewServiceImpl(ProductReviewDao productReviewDao, OrderDao orderDao, UserDao userDao,
			@Qualifier("myBatisProductDao") ProductDao productDao) {
		this.productReviewDao = productReviewDao;
		this.orderDao = orderDao;
		this.userDao = userDao;
		this.productDao = productDao;
	}

	// =================================
	// 新增評價
	// =================================
	@Override
	public ProductReview createReview(Long userId, Long productId, Integer rating, String comment) {
		// -------------------------
		// 1. 檢查星等
		// -------------------------
		if (rating == null || rating < 1 || rating > 5) {
			throw new IllegalArgumentException("評分必須介於 1 到 5 顆星");
		}

		// -------------------------
		// 2. 檢查會員
		// -------------------------
		User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("找不到會員"));

		// -------------------------
		// 3. 檢查商品
		// -------------------------
		Product product = productDao.findById(productId).orElseThrow(() -> new IllegalArgumentException("找不到商品"));

		// -------------------------
		// 4. 是否真的買過
		// -------------------------
		if (!hasPurchased(userId, productId)) {
			throw new IllegalArgumentException("只有購買過此商品的會員才能評價");
		}

		// -------------------------
		// 5. 是否已評價過
		// -------------------------
		if (hasReviewed(userId, productId)) {
			throw new IllegalArgumentException("您已經評價過此商品");
		}

		// -------------------------
		// 6. 建立評價
		// -------------------------
		ProductReview review = new ProductReview();
		review.setUser(user);
		review.setProduct(product);
		review.setRating(rating);
		if (comment != null) {
			comment = comment.trim();
			if (comment.length() > 500) {
				throw new IllegalArgumentException("評價內容不能超過 500 字");
			}
		}
		review.setComment(comment);

		// -------------------------
		// 7. 儲存
		// -------------------------
		return productReviewDao.save(review);
	}

	// =================================
	// 查詢某商品評價
	// =================================
	@Override
	public List<ProductReview> getReviews(Long productId) {
		return productReviewDao.findByProductId(productId);
	}

	// =================================
	// 是否購買過
	// =================================
	@Override
	public boolean hasPurchased(Long userId, Long productId) {
		List<CustomerOrder> orders = orderDao.findByUserId(userId);
		return orders.stream()
				// 把所有訂單的商品攤平成一條資料流
				.flatMap(order -> order.getItems().stream())
				// 只要任何一筆 productId 相同
				// 就代表曾經買過
				.anyMatch(item -> productId.equals(item.getProductId()));
	}

	// =================================
	// 是否已經評價過
	// =================================
	@Override
	public boolean hasReviewed(Long userId, Long productId) {
		return productReviewDao.existsByUserIdAndProductId(userId, productId);
	}

	@Override
	public ProductReviewSummaryResponse getReviewSummary(Long productId) {
		// 取得平均星等
		Double averageRating = productReviewDao.findAverageRating(productId);
		// 取得評價數
		long reviewCount = productReviewDao.countByProductId(productId);

		// =========================
		// 尚無評價
		// AVG 會得到 null
		// =========================
		if (averageRating == null) {
			averageRating = 0.0;
		}

		// =========================
		// 平均星等四捨五入
		// 保留小數點 1 位
		// =========================
		averageRating = Math.round(averageRating * 10.0) / 10.0;

		return new ProductReviewSummaryResponse(averageRating, reviewCount);
	}
}