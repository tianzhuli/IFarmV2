package com.ifarm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifarm.bean.ControlTask;
import com.ifarm.dao.ControlTaskDao;

@Service
public class ControlTaskService extends AbstractFarmService<ControlTaskDao, ControlTask> {
	@Autowired
	private ControlTaskDao controlTaskDao;

	public void saveControlTask(ControlTask controlTask) {
		controlTaskDao.saveBase(controlTask);
	}

	public void updateControlTask(ControlTask controlTask) {
		controlTaskDao.updateControlTask(controlTask);
	}

	public void deleteControlTask(ControlTask controlTask) {
		controlTaskDao.deleteControlTask(controlTask);
	}
	
	public void deleteControlTask(Integer controlTaskId) {
		controlTaskDao.deleteControlTask(controlTaskId);
	}
}
