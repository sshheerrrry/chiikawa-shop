package com.chiikawa.shop.controller;

import com.chiikawa.shop.dao.ProductDao;
import com.chiikawa.shop.entity.Product;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductReportController {
    private final ProductDao productDao;
    private final JwtAuthHelper jwtAuthHelper;

    public ProductReportController(
            @Qualifier("myBatisProductDao") ProductDao productDao,
            JwtAuthHelper jwtAuthHelper) {
        this.productDao = productDao;
        this.jwtAuthHelper = jwtAuthHelper;
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestHeader(value = "Authorization",required = false)
            String authorizationHeader) {
        try {
            // 只有管理員可以匯出
            jwtAuthHelper.requireAdmin(authorizationHeader);

            // 從 DAO 取得所有商品
            List<Product> products = productDao.findAll();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(products);
            InputStream jrxmlStream = Thread.currentThread()
            		.getContextClassLoader()
                    .getResourceAsStream("reports/ProductList_chiikawa.jrxml");

            if (jrxmlStream == null) {
                throw new RuntimeException("找不到 ProductList_chiikawa.jrxml");
            }

            var jasperReport = JasperCompileManager.compileReport(jrxmlStream);

	         // ==============================
	         // 設定 JasperReport 參數
	         // ==============================
	         HashMap<String, Object> parameters = new HashMap<>();
	         parameters.put("ReportTitle","Chiikawa Shop 商品清單");

	         // ==============================
	         // 填入資料 + 標題參數
	         // ==============================
	         JasperPrint jasperPrint = JasperFillManager.fillReport(
	        		 jasperReport,
	        		 parameters,
	        		 dataSource);

            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=chiikawa-products.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}