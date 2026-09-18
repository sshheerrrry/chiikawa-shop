package com.chiikawa.shop.dao.impl;

import com.chiikawa.shop.dao.FavoriteDao;
import com.chiikawa.shop.entity.Favorite;
import com.chiikawa.shop.repository.FavoriteRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FavoriteDaoImpl implements FavoriteDao {
    private final FavoriteRepository favoriteRepository;
    
    public FavoriteDaoImpl(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Override
    public List<Favorite> findByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @Override
    public Optional<Favorite> findByUserIdAndProductId(Long userId,Long productId) {
        return favoriteRepository.findByUserIdAndProductId(userId,productId);
    }

    @Override
    public boolean existsByUserIdAndProductId(Long userId,Long productId) {
        return favoriteRepository.existsByUserIdAndProductId(userId,productId);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }
}