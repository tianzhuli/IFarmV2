package com.ifarm.nosql.bean;


import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "group_control_task")
public class CombinationControlTask {
	private String taskId;
	private String userId;
	private Date taskTime;
	private String taskContent;
	
	public CombinationControlTask(String userId, String taskContent) {
		this.userId = userId;
		this.taskContent = taskContent;
		this.taskTime = new Date();
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(Date taskTime) {
		this.taskTime = taskTime;
	}

	public String getTaskContent() {
		return taskContent;
	}

	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}

}
