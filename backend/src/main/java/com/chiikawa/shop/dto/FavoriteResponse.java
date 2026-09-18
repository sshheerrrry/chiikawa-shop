package com.chiikawa.shop.dto;

import com.chiikawa.shop.entity.Favorite;
import com.chiikawa.shop.entity.Product;

import java.time.LocalDateTime;

public class FavoriteResponse {
    private Long favoriteId;
    private Long productId;
    private String series;
    private String characterName;
    private String description;
    private java.math.BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private LocalDateTime createdAt;

    public static FavoriteResponse from(Favorite favorite) {
        Product product = favorite.getProduct();
        FavoriteResponse response = new FavoriteResponse();
        response.setFavoriteId(favorite.getId());
        response.setProductId(product.getId());
        response.setSeries(product.getSeries());
        response.setCharacterName(product.getCharacterName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setImageUrl(product.getImageUrl());
        response.setCreatedAt(favorite.getCreatedAt());
        return response;
    }

    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;   
    }

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
    }


    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}