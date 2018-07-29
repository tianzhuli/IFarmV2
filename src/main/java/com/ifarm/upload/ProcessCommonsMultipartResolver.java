package com.ifarm.upload;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class ProcessCommonsMultipartResolver extends CommonsMultipartResolver {
	private HttpServletRequest request;
	private String key;

	@Override
	protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
		// TODO Auto-generated method stub
		ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
		upload.setSizeMax(-1);
		if (request != null) {
			HttpSession session = request.getSession();
			UploadProgressListener progressListener = new UploadProgressListener(session, key);
			upload.setProgressListener(progressListener);
		}
		return upload;
	}

	public ProcessCommonsMultipartResolver() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProcessCommonsMultipartResolver(ServletContext servletContext) {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
		// TODO Auto-generated method stub
		this.request = request;
		this.key = request.getParameter("userId");
		return super.resolveMultipart(request);
	}

	@Override
	protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		System.out.println("key:" + key);
		FileUpload fileUpload = prepareFileUpload("UTF-8");
		UploadProgressListener progressListener = new UploadProgressListener(session, key);
		fileUpload.setProgressListener(progressListener);
		List<FileItem> fileItems = null;
		try {
			fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parseFileItems(fileItems, "UTF-8");
	}

}
