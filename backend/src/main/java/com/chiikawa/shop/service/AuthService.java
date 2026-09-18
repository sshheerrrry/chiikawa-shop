package com.chiikawa.shop.service;

import com.chiikawa.shop.dto.LoginRequest;
import com.chiikawa.shop.dto.RegisterRequest;
import com.chiikawa.shop.entity.User;

public interface AuthService {
    User register(RegisterRequest request);
    User login(LoginRequest request);
}
