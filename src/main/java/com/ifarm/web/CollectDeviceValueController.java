package com.ifarm.web;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ifarm.bean.FarmCollectorDevice;
import com.ifarm.service.CollectorDeviceValueService;

@Controller
@RequestMapping(value = "collectorDeviceValue")
public class CollectDeviceValueController {
	@Autowired
	private CollectorDeviceValueService collectorDeviceValueService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CollectDeviceValueController.class);

	@RequestMapping("collectorDeviceCurrentValue")
	@CrossOrigin
	public @ResponseBody
	String collectorDeviceCurrentValue(FarmCollectorDevice fCollectorDevice) {
		String res = collectorDeviceValueService.getCollectorDeviceValues(fCollectorDevice).toString();
		LOGGER.info(res);
		return res;
	}

	@RequestMapping("collectorDeviceCacheVaules")
	@CrossOrigin
	public @ResponseBody
	String collectorDeviceCacheVaules(FarmCollectorDevice fCollectorDevice, String paramType) {
		return collectorDeviceValueService.getCollectorDeviceCacheValues(fCollectorDevice, paramType).toString();
	}

	@RequestMapping("collectorDeviceDistrict")
	@CrossOrigin
	public @ResponseBody
	String collectorDeviceDistrict(FarmCollectorDevice fCollectorDevice) {
		return collectorDeviceValueService.getSensorDistrict(fCollectorDevice).toString();
	}

	@RequestMapping("screenCollectorDeviceValueByDistrictOrType")
	@CrossOrigin
	public @ResponseBody
	String screenCollectorDeviceValueByDistrictOrType(Integer farmId, String deviceDistrict, String deviceType) {
		return collectorDeviceValueService.screenCollectorDeviceValueByDistrictOrType(farmId, deviceDistrict, deviceType).toString();
	}

	@RequestMapping("historyCollectorDeviceValues")
	@CrossOrigin
	public @ResponseBody
	String historyCollectorDeviceValues(FarmCollectorDevice farmCollectorDevice, @RequestParam("beginTime") String beginTime,
			@RequestParam("endTime") String endTime) {
		return collectorDeviceValueService.getHistorySensorValuesDynamic(farmCollectorDevice, Timestamp.valueOf(beginTime),
				Timestamp.valueOf(endTime)).toString();
	}

	@RequestMapping("historyCollectorDeviceExcel")
	@CrossOrigin
	public void historyCollectorDeviceExcel(FarmCollectorDevice fCollectorDevice, @RequestParam("beginTime") String beginTime,
			@RequestParam("endTime") String endTime, HttpServletResponse response) throws IOException {
		HSSFWorkbook workbook = collectorDeviceValueService.getHistorySensorValuesExcel(fCollectorDevice, Timestamp.valueOf(beginTime),
				Timestamp.valueOf(endTime));
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setContentType("application/msexcel;charset=utf-8");
		response.setHeader("Content-disposition", "attachment; filename=" + "historyData.xls");
		workbook.write(output);
		output.close();
	}

}
