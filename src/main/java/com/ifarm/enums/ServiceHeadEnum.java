package com.ifarm.enums;

public enum ServiceHeadEnum {
	REGION_CONTROL_SYSTEM(
			"region_control_system",
			"区域控制系统",
			"系统区域,系统位置,系统编号,系统编码,系统类型,系统类型编码,系统描述,系统号",
			"systemDistrict,systemPosition,systemId,systemCode,systemType,systemTypeCode,systemDescription,systemNo"), 
	REGION_CONTROL_UNIT(
			"region_control_unit",
			"区域控制系统",
			"区域,位置,编号,系统编码,控制单元号,扩展信息,系统类型,控制类型",
			"unitDistrict,unitPosition,unitId,systemCode,unitNo,unitExtInfo,systemType,controlType");
	private String code;
	private String ChineseName;
	private String param;
	private String paramCode;

	private ServiceHeadEnum(String code, String ChineseName, String param,
			String paramCode) {
		this.code = code;
		this.ChineseName = ChineseName;
		this.param = param;
		this.paramCode = paramCode;
	}

	public static ServiceHeadEnum getValueTypeByCode(String code) {
		for (ServiceHeadEnum serviceHeadEnum : ServiceHeadEnum.values()) {
			if (serviceHeadEnum.getCode().equals(code)) {
				return serviceHeadEnum;
			}
		}
		return null;
	}

	public String getChineseName() {
		return ChineseName;
	}

	public void setChineseName(String chineseName) {
		ChineseName = chineseName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getParamCode() {
		return paramCode;
	}

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}
}
