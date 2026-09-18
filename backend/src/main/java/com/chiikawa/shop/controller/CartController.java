package com.chiikawa.shop.controller;

import com.chiikawa.shop.dto.CartItemRequest;
import com.chiikawa.shop.dto.CartResponse;
import com.chiikawa.shop.dto.QuantityRequest;
import com.chiikawa.shop.entity.CartItem;
import com.chiikawa.shop.entity.User;
import com.chiikawa.shop.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final JwtAuthHelper jwtAuthHelper;

    public CartController(CartService cartService, JwtAuthHelper jwtAuthHelper) {
        this.cartService = cartService;
        this.jwtAuthHelper = jwtAuthHelper;
    }

    @GetMapping
    public List<CartResponse> getCart(
    		@RequestHeader("Authorization") String authorizationHeader) {

		// 從 JWT 取得目前登入會員
		User user = jwtAuthHelper.requireUser(authorizationHeader);

		// 從 Service 取得購物車 Entity
		List<CartItem> cartItems = cartService.getCart(user.getId());

		// CartItem Entity->CartResponse DTO
		return cartItems.stream().map(CartResponse::from).toList();
    }

    @PostMapping("/items")
    public CartItem addItem(
            @RequestBody CartItemRequest request,
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {

        User user = jwtAuthHelper.requireUser(authorizationHeader);

        return cartService.addItem(
                user.getId(),
                request.getProductId(),
                request.getQuantity()
        );
    }

    @PutMapping("/items/{id}")
    public CartItem updateQuantity(
            @PathVariable Long id,
            @RequestBody QuantityRequest request,
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {

        User user = jwtAuthHelper.requireUser(authorizationHeader);

        return cartService.updateQuantity(
                user.getId(),
                id,
                request.getQuantity()
        );
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {

        User user = jwtAuthHelper.requireUser(authorizationHeader);
        cartService.removeItem(user.getId(), id);

        return ResponseEntity.noContent().build();
    }
}
