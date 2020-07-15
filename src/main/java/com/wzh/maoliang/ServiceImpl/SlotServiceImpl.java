package com.wzh.maoliang.ServiceImpl;



import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.wzh.maoliang.Dao.SlotValueLabelInfoDao;
import com.wzh.maoliang.Service.SlotService;
import com.wzh.maoliang.entity.SlotValueLabelInfo;

@Service("SlotService")
public class SlotServiceImpl implements SlotService {
	
	@Resource
	private SlotValueLabelInfoDao slotdao;
	

	@Override
	public Optional<SlotValueLabelInfo> findById(int infoId) {
		return slotdao.findById(infoId);
	}


	@Override
	public Optional<SlotValueLabelInfo> findByDataIdAndUserId(int dataId, int userId) {
		return slotdao.findByDataIdAndUserId(dataId,userId);
	}
	
	@Override
	public void save(SlotValueLabelInfo e) {
		slotdao.save(e);
	}
	
}
