package com.chiikawa.shop.service;

import com.chiikawa.shop.entity.CustomerOrder;
import com.chiikawa.shop.entity.PaymentMethod;

import java.util.List;

public interface OrderService {

	CustomerOrder checkout(Long userId, Long addressId, PaymentMethod paymentMethod);

	List<CustomerOrder> history(Long userId);
}