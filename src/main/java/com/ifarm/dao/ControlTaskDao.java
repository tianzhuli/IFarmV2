package com.ifarm.dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.ControlTask;

@Repository
public class ControlTaskDao extends BaseDao<ControlTask> {
	public void saveControlTask(ControlTask controlTask) {
		Session session = getSession();
		session.save(controlTask);
	}

	public void updateControlTask(ControlTask controlTask) {
		Session session = getSession();
		session.update(controlTask);
	}

	public void deleteControlTask(ControlTask controlTask) {
		Session session = getSession();
		session.delete(controlTask);
	}
	
	public void deleteControlTask(Integer controlTaskId){
		Session session = getSession();
		String sql = "delete from farm_controller_log where controllerLogId=?";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter(0, controlTaskId);
		query.executeUpdate();
	}
}
