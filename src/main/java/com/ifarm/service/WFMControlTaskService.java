package com.ifarm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifarm.bean.WFMControlTask;
import com.ifarm.dao.WFMControlTaskDao;

@Service
public class WFMControlTaskService {
	@Autowired
	private WFMControlTaskDao wfmControlTaskDao;

	public void saveControlTask(WFMControlTask wfmControlTask) {
		wfmControlTaskDao.saveBase(wfmControlTask);
	}

	public void updateControlTask(WFMControlTask wfmControlTask) {
		wfmControlTaskDao.updateControlTask(wfmControlTask);
	}

	public void deleteControlTask(WFMControlTask wfmControlTask) {
		wfmControlTaskDao.delteControlTask(wfmControlTask);
	}
	
	public void deleteControlTask(Integer controlTaskId) {
		wfmControlTaskDao.deleteControlTask(controlTaskId);
	}
}
