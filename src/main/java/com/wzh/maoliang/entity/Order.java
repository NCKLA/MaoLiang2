package com.wzh.maoliang.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="t_order")
public class Order implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	数据库表t_order的对象实体
	@Id
	@GeneratedValue
	@Column(name="order_id")
	private Integer orderId;
	
	@Column(name="buyer_nick")
	private String buyerNick;
	
	@Column(name="order_times")
	private String orderTimes;
	
	@Column(name="order_info")
	private String orderInfo;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getOrderTimes() {
		return orderTimes;
	}

	public void setOrderTimes(String orderTimes) {
		this.orderTimes = orderTimes;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", buyerNick=" + buyerNick + ", orderTimes=" + orderTimes + ", orderInfo="
				+ orderInfo + "]";
	}

	
}
