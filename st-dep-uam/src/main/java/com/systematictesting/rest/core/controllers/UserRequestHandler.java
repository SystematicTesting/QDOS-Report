/**
 * Copyright (c) Mar 7, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.rest.core.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.systematictesting.daolayer.beans.License;
import com.systematictesting.daolayer.constants.ResponseCodes;
import com.systematictesting.daolayer.constants.UserConstants;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.exceptions.InvalidLicenseDetectedException;
import com.systematictesting.daolayer.exceptions.LoginFailedException;
import com.systematictesting.daolayer.exceptions.SessionNotValidException;
import com.systematictesting.daolayer.services.UserDao;
import com.systematictesting.rest.core.beans.Response;
import com.systematictesting.rest.core.utils.EncryptionHandler;
import com.systematictesting.services.core.EmailService;

@Controller
@RequestMapping("/user")
public class UserRequestHandler extends AbstractController{

	private static final Logger logger = LoggerFactory.getLogger(UserRequestHandler.class);
	

	@Autowired
	private UserDao userDao;

	@Autowired
	private EmailService emailService;

	@RequestMapping(value = "/getUserDetails.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object getUserDetails(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		logger.debug("HANDLING /getUserDetails.rest REQUEST : " + user);
		User validUser = userDao.getUserDetailsViaEmailAndDefaultApiKey(user);
		if (validUser!=null){
			logger.debug("RESPONSE /getUserDetails.rest : " + validUser);
			return validUser;
		} else {
			throw new SessionNotValidException(ResponseCodes.SESSION_NOT_VALID.message);
		}
	}
	
	
	@RequestMapping(value = "/shareYourSiteReportOnEmail.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object shareYourSiteReportOnEmail(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		logger.debug("HANDLING /shareYourSiteReportOnEmail.rest REQUEST : " + user);
		
		int index=0;
		while(true){
			String siteName = request.getParameter(UserConstants.PREFIX_SITEPARAM+index+UserConstants.POSTFIX_SITENAME);
			if (StringUtils.isEmpty(siteName)){
				break;
			} else {
				String emailList = request.getParameter(UserConstants.PREFIX_SITEPARAM+index+UserConstants.POSTFIX_EMAILLIST);
				String[] arrayOfEmailList = emailList.split(UserConstants.EMAIL_LIST_SEPARATOR);
				List<String> listOfEmails = new ArrayList<String>();
				for (String encodedEmailAddress : arrayOfEmailList){
					String decodedEmailAddress = URLDecoder.decode(encodedEmailAddress,UserConstants.UTF_8_ENCODING);
					listOfEmails.add(decodedEmailAddress.trim());
				}
				user.getEmailSubscriptions().put(siteName, listOfEmails);
			}
			index++;
		}
		logger.debug("PRINTING /shareYourSiteReportOnEmail.rest REQUEST : " + user);
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.INTERNAL_ERROR.message);
		customResponse.setStatus(ResponseCodes.INTERNAL_ERROR.code);
		if (userDao.updateEmailSubscriptions(user)) {
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
		}
		logger.debug("RESPONSE /shareYourSiteReportOnEmail.rest : " + customResponse);
		return customResponse;
	}
	
	
	@RequestMapping(value = "/shareYourReport.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object shareYourReport(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User requestedFromUser = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		logger.debug("HANDLING /shareYourReport.rest REQUEST : " + requestedFromUser);
		
		String targetEmailAddressToShare = request.getParameter(User.FIELDS.EMAIL);
		User requestedToUser = new User();
		requestedToUser.setEmail(targetEmailAddressToShare);
		
		User validFromUser = userDao.getUserDetailsViaEmailAndDefaultApiKey(requestedFromUser);
		User validToUser = userDao.getUserDetailsViaEmail(requestedToUser);
		
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.INTERNAL_ERROR.message);
		customResponse.setStatus(ResponseCodes.INTERNAL_ERROR.code);
		if (emailService.sendShareYourReportEmail(validFromUser, validToUser,generateShareYourReportEncryptedLink(validFromUser, validToUser))){
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
		}
		logger.debug("RESPONSE /shareYourReport.rest : " + customResponse);
		return customResponse;
	
	}
	
	@RequestMapping(value = "/updateMyProfile.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object updateUserProfile(User user, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		logger.debug("HANDLING /user/updateMyProfile.rest REQUEST : " + user);
		User userFromApiKey = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		
		user.setEmail(userFromApiKey.getEmail());
		user.setDefaultAPIkey(userFromApiKey.getDefaultAPIkey());
		
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.INTERNAL_ERROR.message);
		customResponse.setStatus(ResponseCodes.INTERNAL_ERROR.code);
		if (userDao.updateAccount(user)) {
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
			emailService.sendUpdateProfileConfirmationEmail(user);
		}
		logger.debug("RESPONSE /user/updateMyProfile.rest : " + customResponse);
		return customResponse;
	}
	
	@RequestMapping(value = "/updateLicense.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object updateLicense(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ParseException {
		logger.debug("HANDLING /user/updateMyProfile.rest");
		User userFromApiKey = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		String licenseKey = request.getParameter(License.FIELDS.LICENSE_KEY);
		String expiryDateString = request.getParameter(License.FIELDS.EXPIRY_DATE_STRING);
		String featureListString = request.getParameter(License.FIELDS.FEATURE_LIST);
		License license = new License();
		license.setExpiryDateString(expiryDateString);
		license.setLicenseKey(licenseKey);
		String[] featureListArray = featureListString.split(License.DELIMITER);
		for (String feature : featureListArray){
			license.getFeatureList().add(feature.trim());
		}
		DateFormat format = new SimpleDateFormat(License.EXPIRY_DATE_FORMAT);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date expiryDate = format.parse(expiryDateString);
		logger.debug("HANDLING /user/updateMyProfile.rest REQUEST : " + userFromApiKey);
		logger.debug("HANDLING /user/updateMyProfile.rest LICENSE OBJECT : " + license);
		
		license.setExpiryDate(expiryDate.getTime());
		if (license.validateLicense(userFromApiKey.getEmail())){
			userFromApiKey.setLicense(license);
			Response customResponse = new Response();
			if (userDao.updateLicense(userFromApiKey)) {
				customResponse.setMessage(ResponseCodes.SUCCESS.message);
				customResponse.setStatus(ResponseCodes.SUCCESS.code);
			}
			logger.debug("RESPONSE /user/updateMyProfile.rest : " + customResponse);
			return customResponse;
		} else {
			throw new InvalidLicenseDetectedException(ResponseCodes.LICENSE_NOT_VALID.message);
		}
	}
	
	@RequestMapping(value = "/login.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object loginUser(User user, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		logger.debug("HANDLING /user/login.rest REQUEST : " + user);
		User loggedInUser = userDao.loginUser(user);
		Response customResponse = new Response();
		if (loggedInUser != null) {
			setCookiesOnLoginEvent(response, loggedInUser);
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
			logger.debug("RESPONSE /user/login.rest : LOGGED IN USER : " + loggedInUser.getEmail());
			logger.debug("RESPONSE /user/login.rest : " + customResponse);
			return customResponse;
		} else {
			throw new LoginFailedException(ResponseCodes.LOGIN_FAILED.message);
		}
	}

	@RequestMapping(value = "/sendPushEmailNotification.rest-internal", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object sendPushEmailNotification(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String defaultAPIKey = request.getParameter(User.FIELDS.DEFAULT_API_KEY);
		String email = URLDecoder.decode(request.getParameter(User.FIELDS.EMAIL),UserConstants.UTF_8_ENCODING);
		String siteName = URLDecoder.decode(request.getParameter(UserConstants.SITE_NAME),UserConstants.UTF_8_ENCODING);
		String versionNumber = URLDecoder.decode(request.getParameter(UserConstants.VERSION_NUMBER),UserConstants.UTF_8_ENCODING);
		String jsonTestReportSummary = URLDecoder.decode(request.getParameter(UserConstants.JSON_SUMMARY_OF_TEST_REPORT),UserConstants.UTF_8_ENCODING);
		if (logger.isDebugEnabled()) {
			logger.debug("REQUESTED EMAIL = " + email);
			logger.debug("REQUESTED API KEY = " + defaultAPIKey);
			logger.debug("REQUESTED SITE NAME = "+siteName);
			logger.debug("REQUESTED VERSION NUMBER = "+versionNumber);
			logger.debug("REQUESTED JSON TEST SUITE REPORT SUMMARY = "+jsonTestReportSummary);
		}
		User user = new User();
		user.setEmail(email);
		user.setDefaultAPIkey(defaultAPIKey);
		logger.debug("HANDLING /sendPushEmailNotification.rest-internal REQUEST : " + user);
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.INTERNAL_ERROR.message);
		customResponse.setStatus(ResponseCodes.INTERNAL_ERROR.code);
		String emailAddressList = userDao.getEmailAddressListForNotification(user, siteName);
		if (!StringUtils.isEmpty(emailAddressList)){
			boolean result = emailService.sendNotificationEmails(user, emailAddressList, siteName,versionNumber, jsonTestReportSummary);
			if (result){
				customResponse.setMessage(ResponseCodes.SUCCESS.message);
				customResponse.setStatus(ResponseCodes.SUCCESS.code);
			}
		} else {
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
		}
		logger.debug("RESPONSE /sendPushEmailNotification.rest-internal : " + customResponse);
		return customResponse;
	}
	
	
	@RequestMapping(value = "/validateAPIKey.rest-internal", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object validateAPIKey(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String accountId = URLDecoder.decode(request.getParameter(UserConstants.ACCOUNT_ID),UserConstants.UTF_8_ENCODING);
		String sessionId = request.getParameter(UserConstants.SESSION_ID);
		String activeAPIKey = request.getParameter(User.FIELDS.ACTIVE_API_KEY);
		String email = EncryptionHandler.decrypt(accountId, sessionId + UserConstants.GENERIC_PASSWORD_KEY);
		if (logger.isDebugEnabled()) {
			logger.debug("DECRYPTED EMAIL = " + email);
			logger.debug("REQUESTED ACCOUNT ID = " + accountId);
			logger.debug("REQUESTED SESSION ID = " + sessionId);
			logger.debug("REQUESTED API KEY = " + activeAPIKey);
		}
		User user = new User();
		user.setEmail(email);
		user.setActiveAPIkey(activeAPIKey);
		logger.debug("HANDLING /validateAPIKey.rest-internal REQUEST : " + user);
		Response customResponse = validateEmailAndActiveAPIKey(user, userDao);
		logger.debug("RESPONSE /validateAPIKey.rest-internal : " + customResponse);
		return customResponse;
	}

	@RequestMapping(value = "/validateEmailAndAPIKey.rest-internal", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object validateEmailAndAPIKey(HttpServletRequest request, HttpServletResponse response) {
		String email = request.getParameter(User.FIELDS.EMAIL);
		String activeAPIKey = request.getParameter(User.FIELDS.ACTIVE_API_KEY);
		if (logger.isDebugEnabled()) {
			logger.debug("EMAIL = " + email);
			logger.debug("REQUESTED API KEY = " + activeAPIKey);
		}
		User user = new User();
		user.setEmail(email);
		user.setActiveAPIkey(activeAPIKey);
		logger.debug("HANDLING /validateEmailAndAPIKey.rest-internal REQUEST : " + user);
		Response customResponse = validateEmailAndActiveAPIKey(user, userDao);
		logger.debug("RESPONSE /validateEmailAndAPIKey.rest-internal : " + customResponse);
		return customResponse;
	}
	
	@RequestMapping(value = "/create.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object createUser(User user, HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String licenseKey = request.getParameter(License.FIELDS.LICENSE_KEY);
		String expiryDateString = request.getParameter(License.FIELDS.EXPIRY_DATE_STRING);
		String featureListString = request.getParameter(License.FIELDS.FEATURE_LIST);
		License license = null;
		if (licenseKey!=null && licenseKey.length()>0 && expiryDateString!=null && expiryDateString.length()>0 && featureListString!=null && featureListString.length()>0){
			license = new License();
			license.setExpiryDateString(expiryDateString);
			license.setLicenseKey(licenseKey);
			String[] featureListArray = featureListString.split(License.DELIMITER);
			for (String feature : featureListArray){
				license.getFeatureList().add(feature.trim());
			}
			logger.debug("HANDLING /user/create.rest LICENSE OBJECT : " + license);
			DateFormat format = new SimpleDateFormat(License.EXPIRY_DATE_FORMAT);
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date expiryDate = format.parse(expiryDateString);
			license.setExpiryDate(expiryDate.getTime());
		}
		logger.debug("HANDLING /user/create.rest REQUEST : " + user);
		if (license==null){
			return registerUser(user);
		} else if (license!=null && license.validateLicense(user.getEmail())){
			user.setLicense(license);
			return registerUser(user);
		} else {
			throw new InvalidLicenseDetectedException(ResponseCodes.LICENSE_NOT_VALID.message);
			
		}
		
	}
	
	private Object registerUser(User user) {
		Response customResponse = new Response();
		if (userDao.addUser(user)) {
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
			emailService.sendAccountActivationEmail(user);
			logger.debug("RESPONSE /user/create.rest : " + customResponse);
			return customResponse;
		} else {
			customResponse.setMessage(ResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(ResponseCodes.INTERNAL_ERROR.code);
			logger.debug("RESPONSE /user/create.rest : " + customResponse);
			return customResponse;
		}
	}

	@RequestMapping(value = "/activateAccount.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object activateUser(User user, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("HANDLING /user/activateAccount.rest REQUEST : " + user);
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.INTERNAL_ERROR.message);
		customResponse.setStatus(ResponseCodes.INTERNAL_ERROR.code);
		if (userDao.activateAccount(user)) {
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
			emailService.sendPostAccountActivationEmail(user);
		}
		logger.debug("RESPONSE /user/activateAccount.rest : " + customResponse);
		return customResponse;
	}
	
	@RequestMapping(value = "/forgotPassword.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object forgotPassword(User user, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("HANDLING /user/activateAccount.rest REQUEST : " + user);
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.INTERNAL_ERROR.message);
		customResponse.setStatus(ResponseCodes.INTERNAL_ERROR.code);
		if (userDao.resetPassword(user)) {
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
			emailService.sendResetPasswordEmail(user);
			
		}
		logger.debug("RESPONSE /user/activateAccount.rest : " + customResponse);
		return customResponse;
	}
	
	@RequestMapping(value = "/updateReportSharingInvitation.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object updateReportSharingInvitation(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		String invityDefaultApiKey = request.getParameter(UserConstants.INVITY_API_KEY);
		String invityEmail = request.getParameter(UserConstants.INVITY_EMAIL);
		logger.debug("HANDLING /user/updateReportSharingInvitation.rest REQUEST : " + user);
		Response customResponse = new Response();
		if (userDao.acceptAccountSharingInvitation(user, invityDefaultApiKey, invityEmail)) {
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
		}
		logger.debug("RESPONSE /user/updateReportSharingInvitation.rest : " + customResponse);
		return customResponse;
	}
	
	
	@RequestMapping(value = "/changePassword.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object changePasswordUser(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		String oldPassword = request.getParameter(UserConstants.OLD_PASSWORD);
		String newPassword = request.getParameter(UserConstants.NEW_PASSWORD);
		if (logger.isDebugEnabled()) {
			logger.debug("OLD PASSWORD = " + oldPassword);
			logger.debug("NEW PASSWORD = " + newPassword);
		}
		logger.debug("HANDLING /user/changePassword.rest REQUEST : " + user);
		Response customResponse = new Response();
		if (userDao.changePassword(user,oldPassword, newPassword)) {
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
			emailService.sendPasswordChangeEmail(user);
		}
		logger.debug("RESPONSE /user/changePassword.rest : " + customResponse);
		return customResponse;
	}
}
