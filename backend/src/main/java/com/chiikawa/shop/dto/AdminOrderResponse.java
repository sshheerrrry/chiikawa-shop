package com.chiikawa.shop.dto;

import com.chiikawa.shop.entity.CustomerOrder;
import com.chiikawa.shop.entity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AdminOrderResponse {
    private Long id;
    private Long userId;
    private String name;
    private String username;
    private String email;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItem> items;

    public static AdminOrderResponse from(CustomerOrder order) {
        AdminOrderResponse r = new AdminOrderResponse();
        r.id = order.getId();
        r.userId = order.getUser().getId();
        r.name = order.getUser().getName();
        r.username = order.getUser().getUsername();
        r.email = order.getUser().getEmail();
        r.totalAmount = order.getTotalAmount();
        r.createdAt = order.getCreatedAt();
        r.items = order.getItems();
        return r;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}