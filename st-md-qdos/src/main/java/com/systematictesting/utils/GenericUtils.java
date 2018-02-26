package com.systematictesting.utils;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.systematictesting.daolayer.constants.QdosConstants;
import com.systematictesting.rest.core.utils.TimeFormatHandler;

public class GenericUtils {
	
	private static Logger logger = LoggerFactory.getLogger(GenericUtils.class);
	
	private static String method_createReportLocation = " :: createReportLocation() :: ";
	public static String createReportLocation(HttpServletRequest request, String reportLocationTillSiteName, String suiteName, String requestedVersionNumber, String requestedTestCaseID, String requestedDataSetID, String requestedStepID) {
		String reportCreationTime = TimeFormatHandler.now("yyyy-MM-dd-hh-mm-ss");
		if (StringUtils.isNotBlank(requestedVersionNumber)){
			reportCreationTime = requestedVersionNumber;
		}

		String screenshotUploadFolderLocation = reportLocationTillSiteName +QdosConstants.FILE_SEPARATOR+ reportCreationTime +QdosConstants.FILE_SEPARATOR+ suiteName + QdosConstants.FILE_SEPARATOR + requestedTestCaseID + QdosConstants.FILE_SEPARATOR + requestedDataSetID + QdosConstants.FILE_SEPARATOR + requestedStepID;

		File theDir = new File(screenshotUploadFolderLocation);
		if (!theDir.exists()) {
			if (logger.isDebugEnabled()) {
				logger.debug(method_createReportLocation + "Creating Report Directory : "+screenshotUploadFolderLocation);
			}
			boolean result = false;
			try {
				theDir.mkdirs();
				result = true;
			} catch (SecurityException se) {
				se.printStackTrace();
				logger.error(method_createReportLocation + "Failed to create reporting directory. Therefore report can not be published. Please contact system administrator of system.");
				return null;
			}
			if (result) {
				logger.debug(method_createReportLocation + "Reporting Directory created successfully.");
			}
		}
		return screenshotUploadFolderLocation;
	}
	
}
