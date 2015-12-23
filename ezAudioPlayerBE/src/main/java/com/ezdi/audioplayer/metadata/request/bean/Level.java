package com.ezdi.audioplayer.metadata.request.bean;

public class Level {
	int id;
	String subGroupId, targetServerId, levelStatusId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubGroupId() {
		return subGroupId;
	}

	public void setSubGroupId(String subGroupId) {
		this.subGroupId = subGroupId;
	}

	public String getTargetServerId() {
		return targetServerId;
	}

	public void setTargetServerId(String targetServerId) {
		this.targetServerId = targetServerId;
	}

	public String getLevelStatusId() {
		return levelStatusId;
	}

	public void setLevelStatusId(String levelStatusId) {
		this.levelStatusId = levelStatusId;
	}

}
