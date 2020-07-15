package com.wzh.maoliang.Service;

import java.util.Optional;

import com.wzh.maoliang.entity.User;

public interface UserService {


	Optional<User> findByUserIdAndPassWord(int i, String passWord);

	void updateSlotLabelState(int currentDataId,char c, int userId);

	void save(User u);

	Optional<User> findById(int id);

	void updateActionLabelState(int currentDataId,char c, int userId);


}
