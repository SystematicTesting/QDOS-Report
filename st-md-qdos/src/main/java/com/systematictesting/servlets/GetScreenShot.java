package com.systematictesting.servlets;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.systematictesting.daolayer.constants.QdosConstants;
import com.systematictesting.daolayer.entity.User;

public class GetScreenShot extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(GetScreenShot.class);
	private static final long serialVersionUID = 1L;
	private static final String fileSeparator = QdosConstants.FILE_SEPARATOR;
	private Properties properties = null;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
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
			io.printStackTrace();
			logger.error("IO exception occurred on initialization : ", io.getMessage(), io);
			throw new ServletException(io);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestedActiveApiKey = request.getParameter(User.FIELDS.ACTIVE_API_KEY);
		String requestedSiteName = request.getParameter(QdosConstants.SITE_NAME);
		String requestedVersionNumber = request.getParameter(QdosConstants.VERSION_NUMBER);
		String requestedTestSuiteName = request.getParameter(QdosConstants.SUITE_NAME);
		String requestedTestCaseID = request.getParameter(QdosConstants.TEST_CASE_ID);
		String requestedDataSetID = request.getParameter(QdosConstants.DATA_SET_ID);
		String requestedTestStepID = request.getParameter(QdosConstants.STEP_ID);
		String requestedFileName = request.getParameter(QdosConstants.UPLOAD_FILE_NAME);

		if (StringUtils.isBlank(requestedFileName) 
				&& StringUtils.isBlank(requestedTestSuiteName)
				&& StringUtils.isBlank(requestedVersionNumber)
				
				&& StringUtils.isBlank(requestedActiveApiKey)
				&& StringUtils.isBlank(requestedTestCaseID)
				&& StringUtils.isBlank(requestedDataSetID)
				&& StringUtils.isBlank(requestedTestStepID)
				
				&& StringUtils.isBlank(requestedSiteName)) {
			throw new ServletException("REQUIRED PARAMETERS ARE MISSING :: "
					+ "\n" + QdosConstants.SITE_NAME + " : "+ requestedSiteName 
					+ "\n" + QdosConstants.VERSION_NUMBER + " : " + requestedVersionNumber
					+ "\n" + QdosConstants.SUITE_NAME + " : " + requestedTestSuiteName 
					+ "\n" + User.FIELDS.ACTIVE_API_KEY + " : " + requestedActiveApiKey 
					+ "\n" + QdosConstants.TEST_CASE_ID + " : " + requestedTestCaseID 
					+ "\n" + QdosConstants.DATA_SET_ID + " : " + requestedDataSetID 
					+ "\n" + QdosConstants.STEP_ID + " : " + requestedTestStepID 
					+ "\n" + QdosConstants.UPLOAD_FILE_NAME + " : " + requestedFileName);
		}

		String screenShotFileLocation = properties.getProperty(QdosConstants.BOOTUP_TEST_REPORT_ROOT_PATH)
				+ fileSeparator + requestedActiveApiKey
				+ fileSeparator + requestedSiteName 
				+ fileSeparator + requestedVersionNumber 
				+ fileSeparator + requestedTestSuiteName
				+ fileSeparator + requestedTestCaseID
				+ fileSeparator + requestedDataSetID
				+ fileSeparator + requestedTestStepID
				+ fileSeparator + requestedFileName;

		if (logger.isDebugEnabled()) {
			logger.debug("Requested API Key : " + requestedActiveApiKey);
			logger.debug("Requested Site Name : " + requestedSiteName);
			logger.debug("Requested Version Number : " + requestedVersionNumber);
			logger.debug("Requested Test Suite Name : " + requestedTestSuiteName);
			logger.debug("Requested Test Case ID : " + requestedTestCaseID);
			logger.debug("Requested Data Set ID : " + requestedDataSetID);
			logger.debug("Requested Test Step ID : " + requestedTestStepID);
			logger.debug("Requested File Name : " + requestedFileName);
		}

		int length = 0;
		byte[] byteBuffer = new byte[4096];
		DataInputStream in = new DataInputStream(new FileInputStream(screenShotFileLocation));
		ServletContext cntx = getServletContext();
		String mime = cntx.getMimeType(screenShotFileLocation);
		response.setContentType(mime);
		
		ServletOutputStream outStream = response.getOutputStream();

		while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
			outStream.write(byteBuffer, 0, length);
		}
		in.close();
		outStream.close();
	}
}
