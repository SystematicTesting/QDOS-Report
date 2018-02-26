package com.systematictesting.scheduled.tasks;


public interface SiteVersionTaskService {

	String SERVICE_NAME = "SiteVersionTaskService";
	
	void updateStatusOnStalledSiteVersion();
}
