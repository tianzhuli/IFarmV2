package com.ifarm.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.ProductionDevice;

@Repository
public class ProductionDeviceDao extends BaseDao<ProductionDevice> {

	public void saveProductionDevice(ProductionDevice productionDevice) {
		Session session = getSession();
		session.save(productionDevice);
	}

	public ProductionDevice queryProductionDevice(Integer deviceId) {
		Session session = getSession();
		ProductionDevice productionDevice = (ProductionDevice) session.get(ProductionDevice.class, deviceId);
		if (productionDevice != null) {
			session.evict(productionDevice);
		}
		return productionDevice;
	}

	public boolean existProductionDevice(Integer deviceId) {
		Session session = getSession();
		String hql = "select count(*) from ProductionDevice as p where p.deviceId=?";
		Query query = session.createQuery(hql);
		query.setInteger(0, deviceId);
		return (long) query.list().get(0) > 0;
	}

	public boolean existProductionDevice(Integer deviceId, String deviceVerification) {
		Session session = getSession();
		String hql = "select count(*) from ProductionDevice as p where p.deviceId=? and p.deviceVerification=?";
		Query query = session.createQuery(hql);
		query.setInteger(0, deviceId);
		query.setString(1, deviceVerification);
		return (long) query.list().get(0) > 0;
	}
}
