package com.ifarm.web;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ifarm.bean.Farm;
import com.ifarm.bean.FarmPosition;
import com.ifarm.bean.FarmRegion;
import com.ifarm.service.FarmService;
import com.ifarm.service.UserLogService;
import com.ifarm.util.FileUtil;

@RequestMapping(value = "farm")
@RestController
@SuppressWarnings("rawtypes")
public class FarmController {
	@Autowired
	private FarmService farmService;

	@Autowired
	private UserLogService userLogService;

	@RequestMapping(value = "addFarm")
	public String addFarmColletor(@RequestParam("userId") String userId, HttpServletRequest request, Farm farm) {
		String message = farmService.saveFarm(farm);
		userLogService.saveUserLog(request, farm, userId, "add", "farm", message);
		return message;
	}
	
	@RequestMapping(value = "manager/addFarm")
	public String managerAddFarmColletor(@RequestParam("userId") String userId, HttpServletRequest request, Farm farm) {
		String message = farmService.saveFarm(farm);
		userLogService.saveUserLog(request, farm, userId, "add", "farm", message);
		return message;
	}

	@RequestMapping(value = "updateFarmCollector")
	public String updateFarmColletor(@RequestParam("userId") String userId, HttpServletRequest request, Farm farm) {
		String message = farmService.updateFarm(farm);
		userLogService.saveUserLog(request, farm, userId, "update", "farm", message);
		return message;
	}

	@RequestMapping(value = "getUserAroundFarmList")
	@CrossOrigin
	public String getUserAroundFarmList(@RequestParam("aroundPersonId") String aroundPersonId) {
		return farmService.getUserAroundFarmList(aroundPersonId);
	}

	/**
	 * 用户查询农场 很伤，历史遗留问题，这个url命名太蠢，以后系统重构的时候再改吧
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "farmsList")
	@CrossOrigin
	public String getFarmList(String userId) {
		return farmService.getFarmsList(userId);
	}

	@RequestMapping(value = "manager/getFarmList")
	public String mangerGetFarmList(String userId) {
		return farmService.getFarmsList(userId);
	}

	/**
	 * 
	 * @param flag
	 * @param request
	 * @param response
	 * @param farm
	 *            ，添加农场时图片添加为1,已经有农场了更新图片为2,添加农场时无图片为0
	 */

	@RequestMapping(value = "uploadFarm")
	public String uploadFarm(@RequestParam("flag") Integer flag, @RequestParam("userId") String userId, HttpServletRequest request,
			HttpServletResponse response, Farm farm) {
		if (flag == 0) {
			return farmService.saveFarm(farm);
		} else {
			CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			String savePath = request.getServletContext().getRealPath("../../images/farms");
			if (resolver.isMultipart(request)) {
				// 将request变成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 获取multiRequest 中所有的文件名
				Iterator iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 一次遍历所有文件
					MultipartFile file = multiRequest.getFile(iter.next().toString());
					if (file != null) {
						String fileName = file.getOriginalFilename();
						String fileRealName = FileUtil.makeFileName(fileName, "farm");
						String path = FileUtil.makeRealPath(savePath, fileRealName, userId);
						// 上传
						try {
							file.transferTo(new File(path));
							if (flag == 1) {
								farm.setFarmImageUrl(fileRealName);
								farmService.saveFarm(farm);
							} else if (flag == 2) {
								farm.setFarmImageUrl(fileRealName);
								farmService.updateFarm(farm);
							}
						} catch (IllegalStateException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							userLogService.saveUserLog(request, farm, userId, "update", "farm", "error");
							return "error";
						}
					}
				}
			}
			userLogService.saveUserLog(request, farm, userId, "update", "farm", "success");
			return "success";
		}
	}
	
	@RequestMapping("region/addition")
	@CrossOrigin
	public String addFarmRegion(FarmRegion farmRegion) {
		return farmService.addFarmRegion(farmRegion);
	}
	
	@RequestMapping("region/queryList")
	@CrossOrigin
	public String queryFarmRegionList(FarmRegion farmRegion) {
		return farmService.queryFarmRegionList(farmRegion);
	}
	
	@RequestMapping("region/delete")
	@CrossOrigin
	public String deleteFarmRegion(FarmRegion farmRegion) {
		return farmService.deleteRegion(farmRegion);
	}
	
	@RequestMapping("position/addition")
	@CrossOrigin
	public String addFarmPosition(FarmPosition farmPosition) {
		return farmService.addFarmPosition(farmPosition);
	}
	
	@RequestMapping("position/queryList")
	@CrossOrigin
	public String queryFarmPosition(FarmPosition farmPosition) {
		return farmService.queryFarmPosition(farmPosition);
	}
	
	@RequestMapping("position/delete")
	@CrossOrigin
	public String deleteFarmPosition(FarmPosition farmPosition) {
		return farmService.deleteFarmPosition(farmPosition);
	}
}
