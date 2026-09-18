package com.chiikawa.shop.service;

import com.chiikawa.shop.entity.UserAddress;

import java.util.List;

public interface UserAddressService {

	// 查詢會員所有地址
	List<UserAddress> getAll(Long userId);

	// 查詢會員自己的指定地址
	UserAddress getById(Long userId, Long addressId);

	// 新增地址
	UserAddress create(Long userId, UserAddress address);

	// 修改地址
	UserAddress update(Long userId, Long addressId, UserAddress input);

	// 刪除地址
	void delete(Long userId, Long addressId);

	// 設定預設地址
	UserAddress setDefault(Long userId, Long addressId);
}