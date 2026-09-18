package com.chiikawa.shop.controller;

import com.chiikawa.shop.entity.Product;
import com.chiikawa.shop.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final JwtAuthHelper jwtAuthHelper;

    public AdminProductController(ProductService productService,JwtAuthHelper jwtAuthHelper) {
        this.productService = productService;
        this.jwtAuthHelper = jwtAuthHelper;
    }

    // 管理員查詢全部商品：包含上架與下架
    @GetMapping
    public List<Product> getAll(
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {
    	
        jwtAuthHelper.requireAdmin(authorizationHeader);
        return productService.getAllForAdmin();
    }

    // 管理員查詢單一商品
    @GetMapping("/{id}")
    public Product getById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {

        jwtAuthHelper.requireAdmin(authorizationHeader);
        return productService.getById(id);
    }
    
    //新增商品
    @PostMapping
    public Product create(
            @RequestBody Product product,
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {

        jwtAuthHelper.requireAdmin(authorizationHeader);
        return productService.create(product);
    }

    //修改商品
    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody Product product,
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {

        jwtAuthHelper.requireAdmin(authorizationHeader);
        return productService.update(id, product);
    }

    //下架商品
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {
    	
        jwtAuthHelper.requireAdmin(authorizationHeader);
        productService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
    
    //重新上架商品
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {
    	
        jwtAuthHelper.requireAdmin(authorizationHeader);
        productService.activate(id);
        return ResponseEntity.noContent().build();
    }
}
