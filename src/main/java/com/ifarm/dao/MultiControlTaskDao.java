package com.ifarm.dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.MultiControlTask;

@Repository
public class MultiControlTaskDao extends BaseDao<MultiControlTask> {
	public void saveControlTask(MultiControlTask wfmControlTask) {
		Session session = getSession();
		session.save(wfmControlTask);
	}

	public void updateControlTask(MultiControlTask wfmControlTask) {
		Session session = getSession();
		session.update(wfmControlTask);
	}

	public void delteControlTask(MultiControlTask wfmControlTask) {
		Session session = getSession();
		session.delete(wfmControlTask);
	}
	
	public void deleteControlTask(Integer controlTaskId){
		Session session = getSession();
		String sql = "delete from farm_controller_wfm_task where controllerLogId=?";
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter(0, controlTaskId);
		query.executeUpdate();
	}
}
