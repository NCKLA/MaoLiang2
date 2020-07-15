package com.wzh.maoliang.Service;

import java.util.Optional;

import com.wzh.maoliang.entity.Data;


public interface DataService {
	Optional<Data> findByDataIdAndOrderId(int dataId,int orderId);

	Optional<Data> findById(int id);

	int findFirstDataId();
	
	
}
