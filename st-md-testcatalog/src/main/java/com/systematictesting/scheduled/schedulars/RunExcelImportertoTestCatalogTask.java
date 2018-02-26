package com.systematictesting.scheduled.schedulars;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.systematictesting.scheduled.tasks.ExcelImportertoTestCatalogTaskService;

@Component
public class RunExcelImportertoTestCatalogTask {
private static final Logger logger = LoggerFactory.getLogger(RunExcelImportertoTestCatalogTask.class);
	
	@Autowired
	private ExcelImportertoTestCatalogTaskService excelImporterToTestCatalogTaskService;

	public void run() {
		try {
			String dateParam = new Date().toString();
			logger.debug("RUNNING THE JOB :: RunExcelImportertoTestCatalogTask.class :: "+dateParam);
			excelImporterToTestCatalogTaskService.importExcelToTestCatalog();
			logger.debug("STOPPING THE JOB :: RunExcelImportertoTestCatalogTask.class :: "+dateParam);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
