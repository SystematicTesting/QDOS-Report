/**
 * Copyright (c) Mar 7, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.daolayer.services.impl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.systematictesting.daolayer.constants.ResponseCodes;
import com.systematictesting.daolayer.constants.Status;
import com.systematictesting.daolayer.constants.UserConstants;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.exceptions.AccountActivationLinkNotValidException;
import com.systematictesting.daolayer.exceptions.DuplicateEmailException;
import com.systematictesting.daolayer.exceptions.EmailNotValidException;
import com.systematictesting.daolayer.exceptions.InActiveUserException;
import com.systematictesting.daolayer.exceptions.InternalServerErrorException;
import com.systematictesting.daolayer.exceptions.InvalidPasswordException;
import com.systematictesting.daolayer.exceptions.LoginFailedException;
import com.systematictesting.daolayer.exceptions.NewUserNotActivatedException;
import com.systematictesting.daolayer.exceptions.SessionNotValidException;
import com.systematictesting.daolayer.exceptions.SharingInvitationFailedException;
import com.systematictesting.daolayer.services.UserDao;
import com.systematictesting.rest.core.utils.EncryptionHandler;

@Repository(UserDao.SERVICE_NAME)
public class UserDaoImpl implements UserDao {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	private SecureRandom random = new SecureRandom();

	@Autowired
	private MongoOperations mongoOperation;

	@Override
	public boolean addUser(User user) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser == null || !savedUser.getEmail().equals(user.getEmail())) {
			user.setCreatetime(System.currentTimeMillis());
			user.setLastmodifiedtime(System.currentTimeMillis());
			user.setStatus(Status.USER_STATUS.NEW);
			user.setPassword(EncryptionHandler.encrypt(user.getPassword(), user.getEmail()));
			user.setDefaultAPIkey(EncryptionHandler.getMD5String(user.getEmail()));
			user.setActiveAPIkey(user.getDefaultAPIkey());
			Map<String,String> keyVsEmail = new HashMap<String,String>();
			keyVsEmail.put(user.getDefaultAPIkey(), user.getEmail());
			user.setAllsavedAPIkeys(keyVsEmail);
			logger.debug("Saving an user in users table : " + user.toString());
			try {
				mongoOperation.save(user);
				logger.debug("Saving Completed Successfully");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured while adding user in DB : ", e);
				throw e;
			}
		} else {
			throw new DuplicateEmailException(ResponseCodes.DUPLICATE_EMAIL.message);
		}

	}

	private User queryUserInDBViaEmail(String email) {
		Query searchUserQuery = new Query(Criteria.where(User.FIELDS.EMAIL).is(email));
		User savedUser = null;
		try {
			savedUser = mongoOperation.findOne(searchUserQuery, User.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering user in DB : ", e);
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
		return savedUser;
	}

	@Override
	public User loginUser(User user) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser != null && EncryptionHandler.encrypt(user.getPassword(), user.getEmail()).equals(savedUser.getPassword())) {
			if (savedUser != null && !savedUser.getStatus().equals(Status.USER_STATUS.ACTIVE)) {
				if (savedUser.getStatus().equals(Status.USER_STATUS.INACTIVE)) {
					throw new InActiveUserException(ResponseCodes.USER_DEACTIVATED.message);
				} else if (savedUser.getStatus().equals(Status.USER_STATUS.NEW)) {
					throw new NewUserNotActivatedException(ResponseCodes.NEW_USER_DETECTED.message);
				} else {
					throw new InternalServerErrorException(ResponseCodes.INTERNAL_ERROR.message);
				}
			} else {
				protectSystemDataInUserObject(savedUser);
				logger.debug("RESPONSE /user/login.rest : " + savedUser);
				return savedUser;
			}
		} else {
			throw new LoginFailedException(ResponseCodes.LOGIN_FAILED.message);
		}

	}

	@Override
	public boolean activateAccount(User user) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser != null && savedUser.getEmail().equals(user.getEmail()) && savedUser.getActiveAPIkey().equals(user.getActiveAPIkey())) {
			savedUser.setLastmodifiedtime(System.currentTimeMillis());
			savedUser.setStatus(Status.USER_STATUS.ACTIVE);
			logger.debug("Activating an user in users table : " + user.toString());
			try {
				mongoOperation.save(savedUser);
				user.setFirstname(savedUser.getFirstname());
				user.setLastname(savedUser.getLastname());
				logger.debug("Update Completed Successfully");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured while adding user in DB : ", e);
				throw e;
			}
		} else {
			throw new AccountActivationLinkNotValidException(ResponseCodes.ACCOUNT_ACTIVATION_LINK_INVALID.message);
		}
	}
	
	@Override
	public boolean resetPassword(User user) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser != null && savedUser.getEmail().equals(user.getEmail())) {
			savedUser.setLastmodifiedtime(System.currentTimeMillis());
			user.setPassword(new BigInteger(130, random).toString(32));
			user.setFirstname(savedUser.getFirstname());
			user.setLastname(savedUser.getLastname());
			savedUser.setPassword(EncryptionHandler.encrypt(user.getPassword(), savedUser.getEmail()));
			logger.debug("Reseting password of an user in users table : " + savedUser.toString());
			try {
				mongoOperation.save(savedUser);
				logger.debug("Reset Password Completed Successfully");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured while adding user in DB : ", e);
				throw e;
			}
		} else {
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
	}

	@Override
	public boolean validateActiveAPIKey(User user) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser != null && savedUser.getEmail().equals(user.getEmail())) {
			if (savedUser.getAllsavedAPIkeys().containsKey(user.getActiveAPIkey())){
				return true;
			} else {
				return false;
			}
		} else {
			throw new SessionNotValidException(ResponseCodes.SESSION_NOT_VALID.message);
		}
	}

	@Override
	public boolean changePassword(User user, String oldPassword, String newPassword) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser != null && savedUser.getEmail().equals(user.getEmail()) && savedUser.getDefaultAPIkey().equals(user.getDefaultAPIkey())) {
			savedUser.setLastmodifiedtime(System.currentTimeMillis());
			String encryptedPassword = EncryptionHandler.encrypt(oldPassword, user.getEmail());
			if (savedUser.getPassword().equals(encryptedPassword)) {
				savedUser.setPassword(EncryptionHandler.encrypt(newPassword, user.getEmail()));
				logger.debug("Activating an user in users table : " + user.toString());
				try {
					mongoOperation.save(savedUser);
					user.setFirstname(savedUser.getFirstname());
					user.setLastname(savedUser.getLastname());
					logger.debug("Update Completed Successfully");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while adding user in DB : ", e);
					throw e;
				}
			} else {
				throw new InvalidPasswordException(ResponseCodes.INVALID_PASSWORD.message);
			}
		} else {
			throw new SessionNotValidException(ResponseCodes.SESSION_NOT_VALID.message);
		}
	}

	@Override
	public User getUserDetailsViaEmailAndDefaultApiKey(User user) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser != null && savedUser.getEmail().equals(user.getEmail()) && savedUser.getDefaultAPIkey().equals(user.getDefaultAPIkey())) {
			protectSystemDataInUserObject(savedUser);
			return savedUser;
		} else {
			throw new SessionNotValidException(ResponseCodes.SESSION_NOT_VALID.message);
		}
	}

	private void protectSystemDataInUserObject(User savedUser) {
		savedUser.setCreatetime(0L);
		savedUser.setId(UserConstants.PROTECTED);
		savedUser.setLastmodifiedtime(0L);
		savedUser.setPassword(UserConstants.PROTECTED);
		savedUser.setTermsAndConditions(UserConstants.PROTECTED);
	}

	@Override
	public boolean updateAccount(User user) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser != null && savedUser.getEmail().equals(user.getEmail()) && savedUser.getDefaultAPIkey().equals(user.getDefaultAPIkey())) {
			savedUser.setLastmodifiedtime(System.currentTimeMillis());
			savedUser.setFirstname(user.getFirstname());
			savedUser.setLastname(user.getLastname());
			savedUser.setAddress(user.getAddress());
			savedUser.setCity(user.getCity());
			savedUser.setCompanyname(user.getCompanyname());
			savedUser.setCountry(user.getCountry());
			savedUser.setPostcode(user.getPostcode());
			logger.debug("Updating an user in users table : " + savedUser.toString());
			try {
				mongoOperation.save(savedUser);
				logger.debug("Update Completed Successfully");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured while adding user in DB : ", e);
				throw e;
			}
			
		} else {
			throw new SessionNotValidException(ResponseCodes.SESSION_NOT_VALID.message);
		}
	}
	
	@Override
	public boolean updateLicense(User user) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser != null && savedUser.getEmail().equals(user.getEmail()) && savedUser.getDefaultAPIkey().equals(user.getDefaultAPIkey())) {
			savedUser.setLastmodifiedtime(System.currentTimeMillis());
			if (user.getLicense()!=null){
				savedUser.setLicense(user.getLicense());
			}
			logger.debug("Updating an license on user in users table : " + savedUser.toString());
			try {
				mongoOperation.save(savedUser);
				logger.debug("License Updated Completed Successfully");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured while adding user in DB : ", e);
				throw e;
			}
			
		} else {
			throw new SessionNotValidException(ResponseCodes.SESSION_NOT_VALID.message);
		}
	}
	
	@Override
	public boolean updateEmailSubscriptions(User user) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser != null && savedUser.getEmail().equals(user.getEmail()) && savedUser.getDefaultAPIkey().equals(user.getDefaultAPIkey())) {
			savedUser.setLastmodifiedtime(System.currentTimeMillis());
			savedUser.setEmailSubscriptions(user.getEmailSubscriptions());
			logger.debug("Updating an user email subscriptions in users table : " + savedUser.toString());
			try {
				mongoOperation.save(savedUser);
				logger.debug("Update Completed Successfully for Email Subscriptions.");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured while adding user in DB : ", e);
				throw e;
			}
			
		} else {
			throw new SessionNotValidException(ResponseCodes.SESSION_NOT_VALID.message);
		}
	}

	@Override
	public User getUserDetailsViaEmail(User user) {
		return queryUserInDBViaEmail(user.getEmail());
	}

	@Override
	public boolean acceptAccountSharingInvitation(User user, String invityApiKey, String invityEmail) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser != null && savedUser.getEmail().equals(user.getEmail()) && savedUser.getDefaultAPIkey().equals(user.getDefaultAPIkey())) {
			savedUser.setLastmodifiedtime(System.currentTimeMillis());
			savedUser.getAllsavedAPIkeys().put(invityApiKey, invityEmail);
			logger.debug("Adding InityApiKey to the user in users table : " + user.toString());
			try {
				mongoOperation.save(savedUser);
				logger.debug("Update Completed Successfully");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured while adding user in DB : ", e);
				throw e;
			}
		} else {
			throw new SharingInvitationFailedException(ResponseCodes.SHARING_INVITATION_FAILED.message);
		}
	}

	@Override
	public String getEmailAddressListForNotification(User user, String siteName) {
		String resultList = user.getEmail();
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser != null && savedUser.getEmail().equals(user.getEmail()) && savedUser.getDefaultAPIkey().equals(user.getDefaultAPIkey())) {
			if (!StringUtils.isEmpty(savedUser.getEmailSubscriptions().get(siteName))){
				for (String email : savedUser.getEmailSubscriptions().get(siteName)){
					resultList = resultList+UserConstants.EMAIL_LIST_SEPARATOR+email;
				}
			}
		}
		return resultList;
	}

	@Override
	public Map<String,Long> getFeatureListOfAccount(User user) {
		User savedUser = queryUserInDBViaEmail(user.getEmail());
		if (savedUser.getLicense()!=null && savedUser.getLicense().validateLicense(user.getEmail())){
			Map<String, Long> result = new HashMap<String,Long>();
			for (String featureName: savedUser.getLicense().getFeatureList()){
				result.put(featureName, savedUser.getLicense().getExpiryDate());
			}
			return result;
		} else {
			return new HashMap<String,Long>();
		}
		
	}
}
