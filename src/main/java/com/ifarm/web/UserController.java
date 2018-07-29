package com.ifarm.web;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ifarm.annotation.FarmControllerLog;
import com.ifarm.bean.Page;
import com.ifarm.bean.User;
import com.ifarm.bean.UserFarmAuthority;
import com.ifarm.nosql.service.UserLogMongoService;
import com.ifarm.service.UserLogService;
import com.ifarm.service.UserService;
import com.ifarm.util.FileUtil;

@RestController
@RequestMapping(value = "user")
public class UserController{
	@Autowired
	private UserService userService;

	@Autowired
	private UserLogService userLogService;

	@Autowired
	private UserLogMongoService userLogMongoService;

	private static final Logger USER_CONTROLLER_LOG = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "login")
	public String userLogin(@RequestParam("token") String token, HttpServletRequest request, User user) {
		String returnMessage = userService.userLogin(user, token);
		userLogService.saveUserLog(request, user, "login", "user", returnMessage);
		userLogMongoService.saveUserLog(request, user, user.getUserId(), "login", "user", returnMessage);
		return returnMessage;
	}

	@RequestMapping(value = "register")
	public String userRegister(HttpServletRequest request, User user) {
		String returnMessage = userService.userRegister(user).toString();
		userLogService.saveUserLog(request, user, "register", "user", returnMessage);
		return returnMessage;
	}

	@RequestMapping(value = "getUserToken")
	@FarmControllerLog(value = "getUserToken", param = "user")
	public String userGetToken(String userId) {
		return userService.userGetToken(userId);
	}

	@RequestMapping(value = "updateUser")
	public String updateUser(HttpServletRequest request, User user) {
		String returnMessage = userService.updateUser(user);
		userLogService.saveUserLog(request, user, "register", "user", returnMessage);
		return returnMessage;
	}

	/**
	 * 
	 * @param flag
	 *            (0或者1分别表示头像和背景图片)
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "uploadImage")
	public String uploadImage(@RequestParam("flag") Integer flag, HttpServletRequest request, HttpServletResponse response, User user) {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		String savePath = request.getServletContext().getRealPath("/images/users");
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
					String fileRealName = FileUtil.makeFileName(fileName);
					String path = FileUtil.makeRealPath(savePath, fileRealName, user.getUserId());
					USER_CONTROLLER_LOG.info("图片上传存的路径：" + path);
					// 上传
					try {
						file.transferTo(new File(path));
						if (flag == 0) {
							user.setUserImageUrl(fileRealName);
						} else if (flag == 1) {
							user.setUserBackImageUrl(fileRealName);
						}
						userService.updateUser(user);
					} catch (IllegalStateException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "error";
					}
				}
			}
		}
		userLogService.saveUserLog(request, user, "uploadImage", "user", "success");
		return "success";
	}

	@RequestMapping(value = "getUserById")
	public String getUserById(@RequestParam("userId") String userId, HttpServletRequest request) {
		String returnMessage = userService.getUserById(userId);
		return returnMessage;
	}

	/**
	 * 
	 * @param userId
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "getUsersListAround")
	public String getUsersListAround(@RequestParam("userId") String userId, Page page) {
		String returnMessage = userService.getUsersListAround(userId, page);
		return returnMessage;
	}

	/**
	 * 
	 * @param userId
	 * @param farmId
	 * @param authority
	 *            权限，目前是两个onlySee和doControl或者all
	 * @return
	 */
	@RequestMapping(value = "addSubUser")
	public String addSubUser(String userId, Integer farmId, String authority) {
		return userService.addSubUser(userId, farmId, authority);
	}

	@RequestMapping(value = "subUserQuery")
	public String subUserQuery(String userId) {
		return userService.subUserQuery(userId);
	}

	@RequestMapping(value = "subUserAuthorityQuery")
	public String subUserAuthorityQuery(String userId) {
		return userService.subUserAuthorityQuery(userId);
	}

	@RequestMapping(value = "subUserAuthorityUpdate")
	public String subUserAuthorityUpdate(UserFarmAuthority userFarmAuthority) {
		return userService.updateSubUserAuthority(userFarmAuthority);
	}
}
