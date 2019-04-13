package com.ifarm.service.command;

import com.ifarm.bean.ControlCommand;

public abstract class CommandAssemble {
	
	public byte[] assemble(ControlCommand command) {
		return customAssemble(command);
	}
	
	public abstract byte[] customAssemble(ControlCommand command);
}
