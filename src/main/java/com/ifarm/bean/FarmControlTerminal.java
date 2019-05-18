package com.ifarm.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ifarm.annotation.Validator;

@Entity
@Table(name = "farm_control_terminal")
public class FarmControlTerminal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer terminalId;
	@Validator
	private Integer controlDeviceId;
	@Validator
	private Integer unitId;
	@Validator
	private Integer controlDeviceBit;
	@Validator
	private String controlType;
	@Validator
	private String functionName;
	@Validator
	private String functionCode;
	private String terminalCode; // 端口标识
	private Timestamp terminalCreateTime;
	// 优先级，例如水肥药里面泵先启动，优先级可配置
	@Validator
	private String priority;
	private Integer terminalNo;
	private String remark;
	
	public FarmControlTerminal() {
		
	}
	
	public FarmControlTerminal(Integer controlDeviceId, Integer controlDeviceBit) {
		super();
		this.controlDeviceId = controlDeviceId;
		this.controlDeviceBit = controlDeviceBit;
	}

	public FarmControlTerminal(Integer unitId, String terminalCode) {
		super();
		this.unitId = unitId;
		this.terminalCode = terminalCode;
	}

	public Integer getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}

	public Integer getControlDeviceId() {
		return controlDeviceId;
	}

	public void setControlDeviceId(Integer controlDeviceId) {
		this.controlDeviceId = controlDeviceId;
	}
	
	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getControlDeviceBit() {
		return controlDeviceBit;
	}

	public void setControlDeviceBit(Integer controlDeviceBit) {
		this.controlDeviceBit = controlDeviceBit;
	}

	public String getControlType() {
		return controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public Timestamp getTerminalCreateTime() {
		return terminalCreateTime;
	}

	public void setTerminalCreateTime(Timestamp terminalCreateTime) {
		this.terminalCreateTime = terminalCreateTime;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	public Integer getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(Integer terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
