/**
 * Copyright (c) Mar 7, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.daolayer.services;

import java.util.Map;

import com.systematictesting.daolayer.entity.User;

public interface UserDao {

	String SERVICE_NAME = "UserDao";
	
	boolean addUser(User user);
	boolean updateAccount(User user);
	boolean updateLicense(User user);
	boolean updateEmailSubscriptions(User user);
	String getEmailAddressListForNotification(User user, String siteName);
	User loginUser(User user);
	User getUserDetailsViaEmailAndDefaultApiKey(User user);
	User getUserDetailsViaEmail(User user);
	boolean validateActiveAPIKey(User user);
	boolean activateAccount(User user);
	boolean resetPassword(User user);
	boolean acceptAccountSharingInvitation(User user, String invityApiKey, String invityEmail);
	boolean changePassword(User user, String oldPassword, String newPassword);
	Map<String,Long> getFeatureListOfAccount(User user);
}
