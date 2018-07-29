package com.ifarm.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.FarmCollector;

@Repository
public class FarmCollectorDao extends BaseDao<FarmCollector> {
	
	public void saveFarmCollector(FarmCollector farmCollector) {
		Session session = getSession();
		session.save(farmCollector);
	}
}
