package com.ifarm.nosql.bean;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "controlSystem")
public class ControlSystemState {
	private String collectorId;
	private Integer masterControl;
	private List<Integer> fertilizationCans;
	private List<Integer> controlAreas;
	private Date time;

	public ControlSystemState(String collectorId, Date time) {
		super();
		this.collectorId = collectorId;
		this.time = time;
	}

	public String getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(String collectorId) {
		this.collectorId = collectorId;
	}

	public Integer getMasterControl() {
		return masterControl;
	}

	public void setMasterControl(Integer masterControl) {
		this.masterControl = masterControl;
	}

	public List<Integer> getFertilizationCans() {
		return fertilizationCans;
	}

	public void setFertilizationCans(List<Integer> fertilizationCans) {
		this.fertilizationCans = fertilizationCans;
	}

	public List<Integer> getControlAreas() {
		return controlAreas;
	}

	public void setControlAreas(List<Integer> controlAreas) {
		this.controlAreas = controlAreas;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
