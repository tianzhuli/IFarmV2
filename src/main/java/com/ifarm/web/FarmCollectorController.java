package com.ifarm.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ifarm.bean.FarmCollector;
import com.ifarm.service.FarmCollectorService;
import com.ifarm.service.UserLogService;

@Controller
@RequestMapping(value = "farmCollector")
public class FarmCollectorController {

	@Autowired
	private FarmCollectorService farmCollectorService;

	@Autowired
	private UserLogService userLogService;

	@RequestMapping(value = "addFarmCollector")
	public @ResponseBody
	String addFarmColletor(@RequestParam("userId") String userId, HttpServletRequest request, FarmCollector farmCollector) {
		String message = farmCollectorService.saveFarmCollector(farmCollector);
		userLogService.saveUserLog(request, farmCollector, userId, "add", "farmCollector", message);
		return message;
	}

	@RequestMapping(value = "updateFarmCollector")
	public @ResponseBody
	String updateFarmColletor(@RequestParam("userId") String userId, HttpServletRequest request, FarmCollector farmCollector) {
		String message = farmCollectorService.updateFarmCollector(farmCollector);
		userLogService.saveUserLog(request, farmCollector, userId, "update", "farmCollector", message);
		return message;
	}

	@RequestMapping(value = "getFarmColletorsList")
	@CrossOrigin
	public @ResponseBody
	String getFarmColletorsList(HttpServletRequest request, FarmCollector farmCollector) {
		return farmCollectorService.getFarmCollectorsList(farmCollector);
	}

}
