package com.chiikawa.shop.service;

import com.chiikawa.shop.dto.ProductPageResponse;
import com.chiikawa.shop.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAll();
    // 管理員查詢全部商品，包含已下架商品
    List<Product> getAllForAdmin();
    
    Product getById(Long id);
    
    List<Product> search(String series, String characterName);
    
    Product create(Product product);
    
    Product update(Long id, Product product);
    
    // 下架商品
    void deactivate(Long id);
    
    // 重新上架商品
    void activate(Long id);
    
    List<String> getAllSeries();
    
    ProductPageResponse<Product> getPage(int page,int size);
    
    ProductPageResponse<Product> searchPage(
            String series,
            String characterName,
            int page,
            int size
    );
}
