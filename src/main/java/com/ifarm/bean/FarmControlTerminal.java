package com.ifarm.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "farm_control_terminal")
public class FarmControlTerminal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer terminalId;
	private Integer controlDeviceId;
	private Integer systemId;
	private Integer controlDeviceBit;
	private String controlType;
	private String functionName;
	private String functionCode;
	private String terminalIdentifying; // 端口标识
	private Timestamp terminalCreateTime;

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

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
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

	public String getTerminalIdentifying() {
		return terminalIdentifying;
	}

	public void setTerminalIdentifying(String terminalIdentifying) {
		this.terminalIdentifying = terminalIdentifying;
	}

	public Timestamp getTerminalCreateTime() {
		return terminalCreateTime;
	}

	public void setTerminalCreateTime(Timestamp terminalCreateTime) {
		this.terminalCreateTime = terminalCreateTime;
	}

}
