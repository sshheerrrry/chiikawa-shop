package com.chiikawa.shop.service.impl;

import com.chiikawa.shop.dao.CartDao;
import com.chiikawa.shop.dao.ProductDao;
import com.chiikawa.shop.dao.UserDao;
import com.chiikawa.shop.entity.CartItem;
import com.chiikawa.shop.entity.Product;
import com.chiikawa.shop.entity.User;
import com.chiikawa.shop.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartDao cartDao;
	@Autowired
	@Qualifier("myBatisProductDao")
	private ProductDao productDao;
	@Autowired
	private UserDao userDao;

	@Override
	public List<CartItem> getCart(Long userId) {
		return cartDao.findByUserId(userId);
	}

	@Override
	public CartItem addItem(Long userId, Long productId, Integer quantity) {
		int qty = quantity == null ? 1 : quantity;
		if (qty <= 0)
			throw new IllegalArgumentException("數量必須大於 0");

		User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("會員不存在"));

		Product product = productDao.findById(productId).orElseThrow(() -> new IllegalArgumentException("商品不存在"));

		// 商品下架後不可加入購物車
		if (!Boolean.TRUE.equals(product.getActive())) {
			throw new IllegalArgumentException("商品已下架，無法加入購物車");
		}

		CartItem item = cartDao.findByUserIdAndProductId(userId, productId).orElseGet(() -> {
			CartItem newItem = new CartItem();
			newItem.setUser(user);
			newItem.setProduct(product);
			newItem.setQuantity(0);
			return newItem;
		});

		int newQty = item.getQuantity() + qty;
		if (newQty > product.getStock()) {
			throw new IllegalArgumentException("商品庫存不足");
		}

		item.setQuantity(newQty);
		return cartDao.save(item);
	}

	@Override
	public CartItem updateQuantity(Long userId, Long cartItemId, Integer quantity) {
		if (quantity == null || quantity <= 0) {
			throw new IllegalArgumentException("數量必須大於 0");
		}

		CartItem item = cartDao.findById(cartItemId)
				.orElseThrow(() -> new IllegalArgumentException("購物車項目不存在"));

		if (!item.getUser().getId().equals(userId)) {
			throw new IllegalArgumentException("無權限修改");
		}

		// 重新取得商品最新狀態
		Product product = productDao.findById(item.getProduct().getId())
				.orElseThrow(() -> new IllegalArgumentException("商品不存在"));

		// 商品已下架，不允許修改購買數量
		if (!Boolean.TRUE.equals(product.getActive())) {
			throw new IllegalArgumentException("商品已下架，無法修改購買數量");
		}

		// 檢查最新庫存
		if (quantity > product.getStock()) {
			throw new IllegalArgumentException("商品庫存不足");
		}

		item.setQuantity(quantity);
		return cartDao.save(item);
	}

	@Override
	public void removeItem(Long userId, Long cartItemId) {
		CartItem item = cartDao.findById(cartItemId).orElseThrow(() -> new IllegalArgumentException("購物車項目不存在"));

		if (!item.getUser().getId().equals(userId)) {
			throw new IllegalArgumentException("無權限刪除");
		}

		cartDao.delete(item);
	}
}
