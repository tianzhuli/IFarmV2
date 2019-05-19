package com.ifarm.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.FarmCollectorUnit;

@SuppressWarnings("rawtypes")
@Repository
public class FarmCollectorUnitDao extends BaseDao<FarmCollectorUnit> {

	/**
	 * 采集单元的区域和位置
	 * 
	 * @param farmId
	 * @return
	 */
	public List collectorUnitRegion(String farmId) {
		Session session = getSession();
		String sql = "SELECT unit.unitDistrict,unit.unitPosition FROM farm_collector_unit unit WHERE unit.farmId = ? ORDER BY unit.unitDistrict";
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setParameter(0, farmId);
		return sqlQuery.list();
	}

	/**
	 * 区域位置的采集单元列表
	 * 
	 * @param farmId
	 * @param unitDistrict
	 * @param unitLocation
	 * @return
	 */
	public List regionCollectorUnit(String farmId, String unitDistrict,
			String unitLocation) {
		String sql = "SELECT unit.unitDistrict,unit.unitPosition,unit.unitId,unit.unitNo,unit.unitExtInfo FROM farm_collector_unit unit "
				+ "WHERE 1 = 1";
		return getDynamicList(new Object[] { farmId, unitDistrict,
				unitLocation }, sql, new Object[] { "unit.farmId",
				"unit.unitDistrict", "unit.unitPosition" });
	}
}
