package com.ifarm.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;

import javax.persistence.Transient;

import com.ifarm.enums.DeviceValueType;
import com.ifarm.util.ConvertData;
import com.ifarm.util.StringUtil;

/**
 * 所以设备参数的基类，不论是五合一还是以后的七合一都可以继承
 * 
 * @author lab
 * 
 */
public abstract class DeviceValueBase implements Serializable {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 2837488291L;

	transient protected ConvertData convertData = new ConvertData();

	/**
	 * 
	 * @param arr
	 *            数据包
	 * @param size
	 *            数据包长度
	 */
	public abstract void setCollectData(byte[] arr, int size);

	public abstract Timestamp getUpdateTime();

	public abstract void setUpdateTime(Timestamp valueOf);

	public abstract void setDeviceId(Long id);

	public abstract Long getDeviceId();

	/**
	 * 返回类型，五和一，七和一之类
	 * 
	 * @return
	 */
	public abstract DeviceValueType getDeviceValueTyle();

	public Double getDynamicParamValue(String paramType) throws Exception {
		paramType = StringUtil.firstCharUpper(paramType);
		Method method = getClass().getDeclaredMethod("get" + paramType);
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
}
