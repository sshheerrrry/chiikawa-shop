package com.chiikawa.shop.service;

import com.chiikawa.shop.entity.CartItem;
import java.util.List;

public interface CartService {
    List<CartItem> getCart(Long userId);
    CartItem addItem(Long userId, Long productId, Integer quantity);
    CartItem updateQuantity(Long userId, Long cartItemId, Integer quantity);
    void removeItem(Long userId, Long cartItemId);
}
