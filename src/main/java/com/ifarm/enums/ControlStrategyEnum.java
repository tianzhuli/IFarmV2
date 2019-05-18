package com.ifarm.enums;

public enum ControlStrategyEnum {
	
	PRE_BOOT_STRATEGY("PRE_BOOT_STRATEGY", "预启动策略", "PRE_BOOT_STRATEGY", "预启动策略"),
	
	SINGLE_FUNCTION_STRATEGY("SINGLE_FUNCTION_STRATEGY", "单功能策略", "SINGLE_FUNCTION_STRATEGY", "单功能策略"),
	
	MULTI_FUNCTION_STRATEGY("MULTI_FUNCTION_STRATEGY", "多功能策略", "MULTI_FUNCTION_STRATEGY", "多功能策略")
	;
	private String code;
	private String chineseName;
	private String engishName;
	private String description;

	private ControlStrategyEnum(String code, String chineseName,
			String engishName, String description) {
		this.code = code;
		this.chineseName = chineseName;
		this.engishName = engishName;
		this.description = description;
	}
	
	public static ControlStrategyEnum getValueByCode(String code) {
		for (ControlStrategyEnum controlStrategy: ControlStrategyEnum.values()) {
			if (controlStrategy.getCode().equals(code)) {
				return controlStrategy;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEngishName() {
		return engishName;
	}

	public void setEngishName(String engishName) {
		this.engishName = engishName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
