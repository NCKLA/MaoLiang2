package com.wzh.maoliang.ServiceImpl;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wzh.maoliang.Dao.DataDao;
import com.wzh.maoliang.Service.DataService;
import com.wzh.maoliang.entity.Data;
@Service("DataService")
public class DataServiceImpl implements DataService{
	
	@Resource
	private DataDao datadao;
	
	@Override
	public Optional<Data> findByDataIdAndOrderId(int dataId, int orderId) {		
		return datadao.findByDataIdAndOrderId(dataId, orderId);
	}
	@Override
	public Optional<Data> findById(int id){
		return datadao.findById(id);
	}
	@Override
	public int findFirstDataId() {
		return datadao.findFirstByDataId();
	}
}
