package com.ifarm.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ifarm.annotation.TransientField;
import com.ifarm.enums.DeviceValueType;

/**
 * 老版本的deviceValue的坑，目前暂时不能删除，反序列化找不到
 * 
 * @author lab
 * 
 */
@Entity
@Table(name = "collector_device_value")
public class CollectorDeviceValue extends DeviceValueBase {
	/**
	 * 
	 */
	@Transient
	@TransientField
	private static final long serialVersionUID = -3614143073913290853L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@TransientField
	private Integer deviceValueId;
	private Long deviceId;
	private Integer illumination;
	private Double airTemperature;
	private Double airHumidity;
	private Double soilTemperature;
	private Double soilHumidity;
	private Timestamp updateTime;
	@Transient
	transient private byte[] rawPacket;

	/**
	 * @param size
	 *            数据包大小，目前暂无用处
	 * @param arr
	 *            数据包
	 */
	public void setCollectData(byte[] arr, int size) {
		this.illumination = convertData.getdataType3(arr, 0);
		this.airTemperature = convertData.getdataType3(arr, 2) * 1.00 / 100;
		this.airHumidity = convertData.getdataType3(arr, 4) * 1.00 / 100;
		this.airHumidity = this.airHumidity >= 100 ? 99.99 : this.airHumidity;
		this.soilTemperature = convertData.getdataType3(arr, 6) * 1.00 / 100;
		this.soilHumidity = convertData.getdataType3(arr, 8) * 1.00 / 100;
		this.soilHumidity = this.soilHumidity >= 100 ? 99.99 : this.soilHumidity;
		rawPacket = arr;
	}

	public Integer getDeviceValueId() {
		return deviceValueId;
	}

	public void setDeviceValueId(Integer deviceValueId) {
		this.deviceValueId = deviceValueId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getIllumination() {
		return illumination;
	}

	public void setIllumination(Integer illumination) {
		this.illumination = illumination;
	}

	public Double getAirTemperature() {
		return airTemperature;
	}

	public void setAirTemperature(Double airTemperature) {
		this.airTemperature = airTemperature;
	}

	public Double getAirHumidity() {
		return airHumidity;
	}

	public void setAirHumidity(Double airHumidity) {
		this.airHumidity = airHumidity;
	}

	public Double getSoilTemperature() {
		return soilTemperature;
	}

	public void setSoilTemperature(Double soilTemperature) {
		this.soilTemperature = soilTemperature;
	}

	public Double getSoilHumidity() {
		return soilHumidity;
	}

	public void setSoilHumidity(Double soilHumidity) {
		this.soilHumidity = soilHumidity;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Double getDynamicParamValue(String paramType) throws Exception {
		if (paramType.equals("Illumination")) {
			double value = getIllumination().intValue();
			return value;
		}
		Method method = CollectorDeviceValue.class.getDeclaredMethod("get" + paramType);
		return (Double) method.invoke(this);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder();
		Field[] fields = getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			if (fields[i].getAnnotation(Transient.class) != null) {
				continue;
			}
			stringBuilder.append(fields[i].getName() + ":");
			try {
				stringBuilder.append(fields[i].get(this) + ";");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return stringBuilder.toString();
	}

	@Override
	public DeviceValueType getDeviceValueType() {
		// TODO Auto-generated method stub
		return DeviceValueType.COLLECOT_TYPE_FIVE;
	}

}
