package com.chiikawa.shop.service.impl;

import com.chiikawa.shop.dao.CartDao;
import com.chiikawa.shop.dao.OrderDao;
import com.chiikawa.shop.dao.ProductDao;
import com.chiikawa.shop.dao.UserAddressDao;
import com.chiikawa.shop.dao.UserDao;
import com.chiikawa.shop.entity.*;
import com.chiikawa.shop.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private CartDao cartDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserAddressDao userAddressDao;
	@Autowired
	@Qualifier("myBatisProductDao")
	private ProductDao productDao;

	@Override
	@Transactional
	public CustomerOrder checkout(Long userId, Long addressId, PaymentMethod paymentMethod) {
		// 確認會員存在
		User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("會員不存在"));

		// 檢查收件地址
		if (addressId == null) {
			throw new IllegalArgumentException("請選擇收件地址");
		}

		// 必須同時符合：addressId + userId，避免會員使用別人的地址
		UserAddress userAddress = userAddressDao.findByIdAndUserId(addressId, userId)
				.orElseThrow(() -> new IllegalArgumentException("收件地址不存在"));

		// 檢查付款方式
		if (paymentMethod == null) {
			throw new IllegalArgumentException("請選擇付款方式");
		}

		// 取得購物車
		List<CartItem> cartItems = cartDao.findByUserId(userId);
		if (cartItems.isEmpty()) {
			throw new IllegalArgumentException("購物車是空的");
		}

		// 建立訂單
		CustomerOrder order = new CustomerOrder();
		order.setUser(user);

		// 儲存收件資料快照
		order.setRecipientName(userAddress.getRecipientName());
		order.setPhone(userAddress.getPhone());

		// 組合完整地址
		String shippingAddress = buildShippingAddress(userAddress);
		order.setShippingAddress(shippingAddress);

		// 儲存付款方式
		order.setPaymentMethod(paymentMethod);
		BigDecimal total = BigDecimal.ZERO;

		// 處理購物車商品
		for (CartItem cartItem : cartItems) {
			Product product = productDao.findById(cartItem.getProduct().getId())
					.orElseThrow(() -> new IllegalArgumentException("商品不存在"));

			// 結帳前再次確認商品是否仍然上架
			if (!Boolean.TRUE.equals(product.getActive())) {
				throw new IllegalArgumentException(
						product.getSeries() + "－" + product.getCharacterName() + " 商品已下架，無法結帳");
			}

			// 檢查庫存
			if (cartItem.getQuantity() > product.getStock()) {
				throw new IllegalArgumentException(product.getSeries()
						+ "－" + product.getCharacterName() + " 庫存不足");
			}

			// 扣除庫存
			product.setStock(product.getStock() - cartItem.getQuantity());
			productDao.save(product);

			// 建立訂單明細
			OrderItem orderItem = new OrderItem();
			orderItem.setProductId(product.getId());
			orderItem.setProductName(product.getSeries() + "－" + product.getCharacterName());
			orderItem.setPrice(product.getPrice());
			orderItem.setQuantity(cartItem.getQuantity());
			order.addItem(orderItem);

			// 計算總金額
			total = total.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
		}

		// 設定訂單總金額
		order.setTotalAmount(total);

		// 儲存訂單
		CustomerOrder saved = orderDao.save(order);

		// 清空購物車
		cartDao.clearByUserId(userId);

		return saved;
	}

	// 組合訂單收件地址快照
	private String buildShippingAddress(UserAddress address) {
		StringBuilder result = new StringBuilder();

		// 郵遞區號不是必填
		if (address.getPostalCode() != null && !address.getPostalCode().isBlank()) {
			result.append(address.getPostalCode());
			result.append(" ");
		}
		
		result.append(address.getCity());
		result.append(address.getDistrict());
		result.append(address.getAddress());
		return result.toString();
	}

	@Override
	public List<CustomerOrder> history(Long userId) {
		return orderDao.findByUserId(userId);
	}
}
