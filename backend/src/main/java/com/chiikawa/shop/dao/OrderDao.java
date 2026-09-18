package com.chiikawa.shop.dao;

import com.chiikawa.shop.entity.CustomerOrder;
import java.util.List;

public interface OrderDao {
    CustomerOrder save(CustomerOrder order);
    List<CustomerOrder> findByUserId(Long userId);
    // 管理員查詢所有訂單
    List<CustomerOrder> findAll();
}
