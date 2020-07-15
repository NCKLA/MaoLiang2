package com.wzh.maoliang.Service;

import java.util.Optional;

import com.wzh.maoliang.entity.SlotValueLabelInfo;

public interface SlotService {
	public Optional<SlotValueLabelInfo> findById(int id);
	public Optional<SlotValueLabelInfo> findByDataIdAndUserId(int dataId, int userId);
	void save(SlotValueLabelInfo e);
}
