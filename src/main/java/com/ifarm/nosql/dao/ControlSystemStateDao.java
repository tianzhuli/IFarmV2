package com.ifarm.nosql.dao;

import org.springframework.stereotype.Repository;

import com.ifarm.nosql.bean.ControlSystemState;

@Repository
public class ControlSystemStateDao extends MongoDbTemplateDao {
	public void saveControlSystem(ControlSystemState controlSystemState) {
		mongoTemplate.save(controlSystemState);
	}
}
