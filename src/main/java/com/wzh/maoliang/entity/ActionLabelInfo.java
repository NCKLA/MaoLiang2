package com.wzh.maoliang.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_action_labeling")
public class ActionLabelInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="info_id")
	private int infoId;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="data_id")
	private int dataId;
	
	@Column(name="action_label_info")
	private String actionLabelInfo;

	public int getInfoId() {
		return infoId;
	}

	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getDataId() {
		return dataId;
	}

	public void setDataId(int dataId) {
		this.dataId = dataId;
	}

	public String getActionLabelInfo() {
		return actionLabelInfo;
	}

	public void setActionLabelInfo(String actionLabelInfo) {
		this.actionLabelInfo = actionLabelInfo;
	}	
}
