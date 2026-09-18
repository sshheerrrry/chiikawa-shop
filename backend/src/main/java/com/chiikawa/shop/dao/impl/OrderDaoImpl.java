package com.chiikawa.shop.dao.impl;

import com.chiikawa.shop.dao.OrderDao;
import com.chiikawa.shop.entity.CustomerOrder;
import com.chiikawa.shop.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final OrderRepository orderRepository;

    public OrderDaoImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public CustomerOrder save(CustomerOrder order) {
        return orderRepository.save(order);
    }

    @Override
    public List<CustomerOrder> findByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

	@Override
	public List<CustomerOrder> findAll() {
		return orderRepository.findAllByOrderByCreatedAtDesc();
	}
}
