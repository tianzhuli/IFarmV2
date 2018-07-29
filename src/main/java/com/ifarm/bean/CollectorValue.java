package com.ifarm.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "collector_value")
public class CollectorValue implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer collectorVaulesId;
	private Long collectorId;
	private String collectorVersion;
	private Double collectorSignal;
	private Integer collectorSumItem;
	private Integer collectorValidItem;
	/* private Integer collectorDataItem; */
	private Timestamp updateTime;
	
	@Transient
	private static final long serialVersionUID = 7141663320727789577L;
	
	public void setCollectorVaulesId(Integer collectorVaulesId) {
		this.collectorVaulesId = collectorVaulesId;
	}

	public Integer getCollectorVaulesId() {
		return collectorVaulesId;
	}

	public Long getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(Long collectorId) {
		this.collectorId = collectorId;
	}

	public void setCollectorVersion(String collectorVersion) {
		this.collectorVersion = collectorVersion;
	}

	public String getCollectorVersion() {
		return collectorVersion;
	}

	public void setCollectorSignal(Double collectorSignal) {
		this.collectorSignal = collectorSignal;
	}

	public Double getCollectorSignal() {
		return collectorSignal;
	}

	public void setCollectorSumItem(Integer collectorSumItem) {
		this.collectorSumItem = collectorSumItem;
	}

	public Integer getCollectorSumItem() {
		return collectorSumItem;
	}

	public void setCollectorValidItem(Integer collectorValidItem) {
		this.collectorValidItem = collectorValidItem;
	}

	public Integer getCollectorValidItem() {
		return collectorValidItem;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}
}
