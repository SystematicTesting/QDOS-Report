/**
 * Copyright (c) Nov 3, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.daolayer.services;

import java.util.List;

import com.systematictesting.daolayer.entity.UserSite;

public interface UserSiteDao {

	String SERVICE_NAME = "UserSiteDao";
	
	boolean addUserSite(UserSite userSite);
	UserSite getUserSite(UserSite userSite);
	List<UserSite> getFilterdUserSites(String email, String siteStatus, String shareStatus);
	List<UserSite> getSharedUserSites(String email, String siteStatus);
	List<UserSite> getAllUserSites(String email, String siteStatus);
	boolean saveUserSitesShareStatus(List<UserSite> userSites);
}
