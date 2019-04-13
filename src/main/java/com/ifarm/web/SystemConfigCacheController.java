package com.ifarm.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifarm.enums.SystemReturnCodeEnum;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.ConDb;
import com.ifarm.util.StringUtil;
import com.ifarm.util.SystemResultEncapsulation;

@RestController
@RequestMapping("cache")
public class SystemConfigCacheController {
	
	@RequestMapping("load")
	public String configCache(String cacheKey) throws SQLException, IOException {
		ConDb conDb = new ConDb();
		Connection con = conDb.openCon();
		if (StringUtil.equals(cacheKey, "SystemConfig")) {
			CacheDataBase.loadSystemConfigCache(con);
		} else if (StringUtil.equals(cacheKey, "InitBase")) {
			CacheDataBase.loadInitBaseConfig();
		} else if (StringUtil.equals(cacheKey, "Properties")) {
			CacheDataBase.loadProperties();
		}
		con.close();
		return SystemResultEncapsulation.fillResultCode(SystemReturnCodeEnum.SUCCESS);
	}
}
