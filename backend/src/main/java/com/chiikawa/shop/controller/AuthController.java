package com.chiikawa.shop.controller;

import com.chiikawa.shop.config.JwtUtility;
import com.chiikawa.shop.dao.UserDao;
import com.chiikawa.shop.dto.AuthResponse;
import com.chiikawa.shop.dto.LoginRequest;
import com.chiikawa.shop.dto.RefreshTokenRequest;
import com.chiikawa.shop.dto.RegisterRequest;
import com.chiikawa.shop.dto.UserResponse;
import com.chiikawa.shop.entity.User;
import com.chiikawa.shop.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtility jwtUtility;
    private final JwtAuthHelper jwtAuthHelper;
    private final UserDao userDao;
    public AuthController(
            AuthService authService,
            JwtUtility jwtUtility,
            JwtAuthHelper jwtAuthHelper,
            UserDao userDao) {
        this.authService = authService;
        this.jwtUtility = jwtUtility;
        this.jwtAuthHelper = jwtAuthHelper;
        this.userDao = userDao;
                
    }

    // =========================
    // 註冊
    // =========================
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register( @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        String accessToken = jwtUtility.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtility.generateRefreshToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(accessToken,refreshToken,user));
    }

    // =========================
    // 登入
    // =========================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        User user = authService.login(request);
        String accessToken = jwtUtility.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtility.generateRefreshToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(accessToken,refreshToken,user));
    }

    // =========================
    // Refresh Token
    // =========================
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        
        // Refresh Token 是否有效
        if (refreshToken == null || !jwtUtility.validateToken(refreshToken)) {
            return ResponseEntity.status(401).build();
        }

        // 確認真的為 Refresh Token
        if (!jwtUtility.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).build();     
        }

        String username = jwtUtility.extractUsername(refreshToken);

        User user = userDao.findByUsername(username)
                        .orElseThrow(() -> new IllegalArgumentException("找不到會員"));

        // 重新產生 Access Token
        String newAccessToken = jwtUtility.generateAccessToken(username);

        // Refresh Token 也輪替
        String newRefreshToken = jwtUtility.generateRefreshToken(username);

        return ResponseEntity.ok(new AuthResponse(newAccessToken,newRefreshToken,user));
    }

    // =========================
    // 取得目前登入會員
    // =========================
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            @RequestHeader(value = "Authorization",required = false) 
            String authorizationHeader) {
        User user = jwtAuthHelper.requireUser(authorizationHeader);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    // =========================
    // 登出
    // =========================
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of("message",
                        "JWT 登出：請前端刪除 Access Token 與 Refresh Token"));
    }
}