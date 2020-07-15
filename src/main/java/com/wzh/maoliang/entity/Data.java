package com.wzh.maoliang.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_data")
public class Data implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	数据库表t_data的对象实体
	@Id
	@GeneratedValue
	@Column(name="data_id")
	private Integer dataId;
	
	@Column(name="chat_record")
	private String chatRecord;
	
	@Column(name="split_chat_record")
	private String splitChatRecord;
	private String speaker;
	
	@Column(name="slot_value_label")
	private String slotValueLabel;
	
	@Column(name="action_label")
	private String actionLabel;
	private String remark;
	
	@Column(name="o_id")
	private Integer orderId;

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	public String getChatRecord() {
		return chatRecord;
	}

	public void setChatRecord(String chatRecord) {
		this.chatRecord = chatRecord;
	}

	public String getSplitChatRecord() {
		return splitChatRecord;
	}

	public void setSplitChatRecord(String splitChatRecord) {
		this.splitChatRecord = splitChatRecord;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public String getSlotValueLabel() {
		return slotValueLabel;
	}

	public void setSlotValueLabel(String slotValueLabel) {
		this.slotValueLabel = slotValueLabel;
	}

	public String getActionLabel() {
		return actionLabel;
	}

	public void setActionLabel(String actionLabel) {
		this.actionLabel = actionLabel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "Data [dataId=" + dataId + ", chatRecord=" + chatRecord + ", splitChatRecord=" + splitChatRecord
				+ ", speaker=" + speaker + ", slotValueLabel=" + slotValueLabel + ", actionLabel=" + actionLabel
				+ ", remark=" + remark + ", orderId=" + orderId + "]";
	}
	
	
	
}
