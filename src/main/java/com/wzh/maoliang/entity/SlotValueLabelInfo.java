package com.wzh.maoliang.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_slot_value_labeling")
public class SlotValueLabelInfo {
	
	public SlotValueLabelInfo(int userId,int dataId,char labelType,String slotLabelInfo){
		setUserId(userId);
		setDataId(dataId);
		setLabelType(labelType);
		setSlotLabelInfo(slotLabelInfo);
	}
	public SlotValueLabelInfo(){}
	@Id
	@GeneratedValue
	@Column(name="info_id")
	private int infoId;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="data_id")
	private int dataId;
	
	@Column(name="slot_label_info")
	private String slotLabelInfo;
	
	/**
	 * 标注类型  S   skip 表示这题是跳过的  L label  表示是标注并提交的     如果没有标注不会进db
	 */
	@Column(name="label_type")
	private char labelType;

	
	public char getLabelType() {
		return labelType;
	}

	public void setLabelType(char labelType) {
		this.labelType = labelType;
	}
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

	public String getSlotLabelInfo() {
		return slotLabelInfo;
	}

	public void setSlotLabelInfo(String slotLabelInfo) {
		this.slotLabelInfo = slotLabelInfo;
	}
	
	
}
