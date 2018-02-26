/**
 * Copyright (c) Mar 9, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.services.core.impl;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.exceptions.SendEmailException;
import com.systematictesting.rest.core.beans.EmailTemplate;
import com.systematictesting.services.core.EmailService;
import com.systematictesting.services.core.TemplateService;

@Repository(EmailService.SERVICE_NAME)
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	TemplateService templateService;
	
	private Properties emailProperties;
	private String username;
	private String password;
	private String fromEmailAddress;

	public Properties getEmailProperties() {
		return emailProperties;
	}

	public void setEmailProperties(Properties emailProperties) {
		this.emailProperties = emailProperties;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFromEmailAddress() {
		return fromEmailAddress;
	}

	public void setFromEmailAddress(String fromEmailAddress) {
		this.fromEmailAddress = fromEmailAddress;
	}

	@Override
	public boolean sendAccountActivationEmail(User user) {
		logger.debug("Sending Account Activation Email to : "+user.getEmail());
		Session session = getSenderEmailSession();
		EmailTemplate template = templateService.getAccountActivationEmailTemplate(user);
		
		return sendEmail(user.getEmail(), session, template);
	}

	@Override
	public boolean sendPostAccountActivationEmail(User user) {
		logger.debug("Sending POST Account Activation Email to : "+user.getEmail());
		Session session = getSenderEmailSession();
		EmailTemplate template = templateService.getPostAccountActivationEmailTemplate(user);
		
		return sendEmail(user.getEmail(), session, template);
	}

	@Override
	public boolean sendPasswordChangeEmail(User user) {
		logger.debug("Sending Password Changed Email to : "+user);
		Session session = getSenderEmailSession();
		EmailTemplate template = templateService.getPasswordChangedEmailTemplate(user);
		
		return sendEmail(user.getEmail(), session, template);
	}

	@Override
	public boolean sendUpdateProfileConfirmationEmail(User user) {
		logger.debug("Sending Profile update confirmation Email to : "+user.getEmail());
		Session session = getSenderEmailSession();
		EmailTemplate template = templateService.getProfileUpdateEmailTemplate(user);
		
		return sendEmail(user.getEmail(), session, template);
	}
	
	@Override
	public boolean sendShareYourReportEmail(User fromUser, User toUser, String encryptedShareLink) {
		logger.debug("Sending share your report Email to : "+toUser.getEmail());
		Session session = getSenderEmailSession();
		EmailTemplate template = templateService.getShareYourReportEmailTemplate(fromUser, toUser, encryptedShareLink);
		return sendEmail(toUser.getEmail(), session, template);
	}

	private Session getSenderEmailSession() {
		if (getEmailProperties().get("mail.smtp.auth")!=null && getEmailProperties().get("mail.smtp.auth").equals("true")){
			Session session = Session.getDefaultInstance(getEmailProperties(), new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(getUsername(), getPassword());
				}
			});
			return session;
		} else {
			getEmailProperties().remove("mail.smtp.auth");
			getEmailProperties().remove("mail.smtp.socketFactory.class");
			getEmailProperties().remove("mail.smtp.socketFactory.port");
			Session session = Session.getDefaultInstance(getEmailProperties());
			return session;
		}
		
	}

	private boolean sendEmail(String email, Session session, EmailTemplate template) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(getFromEmailAddress(),"Test Automation Reporting Server"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(template.getSubject());
			message.setContent(template.getBody(),"text/html");
			Transport.send(message);
			logger.debug("Mail sent successfully to : "+email);
			return true;
		} catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new SendEmailException(e);
		}
	}

	@Override
	public boolean sendNotificationEmails(User user, String emailAddressList, String siteName, String versionNumber, String jsonTestReportSummary) {
		logger.debug("Sending Email Notification to subscriber list \n\tFROM : "+user.getEmail()+"\n\tTO : "+emailAddressList+"\n\tSITE NAME : "+siteName+"\n\tVERSION NUMBER : "+versionNumber+"\n\tJSON SUMMARY : "+jsonTestReportSummary);
		Session session = getSenderEmailSession();
		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArrayOfTestReportSuites = (JsonArray)jsonParser.parse(jsonTestReportSummary);
		EmailTemplate template = templateService.getSendYourTestReportOnEmailTemplate(siteName, versionNumber,jsonArrayOfTestReportSuites);
		return sendEmail(emailAddressList, session, template);
	}

	@Override
	public boolean sendResetPasswordEmail(User user) {
		logger.debug("Sending Password Reset Email to : "+user);
		Session session = getSenderEmailSession();
		EmailTemplate template = templateService.getResetPasswordEmailTemplate(user);
		
		return sendEmail(user.getEmail(), session, template);
	}
}
