package com.wzh.maoliang.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wzh.maoliang.entity.Data;

public interface DataDao extends JpaRepository<Data,Integer>{
	Optional<Data> findByDataIdAndOrderId(int dataId,int orderId);
	
	@Query(value="select min(data_id) from t_data",nativeQuery = true)
	public int findFirstByDataId();
}
