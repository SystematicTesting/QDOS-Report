package com.systematictesting.services.core.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.services.UserDao;
import com.systematictesting.services.core.LicenseService;

@Repository(LicenseService.SERVICE_NAME)
public class LicenseServiceImpl implements LicenseService {

	private static final Logger logger = LoggerFactory.getLogger(LicenseServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public Map<String,Long> getAvailableFeatures(String email) {
		logger.debug("FEATURE LIST OF RETERIVE : "+email);
		User user = new User();
		user.setEmail(email);
		return userDao.getFeatureListOfAccount(user);
	}
	
	
}
