package com.wzh.maoliang.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Table(name="t_user")
@Entity
public class User {
	
	@Id
	@NotNull(message = "学号不能为空")
	@Column(name="user_id")
	private int userId;
	
	
	@NotEmpty(message = "密码不能为空！")
	@Column(name="pass_word")
	private String passWord;
	
	@Column(name="slot_current_data_id")
	private int slotCurrentDataId;
	
	@Column(name="slot_has_done")
	private char slotHasDoneCurrentData = 'T';
	
	@Column(name="action_current_data_id")
	private int actionCurrentDataId;
	
	@Column(name="action_has_done")
	private char actionHasDoneCurrentData = 'T';
	
	@Column(length=30)
	private String name;
	
	@Column(length=100)
	private String remark;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSlotCurrentDataId() {
		return slotCurrentDataId;
	}

	public void setSlotCurrentDataId(int slotCurrentDataId) {
		this.slotCurrentDataId = slotCurrentDataId;
	}

	public char getSlotHasDoneCurrentData() {
		return slotHasDoneCurrentData;
	}

	public void setSlotHasDoneCurrentData(char slotHasDoneCurrentData) {
		this.slotHasDoneCurrentData = slotHasDoneCurrentData;
	}

	public int getActionCurrentDataId() {
		return actionCurrentDataId;
	}

	public void setActionCurrentDataId(int actionCurrentDataId) {
		this.actionCurrentDataId = actionCurrentDataId;
	}

	public char getActionHasDoneCurrentData() {
		return actionHasDoneCurrentData;
	}

	public void setActionHasDoneCurrentData(char actionHasDoneCurrentData) {
		this.actionHasDoneCurrentData = actionHasDoneCurrentData;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", passWord=" + passWord + ", slotCurrentDataId=" + slotCurrentDataId
				+ ", slotHasDoneCurrentData=" + slotHasDoneCurrentData + ", actionCurrentDataId=" + actionCurrentDataId
				+ ", actionHasDoneCurrentData=" + actionHasDoneCurrentData + ", name=" + name + ", remark=" + remark
				+ "]";
	}
	

}
