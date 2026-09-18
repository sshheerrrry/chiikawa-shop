package com.chiikawa.shop.controller;

import com.chiikawa.shop.dto.ProductPageResponse;
import com.chiikawa.shop.dto.ProductPublicResponse;
import com.chiikawa.shop.entity.Product;
import com.chiikawa.shop.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 一般使用者瀏覽商品：不回傳庫存 stock
    @GetMapping
    public ProductPageResponse<ProductPublicResponse> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size) {
        ProductPageResponse<Product> result = productService.getPage(page,size);
        List<ProductPublicResponse> content = result.getContent().stream()
                        .map(ProductPublicResponse::from)
                        .toList();

        return new ProductPageResponse<>(
                content,
                result.getCurrentPage(),
                result.getPageSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
    
    // 查詢所有商品系列
    @GetMapping("/series")
    public List<String> getAllSeries() {
    	return productService.getAllSeries();
    }

    // 一般使用者查詢單一商品：不回傳庫存 stock
    @GetMapping("/{id:\\d+}")//限制{id}必須是一個以上的數字，\d-->數字0～9; + --> 一個以上
    public ProductPublicResponse getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ProductPublicResponse.from(product);
    }

    // 一般使用者搜尋商品：不回傳庫存 stock
    @GetMapping("/search")
    public ProductPageResponse<ProductPublicResponse> search(
            @RequestParam(required = false) String series,
            @RequestParam(required = false) String characterName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size) {
        ProductPageResponse<Product> result =
                productService.searchPage(series,characterName,page,size);
        List<ProductPublicResponse> content =
                result.getContent()
                        .stream()
                        .map(ProductPublicResponse::from)
                        .toList();

        return new ProductPageResponse<>(
                content,
                result.getCurrentPage(),
                result.getPageSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}
