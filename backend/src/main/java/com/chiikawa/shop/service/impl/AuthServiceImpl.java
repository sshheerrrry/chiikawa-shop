package com.chiikawa.shop.service.impl;

import com.chiikawa.shop.dao.UserDao;
import com.chiikawa.shop.dto.LoginRequest;
import com.chiikawa.shop.dto.RegisterRequest;
import com.chiikawa.shop.entity.User;
import com.chiikawa.shop.entity.UserRole;
import com.chiikawa.shop.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserDao userDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("帳號不可空白");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("密碼不可空白");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email 不可空白");
        }
        String username = request.getUsername().trim();

        String email = request.getEmail().trim();
        
        //檢查帳號是否已存在
        if (userDao.existsByUsername(username)) {
            throw new IllegalArgumentException("帳號已存在");
        }
        
        //檢查Email是否已被註冊
        if (userDao.existsByEmail(email)) {
            throw new IllegalArgumentException("Email 已被註冊");
        }

        //建立User
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName() == null || request.getName().isBlank()
                ? request.getUsername() : request.getName().trim());
        
        user.setEmail(request.getEmail());
        user.setRole(UserRole.USER);
        return userDao.save(user);
    }

    @Override
    public User login(LoginRequest request) {
        User user = userDao.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("帳號或密碼錯誤"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("帳號或密碼錯誤");
        }
        return user;
    }
}
