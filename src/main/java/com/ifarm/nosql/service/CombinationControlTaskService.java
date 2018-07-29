package com.ifarm.nosql.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.ifarm.nosql.bean.CombinationControlTask;
import com.ifarm.nosql.dao.CombinationControlTaskDao;

@Service
public class CombinationControlTaskService {
	@Autowired
	private CombinationControlTaskDao combinationControlTaskDao;
	
	public void saveCombinationControlTask(JSONArray jsonArray,String userId) {
		CombinationControlTask cTask = new CombinationControlTask(userId, jsonArray.toString());
		combinationControlTaskDao.saveCombinationControlTask(cTask);
	}
	
	public String queryCombinationControlTask(String userId) {
		try {
			return combinationControlTaskDao.queryCombinationControlTask(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new JSONArray().toString();
		}
	}
}
