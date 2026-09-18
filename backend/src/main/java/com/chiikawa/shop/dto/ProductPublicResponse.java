package com.chiikawa.shop.dto;

import com.chiikawa.shop.entity.Product;
import java.math.BigDecimal;

public class ProductPublicResponse {

    private Long id;
    private String series;
    private String characterName;
    private String description;
    private BigDecimal price;
    private String imageUrl;

    public static ProductPublicResponse from(Product product) {
        ProductPublicResponse response = new ProductPublicResponse();

        response.id = product.getId();
        response.series = product.getSeries();
        response.characterName = product.getCharacterName();
        response.description = product.getDescription();
        response.price = product.getPrice();
        response.imageUrl = product.getImageUrl();

        return response;
    }

    public Long getId() {
        return id;
    }

    public String getSeries() {
        return series;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
