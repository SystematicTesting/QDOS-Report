package com.systematictesting.scheduled.schedulars;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.systematictesting.scheduled.tasks.SiteVersionTaskService;

@Component
public class RunSiteVersionTask {
	private static final Logger logger = LoggerFactory.getLogger(RunSiteVersionTask.class);
	
	@Autowired
	private SiteVersionTaskService siteVersionTaskService;

	public void run() {
		try {
			String dateParam = new Date().toString();
			logger.debug("RUNNING THE JOB :: RunSiteVersionTask.class :: "+dateParam);
			siteVersionTaskService.updateStatusOnStalledSiteVersion();
			logger.debug("STOPPING THE JOB :: RunSiteVersionTask.class :: "+dateParam);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
