package com.chiikawa.shop.dao;

import com.chiikawa.shop.entity.UserAddress;

import java.util.List;
import java.util.Optional;

public interface UserAddressDao {

	// 新增 / 修改地址
	UserAddress save(UserAddress address);

	// 查詢會員所有地址
	List<UserAddress> findByUserId(Long userId);

	// 查詢會員自己的指定地址
	Optional<UserAddress> findByIdAndUserId(Long id, Long userId);

	// 查詢預設地址
	Optional<UserAddress> findDefaultByUserId(Long userId);

	// 刪除地址
	void delete(UserAddress address);
}