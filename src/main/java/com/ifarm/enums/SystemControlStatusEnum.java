package com.ifarm.enums;

public enum SystemControlStatusEnum {
	ENABLE("1","有效的","enable","enable");
	private String code;
	private String chineseName;
	private String englishName;
	private String description;

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

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private SystemControlStatusEnum(String code, String chineseName,
			String englishName, String description) {
		this.code = code;
		this.chineseName = chineseName;
		this.englishName = englishName;
		this.description = description;
	}
}
