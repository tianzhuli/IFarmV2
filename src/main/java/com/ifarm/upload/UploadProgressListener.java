package com.ifarm.upload;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

import com.ifarm.bean.ProgressEntity;

public class UploadProgressListener implements ProgressListener {
	private HttpSession session;
	private String key;

	public UploadProgressListener() {
	}

	public UploadProgressListener(HttpSession session, String key) {
		this.session = session;
		this.key = key;
		ProgressEntity progressEntity = new ProgressEntity();
		session.setAttribute(key, progressEntity);
	}

	@Override
	public void update(long bytesRead, long contentLength, int items) {
		// TODO Auto-generated method stub
		ProgressEntity progressEntity = (ProgressEntity) session.getAttribute(key);
		progressEntity.setBytesRead(bytesRead);
		progressEntity.setContentLength(contentLength);
		progressEntity.setItems(items);
		session.setAttribute(key, progressEntity);
	}

}
