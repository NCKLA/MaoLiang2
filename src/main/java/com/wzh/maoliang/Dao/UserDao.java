package com.wzh.maoliang.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.wzh.maoliang.entity.User;

public interface UserDao extends JpaRepository<User, Integer> {
	@Transactional
	@Modifying
	@Query(value="update t_user set slot_current_data_id=?1,slot_has_done=?2 where user_id=?3",nativeQuery = true)
	public void updateSlotLabelState(int currentDataId,char has_done,int userId);
	
	Optional<User> findByUserIdAndPassWord(int i,String passWord);
}

