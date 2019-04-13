package com.ifarm.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ifarm.bean.ProgressEntity;
import com.ifarm.util.JsonObjectUtil;

@Controller
@RequestMapping(value = "upload")
public class UploadProcessController {
	@RequestMapping(value = "getProcess")
	public @ResponseBody
	String getProcess(@RequestParam("userId") String userId, HttpSession session) {
		ProgressEntity progressEntity = (ProgressEntity) session.getAttribute(userId);
		return JsonObjectUtil.toJsonObject(progressEntity).toJSONString();
	}
}
