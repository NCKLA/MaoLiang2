package com.wzh.maoliang.ServiceImpl;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wzh.maoliang.Dao.OrderDao;
import com.wzh.maoliang.Service.OrderService;
import com.wzh.maoliang.entity.Order;


@Service("OrderService")
public class OrderServiceImpl implements OrderService {
	@Resource
	private OrderDao orderdao;

	@Override
	public Optional<Order> findById(int id) {
		return orderdao.findById(id);
	}
}
