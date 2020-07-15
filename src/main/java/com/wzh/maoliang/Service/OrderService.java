package com.wzh.maoliang.Service;

import java.util.Optional;

import com.wzh.maoliang.entity.Order;

public interface OrderService {
	public Optional<Order> findById(int id);
}
