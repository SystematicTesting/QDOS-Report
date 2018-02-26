/**
 * Copyright (c) Mar 10, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.services.core;

import com.google.gson.JsonArray;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.rest.core.beans.EmailTemplate;

public interface TemplateService {
	String SERVICE_NAME = "TemplateService";
	
	String ACTIVATION_EMAIL_TEMPLATE_SUBJECT = "email.template.activation.subject";
	String ACTIVATION_EMAIL_TEMPLATE_EMAIL_TITLE = "email.template.activation.keyword.EMAIL_TITLE";
	String ACTIVATION_EMAIL_TEMPLATE_EMAIL_BODY = "email.template.activation.keyword.EMAIL_BODY";
	String ACTIVATION_EMAIL_TEMPLATE_EMAIL_END = "email.template.activation.keyword.EMAIL_END";
	
	String POST_ACTIVATION_EMAIL_TEMPLATE_SUBJECT = "email.template.post.activation.subject";
	String POST_ACTIVATION_EMAIL_TEMPLATE_EMAIL_TITLE = "email.template.post.activation.keyword.EMAIL_TITLE";
	String POST_ACTIVATION_EMAIL_TEMPLATE_EMAIL_BODY = "email.template.post.activation.keyword.EMAIL_BODY";
	String POST_ACTIVATION_EMAIL_TEMPLATE_EMAIL_END = "email.template.post.activation.keyword.EMAIL_END";
	
	String PASSWORD_CHANGE_EMAIL_TEMPLATE_SUBJECT = "email.template.password.change.subject";
	String PASSWORD_CHANGE_EMAIL_TEMPLATE_EMAIL_TITLE = "email.template.password.change.keyword.EMAIL_TITLE";
	String PASSWORD_CHANGE_EMAIL_TEMPLATE_EMAIL_BODY = "email.template.password.change.keyword.EMAIL_BODY";
	String PASSWORD_CHANGE_EMAIL_TEMPLATE_EMAIL_END = "email.template.password.change.keyword.EMAIL_END";
	
	String PASSWORD_RESET_EMAIL_TEMPLATE_SUBJECT = "email.template.password.reset.subject";
	String PASSWORD_RESET_EMAIL_TEMPLATE_EMAIL_TITLE = "email.template.password.reset.keyword.EMAIL_TITLE";
	String PASSWORD_RESET_EMAIL_TEMPLATE_EMAIL_BODY = "email.template.password.reset.keyword.EMAIL_BODY";
	String PASSWORD_RESET_EMAIL_TEMPLATE_EMAIL_END = "email.template.password.reset.keyword.EMAIL_END";
	
	
	String PROFILE_UPDATE_EMAIL_TEMPLATE_SUBJECT = "email.template.profile.update.subject";
	String PROFILE_UPDATE_EMAIL_TEMPLATE_EMAIL_TITLE = "email.template.profile.update.keyword.EMAIL_TITLE";
	String PROFILE_UPDATE_EMAIL_TEMPLATE_EMAIL_BODY = "email.template.profile.update.keyword.EMAIL_BODY";
	String PROFILE_UPDATE_EMAIL_TEMPLATE_EMAIL_END = "email.template.profile.update.keyword.EMAIL_END";
	
	String SHARE_YOUR_REPORT_EMAIL_TEMPLATE_SUBJECT = "email.template.share.your.report.subject";
	String SHARE_YOUR_REPORT_EMAIL_TEMPLATE_EMAIL_TITLE = "email.template.share.your.report.keyword.EMAIL_TITLE";
	String SHARE_YOUR_REPORT_EMAIL_TEMPLATE_EMAIL_BODY = "email.template.share.your.report.keyword.EMAIL_BODY";
	String SHARE_YOUR_REPORT_EMAIL_TEMPLATE_EMAIL_END = "email.template.share.your.report.keyword.EMAIL_END";
	
	String SEND_YOUR_TEST_REPORT_ON_EMAIL_TEMPLATE_SUBJECT = "email.template.send.your.report.on.email.subject";
	String SEND_YOUR_TEST_REPORT_ON_EMAIL_TEMPLATE_GRAPHTEMPLATE = "email.template.send.your.report.on.email.graphTemplate";
	String SEND_YOUR_TEST_REPORT_ON_EMAIL_TEMPLATE_BODY = "email.template.send.your.report.on.email.body";
	
	
	String STANDARD_EMAIL_TEMPLATE = "email.template.standard.body";
	
	String KEYWORD_EMAIL_TITLE = "{EMAIL_TITLE}";
	String KEYWORD_USER = "{USER}";
	String KEYWORD_EMAIL_BODY = "{EMAIL_BODY}";
	String KEYWORD_BUTTON_LABEL = "{BUTTOM_LABEL}";
	String KEYWORD_BUTTON_LINK = "{BUTTON_LINK}";
	String KEYWORD_EMAIL_END = "{EMAIL_END}";
	String KEYWORD_INVITY = "{INVITY}";
	
	String KEYWORD_TEMPORARY_PASSWORD = "{TEMPORARY_PASSWORD}";
	
	String KEYWORD_SITE_ID = "{SITE_ID}";
	String KEYWORD_VERSION_NUMBER = "{VERSION_NUMBER}";
	String KEYWORD_START_TIME = "{START_TIME}";
	String KEYWORD_END_TIME = "{END_TIME}";
	String KEYWORD_DURATION = "{DURATION}";
	String KEYWORD_ENVIRONMENT = "{ENVIRONMENT}";
	String KEYWORD_TEST_SUITE_REPORT_GRAPH_URL = "{TEST_SUITE_REPORT_GRAPH_URL}";
	String KEYWORD_LIST_OF_TEST_SUITES_GRAPHS = "{LIST_OF_TEST_SUITES_GRAPHS}";
	
	
	String WEBSITE_DOMAIN = "website.domain";
	String WIKI_WEBSITE_DOMAIN = "wiki.website.domain";
	
	EmailTemplate getAccountActivationEmailTemplate(User user);
	EmailTemplate getPostAccountActivationEmailTemplate(User user);
	EmailTemplate getPasswordChangedEmailTemplate(User user);
	EmailTemplate getResetPasswordEmailTemplate(User user);
	EmailTemplate getProfileUpdateEmailTemplate(User user);
	EmailTemplate getShareYourReportEmailTemplate(User fromUser, User toUser, String encryptedLink);
	EmailTemplate getSendYourTestReportOnEmailTemplate(String siteName,String versionNumber, JsonArray jsonArrayOfTestSuiteReports);
}
