package com.sxy.graduwork.po;

import java.sql.Timestamp;

public class SearchConfig {
	private String id;
	private String checkedDatabase;
	private String configJson;
	private String schedulePattern;
	private Timestamp createTime;
	private String deleteMark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCheckedDatabase() {
		return checkedDatabase;
	}

	public void setCheckedDatabase(String checkedDatabase) {
		this.checkedDatabase = checkedDatabase;
	}

	public String getConfigJson() {
		return configJson;
	}

	public void setConfigJson(String configJson) {
		this.configJson = configJson;
	}

	public String getSchedulePattern() {
		return schedulePattern;
	}

	public void setSchedulePattern(String schedulePattern) {
		this.schedulePattern = schedulePattern;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getDeleteMark() {
		return deleteMark;
	}

	public void setDeleteMark(String deleteMark) {
		this.deleteMark = deleteMark;
	}

}
