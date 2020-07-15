package com.wzh.maoliang.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wzh.maoliang.entity.Order;

public interface OrderDao extends JpaRepository<Order, Integer> {

}
