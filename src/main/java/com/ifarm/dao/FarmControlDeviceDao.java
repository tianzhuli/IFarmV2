package com.ifarm.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.FarmControlDevice;

@Repository
public class FarmControlDeviceDao extends BaseDao<FarmControlDevice>{
	
	public void saveFarmCollectorDevice(FarmControlDevice farmControlDevice) {
		Session session = getSession();
		session.save(farmControlDevice);
	}
}
