package com.ifarm.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.FarmWFMControlSystem;

@Repository
public class FarmControlSystemWFMDao extends BaseDao<FarmWFMControlSystem>{
	
	public Integer saveFarmControlSystem(FarmWFMControlSystem fWfmControlSystem) {
		Session session = getSession();
		session.save(fWfmControlSystem);
		return fWfmControlSystem.getSystemId();
	}
}
