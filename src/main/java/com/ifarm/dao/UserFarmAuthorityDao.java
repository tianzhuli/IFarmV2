package com.ifarm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.UserFarmAuthority;

@Repository
@SuppressWarnings("unchecked")
public class UserFarmAuthorityDao extends BaseDao<UserFarmAuthority> {

	public List<UserFarmAuthority> queryFarmAuthorityBySubUserId(String userId) {
		Session session = getSession();
		Query query = session.createQuery("from UserFarmAuthority u where u.userId=?");
		query.setParameter(0, userId);
		return query.list();
	}

	public List<UserFarmAuthority> queryUserAuthorityByUserId(String userId) {
		Session session = getSession();
		Query query = session.createQuery("select from UserFarmAuthority u where u.userId like '" + userId+"%'");
		return query.list();
	}
	
	
}
