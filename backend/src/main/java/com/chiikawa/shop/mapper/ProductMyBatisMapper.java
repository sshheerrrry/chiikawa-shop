package com.chiikawa.shop.mapper;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.chiikawa.shop.entity.Product;

@Mapper
public interface ProductMyBatisMapper {
	List<Product> findAll();
	//管理員查詢全部商品，包含 active = 0
	List<Product> findAllForAdmin();
	
	Optional<Product> findById(@Param("id") Long id);
	
	//用系列/角色搜尋
	List<Product> search(
			@Param("series") String series,
			@Param("characterName") String characterName);
	
	//新增商品
	int insert(Product product);
	
	//修改商品
	int update(Product product);
	
	//商品是否存在
	int countById(@Param("id") Long id);
	
	//檢查系列 + 角色是否已經存在
	int countBySeriesAndCharacterName(
	        @Param("series") String series,
	        @Param("characterName") String characterName,
	        @Param("excludeId") Long excludeId
	);
	
	int deleteById(@Param("id") Long id);//刪除商品
	
	List<String> findAllSeries();//查詢所有不重複系列
	
	//分頁
	List<Product> findPage(
	        @Param("offset") int offset,
	        @Param("size") int size
	);

	long countAll();

	//搜尋分頁
	List<Product> searchPage(
	        @Param("series") String series,
	        @Param("characterName") String characterName,
	        @Param("offset") int offset,
	        @Param("size") int size
	);

	long countSearch(
	        @Param("series") String series,
	        @Param("characterName") String characterName
	);
}
