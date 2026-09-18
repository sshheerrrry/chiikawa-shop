package com.chiikawa.shop.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","product_id"})})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)//很多收藏紀錄可以屬於同一個會員
    @JoinColumn(name = "user_id",nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)//同一個商品可以被很多會員收藏
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;


    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;


    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt =LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public Product getProduct() {
        return product;
    }


    public void setProduct(Product product) {
        this.product = product;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}