package com.ifarm.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.ApkVersion;

@Repository
public class ApkVersionDao extends BaseDao<ApkVersion> {
	public ApkVersion queryRecentVersion() {
		Session session = getSession();
		String hql = "from ApkVersion as a order by a.createTime desc";
		Query query = session.createQuery(hql);
		return (ApkVersion) query.list().get(0);
	}
}
