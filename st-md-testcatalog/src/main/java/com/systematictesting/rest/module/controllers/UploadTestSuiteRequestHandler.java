package com.systematictesting.rest.module.controllers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.systematictesting.daolayer.beans.DataSet;
import com.systematictesting.daolayer.beans.TestCaseData;
import com.systematictesting.daolayer.beans.TestCaseDataSchema;
import com.systematictesting.daolayer.beans.TestStepData;
import com.systematictesting.daolayer.beans.TestSuite;
import com.systematictesting.daolayer.constants.ResponseCodes;
import com.systematictesting.daolayer.constants.TestCatalogConstants;
import com.systematictesting.daolayer.constants.TestCatalogResponseCodes;
import com.systematictesting.daolayer.entity.SingleTestSuite;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.exceptions.ExcelFileDoesNotMeetStandardsException;
import com.systematictesting.daolayer.exceptions.FileNotFoundException;
import com.systematictesting.daolayer.exceptions.InvalidUploadPostRequestException;
import com.systematictesting.daolayer.exceptions.MissingMandatoryAPIParameters;
import com.systematictesting.daolayer.exceptions.SessionNotValidException;
import com.systematictesting.daolayer.exceptions.TestSuiteNameAlreadyPresentException;
import com.systematictesting.daolayer.exceptions.TestSuiteNameNotPresentException;
import com.systematictesting.daolayer.services.SingleTestSuiteDao;
import com.systematictesting.rest.core.beans.Response;
import com.systematictesting.rest.core.controllers.AbstractController;
import com.systematictesting.services.core.ExcelImporterService;
import com.systematictesting.services.core.SystemPropertiesService;
import com.systematictesting.utils.TestCatalogUtils;

@Controller
@RequestMapping("/module/testcatalog/upload")
public class UploadTestSuiteRequestHandler extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(UploadTestSuiteRequestHandler.class);

	private int maxFileSize = 5000 * 1024;
	private int maxMemSize = 4 * 1024;

	@Autowired
	private SingleTestSuiteDao singleTestSuiteDao;
	
	@Autowired
	private ExcelImporterService excelImporterService;
	
	@Autowired
	private SystemPropertiesService systemPropertiesService;
	
	@RequestMapping(value = "/deleteTestCases.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object deleteTestCases(@RequestBody List<TestCaseData> requestedTestCases, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to copy Test Suite from stored version.");
		}
		String requestedSiteName = request.getParameter(TestCatalogConstants.SITE_NAME);
		String requestedSuiteName = request.getParameter(TestCatalogConstants.SUITE_NAME);
		
		SingleTestSuite requestedSingleTestSuite = new SingleTestSuite(); 
		requestedSingleTestSuite.setEmail(user.getEmail());
		requestedSingleTestSuite.setSiteName(requestedSiteName);
		requestedSingleTestSuite.setTestSuite(new TestSuite());
		requestedSingleTestSuite.getTestSuite().setSuiteName(requestedSuiteName);
		
		Response customResponse = new Response();
		if (singleTestSuiteDao.deleteTestCases(requestedSingleTestSuite, requestedTestCases)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}

	@RequestMapping(value = "/createTestSuite.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object createTestSuite(@RequestBody SingleTestSuite requestedSingleTestSuite, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to copy Test Suite from stored version.");
		}
		long currentTimestamp = System.currentTimeMillis();
		
		requestedSingleTestSuite.setCreatetime(currentTimestamp);
		Response customResponse = new Response();
		if (singleTestSuiteDao.addTestSuite(requestedSingleTestSuite)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}
	
	@RequestMapping(value = "/deleteElementProperty.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object deleteElementProperty(@RequestBody Map<String, String> elementProperty, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to copy Test Suite from stored version.");
		}
		String requestedSiteName = request.getParameter(TestCatalogConstants.SITE_NAME);
		String requestedSuiteName = request.getParameter(TestCatalogConstants.SUITE_NAME);
		
		SingleTestSuite requestedSingleTestSuite = new SingleTestSuite(); 
		requestedSingleTestSuite.setEmail(user.getEmail());
		requestedSingleTestSuite.setSiteName(requestedSiteName);
		requestedSingleTestSuite.setTestSuite(new TestSuite());
		requestedSingleTestSuite.getTestSuite().setSuiteName(requestedSuiteName);
		
		Response customResponse = new Response();
		if (singleTestSuiteDao.deleteProperty(requestedSingleTestSuite, elementProperty)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}
	
	@RequestMapping(value = "/createElementProperty.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object createElementProperty(@RequestBody Map<String, String> elementProperty, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to copy Test Suite from stored version.");
		}
		String requestedSiteName = request.getParameter(TestCatalogConstants.SITE_NAME);
		String requestedSuiteName = request.getParameter(TestCatalogConstants.SUITE_NAME);
		
		SingleTestSuite requestedSingleTestSuite = new SingleTestSuite(); 
		requestedSingleTestSuite.setEmail(user.getEmail());
		requestedSingleTestSuite.setSiteName(requestedSiteName);
		requestedSingleTestSuite.setTestSuite(new TestSuite());
		requestedSingleTestSuite.getTestSuite().setSuiteName(requestedSuiteName);
		
		Response customResponse = new Response();
		if (singleTestSuiteDao.addProperty(requestedSingleTestSuite, elementProperty)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}
	
	@RequestMapping(value = "/createTestCase.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object createTestCase(@RequestBody TestCaseData requestedSingleTestCase, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to copy Test Suite from stored version.");
		}
		String requestedSiteName = request.getParameter(TestCatalogConstants.SITE_NAME);
		String requestedSuiteName = request.getParameter(TestCatalogConstants.SUITE_NAME);
		
		SingleTestSuite requestedSingleTestSuite = new SingleTestSuite(); 
		requestedSingleTestSuite.setEmail(user.getEmail());
		requestedSingleTestSuite.setSiteName(requestedSiteName);
		requestedSingleTestSuite.setTestSuite(new TestSuite());
		requestedSingleTestSuite.getTestSuite().setSuiteName(requestedSuiteName);
		
		requestedSingleTestCase.setCompleteStatus(TestCatalogConstants.INCOMPLETE);
		
		Response customResponse = new Response();
		if (singleTestSuiteDao.addTestCase(requestedSingleTestSuite, requestedSingleTestCase)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}
	
	@RequestMapping(value = "/createTestStep.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object createTestStep(@RequestBody TestStepData testStep, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to copy Test Suite from stored version.");
		}
		String requestedSiteName = request.getParameter(TestCatalogConstants.SITE_NAME);
		String requestedSuiteName = request.getParameter(TestCatalogConstants.SUITE_NAME);
		String requestedTestCaseId = request.getParameter(TestCatalogConstants.TEST_CASE_ID);
		
		SingleTestSuite requestedSingleTestSuite = new SingleTestSuite(); 
		requestedSingleTestSuite.setEmail(user.getEmail());
		requestedSingleTestSuite.setSiteName(requestedSiteName);
		requestedSingleTestSuite.setTestSuite(new TestSuite());
		requestedSingleTestSuite.getTestSuite().setSuiteName(requestedSuiteName);
		
		Response customResponse = new Response();
		if (singleTestSuiteDao.addTestStep(requestedSingleTestSuite, requestedTestCaseId, testStep)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}
	
	@RequestMapping(value = "/deleteTestStep.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object deleteTestStep(@RequestBody TestStepData testStep, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to copy Test Suite from stored version.");
		}
		String requestedSiteName = request.getParameter(TestCatalogConstants.SITE_NAME);
		String requestedSuiteName = request.getParameter(TestCatalogConstants.SUITE_NAME);
		String requestedTestCaseId = request.getParameter(TestCatalogConstants.TEST_CASE_ID);
		
		SingleTestSuite requestedSingleTestSuite = new SingleTestSuite(); 
		requestedSingleTestSuite.setEmail(user.getEmail());
		requestedSingleTestSuite.setSiteName(requestedSiteName);
		requestedSingleTestSuite.setTestSuite(new TestSuite());
		requestedSingleTestSuite.getTestSuite().setSuiteName(requestedSuiteName);
		
		Response customResponse = new Response();
		if (singleTestSuiteDao.deleteTestStep(requestedSingleTestSuite, requestedTestCaseId, testStep)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}
	
	@RequestMapping(value = "/createTestCaseDataSchema.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object createTestCaseDataSchema(@RequestBody TestCaseDataSchema requestedDataSchemaFields, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to copy Test Suite from stored version.");
		}
		String requestedSiteName = request.getParameter(TestCatalogConstants.SITE_NAME);
		String requestedSuiteName = request.getParameter(TestCatalogConstants.SUITE_NAME);
		String requestedTestCaseId = request.getParameter(TestCatalogConstants.TEST_CASE_ID);
		
		SingleTestSuite requestedSingleTestSuite = new SingleTestSuite(); 
		requestedSingleTestSuite.setEmail(user.getEmail());
		requestedSingleTestSuite.setSiteName(requestedSiteName);
		requestedSingleTestSuite.setTestSuite(new TestSuite());
		requestedSingleTestSuite.getTestSuite().setSuiteName(requestedSuiteName);
		
		Response customResponse = new Response();
		if (singleTestSuiteDao.addTestCaseDataSchema(requestedSingleTestSuite, requestedTestCaseId, requestedDataSchemaFields)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}
	
	@RequestMapping(value = "/createTestCaseDataSet.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object createTestCaseDataSet(@RequestBody Map<String, DataSet> dataSetMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to copy Test Suite from stored version.");
		}
		String requestedSiteName = request.getParameter(TestCatalogConstants.SITE_NAME);
		String requestedSuiteName = request.getParameter(TestCatalogConstants.SUITE_NAME);
		String requestedTestCaseId = request.getParameter(TestCatalogConstants.TEST_CASE_ID);
		
		SingleTestSuite requestedSingleTestSuite = new SingleTestSuite(); 
		requestedSingleTestSuite.setEmail(user.getEmail());
		requestedSingleTestSuite.setSiteName(requestedSiteName);
		requestedSingleTestSuite.setTestSuite(new TestSuite());
		requestedSingleTestSuite.getTestSuite().setSuiteName(requestedSuiteName);
		
		Response customResponse = new Response();
		if (singleTestSuiteDao.addTestCaseDataSet(requestedSingleTestSuite, requestedTestCaseId, dataSetMap)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}
	
	@RequestMapping(value = "/deleteTestCaseDataSet.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object deleteTestCaseDataSet(@RequestBody DataSet dataSet, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to copy Test Suite from stored version.");
		}
		String requestedSiteName = request.getParameter(TestCatalogConstants.SITE_NAME);
		String requestedSuiteName = request.getParameter(TestCatalogConstants.SUITE_NAME);
		String requestedTestCaseId = request.getParameter(TestCatalogConstants.TEST_CASE_ID);
		
		SingleTestSuite requestedSingleTestSuite = new SingleTestSuite(); 
		requestedSingleTestSuite.setEmail(user.getEmail());
		requestedSingleTestSuite.setSiteName(requestedSiteName);
		requestedSingleTestSuite.setTestSuite(new TestSuite());
		requestedSingleTestSuite.getTestSuite().setSuiteName(requestedSuiteName);
		
		Response customResponse = new Response();
		if (singleTestSuiteDao.deleteTestCaseDataSet(requestedSingleTestSuite, requestedTestCaseId, dataSet)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}
	
	@RequestMapping(value = "/copyTestSuite.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object commitCatalog(@RequestBody SingleTestSuite requestedSingleTestSuite, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to copy Test Suite from stored version.");
		}
		long currentTimestamp = System.currentTimeMillis();
		
		requestedSingleTestSuite.setCreatetime(currentTimestamp);
		Response customResponse = new Response();
		if (singleTestSuiteDao.addTestSuite(requestedSingleTestSuite)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}

	@RequestMapping(value = "/saveTestSuite.rest", method = RequestMethod.POST, produces = { "application/json" })
	public @ResponseBody Object saveTestSuite(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("Loading environment specific properties file...");

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			throw new InvalidUploadPostRequestException("FILE NOT UPLOADED. PLEASE SEND THE MULTI-PART File upload request.");
		}
		User requestedFromUser = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(maxMemSize);
		factory.setRepository(new File(systemPropertiesService.getProperties().getProperty(TestCatalogConstants.BOOTUP_TEMP_FILE_UPLOAD_PATH)));

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(maxFileSize);

		@SuppressWarnings("unchecked")
		List<FileItem> fileItems = upload.parseRequest(request);

		Iterator<FileItem> i = fileItems.iterator();
		FileItem fileItemToUpload = null;
		Map<String, String> parameterMap = new HashMap<String, String>();
		while (i.hasNext()) {
			FileItem fi = (FileItem) i.next();
			logger.debug("fi is : " + fi.toString());
			if (!fi.isFormField()) {
				fileItemToUpload = fi;
			} else {
				String name = fi.getFieldName();
				String value = fi.getString();
				parameterMap.put(name, value);
			}
		}
		logger.debug("parameterMap is : " + parameterMap);
		String requestedSiteName = parameterMap.get(TestCatalogConstants.SITE_NAME);
		String requestedFileName = parameterMap.get(TestCatalogConstants.UPLOAD_FILE_NAME);
		String requestedSourceType = parameterMap.get(TestCatalogConstants.SOURCE_TYPE);
		String requestedActiveApiKey = requestedFromUser.getDefaultAPIkey();
		String requestedEmailAddress = requestedFromUser.getEmail();

		if (StringUtils.isNotBlank(requestedEmailAddress) && StringUtils.isNotBlank(requestedActiveApiKey) && StringUtils.isNotBlank(requestedFileName) && StringUtils.isNotBlank(requestedSiteName)) {
			long currentTimestamp = System.currentTimeMillis();
			String defaultCatalogLocation = systemPropertiesService.getProperties().getProperty(TestCatalogConstants.BOOTUP_TEST_CATALOG_ROOT_PATH);
			String userTestSuiteLocation = TestCatalogConstants.FILE_SEPARATOR + requestedActiveApiKey + TestCatalogConstants.FILE_SEPARATOR + requestedSiteName + TestCatalogConstants.FILE_SEPARATOR + TestCatalogConstants.LIBRARY + TestCatalogConstants.FILE_SEPARATOR + currentTimestamp;

			String catalogUploadFolderLocation = defaultCatalogLocation + userTestSuiteLocation;
			TestCatalogUtils.createCatalogLocation(catalogUploadFolderLocation);
			String fileNameWithAbsolutePath = catalogUploadFolderLocation + TestCatalogConstants.FILE_SEPARATOR + requestedFileName;
			File file = new File(fileNameWithAbsolutePath);
			if (file.exists()) {
				file.delete();
			}
			fileItemToUpload.write(file);
			boolean isSavedInSessionSuccess = false;
			XSSFWorkbook xssfWorkbook = excelImporterService.isUploadedFileValid(fileNameWithAbsolutePath);
			if (xssfWorkbook != null) {
				SingleTestSuite singleTestSuite = new SingleTestSuite();
				singleTestSuite.setEmail(requestedEmailAddress);
				singleTestSuite.setSourceEmail(requestedEmailAddress);
				singleTestSuite.setSiteName(requestedSiteName);
				singleTestSuite.setFileLocation(userTestSuiteLocation);
				singleTestSuite.setFileName(requestedFileName);
				singleTestSuite.setSourceType(requestedSourceType);
				singleTestSuite.setTestSuite(excelImporterService.convertExcelToTestSuite(xssfWorkbook));
				singleTestSuite.setCreatetime(currentTimestamp);
				isSavedInSessionSuccess = singleTestSuiteDao.addTestSuite(singleTestSuite);
			}

			Response customResponse = new Response();
			if (isSavedInSessionSuccess && xssfWorkbook != null) {
				customResponse.setMessage(ResponseCodes.SUCCESS.message);
				customResponse.setStatus(ResponseCodes.SUCCESS.code);
			} else {
				throw new FileNotFoundException("File upload and its reference in TempTestSuite table has failed.");
			}
			logger.debug("RESPONSE /saveTestSuite.rest : " + customResponse);

			return customResponse;
		} else {
			throw new MissingMandatoryAPIParameters("REQUIRED PARAMETERS ARE : " + "\n SiteName = " + requestedSiteName + "\n Email = " + requestedEmailAddress + "\n ActiveApiKey = " + requestedActiveApiKey + "\n requestedFileName = " + requestedFileName);
		}

	}

	@ExceptionHandler({ ExcelFileDoesNotMeetStandardsException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody Object handleExcelFileDoesNotMeetStandardsException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(ex.getMessage());
		customResponse.setStatus(TestCatalogResponseCodes.EXCEL_SHEET_DOES_NOT_MEET_STANDARD.code);
		logger.debug("handleExcelFileDoesNotMeetStandardsException Response : " + customResponse);
		logger.error("handleExcelFileDoesNotMeetStandardsException", ex);
		return customResponse;
	}

	@ExceptionHandler({ FileNotFoundException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody Object handleInvalidExcelFileException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(TestCatalogResponseCodes.INVALID_EXCEL_FILE_DETECTED.message);
		customResponse.setStatus(TestCatalogResponseCodes.INVALID_EXCEL_FILE_DETECTED.code);
		logger.debug("handleInvalidExcelFileException Response : " + customResponse);
		logger.error("handleInvalidExcelFileException", ex);
		return customResponse;
	}
	
	@ExceptionHandler({ TestSuiteNameAlreadyPresentException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody Object handleTestSuiteNameAlreadyPresentException(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(TestCatalogResponseCodes.TESTSUITE_NAME_ALREADY_PRESENT.message);
		customResponse.setStatus(TestCatalogResponseCodes.TESTSUITE_NAME_ALREADY_PRESENT.code);
		logger.debug("handleTestSuiteNameAlreadyPresentException Response : " + customResponse);
		logger.error("handleTestSuiteNameAlreadyPresentException", ex);
		return customResponse;
	}
	
	@ExceptionHandler({ TestSuiteNameNotPresentException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	Object handleInvalidTestSuiteExceptions(Exception ex, HttpServletRequest request) {
		Response customResponse = new Response();
		customResponse.setMessage(TestCatalogResponseCodes.INVALID_TEST_SUITE.message);
		customResponse.setStatus(TestCatalogResponseCodes.INVALID_TEST_SUITE.code);
		logger.debug("handleInvalidTestSuiteExceptions Response : " + customResponse);
		logger.error("handleInvalidTestSuiteExceptions",ex);
		return customResponse;
	}
}
