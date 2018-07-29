package com.ifarm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifarm.service.ApkVersionService;

@RestController
@RequestMapping(value = "apk_version")
public class ApkVersionController {
	@Autowired
	private ApkVersionService apkVersionService;
	
	@RequestMapping(value = "newestVersion")
	public String isNewestVersion(String versionId) {
		return apkVersionService.queryRecentVersion(versionId);
	}
	
}
