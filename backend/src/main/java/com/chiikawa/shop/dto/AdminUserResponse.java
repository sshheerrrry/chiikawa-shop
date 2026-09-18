package com.chiikawa.shop.dto;

import com.chiikawa.shop.entity.User;

import java.util.List;

public class AdminUserResponse {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String role;
    private List<AdminOrderResponse> orders;

    public static AdminUserResponse from(User user,List<AdminOrderResponse> orders) {
        AdminUserResponse r = new AdminUserResponse();
        r.id = user.getId();
        r.name = user.getName();
        r.username = user.getUsername();
        r.email = user.getEmail();
        r.role = user.getRole().name();
        r.orders = orders;
        return r;
    }

    public Long getId() {
        return id;
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

    public String getRole() {
        return role;
    }

    public List<AdminOrderResponse> getOrders() {
        return orders;
    }
}