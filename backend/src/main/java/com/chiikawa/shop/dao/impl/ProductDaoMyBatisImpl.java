package com.chiikawa.shop.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chiikawa.shop.dao.ProductDao;
import com.chiikawa.shop.entity.Product;
import com.chiikawa.shop.mapper.ProductMyBatisMapper;

@Repository("myBatisProductDao")
public class ProductDaoMyBatisImpl implements ProductDao{
	@Autowired
	private ProductMyBatisMapper productMyBatisMapper;
	
	@Override
	public List<Product> findAll() {
		return productMyBatisMapper.findAll();
	}
	
	//管理員查詢全部商品，包含已下架商品
	@Override
	public List<Product> findAllForAdmin() {
	    return productMyBatisMapper.findAllForAdmin();
	}

	@Override
	public Optional<Product> findById(Long id) {
		return productMyBatisMapper.findById(id);
	}

	@Override
	public Product save(Product product) {
		// id == null
        // 代表新增
        if (product.getId() == null) {
            productMyBatisMapper.insert(product);
        } else {
            // 有 id
            // 代表修改
            productMyBatisMapper.update(product);
        }
        return product;
	}

	@Override
	public void deleteById(Long id) {
		productMyBatisMapper.deleteById(id);
		
	}

	@Override
	public boolean existsById(Long id) {
		return productMyBatisMapper.countById(id) > 0;
	}
	
	@Override
	public boolean existsBySeriesAndCharacterName(
	        String series,
	        String characterName,
	        Long excludeId) {

	    return productMyBatisMapper
	            .countBySeriesAndCharacterName(series,characterName,excludeId) > 0;
	}

	@Override
	public List<Product> search(String series, String characterName) {
		return productMyBatisMapper.search(series,characterName);
	}

	@Override
	public List<String> findAllSeries() {
		return productMyBatisMapper.findAllSeries();
	}

	@Override
	public List<Product> findPage(int offset,int size) {
	    return productMyBatisMapper.findPage(offset, size);     
	}

	@Override
	public long countAll() {
	    return productMyBatisMapper.countAll();
	}

	@Override
	public List<Product> searchPage(
	        String series,
	        String characterName,
	        int offset,
	        int size) {

	    return productMyBatisMapper.searchPage(series,characterName,offset,size);
	}

	@Override
	public long countSearch(String series,String characterName) {
	    return productMyBatisMapper.countSearch(series,characterName);   
	}

}
