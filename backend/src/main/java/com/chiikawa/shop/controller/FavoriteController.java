package com.chiikawa.shop.controller;

import com.chiikawa.shop.dto.FavoriteResponse;
import com.chiikawa.shop.entity.Favorite;
import com.chiikawa.shop.entity.User;
import com.chiikawa.shop.service.FavoriteService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final JwtAuthHelper jwtAuthHelper;
    
    public FavoriteController(FavoriteService favoriteService,JwtAuthHelper jwtAuthHelper) {
        this.favoriteService = favoriteService;
        this.jwtAuthHelper = jwtAuthHelper;
    }

    // =========================
    // 查詢我的收藏
    // =========================
    @GetMapping
    public List<FavoriteResponse> getFavorites(
        @RequestHeader(value = "Authorization",required = false) String authorizationHeader) {
        User user = jwtAuthHelper.requireUser(authorizationHeader);
        return favoriteService.getFavorites(user.getId())
            .stream()
            .map(FavoriteResponse::from)
            .toList();
    }

    // =========================
    // 新增收藏
    // =========================
    @PostMapping("/{productId}")
    public FavoriteResponse addFavorite(
        @PathVariable Long productId,
        @RequestHeader(value = "Authorization",required = false) String authorizationHeader) {
        User user = jwtAuthHelper.requireUser(authorizationHeader);
        Favorite favorite = favoriteService.addFavorite(user.getId(),productId);
        return FavoriteResponse.from(favorite);
    }

    // =========================
    // 取消收藏
    // =========================
    @DeleteMapping("/{productId}")
    public Map<String, String> removeFavorite(
        @PathVariable Long productId,
        @RequestHeader(value = "Authorization",required = false) String authorizationHeader) {
        User user = jwtAuthHelper.requireUser(authorizationHeader);
        favoriteService.removeFavorite(user.getId(),productId);
        return Map.of("message","已取消收藏");
    }

    // =========================
    // 判斷某商品是否收藏
    // =========================
    @GetMapping("/{productId}/status")
    public Map<String, Boolean> isFavorite(
        @PathVariable Long productId,
        @RequestHeader(value = "Authorization",required = false)String authorizationHeader) {
        User user = jwtAuthHelper.requireUser(authorizationHeader);
        boolean favorite = favoriteService.isFavorite(user.getId(),productId);
        return Map.of("favorite",favorite);
    }
}