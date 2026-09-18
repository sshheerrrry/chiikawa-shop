package com.chiikawa.shop.service;

import java.util.List;

import com.chiikawa.shop.dto.AdminUserResponse;

public interface AdminUserService {
	List<AdminUserResponse> getAllUsers();
}
