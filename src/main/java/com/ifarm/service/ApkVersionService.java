package com.ifarm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ifarm.bean.ApkVersion;
import com.ifarm.dao.ApkVersionDao;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.JsonObjectUtil;

@Service
public class ApkVersionService {
	@Autowired
	private ApkVersionDao apkVersionDao;

	public String queryRecentVersion(String versionId) {
		ApkVersion apkVersion = apkVersionDao.queryRecentVersion();
		JSONObject jsonObject = new JSONObject();
		if (!apkVersion.getVersionId().equals(versionId)) {
			jsonObject = JsonObjectUtil.fromBean(apkVersion);
			jsonObject.put("versionUrl", CacheDataBase.apkVersionPath + jsonObject.get("versionUrl"));
			jsonObject.put("state", "old");
		} else {
			jsonObject.put("state", "new");
		}
		return jsonObject.toString();
	}
}
