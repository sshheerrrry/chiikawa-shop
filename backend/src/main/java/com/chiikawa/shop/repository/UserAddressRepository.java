package com.chiikawa.shop.repository;

import com.chiikawa.shop.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

	// 查詢某會員的所有地址
	List<UserAddress> findByUserIdOrderByIdAsc(Long userId);

	// 查詢某會員的指定地址
	Optional<UserAddress> findByIdAndUserId(Long id, Long userId);

	// 查詢會員目前的預設地址
	Optional<UserAddress> findByUserIdAndIsDefaultTrue(Long userId);
}