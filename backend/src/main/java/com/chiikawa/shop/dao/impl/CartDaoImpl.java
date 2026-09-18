package com.chiikawa.shop.dao.impl;

import com.chiikawa.shop.dao.CartDao;
import com.chiikawa.shop.entity.CartItem;
import com.chiikawa.shop.repository.CartItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartDaoImpl implements CartDao {

    private final CartItemRepository cartItemRepository;

    public CartDaoImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public List<CartItem> findByUserId(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId) {
        return cartItemRepository.findByUserIdAndProductId(userId, productId);
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Override
    public CartItem save(CartItem item) {
        return cartItemRepository.save(item);
    }

    @Override
    public void delete(CartItem item) {
        cartItemRepository.delete(item);
    }

    @Override
    public void clearByUserId(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
