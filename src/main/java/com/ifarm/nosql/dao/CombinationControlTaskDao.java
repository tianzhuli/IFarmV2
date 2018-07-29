package com.ifarm.nosql.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

import com.ifarm.nosql.bean.CombinationControlTask;
import com.ifarm.util.JsonObjectUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Repository
public class CombinationControlTaskDao extends MongoDbTemplateDao {
	private static final int limitNum = 5;
	
	public void saveCombinationControlTask(CombinationControlTask controlTask) {
		mongoTemplate.save(controlTask);
	}

	public String queryCombinationControlTask(String userId) throws Exception {
		DBObject dbObject = new BasicDBObject();
		dbObject.put("userId", userId);
		BasicQuery query = new BasicQuery(dbObject);
		query.with(new Sort(Direction.DESC, "taskTime"));
		query.limit(limitNum);
		List<CombinationControlTask> list = mongoTemplate.find(query, CombinationControlTask.class);
		return JsonObjectUtil.toJsonArrayString(list);
	}
}
