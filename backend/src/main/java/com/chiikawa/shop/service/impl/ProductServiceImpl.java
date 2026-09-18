package com.chiikawa.shop.service.impl;

import com.chiikawa.shop.dao.ProductDao;
import com.chiikawa.shop.dto.ProductPageResponse;
import com.chiikawa.shop.entity.Product;
import com.chiikawa.shop.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	@Qualifier("myBatisProductDao")
	private ProductDao productDao;

	@Override
	public List<Product> getAll() {
		return productDao.findAll();
	}

	// 管理員查詢全部商品，包含已下架商品
	@Override
	public List<Product> getAllForAdmin() {
		return productDao.findAllForAdmin();
	}

	@Override
	public Product getById(Long id) {
		return productDao.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到商品"));
	}

	@Override
	public List<Product> search(String series, String characterName) {
		return productDao.search(series, characterName);
	}

	@Override
	public Product create(Product product) {
		// 基本商品資料驗證
		normalize(product);

		// 新增商品時圖片必填
		validateImage(product);

		// =========================
		// 檢查系列 + 角色是否重複
		// 新增時 excludeId = null
		// =========================
		if (productDao.existsBySeriesAndCharacterName(product.getSeries(), product.getCharacterName(), null)) {
			throw new IllegalArgumentException("此系列與角色的商品已經存在");
		}

		return productDao.save(product);
	}

	@Override
	public Product update(Long id, Product input) {
		Product product = getById(id);

		product.setSeries(input.getSeries());
		product.setCharacterName(input.getCharacterName());
		product.setDescription(input.getDescription());
		product.setPrice(input.getPrice());
		product.setStock(input.getStock());
		
		//有傳新圖片才取代原本圖片
		if (input.getImageUrl() != null && !input.getImageUrl().isBlank()) {
			product.setImageUrl(input.getImageUrl());
		}

		normalize(product);// 基本資料驗證
		validateImage(product);// 修改後商品仍然必須有圖片

		// =========================
		// 檢查系列 + 角色是否重複
		// 修改時傳入目前商品 id，避免把商品自己判定為重複
		// =========================
		if (productDao.existsBySeriesAndCharacterName(
				product.getSeries(), product.getCharacterName(), id)) {
			throw new IllegalArgumentException("此系列與角色的商品已經存在");
		}
		return productDao.save(product);
	}

	@Override
	@Transactional
	public void deactivate(Long id) {
		Product product = productDao.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到商品"));

		// 已經下架就不需要重複處理
		if (!Boolean.TRUE.equals(product.getActive())) {
			throw new IllegalArgumentException("商品已經是下架狀態");
		}

		product.setActive(false);
		productDao.save(product);
	}

	@Override
	@Transactional
	public void activate(Long id) {
		Product product = productDao.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到商品"));

		// 已經上架就不需要重複處理
		if (Boolean.TRUE.equals(product.getActive())) {
			throw new IllegalArgumentException("商品已經是上架狀態");
		}

		product.setActive(true);
		productDao.save(product);
	}

	// 商品基本資料驗證
	private void normalize(Product product) {
		if (product.getSeries() == null || product.getSeries().isBlank()) {
			throw new IllegalArgumentException("商品系列不可空白");
		}

		if (product.getCharacterName() == null || product.getCharacterName().isBlank()) {
			throw new IllegalArgumentException("商品角色不可空白");
		}

		if (product.getPrice() == null || product.getPrice().signum() < 0) {
			throw new IllegalArgumentException("商品價格格式錯誤");
		}

		if (product.getStock() == null || product.getStock() < 0) {
			throw new IllegalArgumentException("庫存不可小於 0");
		}
	}

	// 圖片驗證
	private void validateImage(Product product) {
		if (product.getImageUrl() == null || product.getImageUrl().isBlank()
				|| "/images/placeholder.svg".equals(product.getImageUrl())) {
			throw new IllegalArgumentException("商品圖片為必填欄位");
		}
	}

	@Override
	public List<String> getAllSeries() {
		return productDao.findAllSeries();
	}

	@Override
	public ProductPageResponse<Product> getPage(int page, int size) {
		int safePage = Math.max(page, 1);
		int safeSize = size <= 0 ? 6 : size;
		int offset = (safePage - 1) * safeSize;
		List<Product> content = productDao.findPage(offset, safeSize);
		long totalElements = productDao.countAll();
		int totalPages = (int) Math.ceil((double) totalElements / safeSize);
		return new ProductPageResponse<>(content, safePage, safeSize, totalElements, totalPages);
	}

	@Override
	public ProductPageResponse<Product> searchPage(String series, String characterName, int page, int size) {

		int safePage = Math.max(page, 1);
		int safeSize = size <= 0 ? 6 : size;
		int offset = (safePage - 1) * safeSize;
		List<Product> content = productDao.searchPage(series, characterName, offset, safeSize);
		long totalElements = productDao.countSearch(series, characterName);
		int totalPages = (int) Math.ceil((double) totalElements / safeSize);
		return new ProductPageResponse<>(content, safePage, safeSize, totalElements, totalPages);
	}
}
