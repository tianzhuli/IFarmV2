package com.ifarm.nosql.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifarm.nosql.bean.ControlSystemState;
import com.ifarm.nosql.dao.ControlSystemStateDao;

@Service
public class ControlSystemStateService {
	@Autowired
	private ControlSystemStateDao controlSystemStateDao;

	public void saveControlSystem(ControlSystemState controlSystemState) {
		controlSystemStateDao.saveControlSystem(controlSystemState);
	}
}
