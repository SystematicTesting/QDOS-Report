package com.systematictesting.services.core.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.systematictesting.daolayer.beans.DataSet;
import com.systematictesting.daolayer.beans.TestCaseData;
import com.systematictesting.daolayer.beans.TestStepData;
import com.systematictesting.daolayer.beans.TestSuite;
import com.systematictesting.daolayer.constants.TestCatalogConstants;
import com.systematictesting.daolayer.constants.TestCatalogResponseCodes;
import com.systematictesting.daolayer.exceptions.ExcelFileDoesNotMeetStandardsException;
import com.systematictesting.daolayer.exceptions.FileNotFoundException;
import com.systematictesting.daolayer.services.CatalogDao;
import com.systematictesting.services.core.ExcelImporterService;
import com.systematictesting.utils.ExcelReaderUtils;

@Repository(ExcelImporterService.SERVICE_NAME)
public class ExcelImporterServiceImpl implements ExcelImporterService {

	private static final Logger logger = LoggerFactory.getLogger(ExcelImporterService.class);

	@Autowired
	private CatalogDao catalogDao;

	@Override
	public XSSFWorkbook isUploadedFileValid(String fileNameWithAbsolutePath) throws IOException {
		FileInputStream fis = new FileInputStream(fileNameWithAbsolutePath);
		XSSFWorkbook xlsWorkbook = new XSSFWorkbook(fis);
		fis.close();
		if (!(xlsWorkbook.isWindowsLocked() && xlsWorkbook.isStructureLocked() && xlsWorkbook.isHidden() && xlsWorkbook.getNumberOfSheets() > 1)) {
			boolean suiteNameVerification = verifySuiteName(xlsWorkbook);
			boolean dateFormatVerification = verifyDateFormat(xlsWorkbook);
			boolean allColumnsOnMasterSheet = verifyAllColumnsOnMasterSheet(xlsWorkbook);
			boolean testCaseExistsInMasterSheet = verifyTestCaseExistsInMasterSheet(xlsWorkbook);
			boolean allTestCaseIdSheetsArePresent = verifyAllTestCaseIdSheetsArePresent(xlsWorkbook);
			boolean keyValuePairsSheetPresent = verifyKeyValuePairsSheetPresent(xlsWorkbook);
			boolean allTestCaseSheetsContainsStandardHeader = verifyAllTestCaseSheetsContainsStandardHeader(xlsWorkbook);
			xlsWorkbook.close();
			if (suiteNameVerification && dateFormatVerification && allColumnsOnMasterSheet && testCaseExistsInMasterSheet && allTestCaseIdSheetsArePresent && keyValuePairsSheetPresent && allTestCaseSheetsContainsStandardHeader) {
				return xlsWorkbook;
			} else {
				StringBuilder errorMessage = new StringBuilder("Uploaded Excel file doesn't meet V1 Standard.<br/><ul>");
				if (!suiteNameVerification) {
					errorMessage.append("<li>" + TestCatalogResponseCodes.EXCEL_SHEET_DOES_NOT_MEET_STANDARD.SUITE_NAME_MISSING + "</li>");
				}
				if (!dateFormatVerification) {
					errorMessage.append("<li>" + TestCatalogResponseCodes.EXCEL_SHEET_DOES_NOT_MEET_STANDARD.TEST_SUITE_DATE_MISSING + "</li>");
				}
				if (!allColumnsOnMasterSheet) {
					errorMessage.append("<li>" + TestCatalogResponseCodes.EXCEL_SHEET_DOES_NOT_MEET_STANDARD.MANDATORY_COLUMNS_MISSING_IN_MASTER_SHEET + "</li>");
				}
				if (!testCaseExistsInMasterSheet) {
					errorMessage.append("<li>" + TestCatalogResponseCodes.EXCEL_SHEET_DOES_NOT_MEET_STANDARD.NO_TEST_CASE_IN_TEST_SUITE + "</li>");
				}
				if (!allTestCaseIdSheetsArePresent) {
					errorMessage.append("<li>" + TestCatalogResponseCodes.EXCEL_SHEET_DOES_NOT_MEET_STANDARD.AUTOMATED_TEST_CASE_SHEETS_ARE_MISSING + "</li>");
				}
				if (!keyValuePairsSheetPresent) {
					errorMessage.append("<li>" + TestCatalogResponseCodes.EXCEL_SHEET_DOES_NOT_MEET_STANDARD.KEY_VALUE_PAIRS_SHEET_MISSING + "</li>");
				}
				if (!allTestCaseSheetsContainsStandardHeader) {
					errorMessage.append("<li>" + TestCatalogResponseCodes.EXCEL_SHEET_DOES_NOT_MEET_STANDARD.STANDARD_HEADER_MISSING_IN_TESTCASE_SHEET + "</li>");
				}
				errorMessage.append("</ul>");
				throw new ExcelFileDoesNotMeetStandardsException(errorMessage.toString());
			}
		} else {
			xlsWorkbook.close();
			throw new FileNotFoundException("Uploaded Excel file is not valid.");
		}
	}

	private boolean verifyAllTestCaseSheetsContainsStandardHeader(XSSFWorkbook xlsWorkbook) {
		int totalNumberOfTestCases = ExcelReaderUtils.getRowCount(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME);
		for (int testCaseRowIndex = 5; testCaseRowIndex < totalNumberOfTestCases; testCaseRowIndex++) {
			String sheetName = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 0, testCaseRowIndex);
			if (StringUtils.isNotBlank(sheetName)) {
				int index = xlsWorkbook.getSheetIndex(sheetName);
				if (index == -1) {
					String testCaseMode = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 2, testCaseRowIndex);
					String testCaseId = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 0, testCaseRowIndex);
					if (StringUtils.isBlank(testCaseMode) || testCaseMode.equalsIgnoreCase(TestCatalogConstants.TESTCASE_MODE_AUTOMATE)) {
						logger.error("verifyAllTestCaseSheetsContainsStandardHeader() :: FAILED :: SHEETNAME = " + sheetName + " doesn't exists for TCID = " + testCaseId + " with MODE = " + testCaseMode);
						return false;
					} else {
						continue;
					}
				}
				String column1Name = ExcelReaderUtils.getCellData(xlsWorkbook, sheetName, 0, 1);
				String column2Name = ExcelReaderUtils.getCellData(xlsWorkbook, sheetName, 1, 1);
				String column3Name = ExcelReaderUtils.getCellData(xlsWorkbook, sheetName, 2, 1);
				String column4Name = ExcelReaderUtils.getCellData(xlsWorkbook, sheetName, 3, 1);
				String column5Name = ExcelReaderUtils.getCellData(xlsWorkbook, sheetName, 4, 1);
				String column6Name = ExcelReaderUtils.getCellData(xlsWorkbook, sheetName, 5, 1);
				String column7Name = ExcelReaderUtils.getCellData(xlsWorkbook, sheetName, 6, 1);
				if (!(column1Name.equalsIgnoreCase(TestCatalogConstants.TESTSTEP_TSID) && column2Name.equalsIgnoreCase(TestCatalogConstants.TESTSTEP_DESCRIPTION) && column3Name.equalsIgnoreCase(TestCatalogConstants.TESTSTEP_ACTION) && column4Name.equalsIgnoreCase(TestCatalogConstants.TESTSTEP_ELEMENT_TYPE) && column5Name.equalsIgnoreCase(TestCatalogConstants.TESTSTEP_ELEMENT_KEY) && column6Name.equalsIgnoreCase(TestCatalogConstants.TESTSTEP_DATA_COLUMN_NAME) && column7Name.equalsIgnoreCase(TestCatalogConstants.TESTSTEP_PROCEED_ON_FAIL))) {
					logger.error("verifyAllTestCaseSheetsContainsStandardHeader() :: FAILED :: Columns are not valid.");
					return false;
				}
			}
		}
		if (totalNumberOfTestCases == 0) {
			logger.error("verifyAllTestCaseSheetsContainsStandardHeader() :: FAILED :: Columns are not valid.");
			return false;
		} else {
			return true;
		}
	}

	private boolean verifyKeyValuePairsSheetPresent(XSSFWorkbook xlsWorkbook) {
		int index = xlsWorkbook.getSheetIndex(TestCatalogConstants.KEY_VALUE_PAIRS_SHEET_NAME);
		if (index == -1) {
			logger.error("verifyKeyValuePairsSheetPresent() :: FAILED :: KeyValuePairs Sheet is not present.");
			return false;
		} else {
			return true;
		}
	}

	private boolean verifyAllTestCaseIdSheetsArePresent(XSSFWorkbook xlsWorkbook) {
		int totalNumberOfTestCases = ExcelReaderUtils.getRowCount(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME);
		for (int testCaseRowIndex = 5; testCaseRowIndex < totalNumberOfTestCases; testCaseRowIndex++) {
			String sheetName = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 0, testCaseRowIndex);
			if (StringUtils.isNotBlank(sheetName)) {
				int index = xlsWorkbook.getSheetIndex(sheetName);
				if (index == -1) {
					String testCaseMode = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 2, testCaseRowIndex);
					String testCaseId = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 0, testCaseRowIndex);
					if (StringUtils.isBlank(testCaseMode) || testCaseMode.equalsIgnoreCase(TestCatalogConstants.TESTCASE_MODE_AUTOMATE)) {
						logger.error("verifyAllTestCaseIdSheetsArePresent() :: FAILED :: SHEETNAME = " + sheetName + " doesn't exists for TCID = " + testCaseId + " with MODE = " + testCaseMode);
						return false;
					}
				}
			}
		}
		if (totalNumberOfTestCases == 0) {
			logger.error("verifyAllTestCaseIdSheetsArePresent() :: FAILED :: SHEETNAME = " + TestCatalogConstants.MASTER_SHEET_NAME + " doesn't exists.");
			return false;
		} else {
			return true;
		}
	}

	private boolean verifyTestCaseExistsInMasterSheet(XSSFWorkbook xlsWorkbook) {
		int totalNumberOfTestCases = ExcelReaderUtils.getRowCount(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME);
		if (totalNumberOfTestCases < 5) {
			logger.error("verifyTestCaseExistsInMasterSheet() :: FAILED :: Not testcase detected in MasterSheet. As number of test cases detected = " + totalNumberOfTestCases);
			return false;
		} else {
			return true;
		}
	}

	private boolean verifyAllColumnsOnMasterSheet(XSSFWorkbook xlsWorkbook) {
		String column1Name = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 0, 4);
		String column2Name = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 1, 4);
		String column3Name = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 2, 4);
		if (column1Name != null && column1Name.equals(TestCatalogConstants.TESTCASE_TCID) && column2Name != null && column2Name.equals(TestCatalogConstants.TESTCASE_DESCRIPTION) && column3Name != null && column3Name.equals(TestCatalogConstants.TESTCASE_MODE)) {
			return true;
		}
		logger.error("verifyAllColumnsOnMasterSheet() :: FAILED :: Columns are not valid in MasterSheet");
		return false;
	}

	private boolean verifyDateFormat(XSSFWorkbook xlsWorkbook) {
		String dateKey = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 0, 2);
		if (dateKey != null && dateKey.trim().equalsIgnoreCase(TestCatalogConstants.DATE_KEY)) {
			return true;
		}
		logger.error("verifyDateFormat() :: FAILED :: Date not present in MasterSheet");
		return false;
	}

	private boolean verifySuiteName(XSSFWorkbook xlsWorkbook) {
		String suiteNameKey = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 0, 1);
		String suiteNameValue = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 1, 1);
		if (suiteNameKey != null && suiteNameValue != null && suiteNameKey.trim().equalsIgnoreCase(TestCatalogConstants.SUITE_NAME_KEY) && StringUtils.isNotBlank(suiteNameValue)) {
			return true;
		}
		logger.error("verifySuiteName() :: FAILED :: SuiteName is not present in MasterSheet");
		return false;
	}

	@Override
	public TestSuite convertExcelToTestSuite(XSSFWorkbook xlsWorkbook) throws IOException {
		TestSuite testSuite = new TestSuite();
		String testSuiteName = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, 1, 1);
		testSuite.setSuiteName(testSuiteName);
		for (int tcid = 5; tcid <= ExcelReaderUtils.getRowCount(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME); tcid++) {
			String currentTest = ExcelReaderUtils.getCellDataOnMasterSheet(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, TestCatalogConstants.TESTCASE_TCID, tcid);
			if (StringUtils.isNotBlank(currentTest)) {
				String testCaseMode = ExcelReaderUtils.getCellDataOnMasterSheet(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, TestCatalogConstants.TESTCASE_MODE, tcid);
				String testCaseIdName = ExcelReaderUtils.getCellDataOnMasterSheet(xlsWorkbook, TestCatalogConstants.MASTER_SHEET_NAME, TestCatalogConstants.TESTCASE_DESCRIPTION, tcid);
				TestCaseData objTestCaseData = new TestCaseData();
				objTestCaseData.setTestCaseId(currentTest);
				objTestCaseData.setTestCaseName(testCaseIdName);
				objTestCaseData.setTestCaseMode(testCaseMode.equalsIgnoreCase(TestCatalogConstants.TESTCASE_MODE_AUTOMATE) ? TestCatalogConstants.TESTCASE_MODE_AUTOMATE : TestCatalogConstants.TESTCASE_MODE_MANUAL);
				objTestCaseData.setTestCaseType(TestCatalogConstants.TESTCASE_TYPE_DATA_DRIVEN);

				//Populate just the DataSets
				Map<String, ArrayList<DataSet>> dataSetsMap = new HashMap<String, ArrayList<DataSet>>();
				int totalSets = ExcelReaderUtils.getRowCount(xlsWorkbook, currentTest + TestCatalogConstants.DATASHEET_POSTFIX);
				int dataSetIndex = 2;
				do {
					for (int tsid = 2; tsid <= ExcelReaderUtils.getRowCount(xlsWorkbook, currentTest); tsid++) {
						boolean isDataSetPresent = true;
						if (totalSets>0) {
							String dataSetId = ExcelReaderUtils.getCellData(xlsWorkbook, currentTest + TestCatalogConstants.DATASHEET_POSTFIX, TestCatalogConstants.TESTCASE_DATASET_DSID, dataSetIndex);
							if (StringUtils.isBlank(dataSetId)){
								isDataSetPresent = false;
							}
						}
						if (isDataSetPresent) {
							String data_column_name = ExcelReaderUtils.getCellData(xlsWorkbook, currentTest, TestCatalogConstants.TESTSTEP_DATA_COLUMN_NAME, tsid);
							if (StringUtils.isNotBlank(data_column_name)){
								DataSet dataSetItem = new DataSet();
								dataSetItem.setId(ExcelReaderUtils.getCellData(xlsWorkbook, currentTest + TestCatalogConstants.DATASHEET_POSTFIX, TestCatalogConstants.TESTCASE_DATASET_DSID, dataSetIndex));
								dataSetItem.setDescription(ExcelReaderUtils.getCellData(xlsWorkbook, currentTest + TestCatalogConstants.DATASHEET_POSTFIX, TestCatalogConstants.TESTCASE_DATASET_DESCRIPTION, dataSetIndex));
								dataSetItem.setValue(ExcelReaderUtils.getCellData(xlsWorkbook, currentTest + TestCatalogConstants.DATASHEET_POSTFIX, data_column_name, dataSetIndex));
								if (dataSetsMap.get(data_column_name.trim())==null){
									dataSetsMap.put(data_column_name.trim(), new ArrayList<DataSet>());
									dataSetsMap.get(data_column_name.trim()).add(dataSetItem);
								} else if (dataSetsMap.get(data_column_name.trim())!=null && !dataSetsMap.get(data_column_name.trim()).contains(dataSetItem)){
									dataSetsMap.get(data_column_name.trim()).add(dataSetItem);
								}
							}
						}
					}
					dataSetIndex++;
				} while (dataSetIndex <= totalSets);
				objTestCaseData.setDataSets(dataSetsMap);
				
				//Populate Steps.
				boolean isDataSetPresent = true;
				if (totalSets>0) {
					String dataSetId = ExcelReaderUtils.getCellData(xlsWorkbook, currentTest + TestCatalogConstants.DATASHEET_POSTFIX, TestCatalogConstants.TESTCASE_DATASET_DSID, 2);
					if (StringUtils.isBlank(dataSetId)){
						isDataSetPresent = false;
					}
				} else {
					isDataSetPresent = false;
				}
				if (isDataSetPresent){
					objTestCaseData.setTestCaseType(TestCatalogConstants.TESTCASE_TYPE_DATA_DRIVEN);
				} else {
					objTestCaseData.setTestCaseType(TestCatalogConstants.TESTCASE_TYPE_STATIC);
				}
				for (int tsid = 2; tsid <= ExcelReaderUtils.getRowCount(xlsWorkbook, currentTest); tsid++) {
					String currentTSID = ExcelReaderUtils.getCellData(xlsWorkbook, currentTest, TestCatalogConstants.TESTSTEP_TSID, tsid);
					if (StringUtils.isNotBlank(currentTSID)) {
						String stepDescription = ExcelReaderUtils.getCellData(xlsWorkbook, currentTest, TestCatalogConstants.TESTSTEP_DESCRIPTION, tsid);
						String keyword = ExcelReaderUtils.getCellData(xlsWorkbook, currentTest, TestCatalogConstants.TESTSTEP_ACTION, tsid);
						String object = ExcelReaderUtils.getCellData(xlsWorkbook, currentTest, TestCatalogConstants.TESTSTEP_ELEMENT_KEY, tsid);
						String proceedOnFail = ExcelReaderUtils.getCellData(xlsWorkbook, currentTest, TestCatalogConstants.TESTSTEP_PROCEED_ON_FAIL, tsid);
						String data_column_name = ExcelReaderUtils.getCellData(xlsWorkbook, currentTest, TestCatalogConstants.TESTSTEP_DATA_COLUMN_NAME, tsid);
						String elementType = ExcelReaderUtils.getCellData(xlsWorkbook, currentTest, TestCatalogConstants.TESTSTEP_ELEMENT_TYPE, tsid);
						
						TestStepData objTestStepData = new TestStepData();
						objTestStepData.setElementKey(object);
						objTestStepData.setElementType(elementType);
						objTestStepData.setElementValue(data_column_name);
						objTestStepData.setProceedOnFail(proceedOnFail);
						objTestStepData.setStepDescription(stepDescription);
						objTestStepData.setStepId(currentTSID);
						objTestStepData.setStepKeyword(keyword);
						objTestCaseData.getTestStepsData().add(objTestStepData);	
					}
				
				}
				if (objTestCaseData.getTestStepsData().size()>0){
					objTestCaseData.setCompleteStatus(TestCatalogConstants.COMPLETE);
				} else {
					objTestCaseData.setCompleteStatus(TestCatalogConstants.INCOMPLETE);
				}
				
				testSuite.getTestCaseArray().add(objTestCaseData);
			}
		}

		for (int rowIndex = 2; rowIndex <= ExcelReaderUtils.getRowCount(xlsWorkbook, TestCatalogConstants.KEY_VALUE_PAIRS_SHEET_NAME); rowIndex++) {
			String key = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.KEY_VALUE_PAIRS_SHEET_NAME, TestCatalogConstants.KEY, rowIndex);
			String value = ExcelReaderUtils.getCellData(xlsWorkbook, TestCatalogConstants.KEY_VALUE_PAIRS_SHEET_NAME, TestCatalogConstants.VALUE, rowIndex);
			if (StringUtils.isNotBlank(key)) {
				testSuite.getKeyValuePairs().put(key, value);
			}
		}
		return testSuite;
	}

}
