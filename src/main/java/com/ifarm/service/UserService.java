package com.ifarm.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ifarm.annotation.FarmServiceLog;
import com.ifarm.bean.Page;
import com.ifarm.bean.User;
import com.ifarm.bean.UserFarmAuthority;
import com.ifarm.constant.AuthorityConstant;
import com.ifarm.constant.SystemResultCodeEnum;
import com.ifarm.dao.UserDao;
import com.ifarm.dao.UserFarmAuthorityDao;
import com.ifarm.nosql.bean.UserToken;
import com.ifarm.nosql.dao.UserTokenDao;
import com.ifarm.redis.util.UserRedisUtil;
import com.ifarm.util.CacheDataBase;
import com.ifarm.util.FileUtil;
import com.ifarm.util.JsonObjectUtil;
import com.ifarm.util.RandomUtil;
import com.ifarm.util.SystemResultEncapsulation;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	@Autowired
	private UserTokenDao userTokenDao;

	@Autowired
	private UserFarmAuthorityDao userFarmAuthorityDao;

	@Autowired
	private UserRedisUtil userRedisUtil;
	
	private static final Log user_log = LogFactory.getLog(UserService.class);

	public JSONObject userRegister(User user) {
		JSONObject jsonObject = new JSONObject();
		if (user.getUserId() != null && user.getUserPwd() != null) {
			try {
				String userId = user.getUserId();
				if (userDao.getUserById(userId) == null) {
					String token = userGetToken(user.getUserId());
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					user.setUserRegisterTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
					userDao.saveUser(user);
					// 更新内存，这个应该有问题
					//CacheDataBase.userControlResultMessageCache.put(userId, new LinkedBlockingQueue<String>());
					//CacheDataBase.controlTaskStateCache.put(userId, new LinkedBlockingQueue<ControlTask>());
					//CacheDataBase.wfmControlTaskStateCache.put(userId, new LinkedBlockingQueue<WFMControlTask>());
					jsonObject.put("message", SystemResultCodeEnum.SUCCESS);
					jsonObject.put("token", token);
					//CacheDataBase.userToken.put(user.getUserId(), token);
				} else {
					jsonObject.put("message", SystemResultCodeEnum.REPEAT);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jsonObject.put("message", SystemResultCodeEnum.ERROR);
			}
		} else {
			jsonObject.put("message", SystemResultCodeEnum.FORMAT_ERROR);
		}
		return jsonObject;
	}

	@FarmServiceLog(value = "getToken", param = "user")
	public String userGetToken(String userId) {
		if (userId == null) {
			return SystemResultCodeEnum.ERROR;
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("ifarm");
		stringBuffer.append(UUID.randomUUID());
		String token = stringBuffer.toString().replace("-", "");
		user_log.info(userId + ":" + token);
		userRedisUtil.setUserToken(userId, token);
		return token;
	}

	public String userGetSignature(String userId) {
		if (userId == null) {
			return SystemResultCodeEnum.ERROR;
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("ifarm");
		stringBuffer.append(UUID.randomUUID());
		stringBuffer.append(Base64.encodeBase64String(userId.getBytes()));
		String signature = stringBuffer.toString().replace("-", "");
		user_log.info(userId + ":" + signature);
		//CacheDataBase.userSignature.put(userId, token);
		userRedisUtil.setUserSignature(userId, signature);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		UserToken userToken = new UserToken();
		userToken.setUserId(userId);
		userToken.setTokenId(signature);
		userToken.setTokenTime(simpleDateFormat.format(new Date()));
		return signature;
	}

	public String userLogin(User user, String token) {
		if (user.getUserId() == null || user.getUserPwd() == null) {
			return SystemResultCodeEnum.INVNAIN;
		}
		String userToken = "";
		//userToken = CacheDataBase.userToken.get(user.getUserId());
		userToken = userRedisUtil.getUserToken(user.getUserId());
		if (!token.equals(userToken)) {
			return SystemResultCodeEnum.ERROR_TOKEN;
		}
		String pwdBase64 = new String(Base64.decodeBase64(user.getUserPwd()));
		user.setUserPwd(pwdBase64);
		// ReflectTraverse.traverseObject(user);
		try {
			if (userDao.userLogin(user) > 0) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				user.setUserLastLoginTime(Timestamp.valueOf((simpleDateFormat.format(new Date()))));
				userDao.updateDynamic(user);
				String signature = userGetSignature(user.getUserId());
				// System.out.println(user.getUserId() + ":redis-token：" +
				// userTokenDao.getUserToken(user.getUserId()));
				return SystemResultCodeEnum.SUCCESS + ":" + signature;
			} else {
				return SystemResultCodeEnum.WRONG;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return SystemResultCodeEnum.ERROR;
		}
	}

	public String updateUser(User user) {
		if (userDao.updateDynamic(user)) {
			return SystemResultCodeEnum.SUCCESS;
		} else {
			return SystemResultCodeEnum.ERROR;
		}
	}

	public String getUserById(String userId) {
		User user = userDao.getUserById(userId);
		if (user != null) {
			if (user.getUserImageUrl() != null) {
				String userImagePath = FileUtil.makeRealPathUrl(CacheDataBase.userImagePath, user.getUserImageUrl(), userId);
				user.setUserImageUrl(userImagePath);
			}
			if (user.getUserBackImageUrl() != null) {
				String userImagePath = FileUtil.makeRealPathUrl(CacheDataBase.userImagePath, user.getUserBackImageUrl(), userId);
				user.setUserBackImageUrl(userImagePath);
			}
			return JsonObjectUtil.toJsonObject(user).toString();
		} else {
			return SystemResultCodeEnum.NO_USER;
		}
	}
	
	public User qeuryUserById(String userId) {
		return userDao.getUserById(userId);
	}

	public String getUsersListAround(String userId, Page page) {
		if (page.getBeginIndex() == null || page.getCount() == null) {
			return SystemResultCodeEnum.INVNAIN;
		}
		List<User> list = userDao.getUsersListAround(userId, page);
		return JsonObjectUtil.toJsonArrayString(list);
	}

	public String getAllUserList() {
		List<User> list = userDao.selectAllUser();
		for (int i = 0; i < list.size(); i++) {
			User user2 = list.get(i);
			if (user2.getUserImageUrl() != null && user2.getUserId() != null) {
				String userImagePath = FileUtil.makeRealPathUrl(CacheDataBase.userImagePath, user2.getUserImageUrl(), user2.getUserId());
				user2.setUserImageUrl(userImagePath);
			}
			if (user2.getUserBackImageUrl() != null && user2.getUserId() != null) {
				String userImagePath = FileUtil.makeRealPathUrl(CacheDataBase.userImagePath, user2.getUserBackImageUrl(), user2.getUserId());
				user2.setUserBackImageUrl(userImagePath);
			}
		}
		String result = JsonObjectUtil.toJsonArrayString(list);
		return result;
	}

	public String addSubUser(String userId, Integer farmId, String authority) {
		JSONObject jsonObject = new JSONObject();
		User user = userDao.getUserById(userId);
		if (AuthorityConstant.FARMER.equals(user.getUserRole())) {
			BigInteger subUserCount = userDao.subUserCount(userId);
			if (subUserCount.intValue() > 3 && !AuthorityConstant.FARMER_VIP.equals(user.getUserRole())) {
				jsonObject.put("response", SystemResultCodeEnum.USER_SUB_FULL);
				return jsonObject.toString();
			}
			String subUserId = userId + "_" + RandomUtil.randomSixInteger();
			User subUser = new User(subUserId, AuthorityConstant.INIT_PWD);
			try {
				userDao.saveUser(subUser);
				UserFarmAuthority userFarmAuthority = new UserFarmAuthority(subUserId, farmId, authority);
				userFarmAuthorityDao.saveBase(userFarmAuthority);
				jsonObject.put("response", SystemResultCodeEnum.SUCCESS);
			} catch (Exception e) {
				// TODO: handle exception
				user_log.error(e);
				jsonObject.put("response", SystemResultCodeEnum.ERROR);
			}
		} else {
			jsonObject.put("response", SystemResultCodeEnum.NO_AUTH);
		}
		return jsonObject.toString();
	}

	public String subUserQuery(String userId) {
		try {
			return JsonObjectUtil.toJsonArrayString(userDao.subUserQuery(userId));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return SystemResultCodeEnum.ERROR;
		}
		
	}

	public String subUserAuthorityQuery(String userId) {
		return JsonObjectUtil.toJsonArrayString(userFarmAuthorityDao.queryUserAuthorityByUserId(userId));
	}

	public String updateSubUserAuthority(UserFarmAuthority userFarmAuthority) {
		try {
			userFarmAuthorityDao.updateDynamic(userFarmAuthority);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			user_log.error(e.getMessage());
			user_log.error("sub User更新异常", e);
			return SystemResultEncapsulation.resultCodeDecorate(SystemResultCodeEnum.ERROR);
		}
	}
	
	public static void main(String[] args) {
		System.out.println( new String(Base64.encodeBase64("123456".getBytes())));
	}
}
