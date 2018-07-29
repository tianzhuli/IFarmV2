package com.ifarm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifarm.bean.Camera;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.dao.CameraDao;

@Service
public class CameraService {
	@Autowired
	private CameraDao cameraDao;

	public List<Camera> getCameraList(Camera camera) {
		List<Camera> list = null;
		try {
			list = cameraDao.getDynamicList(camera);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	public String saveCamera(Camera camera) {
		try {
			cameraDao.saveBase(camera);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return SystemResultCodeEnum.ERROR;
		}
		return SystemResultCodeEnum.SUCCESS;
	}
}
