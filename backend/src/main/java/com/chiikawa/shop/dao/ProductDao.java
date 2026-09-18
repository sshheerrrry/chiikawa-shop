package com.chiikawa.shop.dao;

import com.chiikawa.shop.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductDao {
	// 查詢全部商品
	List<Product> findAll();

	// 管理員查詢全部商品，包含已下架商品
	List<Product> findAllForAdmin();

	// 查詢單一商品
	Optional<Product> findById(Long id);

	Product save(Product product);

	void deleteById(Long id);

	boolean existsById(Long id);

	// 檢查系列 + 角色是否已經存在
	// excludeId：修改商品時排除自己
	boolean existsBySeriesAndCharacterName(
			String series, String characterName, Long excludeId);

	// 用系列/角色搜尋
	List<Product> search(String series, String characterName);

	// 查詢所有不重複系列
	List<String> findAllSeries();

	// 分頁
	List<Product> findPage(int offset, int size);

	long countAll();

	// 搜尋+分頁
	List<Product> searchPage(String series, String characterName, int offset, int size);

	long countSearch(String series, String characterName);
}
