package com.ifarm.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "apk")
public class UtilController {
	@RequestMapping(value = "newest")
	public void downApk(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String savePath = request.getServletContext().getRealPath("/apk");
		System.out.println(savePath);
		OutputStream output = response.getOutputStream();
		FileInputStream in = new FileInputStream(savePath + "//Ifarm.apk");
		response.reset();
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Content-disposition", "attachment; filename=" + "Ifarm.apk");
		byte buffer[] = new byte[40 * 1024];
		int len = 0;
		while ((len = in.read(buffer)) > 0) {
			// 输出缓冲区的内容到浏览器，实现文件下载
			output.write(buffer, 0, len);
		}
		// 关闭文件输入流
		in.close();
		// 关闭输出流
		output.close();
	}
}
