package com.ifarm.enums;

public enum SystemReturnCodeEnum {
	SUCCESS("100", "成功", "success", "成功"),
	UNKOWN_ERROR("101", "未知异常", "error", "未知异常"),
	PARAM_ERROR("102", "参数异常", "param_error", "参数异常"),
	UNIQUE_ERROR("103", "唯一性错误", "unique_error", "唯一性错误"),
	DATA_ERROR("104", "数据错误", "data_error", "数据错误");
	private String code;
	private String chineseName;
	private String engishName;
	private String description;

	private SystemReturnCodeEnum(String code, String chineseName,
			String engishName, String description) {
		this.code = code;
		this.chineseName = chineseName;
		this.engishName = engishName;
		this.description = description;
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
