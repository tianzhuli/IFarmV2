package com.ifarm.nosql.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

import com.ifarm.nosql.bean.UserLogMongo;
import com.ifarm.util.JsonObjectUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Repository
public class UserLogMongoDao extends MongoDbTemplateDao {
	public void saveUserLog(UserLogMongo userLog) {
		mongoTemplate.save(userLog);
	}

	public String getUserLog(String userId) {
		DBObject dbObject = new BasicDBObject();
		dbObject.put("userId", userId);
		BasicQuery query = new BasicQuery(dbObject);
		List<UserLogMongo> list = mongoTemplate.find(query, UserLogMongo.class);
		return JsonObjectUtil.toJsonArrayString(list);
	}

	public String getUserLogByTime(String beginTime, String endTime) {
		List<UserLogMongo> list = mongoTemplate.find(query(where("logCreateTime").gte(Timestamp.valueOf(beginTime)).lte(Timestamp.valueOf(endTime))),
				UserLogMongo.class);
		return JsonObjectUtil.toJsonArrayString(list);
	}
}
