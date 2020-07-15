package com.wzh.maoliang.ServiceImpl;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wzh.maoliang.Dao.UserDao;
import com.wzh.maoliang.Service.UserService;
import com.wzh.maoliang.entity.User;
@Service
public class UserServiceImpl implements UserService {
	@Resource
	private UserDao userdao;

	@Override
	public void updateSlotLabelState(int currentDataId,char c, int userId) {
		userdao.updateSlotLabelState(currentDataId,c, userId);
		
	}
	@Override
	public Optional<User> findByUserIdAndPassWord(int i,String passWord) {
		return userdao.findByUserIdAndPassWord(i, passWord);
		
	}
	@Override
	public void updateActionLabelState(int currentDataId,char c, int userId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void save(User u) {
		userdao.save(u);
	}
	@Override
	public Optional<User> findById(int id) {
		return userdao.findById(id);
	}
	
}
