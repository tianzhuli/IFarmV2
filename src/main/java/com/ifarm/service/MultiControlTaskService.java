package com.ifarm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifarm.bean.MultiControlTask;
import com.ifarm.dao.MultiControlTaskDao;

@Service
public class MultiControlTaskService extends AbstractFarmService<MultiControlTaskDao, MultiControlTask> {
	@Autowired
	private MultiControlTaskDao wfmControlTaskDao;

	public void saveControlTask(MultiControlTask wfmControlTask) {
		wfmControlTaskDao.saveBase(wfmControlTask);
	}

	public void updateControlTask(MultiControlTask wfmControlTask) {
		wfmControlTaskDao.updateControlTask(wfmControlTask);
	}

	public void deleteControlTask(MultiControlTask wfmControlTask) {
		wfmControlTaskDao.delteControlTask(wfmControlTask);
	}
	
	public void deleteControlTask(Integer controlTaskId) {
		wfmControlTaskDao.deleteControlTask(controlTaskId);
	}
}
