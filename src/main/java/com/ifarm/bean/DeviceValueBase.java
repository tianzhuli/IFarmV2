package com.ifarm.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Transient;

import com.ifarm.util.ConvertData;

/**
 * 所以设备参数的基类，不论是五合一还是以后的七合一都可以继承
 * @author lab
 *
 */
public abstract class DeviceValueBase implements Serializable{
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 2837488291L;
	
	transient protected ConvertData convertData = new ConvertData();

	/**
	 * 
	 * @param arr 数据包
	 * @param size 数据包长度
	 */
	public abstract void setCollectData(byte[] arr, int size);

	public abstract Double getDynamicParamValue(String paramType) throws Exception;

	public abstract Timestamp getUpdateTime();

	public abstract void setUpdateTime(Timestamp valueOf);

	public abstract void setDeviceId(Long id);

	public abstract Long getDeviceId();
}
