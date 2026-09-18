package com.chiikawa.shop.dao;

import com.chiikawa.shop.entity.CartItem;
import java.util.List;
import java.util.Optional;

public interface CartDao {
    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    Optional<CartItem> findById(Long id);
    CartItem save(CartItem item);
    void delete(CartItem item);
    void clearByUserId(Long userId);
}
