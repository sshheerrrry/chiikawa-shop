package com.chiikawa.shop.dao;

import com.chiikawa.shop.entity.Favorite;

import java.util.List;
import java.util.Optional;

public interface FavoriteDao {
    Favorite save(Favorite favorite);
    
    List<Favorite> findByUserId(Long userId);

    Optional<Favorite> findByUserIdAndProductId(Long userId,Long productId);

    boolean existsByUserIdAndProductId(Long userId,Long productId);
    
    void delete(Favorite favorite);
}