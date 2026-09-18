package com.chiikawa.shop.repository;

import com.chiikawa.shop.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findBySeriesContainingIgnoreCase(String series);

    List<Product> findByCharacterNameContainingIgnoreCase(String characterName);

    List<Product> findBySeriesContainingIgnoreCaseAndCharacterNameContainingIgnoreCase(
            String series, String characterName);
    
    //分頁搜尋
    Page<Product> findBySeriesContainingIgnoreCase(String series,Pageable pageable);

    Page<Product> findByCharacterNameContainingIgnoreCase(String characterName,Pageable pageable);

    Page<Product>
    findBySeriesContainingIgnoreCaseAndCharacterNameContainingIgnoreCase(
            String series,
            String characterName,
            Pageable pageable
    );
    
    //查詢不重複系列
    @Query("SELECT DISTINCT p.series FROM Product p ORDER BY p.series")
    List<String> findDistinctSeries();
}
