package com.ifarm.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.CollectorDeviceThreshold;

@Repository
public class SensorParameterDao extends BaseDao<CollectorDeviceThreshold> {
	public void saveSensorParamter(CollectorDeviceThreshold sensorParameter) {
		Session session = getSession();
		session.save(sensorParameter);
	}
}
