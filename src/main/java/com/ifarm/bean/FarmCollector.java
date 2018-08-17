package com.ifarm.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ifarm.annotation.LikeField;

@Entity
@Table(name = "farm_collector")
public class FarmCollector {
	@Id
	private Long collectorId;
	private Integer farmId;
	@LikeField
	private String collectorLocation;
	@LikeField
	private String collectorType;
	@LikeField
	private String collectorVersion;
	private Timestamp collectorCreateTime;

	public Long getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(Long collectorId) {
		this.collectorId = collectorId;
	}

	public Integer getFarmId() {
		return farmId;
	}

	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}

	public void setCollectorLocation(String collectorLocation) {
		this.collectorLocation = collectorLocation;
	}

	public String getCollectorLocation() {
		return collectorLocation;
	}

	public void setCollectorType(String collectorType) {
		this.collectorType = collectorType;
	}

	public String getCollectorType() {
		return collectorType;
	}

	public void setCollectorVersion(String collectorVersion) {
		this.collectorVersion = collectorVersion;
	}

	public String getCollectorVersion() {
		return collectorVersion;
	}

	public void setCollectorCreateTime(Timestamp collectorCreateTime) {
		this.collectorCreateTime = collectorCreateTime;
	}

	public Timestamp getCollectorCreateTime() {
		return collectorCreateTime;
	}
}
