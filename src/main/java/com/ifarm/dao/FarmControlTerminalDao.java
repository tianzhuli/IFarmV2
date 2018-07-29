package com.ifarm.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ifarm.bean.FarmControlTerminal;

@Repository
@SuppressWarnings("rawtypes")
public class FarmControlTerminalDao extends BaseDao<FarmControlTerminal> {
	
	public List getFarmControlOperationList(FarmControlTerminal farmControlTerminal) {
		Session session = getSession();
		String sql = "SELECT t.functionName,t.functionCode FROM farm_control_terminal t WHERE t.systemId=?";
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setParameter(0, farmControlTerminal.getSystemId());
		return sqlQuery.list();
	}
	
	public Integer saveFarmControlTerminal(FarmControlTerminal farmControlTerminal) {
		Session session = getSession();
		session.save(farmControlTerminal);
		return farmControlTerminal.getTerminalId();
	}
}
