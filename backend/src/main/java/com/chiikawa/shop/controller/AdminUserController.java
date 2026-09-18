package com.chiikawa.shop.controller;

import com.chiikawa.shop.dto.AdminUserResponse;
import com.chiikawa.shop.service.AdminUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
	@Autowired
    private AdminUserService adminUserService;
	@Autowired
    private JwtAuthHelper jwtAuthHelper;

    @GetMapping
    public List<AdminUserResponse> getAllUsers(
            @RequestHeader(value = "Authorization",required = false) String authorizationHeader) {
        jwtAuthHelper.requireAdmin(authorizationHeader);
        return adminUserService.getAllUsers();
    }
    
    
}