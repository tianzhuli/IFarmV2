package com.ifarm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.FarmControlSystem;
import com.ifarm.bean.FarmControlTerminal;
import com.ifarm.bean.FarmWFMControlSystem;
import com.ifarm.redis.util.FarmControlSystemTypeUtil;
import com.ifarm.service.FarmControlSystemService;
import com.ifarm.service.FarmControlSystemWFMService;
import com.ifarm.service.FarmControlTerminalService;
import com.ifarm.util.CacheDataBase;

@RestController
@RequestMapping("farmControlSystem")
public class FarmControlSystemController {
	@Autowired
	private FarmControlSystemWFMService farmControlSystemWFMService;

	@Autowired
	private FarmControlSystemService farmControlSystemService;

	@Autowired
	private FarmControlSystemTypeUtil farmControlSystemTypeUtil;
	
	@Autowired
	private FarmControlTerminalService farmControlTerminalService;

	@RequestMapping("type")
	public String farmControlSystemType() {
		return CacheDataBase.initBaseConfig.get("controlSystemType.json").getJSONArray("controlSystemType").toJSONString();
	}

	@RequestMapping("terminalType")
	public String farmControlSystemTerminalType(String controlType) {
		JSONObject jsonObject = CacheDataBase.initBaseConfig.get("terminalType.json").getJSONObject("terminalType").getJSONObject(controlType);
		return jsonObject.toJSONString();
	}

	@RequestMapping("wfm/terminalType")
	public String wfmFarmControlSystemTerminalType(FarmWFMControlSystem fControlSystem) {
		return farmControlSystemWFMService.farmControlSystemWFMTerimal(fControlSystem).toString();
	}

	@RequestMapping("addition")
	public String controlSystemAddition(FarmControlSystem farmControlSystem) {
		return farmControlSystemService.saveControlSystem(farmControlSystem);
	}
	
	@RequestMapping("deleteControlSystem")
	public String deleteControlSystem(FarmControlSystem farmControlSystem) {
		return farmControlSystemService.deleteFarmControlSystem(farmControlSystem);
	}
	
	@RequestMapping("query")
	public String queryControlSystem(FarmControlSystem farmControlSystem) {
		return farmControlSystemService.queryFarmControlSystem(farmControlSystem);
	}

	@RequestMapping("wfm/addition")
	public String wfmControlSystemAddition(FarmWFMControlSystem farmWFMControlSystem) {
		return farmControlSystemWFMService.saveControlSystem(farmWFMControlSystem);
	}
	
	@RequestMapping("wfm/delete")
	public String wfmControlSystemDelete(FarmWFMControlSystem farmWFMControlSystem) {
		return farmControlSystemWFMService.deleteFarmControlSystem(farmWFMControlSystem);
	}
	
	@RequestMapping("wfm/query")
	public String wfmControlSystemQuery(FarmWFMControlSystem farmWFMControlSystem) {
		return farmControlSystemWFMService.queryFarmControlSystem(farmWFMControlSystem);
	}
	
	@RequestMapping("terminal/addition")
	public String terminalAddition(FarmControlTerminal farmControlTerminal) {
		return farmControlTerminalService.saveFarmControlSystem(farmControlTerminal);
	}
	
	@RequestMapping("terminal/query")
	public String controlTerminals(FarmControlTerminal farmControlTerminal) {
		return farmControlTerminalService.getFarmControlTerminals(farmControlTerminal);
	}
	
	@RequestMapping("terminal/delete")
	public String deleteControlTerminals(FarmControlTerminal farmControlTerminal) {
		return farmControlTerminalService.deleteFarmControlTerminal(farmControlTerminal);
	}
}
