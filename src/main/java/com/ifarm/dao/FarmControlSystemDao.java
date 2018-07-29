package com.ifarm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.FarmControlSystem;

@Repository
@SuppressWarnings({ "rawtypes" })
public class FarmControlSystemDao extends BaseDao<FarmControlSystem> {

	public List farmControlRegion(Integer farmId) {
		Session session = getSession();
		String sql = "SELECT f.systemDistrict FROM farm_control_system f WHERE f.farmId=? GROUP BY systemDistrict";
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setParameter(0, farmId);
		return sqlQuery.list();
	}
	
	public Integer saveFarmControlSystem(FarmControlSystem farmControlSystem) {
		Session session = getSession();
		session.save(farmControlSystem);
		return farmControlSystem.getSystemId();
	}
	
	/**
	 * 某个用户，某种类型的控制系统（不包括水肥药）
	 * 
	 * @param userId
	 * @param systemCode
	 * @return
	 */
	public List farmControlSystemState(String userId, String systemCode) {
		Session session = getSession();
		String sql = "select c.systemId,c.farmId,f.farmName,c.systemCode,c.systemType,c.systemTypeCode,c.systemDistrict,c.systemNo,c.systemDescription,"
				+ "c.systemLocation,c.systemCreateTime from FarmControlSystem c,Farm f where c.farmId=f.farmId and f.userId=? and c.systemCode=?";
		Query query = session.createQuery(sql);
		query.setParameter(0, userId);
		query.setParameter(1, systemCode);
		return query.list();
	}

	public List farmWFMControlSystemState(String userId) {
		Session session = getSession();
		String sql = "select c.systemId,c.farmId,f.farmName,c.systemCode,c.systemType,c.systemTypeCode,c.systemDistrict,c.systemNo,c.systemDescription,"
				+ "c.systemLocation,c.systemCreateTime,c.medicineNum,c.districtNum,c.fertierNum from farm_control_system_wfm c,farm f where c.farmId=f.farmId and f.userId=?";
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, userId);
		return query.list();
	}

	/**
	 * 根据控制系统的ID获取到所有执行终端的信息
	 * 
	 * @param systemId
	 * @return
	 */
	public List farmControlSystemToTerminal(Integer systemId) {
		Session session = getSession();
		String sql = "SELECT t.terminalId,t.controlDeviceId,t.systemId,t.controlDeviceBit,t.controlType,t.functionCode,t.terminalIdentifying,t.terminalCreateTime FROM FarmControlTerminal t,FarmControlSystem s where t.systemId = s.systemId and s.systemId = ?";
		Query query = session.createQuery(sql);
		query.setParameter(0, systemId);
		return query.list();
	}

	/**
	 * 水肥药系统是单独处理，业务逻辑完全不一样
	 * 
	 * @param systemId
	 * @return
	 */
	public List farmWFMControlSystemToTerminal(Integer systemId, String terminalIdentifying) {
		Session session = getSession();
		String sql = "SELECT t.controlDeviceId,t.controlDeviceBit,t.functionCode,t.terminalIdentifying FROM FarmControlTerminal t where t.systemId = ? and t.terminalIdentifying = ?";
		Query query = session.createQuery(sql);
		query.setParameter(0, systemId);
		query.setParameter(1, terminalIdentifying);
		return query.list();
	}

	/**
	 * 后期为权限验证
	 * 
	 * @param userId
	 * @param farmId
	 * @param collectorId
	 * @return
	 */
	public boolean farmControlSystemVerification(String userId, Integer farmId, Integer systemId) {
		Session session = getSession();
		String sqlString = "SELECT COUNT(*) FROM farm f INNER JOIN farm_control_system c ON f.farmId=c.farmId WHERE f.farmId=? AND "
				+ "f.userId=? AND c.systemId=?";
		SQLQuery query = session.createSQLQuery(sqlString);
		query.setParameter(0, farmId);
		query.setParameter(1, userId);
		query.setParameter(2, systemId);
		Integer num = Integer.parseInt(query.list().get(0).toString());
		if (num > 0) {
			return true;
		}
		return false;
	}
}
