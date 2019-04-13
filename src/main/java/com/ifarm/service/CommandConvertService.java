package com.ifarm.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifarm.bean.ControlCommand;
import com.ifarm.bean.ProductionDevice;
import com.ifarm.dao.ProductionDeviceDao;
import com.ifarm.exception.ValidatorException;
import com.ifarm.service.command.CommandAssemble;
import com.ifarm.service.command.OnOffDeviceCommandAssemble;
import com.ifarm.service.command.ValveDeviceCommandAssemble;

@Service
public class CommandConvertService {

	@Autowired
	private ProductionDeviceDao productionDeviceDao;

	@Autowired
	private OnOffDeviceCommandAssemble offDeviceCommandAssemble;

	@Autowired
	private ValveDeviceCommandAssemble valveDeviceCommandAssemble;

	private static Map<String, CommandAssemble> commandAssembleMap = new HashMap<>();

	@PostConstruct
	public void init() {
		commandAssembleMap.put("TDQ801", offDeviceCommandAssemble);
		commandAssembleMap.put("FKQ801", valveDeviceCommandAssemble);
	}

	public byte[] commandToByte(ControlCommand command) {
		ProductionDevice productionDevice = productionDeviceDao
				.queryProductionDevice(command.getControlDeviceId());
		CommandAssemble commandAssemble = commandAssembleMap
				.get(productionDevice.getDeviceCode());
		if (commandAssemble == null) {
			throw new ValidatorException("commandAssemble is null");
		}
		return commandAssemble.assemble(command);
	}
}
