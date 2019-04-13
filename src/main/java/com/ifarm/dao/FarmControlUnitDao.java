package com.ifarm.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.FarmControlUnit;

@SuppressWarnings("rawtypes")
@Repository
public class FarmControlUnitDao extends BaseDao<FarmControlUnit> {

	/**
	 * 控制单元的区域和位置
	 * 
	 * @param farmId
	 * @return
	 */
	public List controlUnitRegion(String farmId) {
		Session session = getSession();
		String sql = "SELECT unit.unitDistrict,unit.unitPosition FROM farm_control_unit unit WHERE unit.farmId = ? ORDER BY unit.unitDistrict";
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setParameter(0, farmId);
		return sqlQuery.list();
	}

	/**
	 * 区域位置的控制单元列表
	 * 
	 * @param farmId
	 * @param unitDistrict
	 * @param unitLocation
	 * @return
	 */
	public List regionControlUnit(String farmId, String unitDistrict,
			String unitLocation, String systemCode) {
		String sql = "SELECT unit.unitDistrict,unit.unitPosition,unit.unitId,unit.systemCode,unit.unitNo,unit.unitExtInfo FROM farm_control_unit unit "
				+ "WHERE 1 = 1";
		return getDynamicList(new Object[] { farmId, unitDistrict,
				unitLocation, systemCode }, sql, new Object[] { "unit.farmId",
				"unit.unitDistrict", "unit.unitPosition","unit.systemCode" });
	}
}
