package com.chiikawa.shop.dto;

import com.chiikawa.shop.entity.User;

public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;

    public static UserResponse from(User user) {
        UserResponse r = new UserResponse();
        r.id = user.getId();
        r.username = user.getUsername();
        r.name = user.getName();
        r.email = user.getEmail();
        r.role = user.getRole().name();
        return r;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
