package com.ifarm.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifarm.bean.FarmCollector;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.dao.FarmCollectorDao;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.SystemResultEncapsulation;

/**
 * 集中器的service
 * 
 * @author lab
 * 
 */
@Service
public class FarmCollectorService {
	@Autowired
	private FarmCollectorDao farmCollectorDao;

	private static final Logger farmCollector_log = LoggerFactory.getLogger(FarmCollectorService.class);

	public String saveFarmCollector(FarmCollector farmCollector) {
		Long collectorId = farmCollector.getCollectorId();
		if (collectorId == null) {
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.NO_ID);
		}
		FarmCollector baseFarmCollector = farmCollectorDao.getTById(collectorId, FarmCollector.class);
		if (baseFarmCollector != null) {
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ID_EXIST);
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		farmCollector.setCollectorCreateTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
		try {
			farmCollectorDao.saveFarmCollector(farmCollector);
			// 更新到内存
			//CacheDataBase.collectorDetailMap.put(collectorId, JsonObjectUtil.fromBean(farmCollector));
			//CacheDataBase.controlCommandCache.put(collectorId, new PriorityBlockingQueue<ControlCommand>());
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			farmCollector_log.error("添加集中器异常", e);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}

	public String updateFarmCollector(FarmCollector farmCollector) {
		if (farmCollectorDao.updateDynamic(farmCollector)) {
			return SystemResultCodeEnum.SUCCESS;
		} else {
			return SystemResultCodeEnum.ERROR;
		}
	}

	public String getFarmCollectorsList(FarmCollector farmCollector) {
		return JsonObjectUtil.toJsonArrayString(farmCollectorDao.getDynamicList(farmCollector));
	}
	
}
