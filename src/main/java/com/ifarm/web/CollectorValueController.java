package com.ifarm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ifarm.bean.FarmCollector;
import com.ifarm.service.CollectorValueService;

@Controller
@RequestMapping(value = "collectorValues")
public class CollectorValueController {
	@Autowired
	private CollectorValueService collectorValueService;

	@RequestMapping(value = "getCollectorValues")
	public @ResponseBody
	String getCollectorValues(FarmCollector farmCollector) {
		return collectorValueService.getCollectorValues(farmCollector);
	}
}
