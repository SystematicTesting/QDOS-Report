/**
 * Copyright (c) Mar 10, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.services.core.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.systematictesting.daolayer.constants.UserConstants;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.exceptions.SendEmailException;
import com.systematictesting.rest.core.beans.EmailTemplate;
import com.systematictesting.rest.core.beans.GraphTemplate;
import com.systematictesting.services.core.TemplateService;

@Repository(TemplateService.SERVICE_NAME)
public class TemplateServiceImpl implements TemplateService {

	private static final Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);

	private Properties emailTemplateProperties;

	public Properties getEmailTemplateProperties() {
		return emailTemplateProperties;
	}

	public void setEmailTemplateProperties(Properties emailTemplateProperties) {
		this.emailTemplateProperties = emailTemplateProperties;
	}

	@Override
	public EmailTemplate getAccountActivationEmailTemplate(User user) {
		logger.debug("Fetching ACTIVATION EMAIL Template...");
		String subject = getEmailTemplateProperties().getProperty(ACTIVATION_EMAIL_TEMPLATE_SUBJECT);
		String standardTemplate = getEmailTemplateProperties().getProperty(STANDARD_EMAIL_TEMPLATE);
		
		String emailTitle = getEmailTemplateProperties().getProperty(ACTIVATION_EMAIL_TEMPLATE_EMAIL_TITLE);
		String emailBody = getEmailTemplateProperties().getProperty(ACTIVATION_EMAIL_TEMPLATE_EMAIL_BODY);
		String emailEnd = getEmailTemplateProperties().getProperty(ACTIVATION_EMAIL_TEMPLATE_EMAIL_END);
		
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_TITLE, emailTitle);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_BODY, emailBody);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_END, emailEnd);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_USER, user.getFirstname()+" "+user.getLastname());
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LABEL, "Activate Account");
		
		String websiteDomain = getEmailTemplateProperties().getProperty(WEBSITE_DOMAIN);
		if (subject != null) {
			
			StringBuilder accountActivationURL = new StringBuilder(websiteDomain);
			accountActivationURL.append(UserConstants.ACCOUNT_ACTIVATION_END_POINT);
			accountActivationURL.append(UserConstants.REQ_PARAM_PREFIX).append(User.FIELDS.EMAIL).append(UserConstants.VALUE_SEPARATOR_IN_URL);
			try {
				accountActivationURL.append(URLEncoder.encode(user.getEmail(), UserConstants.UTF_8_ENCODING));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new SendEmailException(e);
			}
			accountActivationURL.append(UserConstants.PAIR_SEPARATOR_IN_URL).append(User.FIELDS.ACTIVE_API_KEY).append(UserConstants.VALUE_SEPARATOR_IN_URL);
			accountActivationURL.append(user.getActiveAPIkey());
			
			standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LINK, accountActivationURL.toString());
			
			EmailTemplate result = new EmailTemplate(subject, standardTemplate);
			logger.debug("Sending ACTIVATION EMAIL Template : " + result);
			return result;
		} else {
			return null;
		}
	}

	@Override
	public EmailTemplate getPostAccountActivationEmailTemplate(User user) {
		logger.debug("Fetching POST ACTIVATION EMAIL Template...");
		String subject = getEmailTemplateProperties().getProperty(POST_ACTIVATION_EMAIL_TEMPLATE_SUBJECT);
		String standardTemplate = getEmailTemplateProperties().getProperty(STANDARD_EMAIL_TEMPLATE);
		String wikiWebsiteDomain = getEmailTemplateProperties().getProperty(WIKI_WEBSITE_DOMAIN);
		
		String emailTitle = getEmailTemplateProperties().getProperty(POST_ACTIVATION_EMAIL_TEMPLATE_EMAIL_TITLE);
		String emailBody = getEmailTemplateProperties().getProperty(POST_ACTIVATION_EMAIL_TEMPLATE_EMAIL_BODY);
		String emailEnd = getEmailTemplateProperties().getProperty(POST_ACTIVATION_EMAIL_TEMPLATE_EMAIL_END);
		
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_TITLE, emailTitle);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_BODY, emailBody);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_END, emailEnd);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_USER, user.getFirstname()+" "+user.getLastname());
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LABEL, "Documentation Link");
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LINK, wikiWebsiteDomain);
		
		if (subject != null) {
			EmailTemplate result = new EmailTemplate(subject, standardTemplate);
			logger.debug("Sending POST ACTIVATION EMAIL Template : " + result);
			return result;
		} else {
			return null;
		}
	}

	@Override
	public EmailTemplate getPasswordChangedEmailTemplate(User user) {
		logger.debug("Fetching PASSWORD CHANGED EMAIL Template...");
		String subject = getEmailTemplateProperties().getProperty(PASSWORD_CHANGE_EMAIL_TEMPLATE_SUBJECT);
		String standardTemplate = getEmailTemplateProperties().getProperty(STANDARD_EMAIL_TEMPLATE);
		String websiteDomain = getEmailTemplateProperties().getProperty(WEBSITE_DOMAIN);
		
		String emailTitle = getEmailTemplateProperties().getProperty(PASSWORD_CHANGE_EMAIL_TEMPLATE_EMAIL_TITLE);
		String emailBody = getEmailTemplateProperties().getProperty(PASSWORD_CHANGE_EMAIL_TEMPLATE_EMAIL_BODY);
		String emailEnd = getEmailTemplateProperties().getProperty(PASSWORD_CHANGE_EMAIL_TEMPLATE_EMAIL_END);
		
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_TITLE, emailTitle);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_BODY, emailBody);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_END, emailEnd);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_USER, user.getFirstname()+" "+user.getLastname());
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LABEL, "My Account");
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LINK, websiteDomain);
		
		if (subject != null) {
			EmailTemplate result = new EmailTemplate(subject, standardTemplate);
			logger.debug("Sending PASSWORD CHANGED EMAIL Template : " + result);
			return result;
		} else {
			return null;
		}
	}

	@Override
	public EmailTemplate getProfileUpdateEmailTemplate(User user) {
		logger.debug("Fetching PROFILE UPDATE EMAIL Template...");
		String subject = getEmailTemplateProperties().getProperty(PROFILE_UPDATE_EMAIL_TEMPLATE_SUBJECT);
		String standardTemplate = getEmailTemplateProperties().getProperty(STANDARD_EMAIL_TEMPLATE);
		String websiteDomain = getEmailTemplateProperties().getProperty(WEBSITE_DOMAIN);
		
		String emailTitle = getEmailTemplateProperties().getProperty(PROFILE_UPDATE_EMAIL_TEMPLATE_EMAIL_TITLE);
		String emailBody = getEmailTemplateProperties().getProperty(PROFILE_UPDATE_EMAIL_TEMPLATE_EMAIL_BODY);
		String emailEnd = getEmailTemplateProperties().getProperty(PROFILE_UPDATE_EMAIL_TEMPLATE_EMAIL_END);
		
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_TITLE, emailTitle);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_BODY, emailBody);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_END, emailEnd);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_USER, user.getFirstname()+" "+user.getLastname());
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LABEL, "My Account");
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LINK, websiteDomain);
		
		if (subject != null) {
			EmailTemplate result = new EmailTemplate(subject, standardTemplate);
			logger.debug("Sending PROFILE UPDATE EMAIL Template : " + result);
			return result;
		} else {
			return null;
		}
	}

	@Override
	public EmailTemplate getShareYourReportEmailTemplate(User fromUser, User toUser, String encryptedLink) {
		logger.debug("Fetching SHARE YOUR REPORT EMAIL Template...");
		String subject = getEmailTemplateProperties().getProperty(SHARE_YOUR_REPORT_EMAIL_TEMPLATE_SUBJECT);
		String standardTemplate = getEmailTemplateProperties().getProperty(STANDARD_EMAIL_TEMPLATE);
		String websiteDomain = getEmailTemplateProperties().getProperty(WEBSITE_DOMAIN);
		
		String emailTitle = getEmailTemplateProperties().getProperty(SHARE_YOUR_REPORT_EMAIL_TEMPLATE_EMAIL_TITLE);
		String emailBody = getEmailTemplateProperties().getProperty(SHARE_YOUR_REPORT_EMAIL_TEMPLATE_EMAIL_BODY);
		emailBody = StringUtils.replace(emailBody, KEYWORD_INVITY, fromUser.getFirstname()+" "+fromUser.getLastname());
		String emailEnd = getEmailTemplateProperties().getProperty(SHARE_YOUR_REPORT_EMAIL_TEMPLATE_EMAIL_END);
		
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_TITLE, emailTitle);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_BODY, emailBody);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_END, emailEnd);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_USER, toUser.getFirstname()+" "+toUser.getLastname());
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LABEL, "Accept Invitation");
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LINK, websiteDomain+encryptedLink);
		
		if (subject != null) {
			EmailTemplate result = new EmailTemplate(subject, standardTemplate);
			logger.debug("Sending SHARE YOUR REPORT EMAIL Template : " + result);
			return result;
		} else {
			return null;
		}
	}

	@Override
	public EmailTemplate getSendYourTestReportOnEmailTemplate(String siteName, String versionNumber, JsonArray jsonArrayOfTestSuiteReports) {
		logger.debug("Fetching SEND YOUR REPORT ON EMAIL Template...");
		String subject = getEmailTemplateProperties().getProperty(SEND_YOUR_TEST_REPORT_ON_EMAIL_TEMPLATE_SUBJECT);
		String body = getEmailTemplateProperties().getProperty(SEND_YOUR_TEST_REPORT_ON_EMAIL_TEMPLATE_BODY);
		
		StringBuilder fullGraphTemplate = new StringBuilder();
		for (int index=0;index<jsonArrayOfTestSuiteReports.size();index++){
			JsonObject jsonSingleTestReport = jsonArrayOfTestSuiteReports.get(index).getAsJsonObject();
			GraphTemplate graphTemplateBean = new GraphTemplate();
			graphTemplateBean.setDuration(jsonSingleTestReport.get("duration").getAsString());
			graphTemplateBean.setEndTime(jsonSingleTestReport.get("endTime").getAsString());
			graphTemplateBean.setEnvironment(jsonSingleTestReport.get("environment").getAsString());
			graphTemplateBean.setStartTime(jsonSingleTestReport.get("startTime").getAsString());
			graphTemplateBean.setGraphUrl(jsonSingleTestReport.get("testSuiteName").getAsString(), getValuesFromJsonArray(jsonSingleTestReport.get("summary").getAsJsonArray(),"PASS"), getValuesFromJsonArray(jsonSingleTestReport.get("summary").getAsJsonArray(),"FAIL"), getValuesFromJsonArray(jsonSingleTestReport.get("summary").getAsJsonArray(),"MANUAL"), getValuesFromJsonArray(jsonSingleTestReport.get("summary").getAsJsonArray(),"ABORTED"));
			
			String graphTemplateString = getEmailTemplateProperties().getProperty(SEND_YOUR_TEST_REPORT_ON_EMAIL_TEMPLATE_GRAPHTEMPLATE);
			graphTemplateString = StringUtils.replace(graphTemplateString, KEYWORD_TEST_SUITE_REPORT_GRAPH_URL, graphTemplateBean.getGraphUrl());
			graphTemplateString = StringUtils.replace(graphTemplateString, KEYWORD_START_TIME, graphTemplateBean.getStartTime());
			graphTemplateString = StringUtils.replace(graphTemplateString, KEYWORD_END_TIME, graphTemplateBean.getEndTime());
			graphTemplateString = StringUtils.replace(graphTemplateString, KEYWORD_DURATION, graphTemplateBean.getDuration());
			graphTemplateString = StringUtils.replace(graphTemplateString, KEYWORD_ENVIRONMENT, graphTemplateBean.getEnvironment());
			
			fullGraphTemplate.append(graphTemplateString);
		}
		
		body = StringUtils.replace(body, KEYWORD_SITE_ID, siteName);
		body = StringUtils.replace(body, KEYWORD_VERSION_NUMBER, versionNumber);
		body = StringUtils.replace(body, KEYWORD_LIST_OF_TEST_SUITES_GRAPHS, fullGraphTemplate.toString());
		
		if (subject != null) {
			EmailTemplate result = new EmailTemplate(subject, body);
			logger.debug("Sending SEND YOUR REPORT ON EMAIL Template : " + result);
			return result;
		} else {
			return null;
		}
	}
	
	private String getValuesFromJsonArray(JsonArray jsonArray, String key){
		String result = null;
		for (int index=0;index<jsonArray.size();index++){
			JsonArray singleArrayElement = jsonArray.get(index).getAsJsonArray();
			if (singleArrayElement.get(0).getAsString().equals(key)){
				result = singleArrayElement.get(1).getAsString();
				break;
			}
		}
		return result;
	}

	@Override
	public EmailTemplate getResetPasswordEmailTemplate(User user) {
		logger.debug("Fetching PASSWORD RESET EMAIL Template...");
		String subject = getEmailTemplateProperties().getProperty(PASSWORD_RESET_EMAIL_TEMPLATE_SUBJECT);
		String standardTemplate = getEmailTemplateProperties().getProperty(STANDARD_EMAIL_TEMPLATE);
		String websiteDomain = getEmailTemplateProperties().getProperty(WEBSITE_DOMAIN);
		
		String emailTitle = getEmailTemplateProperties().getProperty(PASSWORD_RESET_EMAIL_TEMPLATE_EMAIL_TITLE);
		String emailBody = getEmailTemplateProperties().getProperty(PASSWORD_RESET_EMAIL_TEMPLATE_EMAIL_BODY);
		emailBody = StringUtils.replace(emailBody, KEYWORD_TEMPORARY_PASSWORD, user.getPassword());
		String emailEnd = getEmailTemplateProperties().getProperty(PASSWORD_RESET_EMAIL_TEMPLATE_EMAIL_END);
		
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_TITLE, emailTitle);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_BODY, emailBody);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_EMAIL_END, emailEnd);
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_USER, user.getFirstname()+" "+user.getLastname());
		standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LABEL, "Reset Password");
		if (websiteDomain.endsWith("/")){
			standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LINK, websiteDomain+"#/ChangePassword");
		} else {
			standardTemplate = StringUtils.replace(standardTemplate, KEYWORD_BUTTON_LINK, websiteDomain+"/#/ChangePassword");
		}
		if (subject != null) {
			EmailTemplate result = new EmailTemplate(subject, standardTemplate);
			logger.debug("Sending PASSWORD RESET EMAIL Template : " + result);
			return result;
		} else {
			return null;
		}
	}

}
