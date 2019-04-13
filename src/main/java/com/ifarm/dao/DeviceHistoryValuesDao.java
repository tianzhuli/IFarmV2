package com.ifarm.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.DeviceValueBase;
import com.ifarm.bean.FarmCollectorDevice;

@Repository
public class DeviceHistoryValuesDao extends BaseDao<DeviceValueBase> {

	public void saveSensorValues(DeviceValueBase collectorDeviceValue) {
		Session session = getSession();
		session.save(collectorDeviceValue);
	}

	@SuppressWarnings("unchecked")
	public List<Object> getHistorySensorValues(FarmCollectorDevice fCollectorDevice, Timestamp beginTime, Timestamp endTime) {
		Session session = getSession();
		StringBuffer hql = new StringBuffer(
				"SELECT farm.farmId,farm.farmName,farm_collector_device.deviceId,farm_collector_device.deviceOrderNo,farm_collector_device.deviceDistrict,farm_collector_device.deviceType,farm_collector_device.deviceLocation," +
				"collector_device_value.illumination,collector_device_value.airTemperature,collector_device_value.airHumidity,collector_device_value.soilTemperature,collector_device_value.soilHumidity,collector_device_value.updateTime " +
				"FROM collector_device_value INNER JOIN farm_collector_device ON collector_device_value.deviceId = farm_collector_device.deviceId INNER JOIN farm ON farm_collector_device.farmId = farm.farmId WHERE 1=1");
		if (fCollectorDevice.getFarmId() != null) {
			hql.append(" AND farm_collector_device.farmId=\'" + fCollectorDevice.getFarmId() + "\'");
		}
		if (fCollectorDevice.getCollectorId() != null) {
			hql.append(" AND farm_collector_device.collectorId=\'" + fCollectorDevice.getCollectorId() + "\'");
		}
		if (fCollectorDevice.getDeviceId() != null) {
			hql.append(" AND farm_collector_device.deviceId=\'" + fCollectorDevice.getDeviceId() + "\'");
		}
		hql.append(" AND collector_device_value.updateTime > ? AND collector_device_value.updateTime < ? ORDER BY farm_collector_device.farmId ASC,farm_collector_device.collectorId ASC,farm_collector_device.deviceId ASC,collector_device_value.updateTime DESC");
		SQLQuery sqlQuery = session.createSQLQuery(hql.toString());
		sqlQuery.setTimestamp(0, beginTime);
		sqlQuery.setTimestamp(1, endTime);
		List<Object> list = sqlQuery.list();
		return list;
	}
}
