package com.chiikawa.shop.service.impl;

import com.chiikawa.shop.dao.FavoriteDao;
import com.chiikawa.shop.dao.ProductDao;
import com.chiikawa.shop.dao.UserDao;
import com.chiikawa.shop.entity.Favorite;
import com.chiikawa.shop.entity.Product;
import com.chiikawa.shop.entity.User;
import com.chiikawa.shop.service.FavoriteService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteDao favoriteDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    public FavoriteServiceImpl(FavoriteDao favoriteDao,UserDao userDao,
        @Qualifier("myBatisProductDao") ProductDao productDao) {
        this.favoriteDao = favoriteDao;
        this.userDao = userDao;
        this.productDao = productDao;   
    }

    // =========================
    // 新增收藏
    // =========================
    @Override
    public Favorite addFavorite(Long userId,Long productId) {
        // 已經收藏過
        if (favoriteDao.existsByUserIdAndProductId(userId,productId)) {
            throw new RuntimeException("此商品已收藏");
        }

        // 找會員
        User user = userDao.findById(userId)
        		.orElseThrow(() -> new RuntimeException("找不到會員"));

        // 找商品
        Product product =productDao.findById(productId)
                .orElseThrow(() -> new RuntimeException("找不到商品"));
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        return favoriteDao.save(favorite);
    }

    // =========================
    // 查詢收藏
    // =========================
    @Override
    public List<Favorite> getFavorites(Long userId) {
        return favoriteDao.findByUserId(userId);      
    }

    // =========================
    // 取消收藏
    // =========================
    @Override
    public void removeFavorite(Long userId,Long productId) {
        Favorite favorite =favoriteDao.findByUserIdAndProductId(userId,productId)
                .orElseThrow(() -> new RuntimeException("尚未收藏此商品"));
        favoriteDao.delete(favorite);
    }

    // =========================
    // 判斷是否收藏
    // =========================
    @Override
    public boolean isFavorite(Long userId,Long productId) {
        return favoriteDao.existsByUserIdAndProductId(userId,productId);
    }
}