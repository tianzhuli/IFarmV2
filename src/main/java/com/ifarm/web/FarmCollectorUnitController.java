package com.ifarm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifarm.bean.FarmCollectorUnit;
import com.ifarm.service.FarmCollectorUnitService;

@RestController
@RequestMapping("farmCollectorUnit")
public class FarmCollectorUnitController {

	@Autowired
	private FarmCollectorUnitService farmCollectorUnitService;

	@RequestMapping("addition")
	public String controlUnitAddition(FarmCollectorUnit farmCollectorUnit) {
		return farmCollectorUnitService.saveCollectorUnit(farmCollectorUnit);
	}

	@RequestMapping("query")
	public String collectorUnitQuery(FarmCollectorUnit farmCollectorUnit) {
		return farmCollectorUnitService
				.queryDynamicListAddLike(farmCollectorUnit);
	}

	@RequestMapping("delete")
	public String controlUnitDelete(FarmCollectorUnit farmCollectorUnit) {
		return farmCollectorUnitService.baseDelete(farmCollectorUnit);
	}

	@RequestMapping("regions")
	@CrossOrigin
	public String controlUnitRegion(String farmId) {
		return farmCollectorUnitService.controlUnitRegion(farmId);
	}

	@RequestMapping("region/unitList")
	@CrossOrigin
	public String regionControlUnit(String farmId, String unitDistrict,
			String unitLocation) {
		return farmCollectorUnitService.regionControlUnit(farmId, unitDistrict,
				unitLocation);
	}
}
