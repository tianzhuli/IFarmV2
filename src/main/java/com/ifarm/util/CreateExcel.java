package com.ifarm.util;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;

public class CreateExcel {
	private JSONArray jsonArray;
	private String titles;

	public CreateExcel(JSONArray array, String titles) {
		this.jsonArray = array;
		this.titles = titles;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public HSSFWorkbook getExcel() {
		HSSFWorkbook wkb = new HSSFWorkbook();
		HSSFSheet sheet = wkb.createSheet("sheet1");
		try {
			String[] tableHeader = titles.split(",");
			int tableHeaderLength = tableHeader.length;
			HSSFRow fisrtHssfRow = sheet.createRow(0);
			for (int i = 0; i < tableHeaderLength; i++) {
				fisrtHssfRow.createCell(i).setCellValue(tableHeader[i]);
			}
			int rowNum = 1;
			for (int i = 0; i < jsonArray.size(); i++, rowNum++) {
				JSONArray array = jsonArray.getJSONArray(i);
				HSSFRow row = sheet.createRow(rowNum);
				for (int j = 0; j < array.size(); j++) {
					try {
						row.createCell(j).setCellValue(array.getString(j));
					} catch (Exception e) {
						// TODO: handle exception
						row.createCell(j).setCellValue("null");
					}

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return wkb;
	}
}
