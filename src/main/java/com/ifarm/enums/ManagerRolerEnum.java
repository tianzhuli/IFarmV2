package com.ifarm.enums;

public enum ManagerRolerEnum {
	PRODUCTION("production","生产者");
	
	private String code;
	private String ChineseName;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getChineseName() {
		return ChineseName;
	}

	public void setChineseName(String chineseName) {
		ChineseName = chineseName;
	}
	
	private ManagerRolerEnum(String code, String chineseName) {
		this.code = code;
		this.ChineseName = chineseName;
	}
	
	public static ManagerRolerEnum getValueTypeByCode(String code) {
		for (ManagerRolerEnum managerRolerEnum: ManagerRolerEnum.values()) {
			if (managerRolerEnum.getCode().equals(code)) {
				return managerRolerEnum;
			}
		}
		return null;
	}

}
