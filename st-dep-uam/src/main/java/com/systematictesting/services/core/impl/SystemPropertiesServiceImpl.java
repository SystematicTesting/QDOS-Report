package com.systematictesting.services.core.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang3.StringUtils;

import com.systematictesting.daolayer.constants.UserConstants;
import com.systematictesting.services.core.SystemPropertiesService;

@Repository(SystemPropertiesService.SERVICE_NAME)
public class SystemPropertiesServiceImpl implements SystemPropertiesService {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemPropertiesServiceImpl.class);
	private Properties properties = null;
	
	@Autowired
	ServletContext servletContext;

	private void loadProperties() throws IOException {
		if (properties == null) {
			properties = new Properties();
			try {
				if (StringUtils.isNotBlank(System.getProperty(UserConstants.REPORTING_APP_HOME))) {
					String customConfigFile = System.getProperty(UserConstants.REPORTING_APP_HOME) + UserConstants.FILE_SEPARATOR + UserConstants.CONF_FOLDER + UserConstants.FILE_SEPARATOR + UserConstants.REPORT_APP_CONFIG_FILE_NAME;
					logger.info("Reading Config file : " + customConfigFile);
					InputStream input = new FileInputStream(customConfigFile);
					properties.load(input);
				} else {
					logger.info("Reading Config file : " + UserConstants.SREPORT_CONFIG_FILE);
					InputStream input = servletContext.getResourceAsStream(UserConstants.SREPORT_CONFIG_FILE);
					properties.load(input);
				}
				logger.info("Properties loaded successfully.");
			} catch (IOException io) {
				logger.error("IO exception occurred on initialization : ", io.getMessage(), io);
				io.printStackTrace();
				throw new IOException(io);
			}
		}
	}

	@Override
	public Properties getProperties() throws IOException {
		loadProperties();
		if (properties != null){
			return properties;
		}
		return new Properties();
	}

}
