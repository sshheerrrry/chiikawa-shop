package com.chiikawa.shop.controller;

import com.chiikawa.shop.dto.UserAddressResponse;
import com.chiikawa.shop.entity.User;
import com.chiikawa.shop.entity.UserAddress;
import com.chiikawa.shop.service.UserAddressService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class UserAddressController {

	private final UserAddressService userAddressService;
	private final JwtAuthHelper jwtAuthHelper;

	public UserAddressController(UserAddressService userAddressService, JwtAuthHelper jwtAuthHelper) {
		this.userAddressService = userAddressService;
		this.jwtAuthHelper = jwtAuthHelper;
	}

	// 查詢我的所有收件地址
	@GetMapping
	public List<UserAddressResponse> getAll(
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
		User user = jwtAuthHelper.requireUser(authorizationHeader);
		return userAddressService.getAll(user.getId()).stream().map(UserAddressResponse::from).toList();
	}

	// 查詢我的單一收件地址
	@GetMapping("/{id}")
	public UserAddressResponse getById(@PathVariable Long id,
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
		User user = jwtAuthHelper.requireUser(authorizationHeader);
		UserAddress address = userAddressService.getById(user.getId(), id);
		return UserAddressResponse.from(address);
	}

	// 新增收件地址
	@PostMapping
	public UserAddressResponse create(@RequestBody UserAddress address,
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
		User user = jwtAuthHelper.requireUser(authorizationHeader);
		UserAddress saved = userAddressService.create(user.getId(), address);
		return UserAddressResponse.from(saved);
	}

	// 修改收件地址
	@PutMapping("/{id}")
	public UserAddressResponse update(@PathVariable Long id, @RequestBody UserAddress address,
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
		User user = jwtAuthHelper.requireUser(authorizationHeader);
		UserAddress updated = userAddressService.update(user.getId(), id, address);
		return UserAddressResponse.from(updated);
	}

	// 刪除收件地址
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id,
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
		User user = jwtAuthHelper.requireUser(authorizationHeader);
		userAddressService.delete(user.getId(), id);
		return ResponseEntity.noContent().build();
	}

	// 設定預設收件地址
	@PatchMapping("/{id}/default")
	public UserAddressResponse setDefault(@PathVariable Long id,
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
		User user = jwtAuthHelper.requireUser(authorizationHeader);
		UserAddress address = userAddressService.setDefault(user.getId(), id);
		return UserAddressResponse.from(address);
	}
}