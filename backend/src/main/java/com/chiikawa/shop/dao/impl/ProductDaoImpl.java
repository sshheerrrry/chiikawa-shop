package com.chiikawa.shop.dao.impl;

import com.chiikawa.shop.dao.ProductDao;
import com.chiikawa.shop.entity.Product;
import com.chiikawa.shop.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpaProductDao")
public class ProductDaoImpl implements ProductDao {
	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public List<Product> findAllForAdmin() {
		return productRepository.findAll();
	}

	@Override
	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	@Override
	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return productRepository.existsById(id);
	}

	@Override
	public List<Product> search(String series, String characterName) {
		boolean hasSeries = series != null && !series.isBlank();
		boolean hasCharacter = characterName != null && !characterName.isBlank();

		if (hasSeries && hasCharacter) {
			return productRepository.findBySeriesContainingIgnoreCaseAndCharacterNameContainingIgnoreCase(series.trim(),
					characterName.trim());
		}

		if (hasSeries) {
			return productRepository.findBySeriesContainingIgnoreCase(series.trim());
		}

		if (hasCharacter) {
			return productRepository.findByCharacterNameContainingIgnoreCase(characterName.trim());
		}

		return productRepository.findAll();
	}

	@Override
	public List<String> findAllSeries() {
		return productRepository.findDistinctSeries();
	}

	@Override
	public List<Product> findPage(int offset, int size) {
		int page = offset / size;
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		return productRepository.findAll(pageable).getContent();
	}

	@Override
	public long countAll() {
		return productRepository.count();
	}

	@Override
	public List<Product> searchPage(String series, String characterName, int offset, int size) {
		int page = offset / size;
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		boolean hasSeries = series != null && !series.isBlank();
		boolean hasCharacter = characterName != null && !characterName.isBlank();

		// 系列+角色
		if (hasSeries && hasCharacter) {
			return productRepository.findBySeriesContainingIgnoreCaseAndCharacterNameContainingIgnoreCase(series,
					characterName, pageable).getContent();
		}

		// 只有系列
		if (hasSeries) {
			return productRepository.findBySeriesContainingIgnoreCase(series, pageable).getContent();
		}

		// 只有角色
		if (hasCharacter) {
			return productRepository.findByCharacterNameContainingIgnoreCase(characterName, pageable).getContent();
		}

		// 都沒條件
		return productRepository.findAll(pageable).getContent();
	}

	@Override
	public long countSearch(String series, String characterName) {
		Pageable pageable = PageRequest.of(0, 1);

		boolean hasSeries = series != null && !series.isBlank();
		boolean hasCharacter = characterName != null && !characterName.isBlank();

		if (hasSeries && hasCharacter) {
			return productRepository.findBySeriesContainingIgnoreCaseAndCharacterNameContainingIgnoreCase(series,
					characterName, pageable).getTotalElements();
		}

		if (hasSeries) {
			return productRepository.findBySeriesContainingIgnoreCase(series, pageable).getTotalElements();
		}

		if (hasCharacter) {
			return productRepository.findByCharacterNameContainingIgnoreCase(characterName, pageable)
					.getTotalElements();
		}
		return productRepository.count();
	}

	@Override
	public boolean existsBySeriesAndCharacterName(String series, String characterName, Long excludeId) {
		return productRepository.findAll().stream().anyMatch(product -> {
			// 修改時排除目前商品自己
			if (excludeId != null && excludeId.equals(product.getId())) {
				return false;
			}

			if (product.getSeries() == null || product.getCharacterName() == null) {
				return false;
			}

			return product.getSeries().trim().equalsIgnoreCase(series.trim())
					&& product.getCharacterName().trim().equalsIgnoreCase(characterName.trim());
		});
	}
}
