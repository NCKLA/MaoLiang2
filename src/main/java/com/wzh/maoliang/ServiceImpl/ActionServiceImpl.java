package com.wzh.maoliang.ServiceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wzh.maoliang.Dao.ActionLabelInfoDao;
import com.wzh.maoliang.Service.ActionService;
@Service("ActionService")
public class ActionServiceImpl implements ActionService {
	@Resource
	private ActionLabelInfoDao actiondao;
}
