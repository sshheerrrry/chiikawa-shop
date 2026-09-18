package com.chiikawa.shop.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chiikawa.shop.dao.OrderDao;
import com.chiikawa.shop.dao.UserDao;
import com.chiikawa.shop.dto.AdminOrderResponse;
import com.chiikawa.shop.dto.AdminUserResponse;
import com.chiikawa.shop.entity.UserRole;
import com.chiikawa.shop.service.AdminUserService;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final UserDao userDao;
    private final OrderDao orderDao;

    public AdminUserServiceImpl(UserDao userDao,OrderDao orderDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
    }

    @Override
    public List<AdminUserResponse> getAllUsers() {
        return userDao.findAll().stream().filter(user ->user.getRole() == UserRole.USER)
                .map(user -> {List<AdminOrderResponse> orders = orderDao
                                    .findByUserId(user.getId())
                                    .stream()
                                    .map(AdminOrderResponse::from)
                                    .toList();
                    return AdminUserResponse.from(user,orders);})
                .toList();
    }
}