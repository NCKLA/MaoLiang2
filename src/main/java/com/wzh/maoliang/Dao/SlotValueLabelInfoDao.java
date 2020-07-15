package com.wzh.maoliang.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wzh.maoliang.entity.SlotValueLabelInfo;

public interface SlotValueLabelInfoDao extends JpaRepository<SlotValueLabelInfo, Integer> {	
	Optional<SlotValueLabelInfo> findByDataIdAndUserId(int userId, int dataId);
}
