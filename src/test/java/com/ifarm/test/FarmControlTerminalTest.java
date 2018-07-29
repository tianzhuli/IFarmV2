package com.ifarm.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ifarm.bean.FarmControlDevice;
import com.ifarm.bean.FarmControlTerminal;
import com.ifarm.util.ConDb;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class FarmControlTerminalTest {
	public static void main(String[] args) throws SQLException {
		ConDb conDb = new ConDb();
		Connection connection = conDb.openCon();
		int deviceId = 10000001;
		for (int i = 1; i <= 1; i++) {
			FarmControlDevice farmControlDevice = new FarmControlDevice();
			farmControlDevice.setControlDeviceId(deviceId);
			farmControlDevice.setDeviceType("水泵控制");
			insertControlDevice(connection, farmControlDevice);
			FarmControlTerminal farmControlTerminal = new FarmControlTerminal();
			farmControlTerminal.setControlDeviceId(deviceId);
			farmControlTerminal.setControlDeviceBit(0);
			farmControlTerminal.setFunctionName("正转");
			farmControlTerminal.setFunctionCode("forward");
			farmControlTerminal.setTerminalIdentifying("pump");
			insertControlTerminal(connection, farmControlTerminal);
			FarmControlTerminal farmControlTerminal1 = new FarmControlTerminal();
			farmControlTerminal1.setControlDeviceId(deviceId);
			farmControlTerminal1.setControlDeviceBit(1);
			farmControlTerminal1.setFunctionName("反转");
			farmControlTerminal1.setFunctionCode("reverse");
			farmControlTerminal1.setTerminalIdentifying("pump");
			insertControlTerminal(connection, farmControlTerminal1);
			deviceId++;
		}
		for (int i = 1; i <= 4; i++) {
			FarmControlDevice farmControlDevice = new FarmControlDevice();
			farmControlDevice.setControlDeviceId(deviceId);
			farmControlDevice.setDeviceType("肥料罐控制");
			insertControlDevice(connection, farmControlDevice);
			FarmControlTerminal farmControlTerminal = new FarmControlTerminal();
			farmControlTerminal.setControlDeviceId(deviceId);
			farmControlTerminal.setControlDeviceBit(0);
			farmControlTerminal.setFunctionName("正转");
			farmControlTerminal.setFunctionCode("forward");
			farmControlTerminal.setTerminalIdentifying("fertilizerCan" + i);
			insertControlTerminal(connection, farmControlTerminal);
			FarmControlTerminal farmControlTerminal1 = new FarmControlTerminal();
			farmControlTerminal1.setControlDeviceId(deviceId);
			farmControlTerminal1.setControlDeviceBit(1);
			farmControlTerminal1.setFunctionName("反转");
			farmControlTerminal1.setFunctionCode("reverse");
			farmControlTerminal1.setTerminalIdentifying("fertilizerCan" + i);
			insertControlTerminal(connection, farmControlTerminal1);
			deviceId++;
		}
		for (int i = 1; i <= 4; i++) {
			FarmControlDevice farmControlDevice = new FarmControlDevice();
			farmControlDevice.setControlDeviceId(deviceId);
			farmControlDevice.setDeviceType("药罐控制");
			insertControlDevice(connection, farmControlDevice);
			FarmControlTerminal farmControlTerminal = new FarmControlTerminal();
			farmControlTerminal.setControlDeviceId(deviceId);
			farmControlTerminal.setControlDeviceBit(0);
			farmControlTerminal.setFunctionName("正转");
			farmControlTerminal.setFunctionCode("forward");
			farmControlTerminal.setTerminalIdentifying("medicineCan" + i);
			insertControlTerminal(connection, farmControlTerminal);
			FarmControlTerminal farmControlTerminal1 = new FarmControlTerminal();
			farmControlTerminal1.setControlDeviceId(deviceId);
			farmControlTerminal1.setControlDeviceBit(1);
			farmControlTerminal1.setFunctionName("反转");
			farmControlTerminal1.setFunctionCode("reverse");
			farmControlTerminal1.setTerminalIdentifying("medicineCan" + i);
			insertControlTerminal(connection, farmControlTerminal1);
			deviceId++;
		}
		for (int i = 1; i <= 5; i++) {
			FarmControlDevice farmControlDevice = new FarmControlDevice();
			farmControlDevice.setControlDeviceId(deviceId);
			farmControlDevice.setDeviceType("喷灌区域控制");
			insertControlDevice(connection, farmControlDevice);
			FarmControlTerminal farmControlTerminal = new FarmControlTerminal();
			farmControlTerminal.setControlDeviceId(deviceId);
			farmControlTerminal.setControlDeviceBit(0);
			farmControlTerminal.setFunctionName("正转");
			farmControlTerminal.setFunctionCode("forward");
			farmControlTerminal.setTerminalIdentifying("districtNum" + i);
			insertControlTerminal(connection, farmControlTerminal);
			FarmControlTerminal farmControlTerminal1 = new FarmControlTerminal();
			farmControlTerminal1.setControlDeviceId(deviceId);
			farmControlTerminal1.setControlDeviceBit(1);
			farmControlTerminal1.setFunctionName("反转");
			farmControlTerminal1.setFunctionCode("reverse");
			farmControlTerminal1.setTerminalIdentifying("districtNum" + i);
			insertControlTerminal(connection, farmControlTerminal1);
			deviceId++;
		}
		connection.close();
	}

	public static void insertControlDevice(Connection con, FarmControlDevice farmControlDevice) throws SQLException {
		PreparedStatement preparedStatement = null;
		try {
			StringBuffer stringBuffer = new StringBuffer(
					"INSERT INTO farm_control_device(controlDeviceId,collectorId,deviceType) VALUES(?,70100010,?);");
			preparedStatement = con.prepareStatement(stringBuffer.toString());
			preparedStatement.setInt(1, farmControlDevice.getControlDeviceId());
			preparedStatement.setString(2, farmControlDevice.getDeviceType());
			preparedStatement.execute();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			preparedStatement.close();
		}
	}

	public static void insertControlTerminal(Connection con, FarmControlTerminal farmControlTerminal) throws SQLException {
		PreparedStatement preparedStatement = null;
		try {
			StringBuffer stringBuffer = new StringBuffer(
					"INSERT INTO farm_control_terminal(controlDeviceId,systemId,controlDeviceBit,controlType,functionName,functionCode,terminalIdentifying) VALUES(?,100001,?,'waterFertilizerMedicineControl',?,?,?);");
			preparedStatement = con.prepareStatement(stringBuffer.toString());
			preparedStatement.setInt(1, farmControlTerminal.getControlDeviceId());
			preparedStatement.setInt(2, farmControlTerminal.getControlDeviceBit());
			preparedStatement.setString(3, farmControlTerminal.getFunctionName());
			preparedStatement.setString(4, farmControlTerminal.getFunctionCode());
			preparedStatement.setString(5, farmControlTerminal.getTerminalIdentifying());
			preparedStatement.execute();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			preparedStatement.close();
		}
	}
}
