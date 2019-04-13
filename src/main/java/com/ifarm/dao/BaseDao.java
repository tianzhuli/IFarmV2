package com.ifarm.dao;

import java.lang.reflect.Field;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ifarm.annotation.LikeField;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class BaseDao<T> {
	private SessionFactory sessionFactory;

	private final static Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// 获取 Session，注意：没有使用 openSession() ,使用 getCurrentSession()才能被 Spring 管理
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public T getTById(String id, Class<T> tClass) {
		Session session = getSession();
		T t = (T) session.get(tClass, id);
		if (t != null) {
			session.evict(t);
		}
		return t;
	}
	
	public T getTById(Object id, Class<T> tClass) {
		if (id instanceof String) {
			return getTById((String)id, tClass);
		} else if (id instanceof Integer) {
			return getTById((Integer)id, tClass);
		} else if (id instanceof Long) {
			return getTById((Long)id, tClass);
		}
		return null;
	}

	public T getTById(Integer id, Class<T> tClass) {
		Session session = getSession();
		T t = (T) session.get(tClass, id);
		if (t != null) {
			session.evict(t);
		}
		return t;
	}

	public T getTById(Long id, Class<T> tClass) {
		Session session = getSession();
		T t = (T) session.get(tClass, id);
		if (t != null) {
			session.evict(t);
		}
		return t;
	}

	public List<T> getDynamicList(T t) {
		Session session = getSession();
		DetachedCriteria criteria = DetachedCriteria.forClass(t.getClass());
		Field[] fields = t.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			try {
				if (fields[i].get(t) != null) {
					criteria.add(Restrictions.eq(fields[i].getName(),
							fields[i].get(t)));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				LOGGER.error("criteria add error", e);
			}
		}
		List<T> list = null;
		try {
			Criteria crit = criteria.getExecutableCriteria(session);
			list = crit.list();
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("criteria list error", e);
		}
		return list;
	}

	public List<T> getDynamicListAddLike(T t) {
		Session session = getSession();
		DetachedCriteria criteria = DetachedCriteria.forClass(t.getClass());
		Field[] fields = t.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			try {
				if (fields[i].get(t) != null) {
					String fieldName = fields[i].getName();
					LikeField likeField = fields[i]
							.getAnnotation(LikeField.class);
					if (likeField != null) {
						criteria.add(Restrictions.like(fieldName, "%"
								+ fields[i].get(t) + "%"));
					} else {
						criteria.add(Restrictions.eq(fieldName,
								fields[i].get(t)));
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				LOGGER.error("criteria add error", e);
			}
		}
		List<T> list = null;
		try {
			Criteria crit = criteria.getExecutableCriteria(session);
			list = crit.list();
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("criteria list error", e);
		}
		return list;
	}

	public boolean updateDynamic(T t) {
		Session session = getSession();
		String tableName = t.getClass().getSimpleName();
		StringBuffer hqlBuffer = new StringBuffer("update " + tableName
				+ " t set ");
		Field[] fields = t.getClass().getDeclaredFields();
		try {
			fields[0].setAccessible(true);
			if (fields[0].get(t) == null) {
				return false;
			}
			for (int i = 1; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (fields[i].get(t) != null) {
					hqlBuffer.append("t." + fields[i].getName() + "=?" + ",");
				}
			}
			hqlBuffer.deleteCharAt(hqlBuffer.length() - 1);
			hqlBuffer.append(" where t." + fields[0].getName() + "=?");
			// System.out.println(hqlBuffer);
			Query query = session.createQuery(hqlBuffer.toString());
			int position = 0;
			for (int i = 1; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (fields[i].get(t) != null) {
					Object val = fields[i].get(t);
					query.setParameter(position, val);
					position++;
				}
			}
			query.setParameter(position, fields[0].get(t));
			LOGGER.info(tableName + " update query:" + query.getQueryString());
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(tableName + "动态更新异常", e);
			return false;
		}
	}

	public void saveBase(T t) {
		Session session = getSession();
		session.save(t);

	}

	/**
	 * 通过主键来删除
	 * 
	 * @param t
	 */
	public void deleteBase(T t) {
		Session session = getSession();
		session.delete(t);
	}

	protected List getDynamicList(Object[] objects, String sql,
			Object... valTitle) {
		Session session = getSession();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				sql += " AND " + valTitle[i] + "= ?";
			}
		}
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		int initNum = 0;
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				sqlQuery.setParameter(initNum++, objects[i]);
			}
		}
		return sqlQuery.list();
	}
}
