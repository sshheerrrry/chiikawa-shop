package com.chiikawa.shop.config;

import com.chiikawa.shop.entity.Product;
import com.chiikawa.shop.entity.User;
import com.chiikawa.shop.entity.UserRole;
import com.chiikawa.shop.repository.ProductRepository;
import com.chiikawa.shop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           ProductRepository productRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // 建立管理員 admin / 000
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("000"));
            admin.setName("管理員");
            admin.setEmail("admin@chiikawa.shop");
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
        }

        // products 為空時才建立初始資料，避免每次啟動重複新增。
        if (productRepository.count() == 0) {
            saveProduct("天使惡魔", "吉伊", 450);
            saveProduct("天使惡魔", "小八", 499);
            saveProduct("天使惡魔", "烏薩奇", 550);

            saveProduct("平行世界", "吉伊", 450);
            saveProduct("平行世界", "小八", 499);
            saveProduct("平行世界", "烏薩奇", 550);

            saveProduct("樂園", "吉伊", 450);
            saveProduct("樂園", "小八", 499);
            saveProduct("樂園", "烏薩奇", 550);

            saveProduct("壽司", "吉伊", 450);
            saveProduct("壽司", "小八", 499);
            saveProduct("壽司", "烏薩奇", 550);
        }
    }

    private void saveProduct(String series, String characterName, int price) {
        Product product = new Product();

        // 不設定 id。
        // @GeneratedValue(strategy = GenerationType.IDENTITY)
        // 會交給 MySQL AUTO_INCREMENT 自動產生。
        product.setSeries(series);
        product.setCharacterName(characterName);
        product.setDescription(series + "系列－" + characterName);
        product.setPrice(BigDecimal.valueOf(price));
        product.setStock(10);
        product.setImageUrl("/images/placeholder.svg");

        productRepository.save(product);
    }
}
