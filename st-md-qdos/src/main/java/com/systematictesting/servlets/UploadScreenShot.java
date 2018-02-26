/*
 * Copyright (c) Mar 5, 2014 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.systematictesting.daolayer.constants.QdosConstants;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.utils.GenericUtils;

public class UploadScreenShot extends HttpServlet {

	private static Logger logger = LoggerFactory.getLogger(UploadScreenShot.class);
	private static final long serialVersionUID = 1L;

	private int maxFileSize = 10000 * 1024;
	private int maxMemSize = 4 * 1024;
	private Properties properties = null;

	public void init(ServletConfig config) throws ServletException {
		logger.debug("Loading environment specific properties file...");
		properties = new Properties();
		try {
			if (StringUtils.isNotBlank(System.getProperty(QdosConstants.REPORTING_APP_HOME))) {
				String customConfigFile = System.getProperty(QdosConstants.REPORTING_APP_HOME) + QdosConstants.FILE_SEPARATOR + QdosConstants.CONF_FOLDER + QdosConstants.FILE_SEPARATOR + QdosConstants.REPORT_APP_CONFIG_FILE_NAME;
				logger.debug("Reading Config file : " + customConfigFile);
				InputStream input = new FileInputStream(customConfigFile);
				properties.load(input);
			} else {
				String filename = config.getInitParameter(QdosConstants.FILTER_PARAM_CONFIG_FILE);
				logger.debug("Reading Config file : " + filename);
				InputStream input = config.getServletContext().getResourceAsStream(filename);
				properties.load(input);
			}
			logger.debug("Properties loaded successfully.");
		} catch (IOException io) {
			logger.error("IO exception occurred on initialization : ", io.getMessage(), io);
			io.printStackTrace();
			throw new ServletException(io);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		writeReportScreenshotFile(request);
	}

	private void writeReportScreenshotFile(HttpServletRequest request) throws ServletException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			throw new ServletException("FILE NOT UPLOADED. PLEASE SEND THE MULTI-PART File upload request.");
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(maxMemSize);
		factory.setRepository(new File(properties.getProperty(QdosConstants.BOOTUP_TEMP_FILE_UPLOAD_PATH)));

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(maxFileSize);

		try {
			@SuppressWarnings("unchecked")
			List<FileItem> fileItems = upload.parseRequest(request);

			Iterator<FileItem> i = fileItems.iterator();
			FileItem fileItemToUpload = null;
			Map<String, String> parameterMap = new HashMap<String, String>();
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {
					fileItemToUpload = fi;
				} else {
					String name = fi.getFieldName();
					String value = fi.getString();
					parameterMap.put(name, value);
				}
			}
			String requestedSuiteName = parameterMap.get(QdosConstants.SUITE_NAME);
			String requestedSiteName = parameterMap.get(QdosConstants.SITE_NAME);
			String requestedVersionNumber = parameterMap.get(QdosConstants.VERSION_NUMBER);
			String requestedTestCaseID = parameterMap.get(QdosConstants.TEST_CASE_ID);
			String requestedDataSetID = parameterMap.get(QdosConstants.DATA_SET_ID);
			String requestedStepID = parameterMap.get(QdosConstants.STEP_ID);
			String requestedFileName = parameterMap.get(QdosConstants.UPLOAD_FILE_NAME);
			String requestedActiveApiKey = parameterMap.get(User.FIELDS.ACTIVE_API_KEY);
			String requestedEmailAddress = parameterMap.get(User.FIELDS.EMAIL);

			logger.debug("REQUIRED PARAMETERS ARE : "
					+ "\n SuiteName = " + requestedSuiteName 
					+ "\n SiteName = " + requestedSiteName 
					+ "\n Email = " + requestedEmailAddress 
					+ "\n ActiveApiKey = " + requestedActiveApiKey 
					+ "\n VersionNumber = " + requestedVersionNumber 
					+ "\n Test Case ID = "+requestedTestCaseID
					+ "\n Data Set ID = " + requestedDataSetID
					+ "\n Step ID = " + requestedStepID
					+ "\n requestedFileName = "+ requestedFileName);
			
			if (StringUtils.isNotBlank(requestedEmailAddress) 
					&& StringUtils.isNotBlank(requestedActiveApiKey)
					&& StringUtils.isNotBlank(requestedFileName) 
					&& StringUtils.isNotBlank(requestedVersionNumber)
					&& StringUtils.isNotBlank(requestedTestCaseID)
					&& StringUtils.isNotBlank(requestedDataSetID)
					&& StringUtils.isNotBlank(requestedStepID)
					&& StringUtils.isNotBlank(requestedSiteName)
					&& StringUtils.isNotBlank(requestedSuiteName)) {
				String reportLocationTillSiteName = properties.getProperty(QdosConstants.BOOTUP_TEST_REPORT_ROOT_PATH) + QdosConstants.FILE_SEPARATOR 
						+ requestedActiveApiKey + QdosConstants.FILE_SEPARATOR 
						+ requestedSiteName + QdosConstants.FILE_SEPARATOR;
				String screenshotUploadFolderLocation = GenericUtils.createReportLocation(request, reportLocationTillSiteName,
						requestedSuiteName, requestedVersionNumber, requestedTestCaseID, requestedDataSetID, requestedStepID);
				if (screenshotUploadFolderLocation != null) {
					File file = new File(screenshotUploadFolderLocation + QdosConstants.FILE_SEPARATOR + requestedFileName);
					fileItemToUpload.write(file);
				} else {
					throw new ServletException("REPORT LOCATION not present against SITE NAME : " + requestedSiteName
							+ " :: SUITE NAME : " + requestedSuiteName + " :: VERSION : " + requestedVersionNumber);
				}
			} else {
				throw new ServletException("REQUIRED PARAMETERS ARE MISSING : \n SuiteName = " + requestedSuiteName
						+ "\n SiteName = " + requestedSiteName + "\n requestedFileName = " + requestedFileName
						+ "\n requestedVersionNumber = " + requestedVersionNumber);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException("FILE NOT UPLOADED : ", ex);
		}
	}

}
