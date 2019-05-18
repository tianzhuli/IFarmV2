package com.ifarm.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ifarm.annotation.LikeField;
import com.ifarm.annotation.Validator;

@Entity
@Table(name = "farm_collector_unit")
public class FarmCollectorUnit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer unitId;
	@Validator
	private Integer farmId;
	@LikeField
	@Validator
	private String unitDistrict;
	@Validator
	private String unitNo;
	private String unitExtInfo;
	@LikeField
	@Validator
	private String unitPosition;
	private Timestamp unitCreateTime;

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getFarmId() {
		return farmId;
	}

	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}

	public String getUnitDistrict() {
		return unitDistrict;
	}

	public void setUnitDistrict(String unitDistrict) {
		this.unitDistrict = unitDistrict;
	}

	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getUnitExtInfo() {
		return unitExtInfo;
	}

	public void setUnitExtInfo(String unitExtInfo) {
		this.unitExtInfo = unitExtInfo;
	}

	public String getUnitPosition() {
		return unitPosition;
	}

	public void setUnitPosition(String unitPosition) {
		this.unitPosition = unitPosition;
	}

	public Timestamp getUnitCreateTime() {
		return unitCreateTime;
	}

	public void setUnitCreateTime(Timestamp unitCreateTime) {
		this.unitCreateTime = unitCreateTime;
	}

}
