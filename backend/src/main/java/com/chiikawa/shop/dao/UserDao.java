package com.chiikawa.shop.dao;

import com.chiikawa.shop.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    // 管理員查詢所有會員
    List<User> findAll();
}
