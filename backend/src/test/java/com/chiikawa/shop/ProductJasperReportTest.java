package com.chiikawa.shop;

import com.chiikawa.shop.dao.ProductDao;
import com.chiikawa.shop.entity.Product;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ProductJasperReportTest {
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(ProductJasperReportTest.class);

    // 從 DAO 取得商品資料
    // 不直接使用 Repository
    @Autowired
    @Qualifier("myBatisProductDao")
    private ProductDao productDao;

    @Test
    public void testGenerateProductReport() throws Exception {

        try {

            logger.info("開始執行商品 JasperReport PDF 產出測試");

            // ==========================================
            // 步驟 1：從 DAO 取得商品資料
            // ==========================================

            logger.info("正在透過 ProductDao 從資料庫取得商品資料...");

            List<Product> products = productDao.findAll();
                    
            logger.info("成功從資料庫取得 {} 筆商品資料",products.size());

            if (products.isEmpty()) {
                logger.warn("目前資料庫沒有商品，PDF 商品資料區將會是空的");
            }

            // ==========================================
            // 步驟 2：建立 Jasper DataSource
            // ==========================================

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(products);
            logger.info("JRBeanCollectionDataSource 建立完成");
            
            // ==========================================
            // 步驟 3：讀取 JRXML
            // ==========================================

            logger.info("開始讀取 ProductList_chiikawa.jrxml...");

            InputStream jrxmlStream =getClass().getResourceAsStream("/reports/ProductList_chiikawa.jrxml");

            if (jrxmlStream == null) {
                throw new RuntimeException(
                        "找不到 ProductList_chiikawa.jrxml，"
                        + "請確認檔案位於 "
                        + "src/main/resources/reports/"
                );
            }

            logger.info("ProductList_chiikawa.jrxml 讀取成功");

            // ==========================================
            // 步驟 4：編譯 JasperReport
            // ==========================================

            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            logger.info("ProductList_chiikawa.jrxml 編譯成功");

            // ==========================================
            // 步驟 5：設定報表參數
            // ==========================================

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle","吉伊卡哇商品清單");
            logger.info("報表參數設定完成");

            // ==========================================
            // 步驟 6：填入商品資料
            // ==========================================

            JasperPrint print = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
            logger.info("商品資料填入 JasperReport 完成");

            // ==========================================
            // 步驟 7：輸出 PDF
            // ==========================================

            String outputPath = "product_report.pdf";
            JasperExportManager.exportReportToPdfFile(print,outputPath);

            // ==========================================
            // 步驟 8：顯示 PDF 完整位置
            // ==========================================

            File outputFile = new File(outputPath);
            logger.info("商品 PDF 報表產出完成");
            logger.info("PDF 完整路徑：{}",outputFile.getAbsolutePath());
        } catch (Exception e) {
            logger.error("商品 PDF 報表產出失敗",e);
            if (e.getCause() != null) {
                logger.error("詳細錯誤原因：",e.getCause());
            }
            throw e;
        }
    }
}