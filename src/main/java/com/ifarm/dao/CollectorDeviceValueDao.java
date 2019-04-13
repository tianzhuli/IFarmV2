package com.ifarm.dao;

import org.springframework.stereotype.Repository;

import com.ifarm.bean.DeviceValueBase;

@Repository
public class CollectorDeviceValueDao extends BaseDao<DeviceValueBase> {
	/**
	 * 扩展后续的多样化添加
	 * 
	 * @param sensorValue
	 */
	public void saveSensorValue(DeviceValueBase deviceValueBase) {
		/*
		 * Session session = getSession(); String queryString =
		 * "INSERT INTO device_value (sensorId,sensorValues,updateTime) values(?,?,?)"
		 * ; SQLQuery sqlQuery = session.createSQLQuery(queryString);
		 * sqlQuery.addEntity(CollectorDeviceValue.class);
		 * sqlQuery.executeUpdate();
		 */
	}
}
