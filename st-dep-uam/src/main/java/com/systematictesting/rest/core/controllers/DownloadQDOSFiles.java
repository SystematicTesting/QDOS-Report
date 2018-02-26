package com.systematictesting.rest.core.controllers;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.systematictesting.daolayer.constants.UserConstants;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.exceptions.FileNotFoundException;
import com.systematictesting.daolayer.exceptions.SessionNotValidException;
import com.systematictesting.services.core.SystemPropertiesService;

@Controller
@RequestMapping("/files")
public class DownloadQDOSFiles extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(DownloadQDOSFiles.class);

	@Autowired
	private SystemPropertiesService systemPropertiesService;

	@Autowired
	ServletContext servletContext;

	@RequestMapping(value = "/downloadQDOSFiles.rest", method = RequestMethod.GET, produces = { "application/*" }, consumes = { "text/plain", "application/*" })
	public void downloadTestSuite(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
		String fileNameToDownload = request.getParameter("fileName");
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to download TestSuites excel file.");
		}
		if (StringUtils.isBlank(fileNameToDownload)) {
			throw new Exception("No file requested to download.");
		}
		String defaultQDOSClientFilesLocation = systemPropertiesService.getProperties().getProperty(UserConstants.BOOTUP_QDOS_CLIENT_FILES_PATH);
		String absoluteFilePathOfRequestedFile = defaultQDOSClientFilesLocation + UserConstants.FILE_SEPARATOR + fileNameToDownload;
		logger.debug("DOWNLOADING FILE : " + absoluteFilePathOfRequestedFile);
		try {
			FileInputStream fileToDownloadStream = new FileInputStream(absoluteFilePathOfRequestedFile);
			int length = 0;
			byte[] byteBuffer = new byte[4096];
			DataInputStream in = new DataInputStream(fileToDownloadStream);

			String mime = servletContext.getMimeType(absoluteFilePathOfRequestedFile);
			response.setContentType(mime);

			ServletOutputStream outStream = response.getOutputStream();

			while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
				outStream.write(byteBuffer, 0, length);
			}
			in.close();
			outStream.close();
		} catch (IOException io) {
			throw new FileNotFoundException(io.getMessage());
		}
	}

}
