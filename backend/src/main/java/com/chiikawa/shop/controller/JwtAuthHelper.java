package com.chiikawa.shop.controller;

import com.chiikawa.shop.config.JwtUtility;
import com.chiikawa.shop.dao.UserDao;
import com.chiikawa.shop.entity.User;
import com.chiikawa.shop.entity.UserRole;

import org.springframework.stereotype.Component;

@Component
public class JwtAuthHelper {

    private final JwtUtility jwtUtility;
    private final UserDao userDao;

    public JwtAuthHelper(JwtUtility jwtUtility,UserDao userDao) {
        this.jwtUtility = jwtUtility;
        this.userDao = userDao;
    }

    public User requireUser(String authorizationHeader) {
        String token = jwtUtility.resolveToken(authorizationHeader);
        
        // Token 不存在
        if (token == null) {
            throw new SecurityException("尚未登入");
        }

        // Token 無效或過期
        if (!jwtUtility.validateToken(token)) {
            throw new SecurityException("JWT 無效或已過期");
        }

        // 一般 API 只允許 Access Token
        if (!jwtUtility.isAccessToken(token)) {
            throw new SecurityException("請使用 Access Token");
        }

        String username = jwtUtility.extractUsername(token);
        return userDao
                .findByUsername(username)
                .orElseThrow(() -> new SecurityException("找不到登入會員"));
    }

    public User requireAdmin(String authorizationHeader) {
        User user = requireUser(authorizationHeader);
        if (user.getRole() != UserRole.ADMIN) {
            throw new SecurityException("需要管理員權限");
        }

        return user;
    }
}