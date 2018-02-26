package com.systematictesting.services.core;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.systematictesting.daolayer.beans.TestSuite;

public interface ExcelImporterService {
	String SERVICE_NAME = "ExcelImporterService";
	
	XSSFWorkbook isUploadedFileValid(String fileNameWithAbsolutePath) throws IOException;
	TestSuite convertExcelToTestSuite(XSSFWorkbook xssfWorkbook) throws IOException;
}
