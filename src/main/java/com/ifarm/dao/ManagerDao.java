package com.ifarm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.Manager;

@Repository
@SuppressWarnings("unchecked")
public class ManagerDao extends BaseDao<Manager> {

	public List<Manager> getAllManager() {
		String hsql = "from Manager";
		Session session = getSession();
		Query query = session.createQuery(hsql);
		return query.list();
	}

	public boolean updateManager(Manager manager) {
		try {
			Session session = getSession();
			session.update(manager);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public List<Manager> getManagerDynamicList(Manager manager) {
		Session session = getSession();
		DetachedCriteria criteria = DetachedCriteria.forClass(Manager.class);
		if (manager.getManagerId() != null) {
			criteria.add(Restrictions.eq("managerId", manager.getManagerId()));
		}
		if (manager.getManagerName() != null) {
			criteria.add(Restrictions.eq("managerName", manager.getManagerName()));
		}
		if (manager.getManagerPwd() != null) {
			criteria.add(Restrictions.eq("managerPwd", manager.getManagerPwd()));
		}
		Criteria crit = criteria.getExecutableCriteria(session);
		List<Manager> list = crit.list();
		return list;
	}

	public Manager getManagerById(String managerId) {
		Session session = getSession();
		Manager manager = (Manager) session.get(Manager.class, managerId);
		session.evict(manager);
		return manager;
	}

	public boolean login(String managerId, String managerPwd) {
		Session session = getSession();
		String hql = "select m.managerId from manager as m where m.managerId=? and managerPwd=?";
		Query query = session.createSQLQuery(hql);
		query.setString(0, managerId);
		query.setString(1, managerPwd);
		return query.list().size() > 0;
	}
}
