package com.chiikawa.shop.dao.impl;

import com.chiikawa.shop.dao.UserAddressDao;
import com.chiikawa.shop.entity.UserAddress;
import com.chiikawa.shop.repository.UserAddressRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserAddressDaoImpl implements UserAddressDao {

	private final UserAddressRepository userAddressRepository;

	public UserAddressDaoImpl(UserAddressRepository userAddressRepository) {
		this.userAddressRepository = userAddressRepository;
	}

	// 新增 / 修改
	@Override
	public UserAddress save(UserAddress address) {
		return userAddressRepository.save(address);
	}

	// 查詢會員所有地址
	@Override
	public List<UserAddress> findByUserId(Long userId) {
		return userAddressRepository.findByUserIdOrderByIdAsc(userId);
	}

	// 查詢會員自己的指定地址
	@Override
	public Optional<UserAddress> findByIdAndUserId(Long id, Long userId) {
		return userAddressRepository.findByIdAndUserId(id, userId);
	}

	// 查詢預設地址
	@Override
	public Optional<UserAddress> findDefaultByUserId(Long userId) {
		return userAddressRepository.findByUserIdAndIsDefaultTrue(userId);
	}

	// 刪除地址
	@Override
	public void delete(UserAddress address) {
		userAddressRepository.delete(address);
	}
}