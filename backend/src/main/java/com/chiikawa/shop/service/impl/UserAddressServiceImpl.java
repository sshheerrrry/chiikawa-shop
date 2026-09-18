package com.chiikawa.shop.service.impl;

import com.chiikawa.shop.dao.UserAddressDao;
import com.chiikawa.shop.dao.UserDao;
import com.chiikawa.shop.entity.User;
import com.chiikawa.shop.entity.UserAddress;
import com.chiikawa.shop.service.UserAddressService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

	private final UserAddressDao userAddressDao;
	private final UserDao userDao;

	public UserAddressServiceImpl(UserAddressDao userAddressDao, UserDao userDao) {
		this.userAddressDao = userAddressDao;
		this.userDao = userDao;
	}

	// 查詢會員所有地址
	@Override
	public List<UserAddress> getAll(Long userId) {
		return userAddressDao.findByUserId(userId);
	}

	// 查詢會員自己的指定地址
	@Override
	public UserAddress getById(Long userId, Long addressId) {
		return userAddressDao.findByIdAndUserId(addressId, userId)
				.orElseThrow(() -> new IllegalArgumentException("找不到收件地址"));
	}

	// 新增地址
	@Override
	@Transactional
	public UserAddress create(Long userId, UserAddress address) {

		// 確認會員存在
		User user = userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("會員不存在"));

		// 驗證地址資料
		validate(address);

		// 設定地址所屬會員
		address.setUser(user);

		// 第一個地址自動成為預設地址
		List<UserAddress> addresses = userAddressDao.findByUserId(userId);

		if (addresses.isEmpty()) {
			address.setIsDefault(true);
		} else if (Boolean.TRUE.equals(address.getIsDefault())) {
			// 新地址指定為預設
			// 先取消原本預設地址
			clearDefault(userId);
		}
		return userAddressDao.save(address);
	}

	// 修改地址
	@Override
	@Transactional
	public UserAddress update(Long userId, Long addressId, UserAddress input) {
		UserAddress address = getById(userId, addressId);

		// 驗證新資料
		validate(input);
		
		address.setRecipientName(input.getRecipientName());
		address.setPhone(input.getPhone());
		address.setPostalCode(input.getPostalCode());
		address.setCity(input.getCity());
		address.setDistrict(input.getDistrict());
		address.setAddress(input.getAddress());
		
		// 如果修改時指定成預設地址
		if (Boolean.TRUE.equals(input.getIsDefault())) {
			clearDefault(userId);
			address.setIsDefault(true);
		}
		return userAddressDao.save(address);
	}

	// 刪除地址
	@Override
	@Transactional
	public void delete(Long userId, Long addressId) {

		// 只能取得自己的地址
		UserAddress address = getById(userId, addressId);

		// 記住刪除的是不是預設地址
		boolean wasDefault = Boolean.TRUE.equals(address.getIsDefault());
		userAddressDao.delete(address);

		// 如果刪掉的是預設地址，找剩下的第一個地址，自動設成新的預設地址
		if (wasDefault) {
			List<UserAddress> remaining = userAddressDao.findByUserId(userId);
			if (!remaining.isEmpty()) {
				UserAddress newDefault = remaining.get(0);
				newDefault.setIsDefault(true);
				userAddressDao.save(newDefault);
			}
		}
	}

	// 設定預設地址
	@Override
	@Transactional
	public UserAddress setDefault(Long userId, Long addressId) {
		UserAddress address = getById(userId, addressId);

		// 先取消其他預設地址
		clearDefault(userId);

		// 再把指定地址設為預設
		address.setIsDefault(true);

		return userAddressDao.save(address);
	}

	// 取消會員目前所有預設地址
	private void clearDefault(Long userId) {
		List<UserAddress> addresses = userAddressDao.findByUserId(userId);

		for (UserAddress address : addresses) {
			if (Boolean.TRUE.equals(address.getIsDefault())) {
				address.setIsDefault(false);
				userAddressDao.save(address);
			}
		}
	}

	// 地址資料驗證
	private void validate(UserAddress address) {
		if (address.getRecipientName() == null || address.getRecipientName().isBlank()) {
			throw new IllegalArgumentException("收件人姓名不可空白");
		}

		if (address.getPhone() == null || address.getPhone().isBlank()) {
			throw new IllegalArgumentException("電話不可空白");
		}

		if (address.getCity() == null || address.getCity().isBlank()) {
			throw new IllegalArgumentException("縣市不可空白");
		}

		if (address.getDistrict() == null || address.getDistrict().isBlank()) {
			throw new IllegalArgumentException("區域不可空白");
		}

		if (address.getAddress() == null || address.getAddress().isBlank()) {
			throw new IllegalArgumentException("詳細地址不可空白");
		}
	}
}