package com.systematictesting.scheduled.tasks.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.systematictesting.scheduled.tasks.ExcelImportertoTestCatalogTaskService;

@Repository(ExcelImportertoTestCatalogTaskService.SERVICE_NAME)
public class ExcelImportertoTestCatalogTaskServiceImpl implements ExcelImportertoTestCatalogTaskService {

	private static final Logger logger = LoggerFactory.getLogger(ExcelImportertoTestCatalogTaskServiceImpl.class);

	
	
	@Override
	public void importExcelToTestCatalog() {
		// TODO Auto-generated method stub
		
	}
	

}
