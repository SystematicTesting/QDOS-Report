package com.systematictesting.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCatalogUtils {

	private static Logger logger = LoggerFactory.getLogger(TestCatalogUtils.class);
	
	private static String method_createReportLocation = " :: createCatalogLocation() :: ";
	
	public static void createCatalogLocation(String catalogUploadFolderLocation) throws SecurityException {

		File theDir = new File(catalogUploadFolderLocation);
		if (!theDir.exists()) {
			if (logger.isDebugEnabled()) {
				logger.debug(method_createReportLocation + "Creating Catalog Directory : "+catalogUploadFolderLocation);
			}
			boolean result = false;
			try {
				theDir.mkdirs();
				result = true;
			} catch (SecurityException se) {
				se.printStackTrace();
				logger.error(method_createReportLocation + "Failed to create catalog directory. Therefore report can not be published. Please contact system administrator of system.");
				throw se;
			}
			if (result) {
				logger.debug(method_createReportLocation + "Catalog Directory created successfully.");
			}
		}
	}

}
