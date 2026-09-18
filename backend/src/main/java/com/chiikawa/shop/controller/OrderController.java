package com.chiikawa.shop.controller;

import com.chiikawa.shop.dto.AdminOrderResponse;
import com.chiikawa.shop.dto.CheckoutRequest;
import com.chiikawa.shop.dto.OrderResponse;
import com.chiikawa.shop.entity.CustomerOrder;
import com.chiikawa.shop.entity.User;
import com.chiikawa.shop.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;
	private final JwtAuthHelper jwtAuthHelper;

	public OrderController(OrderService orderService, JwtAuthHelper jwtAuthHelper) {
		this.orderService = orderService;
		this.jwtAuthHelper = jwtAuthHelper;
	}

	@PostMapping("/checkout")
	public OrderResponse checkout(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody CheckoutRequest request) {

		// 從 JWT 取得目前登入會員
		User user = jwtAuthHelper.requireUser(authorizationHeader);

		// 執行結帳
		CustomerOrder order = orderService.checkout(
				user.getId(), 
				request.getAddressId(), 
				request.getPaymentMethod());

		// Entity -> DTO
		return OrderResponse.from(order);
	}

	@GetMapping("/history")
	public List<OrderResponse> history(@RequestHeader("Authorization") String authorizationHeader) {

		// 從 JWT 取得目前登入會員
		User user = jwtAuthHelper.requireUser(authorizationHeader);

		// 查詢會員歷史訂單
		List<CustomerOrder> orders = orderService.history(user.getId());

		// List<CustomerOrder>-->List<OrderResponse>
		return orders.stream().map(OrderResponse::from).toList();
	}
}
