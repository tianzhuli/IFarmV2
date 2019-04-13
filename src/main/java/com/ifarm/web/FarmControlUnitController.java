package com.ifarm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifarm.bean.FarmControlUnit;
import com.ifarm.service.FarmControlUnitService;

@RestController
@RequestMapping("farmControlUnit")
public class FarmControlUnitController {

	@Autowired
	private FarmControlUnitService farmControlUnitService;

	@RequestMapping("addition")
	public String controlUnitAddition(FarmControlUnit farmControlUnit) {
		return farmControlUnitService.saveControlUnit(farmControlUnit);
	}

	@RequestMapping("query")
	public String controlUnitQuery(FarmControlUnit farmControlUnit) {
		return farmControlUnitService.queryDynamicListAddLike(farmControlUnit);
	}

	@RequestMapping("delete")
	public String controlUnitDelete(FarmControlUnit farmControlUnit) {
		return farmControlUnitService.baseDelete(farmControlUnit);
	}

	@RequestMapping("regions")
	@CrossOrigin
	public String controlUnitRegion(String farmId) {
		return farmControlUnitService.controlUnitRegion(farmId);
	}

	@RequestMapping("region/unitList")
	@CrossOrigin
	public String regionControlUnit(String farmId, String unitDistrict,
			String unitLocation, String systemCode) {
		return farmControlUnitService.regionControlUnit(farmId, unitDistrict,
				unitLocation, systemCode);
	}
}
