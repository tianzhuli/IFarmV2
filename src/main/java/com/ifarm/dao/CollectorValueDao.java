package com.ifarm.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.CollectorValue;

@Repository
public class CollectorValueDao extends BaseDao<CollectorValue> {
	public void saveCollectorValues(CollectorValue collectorValues) {
		Session session = getSession();
		session.save(collectorValues);
	}
}
