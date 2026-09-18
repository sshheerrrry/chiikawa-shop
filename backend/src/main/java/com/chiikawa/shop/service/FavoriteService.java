package com.chiikawa.shop.service;

import com.chiikawa.shop.entity.Favorite;

import java.util.List;

public interface FavoriteService {
    Favorite addFavorite(Long userId,Long productId);
    List<Favorite> getFavorites(Long userId);
    void removeFavorite(Long userId,Long productId);
    boolean isFavorite(Long userId,Long productId);
}