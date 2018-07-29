package com.ifarm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifarm.bean.Camera;
import com.ifarm.service.CameraService;
import com.ifarm.util.JsonObjectUtil;

@RestController
@RequestMapping(value = "camera")
public class CameraController {
	@Autowired
	private CameraService cameraService;

	@RequestMapping(value = "addition")
	public String addCamera(Camera camera) {
		return cameraService.saveCamera(camera);
	}

	@RequestMapping(value = "list")
	public String cameraList(Camera camera) {
		return JsonObjectUtil.toJsonArrayString(cameraService.getCameraList(camera));
	}

}
