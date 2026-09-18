package com.chiikawa.shop.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Component
public class JwtUtility {

    // Access Token：15 分鐘
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000L;
    
    // Refresh Token：7 天
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtUtility(KeyPair keyPair) {
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    // =========================
    // 產生 Access Token
    // =========================
    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime()+ ACCESS_TOKEN_EXPIRATION);
        return Jwts.builder()
                .setSubject(username)
                .claim("tokenType", "ACCESS")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(privateKey,SignatureAlgorithm.RS256)
                .compact();
    }

    // =========================
    // 產生 Refresh Token
    // =========================
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime()+ REFRESH_TOKEN_EXPIRATION);
        return Jwts.builder()
                .setSubject(username)
                .claim("tokenType", "REFRESH")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(privateKey,SignatureAlgorithm.RS256)
                .compact();
    }

    // =========================
    // 驗證 Token
    // =========================
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("validateToken error: "+ e.getMessage());
            return false;
        }
    }

    // =========================
    // Access Token 判斷
    // =========================
    public boolean isAccessToken(String token) {
        try {
            Claims claims = getClaims(token);
            return "ACCESS".equals(claims.get("tokenType",String.class));
        } catch (Exception e) {
            return false;
        }
    }

    // =========================
    // Refresh Token 判斷
    // =========================
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = getClaims(token);
            return "REFRESH".equals(claims.get("tokenType",String.class));
        } catch (Exception e) {
            return false;
        }
    }

    // =========================
    // 取得 username
    // =========================
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // =========================
    // RSA Public Key 驗證
    // =========================
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // =========================
    // Bearer Token 處理
    // =========================
    public String resolveToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7);  
    }
}