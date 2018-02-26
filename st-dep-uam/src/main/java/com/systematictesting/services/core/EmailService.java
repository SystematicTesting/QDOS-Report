/**
 * Copyright (c) Mar 9, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.services.core;

import com.systematictesting.daolayer.entity.User;


public interface EmailService {
	String SERVICE_NAME = "EmailService";
	boolean sendAccountActivationEmail(User user);
	boolean sendPostAccountActivationEmail(User user);
	boolean sendPasswordChangeEmail(User user);
	boolean sendResetPasswordEmail(User user);
	boolean sendUpdateProfileConfirmationEmail(User user);
	boolean sendShareYourReportEmail(User fromUser, User toUser, String encryptedShareLink);
	boolean sendNotificationEmails(User user, String emailAddressList, String siteName, String versionNumber, String jsonTestReportSummary);
}
