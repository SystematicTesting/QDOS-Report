/**
 * Copyright (c) Mar 16, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.rest.core.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.systematictesting.daolayer.beans.License;
import com.systematictesting.daolayer.constants.ResponseCodes;
import com.systematictesting.daolayer.constants.UserConstants;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.exceptions.AccountActivationLinkNotValidException;
import com.systematictesting.daolayer.exceptions.DuplicateEmailException;
import com.systematictesting.daolayer.exceptions.EmailNotValidException;
import com.systematictesting.daolayer.exceptions.FileNotFoundException;
import com.systematictesting.daolayer.exceptions.InActiveUserException;
import com.systematictesting.daolayer.exceptions.InternalServerErrorException;
import com.systematictesting.daolayer.exceptions.InvalidActiveAPIException;
import com.systematictesting.daolayer.exceptions.InvalidLicenseDetectedException;
import com.systematictesting.daolayer.exceptions.InvalidPasswordException;
import com.systematictesting.daolayer.exceptions.InvalidUploadPostRequestException;
import com.systematictesting.daolayer.exceptions.LoginFailedException;
import com.systematictesting.daolayer.exceptions.MissingMandatoryAPIParameters;
import com.systematictesting.daolayer.exceptions.NewUserNotActivatedException;
import com.systematictesting.daolayer.exceptions.SendEmailException;
import com.systematictesting.daolayer.exceptions.SessionNotValidException;
import com.systematictesting.daolayer.exceptions.SharingInvitationFailedException;
import com.systematictesting.daolayer.exceptions.SiteNameNotActiveException;
import com.systematictesting.daolayer.exceptions.UserSiteAlreadyPresentException;
import com.systematictesting.daolayer.exceptions.UserSiteNotPresentException;
import com.systematictesting.daolayer.services.UserDao;
import com.systematictesting.rest.core.beans.Response;
import com.systematictesting.rest.core.utils.EncryptionHandler;

public abstract class AbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);
	
	protected User getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(HttpServletRequest request) throws UnsupportedEncodingException {
		String accountIdFromCookie = getValueFromRequestCookies(request, UserConstants.ACCOUNT_ID);
		String accountId = accountIdFromCookie!=null?URLDecoder.decode(accountIdFromCookie,UserConstants.UTF_8_ENCODING):null;
		String sessionId = getValueFromRequestCookies(request, UserConstants.SESSION_ID);
		String defaultAPIKey = getValueFromRequestCookies(request, User.FIELDS.DEFAULT_API_KEY);
		if (logger.isDebugEnabled()){
			logger.debug("REQUESTED ACCOUNT ID = " + accountId);
			logger.debug("REQUESTED SESSION ID = " + sessionId);
			logger.debug("REQUESTED DEFAULT API KEY = " + defaultAPIKey);
		}
		if (StringUtils.isNotBlank(accountId) && StringUtils.isNotBlank(sessionId)){
			String email = EncryptionHandler.decrypt(accountId, sessionId + UserConstants.GENERIC_PASSWORD_KEY);
			logger.debug("DECRYPTED EMAIL on getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey() = " + email);
			User user = new User();
			user.setEmail(email);
			user.setDefaultAPIkey(defaultAPIKey);
			return user;
		} else {
			throw new SessionNotValidException("Invalid Session Detected.");
		}
	}
	
	protected String getValueFromRequestCookies(HttpServletRequest request, String cookieName){
		String value = null;
		if (request.getCookies()!=null){
			for(Cookie cookie : request.getCookies()){
				if (cookie.getName().equals(cookieName)){
					value = cookie.getValue();
					break;
				}
			}
		}
		return value;
	}
	
	protected void setCookiesOnLoginEvent(HttpServletResponse response, User loggedInUser) throws UnsupportedEncodingException {
		Random randomGenerator = new Random();
		int sessionId = randomGenerator.nextInt(1000);
		Cookie userDefaultAPIKey = new Cookie(User.FIELDS.DEFAULT_API_KEY, loggedInUser.getDefaultAPIkey());
		userDefaultAPIKey.setPath(UserConstants.COOKIE_PATH);
		Cookie userActiveAPIkey = new Cookie(User.FIELDS.ACTIVE_API_KEY, loggedInUser.getActiveAPIkey());
		userActiveAPIkey.setPath(UserConstants.COOKIE_PATH);
		Cookie userAllsavedAPIkeys = new Cookie(User.FIELDS.ALL_SAVED_API_KEYS, loggedInUser.getAllsavedAPIkeysForCookie());
		userAllsavedAPIkeys.setPath(UserConstants.COOKIE_PATH);
		Cookie userSessionId = new Cookie(UserConstants.SESSION_ID, sessionId + "");
		userSessionId.setPath(UserConstants.COOKIE_PATH);
		Cookie userSessionSecretKey = new Cookie(UserConstants.ACCOUNT_ID, URLEncoder.encode(EncryptionHandler.encrypt(loggedInUser.getEmail(), sessionId + UserConstants.GENERIC_PASSWORD_KEY),UserConstants.UTF_8_ENCODING));
		userSessionSecretKey.setPath(UserConstants.COOKIE_PATH);
		Cookie userEmail = new Cookie(User.FIELDS.EMAIL, loggedInUser.getEmail());
		userEmail.setPath(UserConstants.COOKIE_PATH);
		
		Cookie licenseCookie = new Cookie(User.FIELDS.LICENSE+License.FIELDS.EXPIRY_DATE_STRING, License.NOT_APPLICABLE);
		if (loggedInUser.getLicense()!=null && loggedInUser.getLicense().getExpiryDateString()!=null && loggedInUser.getLicense().getExpiryDateString().length()!=0){
			licenseCookie = new Cookie(User.FIELDS.LICENSE+License.FIELDS.EXPIRY_DATE_STRING, loggedInUser.getLicense().getExpiryDateString());
			Cookie isEnterpriseLicense = new Cookie(UserConstants.IS_ENTERPRISE_LICENSE, loggedInUser.isLicenseValid()+"");
			isEnterpriseLicense.setPath(UserConstants.COOKIE_PATH);
			response.addCookie(isEnterpriseLicense);
		}
		licenseCookie.setPath(UserConstants.COOKIE_PATH);
		// TODO set the cookies to secure flag = true for encrypted connections
		response.addCookie(userDefaultAPIKey);
		response.addCookie(userActiveAPIkey);
		response.addCookie(userAllsavedAPIkeys);
		response.addCookie(userSessionId);
		response.addCookie(userSessionSecretKey);
		response.addCookie(userEmail);
		response.addCookie(licenseCookie);
	}
	
	protected String generateShareYourReportEncryptedLink(User fromUser, User toUser) throws UnsupportedEncodingException{
		Random randomGenerator = new Random();
		int sessionId = randomGenerator.nextInt(1000);
		String accountId = EncryptionHandler.encrypt(toUser.getEmail(), sessionId + UserConstants.GENERIC_PASSWORD_KEY);
		StringBuilder url = new StringBuilder();
		url.append(UserConstants.INVITATION_TO_SHARE_REPORT_END_POINT).append(UserConstants.REQ_PARAM_PREFIX);		
		url.append(User.FIELDS.DEFAULT_API_KEY).append(UserConstants.VALUE_SEPARATOR_IN_URL).append(toUser.getDefaultAPIkey()).append(UserConstants.PAIR_SEPARATOR_IN_URL);
		url.append(UserConstants.SESSION_ID).append(UserConstants.VALUE_SEPARATOR_IN_URL).append(sessionId).append(UserConstants.PAIR_SEPARATOR_IN_URL);
		url.append(UserConstants.ACCOUNT_ID).append(UserConstants.VALUE_SEPARATOR_IN_URL).append(URLEncoder.encode(accountId, UserConstants.UTF_8_ENCODING)).append(UserConstants.PAIR_SEPARATOR_IN_URL);
		url.append(UserConstants.INVITY_EMAIL).append(UserConstants.VALUE_SEPARATOR_IN_URL).append(URLEncoder.encode(fromUser.getEmail(), UserConstants.UTF_8_ENCODING)).append(UserConstants.PAIR_SEPARATOR_IN_URL);
		url.append(UserConstants.INVITY_API_KEY).append(UserConstants.VALUE_SEPARATOR_IN_URL).append(fromUser.getDefaultAPIkey());
		return url.toString();
	}
	
	protected Response validateEmailAndActiveAPIKey(User user, UserDao userDao) {
		Response customResponse = new Response();
		if (userDao.validateActiveAPIKey(user)) {
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
		} else {
			throw new InvalidActiveAPIException(ResponseCodes.INVALID_ACTIVE_API_KEY.message);
		}
		return customResponse;
	}
	
	protected Object readAFile(ServletContext servletContext, String responseFile) throws Exception {
		InputStream inputStream = null;
        try {
            inputStream = servletContext.getResourceAsStream(responseFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    		StringBuilder sb = new StringBuilder();
    		String line;
    		
    		while((line = bufferedReader.readLine())!= null){
    		    sb.append(line);
    		}
            return sb.toString();
        } finally {
            if (inputStream != null) {
               inputStream.close();
            }
        }
	}

	@ExceptionHandler({ Exception.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.INTERNAL_ERROR.message +" "+ ex.getMessage());
		customResponse.setStatus(ResponseCodes.INTERNAL_ERROR.code);
		logger.debug("Exception Response : " + customResponse);
		logger.error("Exception",ex);
		return customResponse;
	}
	
	@ExceptionHandler({ SessionNotValidException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleSessionNotValidException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.SESSION_NOT_VALID.message);
		customResponse.setStatus(ResponseCodes.SESSION_NOT_VALID.code);
		logger.debug("SessionNotValidException Response : " + customResponse);
		logger.error("SessionNotValidException",ex);
		return customResponse;
	}
	
	@ExceptionHandler({ EmailNotValidException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleEmailNotValidException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.EMAIL_NOT_VALID.message);
		customResponse.setStatus(ResponseCodes.EMAIL_NOT_VALID.code);
		logger.debug("EmailNotValidException Response : " + customResponse);
		logger.error("EmailNotValidException",ex);
		return customResponse;
	}
	
	@ExceptionHandler({ InvalidPasswordException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleInvalidPasswordException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.INVALID_PASSWORD.message);
		customResponse.setStatus(ResponseCodes.INVALID_PASSWORD.code);
		logger.debug("InvalidPasswordException Response : " + customResponse);
		logger.error("InvalidPasswordException",ex);
		return customResponse;
	}

	@ExceptionHandler({ InvalidActiveAPIException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleInvalidActiveAPIException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.INVALID_ACTIVE_API_KEY.message);
		customResponse.setStatus(ResponseCodes.INVALID_ACTIVE_API_KEY.code);
		logger.debug("InvalidActiveAPIException Response : " + customResponse);
		logger.error("InvalidActiveAPIException",ex);
		return customResponse;
	}

	@ExceptionHandler({ AccountActivationLinkNotValidException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleAccountActivationLinkNotValidException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.ACCOUNT_ACTIVATION_LINK_INVALID.message);
		customResponse.setStatus(ResponseCodes.ACCOUNT_ACTIVATION_LINK_INVALID.code);
		logger.debug("AccountActivationLinkNotValidException Response : " + customResponse);
		logger.error("AccountActivationLinkNotValidException",ex);
		return customResponse;
	}
	
	@ExceptionHandler({ SharingInvitationFailedException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleSharingInvitationFailedException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.SHARING_INVITATION_FAILED.message);
		customResponse.setStatus(ResponseCodes.SHARING_INVITATION_FAILED.code);
		logger.debug("SharingInvitationFailedException Response : " + customResponse);
		logger.error("SharingInvitationFailedException",ex);
		return customResponse;
	}

	@ExceptionHandler({ com.mongodb.MongoException.class, org.springframework.dao.DataAccessResourceFailureException.class, java.net.ConnectException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleMongoException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.DB_CONNECTION_FAILURE.message);
		customResponse.setStatus(ResponseCodes.DB_CONNECTION_FAILURE.code);
		logger.debug("MongoException Response : " + customResponse);
		logger.error("MongoException",ex);
		return customResponse;
	}

	@ExceptionHandler({ InternalServerErrorException.class, SendEmailException.class, UnsupportedEncodingException.class, JSONException.class, ParseException.class, IOException.class})
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleInternalServerErrorException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.INTERNAL_ERROR.message);
		customResponse.setStatus(ResponseCodes.INTERNAL_ERROR.code);
		logger.debug("InternalServerErrorException Response : " + customResponse);
		logger.error("InternalServerErrorException",ex);
		return customResponse;
	}

	@ExceptionHandler({ NewUserNotActivatedException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleNewUserNotActivatedException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.NEW_USER_DETECTED.message);
		customResponse.setStatus(ResponseCodes.NEW_USER_DETECTED.code);
		logger.debug("NewUserNotActivatedException Response : " + customResponse);
		logger.error("NewUserNotActivatedException",ex);
		return customResponse;
	}
	
	@ExceptionHandler({ InvalidUploadPostRequestException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleInvalidUploadPostRequestException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.INVALID_UPLOAD_POST_REQUEST.message);
		customResponse.setStatus(ResponseCodes.INVALID_UPLOAD_POST_REQUEST.code);
		logger.debug("InvalidUploadPostRequestException Response : " + customResponse);
		logger.error("InvalidUploadPostRequestException",ex);
		return customResponse;
	}

	@ExceptionHandler({ InActiveUserException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleInActiveUserException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.USER_DEACTIVATED.message);
		customResponse.setStatus(ResponseCodes.USER_DEACTIVATED.code);
		logger.debug("InActiveUserException Response : " + customResponse);
		logger.error("InActiveUserException",ex);
		return customResponse;
	}

	@ExceptionHandler({ LoginFailedException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	Object handleLoginFailedException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.LOGIN_FAILED.message);
		customResponse.setStatus(ResponseCodes.LOGIN_FAILED.code);
		logger.debug("LoginFailedException Response : " + customResponse);
		logger.error("LoginFailedException",ex);
		return customResponse;
	}

	@ExceptionHandler({ DuplicateEmailException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	Object handleDuplicateEmailException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.DUPLICATE_EMAIL.message);
		customResponse.setStatus(ResponseCodes.DUPLICATE_EMAIL.code);
		logger.debug("DuplicateEmailException Response : " + customResponse);
		logger.error("DuplicateEmailException",ex);
		return customResponse;
	}
	
	@ExceptionHandler({ SiteNameNotActiveException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	Object handleSiteNameNotActiveException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.SITE_NAME_NOT_ACTIVE.message);
		customResponse.setStatus(ResponseCodes.SITE_NAME_NOT_ACTIVE.code);
		logger.debug("handleSiteNameNotActiveException Response : " + customResponse);
		logger.error("handleSiteNameNotActiveException",ex);
		return customResponse;
	}
	
	@ExceptionHandler({ UserSiteNotPresentException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	Object handleUserSiteNotPresentException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.USER_SITE_NOT_PRESENT.message);
		customResponse.setStatus(ResponseCodes.USER_SITE_NOT_PRESENT.code);
		logger.debug("handleUserSiteNotPresentException Response : " + customResponse);
		logger.error("handleUserSiteNotPresentException",ex);
		return customResponse;
	}
	
	@ExceptionHandler({ UserSiteAlreadyPresentException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	Object handleUserSiteAlreadyPresentException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.USER_SITE_ALREADY_PRESENT.message);
		customResponse.setStatus(ResponseCodes.USER_SITE_ALREADY_PRESENT.code);
		logger.debug("handleUserSiteAlreadyPresentException Response : " + customResponse);
		logger.error("handleUserSiteAlreadyPresentException",ex);
		return customResponse;
	}
	
	@ExceptionHandler({ MissingMandatoryAPIParameters.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	Object handleMissingMandatoryAPIParameters(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.MISSING_MANDATORY_API_PARAMS.message);
		customResponse.setStatus(ResponseCodes.MISSING_MANDATORY_API_PARAMS.code);
		logger.debug("handleMissingMandatoryAPIParameters Response : " + customResponse);
		logger.error("handleMissingMandatoryAPIParameters",ex);
		return customResponse;
	}
	
	@ExceptionHandler({ InvalidLicenseDetectedException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	Object handleInvalidLicenseDetectedException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.LICENSE_NOT_VALID.message);
		customResponse.setStatus(ResponseCodes.LICENSE_NOT_VALID.code);
		logger.debug("handleInvalidLicenseDetectedException Response : " + customResponse);
		logger.error("handleInvalidLicenseDetectedException",ex);
		return customResponse;
	}
	
	@ExceptionHandler({ FileNotFoundException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody
	Object handleFileNotFoundException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ResponseCodes.FILE_NOT_FOUND.message);
		customResponse.setStatus(ResponseCodes.FILE_NOT_FOUND.code);
		logger.debug("handleFileNotFoundException Response : " + customResponse);
		logger.error("handleFileNotFoundException",ex);
		return customResponse;
	}
	
	
}
