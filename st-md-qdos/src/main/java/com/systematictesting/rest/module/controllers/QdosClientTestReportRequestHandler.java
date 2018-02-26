package com.systematictesting.rest.module.controllers;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.systematictesting.daolayer.beans.Report;
import com.systematictesting.daolayer.beans.ResultSummary;
import com.systematictesting.daolayer.beans.TestCase;
import com.systematictesting.daolayer.beans.TestStep;
import com.systematictesting.daolayer.beans.TestSuiteDetails;
import com.systematictesting.daolayer.beans.TestSuiteReport;
import com.systematictesting.daolayer.constants.ResponseCodes;
import com.systematictesting.daolayer.constants.QdosConstants;
import com.systematictesting.daolayer.constants.QdosStatus;
import com.systematictesting.daolayer.entity.QdosReport;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.exceptions.InternalServerErrorException;
import com.systematictesting.daolayer.exceptions.InvalidActiveAPIException;
import com.systematictesting.daolayer.services.QdosReportDao;
import com.systematictesting.daolayer.services.UserDao;
import com.systematictesting.rest.core.beans.Response;
import com.systematictesting.rest.core.controllers.AbstractController;

@Controller
@RequestMapping("/module/qdos/client")
public class QdosClientTestReportRequestHandler extends AbstractController{
	
	private static final Logger logger = LoggerFactory.getLogger(QdosClientTestReportRequestHandler.class);

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private QdosReportDao qdosReportDao;
	
	@RequestMapping(value = "/startTestSuite.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object startTestSuite(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, JSONException {
		return processTestSuite(request, QdosStatus.REAL_TIME_STATUS.NEW);
	}

	@RequestMapping(value = "/finishTestSuite.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object finishTestSuite(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, JSONException {
		return processTestSuite(request, QdosStatus.REAL_TIME_STATUS.FINISHED);
	}

	
	@RequestMapping(value = "/startTestCase.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object startTestCase(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, JSONException {
		return processTestCase(request, QdosStatus.REAL_TIME_STATUS.NEW);
	}
	
	@RequestMapping(value = "/finishTestCase.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object finishTestCase(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, JSONException {
		return processTestCase(request, QdosStatus.REAL_TIME_STATUS.FINISHED);
	}
	
	@RequestMapping(value = "/pushTestStep.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object pushTestStep(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, JSONException {
		String email = request.getParameter(User.FIELDS.EMAIL);
		String activeAPIkey = request.getParameter(User.FIELDS.ACTIVE_API_KEY);
		String siteName = request.getParameter(QdosConstants.SITE_NAME);
		String versionNumberWithPrefix = request.getParameter(QdosReport.FIELDS.VERSION_NUMBER);
		String versionNumber = versionNumberWithPrefix.substring(QdosReport.FIELDS.VERSION_NAME_PREFIX.length());
		String suiteName = request.getParameter(QdosConstants.SUITE_NAME);
		String siteDuration = request.getParameter(QdosConstants.SITE_DURATION);
		String suiteDuration = request.getParameter(QdosConstants.SUITE_DURATION);
		String testCaseDuration = request.getParameter(QdosConstants.TEST_CASE_DURATION);
		String testCaseId = request.getParameter(QdosConstants.TEST_CASE_ID);
		String testStepString = request.getParameter(QdosConstants.TEST_STEP_DATA);
		
		logger.debug("HANDLING pushTestStep() :: REQUEST PARAMS : "
				+"\n\t email = "+email
				+"\n\t activeAPIkey = "+activeAPIkey
				+"\n\t siteName = "+siteName
				+"\n\t versionNumber = "+versionNumber
				+"\n\t suiteName = "+suiteName
				+"\n\t siteDuration = "+siteDuration
				+"\n\t suiteDuration = "+suiteDuration
				+"\n\t testCaseDuration = "+testCaseDuration
				+"\n\t testCaseId = "+testCaseId
				+"\n\t testStepString = "+testStepString
				);
		
		User user = new User();
		user.setEmail(email);
		user.setDefaultAPIkey(activeAPIkey);
		
		logger.debug("HANDLING pushTestStep() REQUEST : " + user);
		User validUser = userDao.getUserDetailsViaEmailAndDefaultApiKey(user);
		logger.debug("pushTestStep() :: validUser = " + validUser);
		if (validUser!=null){
			QdosReport qdosReport = new QdosReport();
			qdosReport.setEmail(email);
			qdosReport.setSiteName(siteName);
			qdosReport.setVersionNumber(Long.parseLong(versionNumber));
			qdosReport.setTotalTime(Long.parseLong(siteDuration));
			logger.debug("pushTestStep() :: qdosReport = " + qdosReport);
			JSONObject testStepJsonObject = new JSONObject(testStepString);
			logger.debug("pushTestStep() :: testStepJsonObject = " + testStepJsonObject);
			TestStep testStepJavaObject = convertTestStepJsonObjectToJavaObject(testStepJsonObject);
			logger.debug("pushTestStep() :: testStepJavaObject = " + testStepJavaObject);
			if (qdosReportDao.updateTestStepInTestCase(qdosReport, suiteName, Long.parseLong(suiteDuration), testCaseId, Long.parseLong(testCaseDuration), testStepJavaObject)){
				Response customResponse = new Response();
				customResponse.setMessage(ResponseCodes.SUCCESS.message);
				customResponse.setStatus(ResponseCodes.SUCCESS.code);
				return customResponse;
			} else {
				logger.error("Test step not updated in Suite Name = "+suiteName+" :: under Site = "+siteName+" :: in Version = "+versionNumber+" :: in Test Case ID = "+testCaseId+" :: on Email = "+email+" :: with Test step String = "+testStepString);
				throw new InternalServerErrorException(ResponseCodes.INTERNAL_ERROR.message);
			}
		} else {
			throw new InvalidActiveAPIException(ResponseCodes.INVALID_ACTIVE_API_KEY.message);
		}
	}

	private Object processTestCase(HttpServletRequest request, String startOrFinishStatus) throws JSONException {
		String email = request.getParameter(User.FIELDS.EMAIL);
		String activeAPIkey = request.getParameter(User.FIELDS.ACTIVE_API_KEY);
		String siteName = request.getParameter(QdosReport.FIELDS.SITE_NAME);
		String versionNumberWithPrefix = request.getParameter(QdosReport.FIELDS.VERSION_NUMBER);
		String versionNumber = versionNumberWithPrefix.substring(QdosReport.FIELDS.VERSION_NAME_PREFIX.length());
		String suiteName = request.getParameter(QdosConstants.SUITE_NAME);
		String testCaseString = request.getParameter(QdosConstants.TEST_CASE_DATA);
		String siteDuration = request.getParameter(QdosConstants.SITE_DURATION);
		String suiteDuration = request.getParameter(QdosConstants.SUITE_DURATION);
		
		User user = new User();
		user.setEmail(email);
		user.setDefaultAPIkey(activeAPIkey);
		
		logger.debug("HANDLING processTestCase() REQUEST : " + user);
		User validUser = userDao.getUserDetailsViaEmailAndDefaultApiKey(user);
		if (validUser!=null){
			JSONObject testCaseJsonObject = new JSONObject(testCaseString);
			TestCase testCaseJavaObject = convertTestCaseJsonObjectToJavaObject(testCaseJsonObject, startOrFinishStatus);
			
			QdosReport qdosReport = new QdosReport();
			qdosReport.setEmail(email);
			qdosReport.setSiteName(siteName);
			qdosReport.setVersionNumber(Long.parseLong(versionNumber));
			qdosReport.setTotalTime(Long.parseLong(siteDuration));
			
			if (qdosReportDao.updateTestCaseInTestSuite(startOrFinishStatus, qdosReport, suiteName, Long.parseLong(suiteDuration), testCaseJavaObject)){
				Response customResponse = new Response();
				customResponse.setMessage(ResponseCodes.SUCCESS.message);
				customResponse.setStatus(ResponseCodes.SUCCESS.code);
				return customResponse;
			} else {
				logger.error("Test case not updated in Suite Name = "+suiteName+" :: under Site = "+siteName+" :: in Version = "+versionNumber+" :: on Email = "+email+" :: with Test Case String = "+testCaseString);
				throw new InternalServerErrorException(ResponseCodes.INTERNAL_ERROR.message);
			}
		} else {
			throw new InvalidActiveAPIException(ResponseCodes.INVALID_ACTIVE_API_KEY.message);
		}
	}
	
	private TestCase convertTestCaseJsonObjectToJavaObject(JSONObject testCaseJsonObject, String startOrFinishStatus) throws JSONException {
		TestCase testCaseJavaObject = new TestCase();
		testCaseJavaObject.setTestCaseId(testCaseJsonObject.getString(QdosConstants.TEST_CASE_ID));
		testCaseJavaObject.setTestCaseName(testCaseJsonObject.getString(QdosConstants.TEST_CASE_NAME));
		testCaseJavaObject.setStatusClass(testCaseJsonObject.getString(QdosConstants.STATUS_CLASS));
		testCaseJavaObject.setStartTime(testCaseJsonObject.getString(QdosConstants.START_TIME));
		testCaseJavaObject.setDuration(testCaseJsonObject.getLong(QdosConstants.DURATION));
		if (startOrFinishStatus.equals(QdosStatus.REAL_TIME_STATUS.NEW)){
			testCaseJavaObject.setStatus(QdosStatus.REAL_TIME_STATUS.RUNNING);
			testCaseJavaObject.setEndTime(QdosStatus.REAL_TIME_STATUS.RUNNING);
		} else {
			testCaseJavaObject.setStatus(testCaseJsonObject.getString(QdosReport.FIELDS.STATUS));
			testCaseJavaObject.setEndTime(testCaseJsonObject.getString(QdosConstants.END_TIME));
			testCaseJavaObject.setVideoFile(testCaseJsonObject.getString(QdosConstants.VIDEO_FILE));
		}
		
		return testCaseJavaObject;
	}
	
	private TestStep convertTestStepJsonObjectToJavaObject(JSONObject testStepJsonObject) throws JSONException {
		TestStep testStepJavaObject = new TestStep();
		if (testStepJsonObject.has(QdosConstants.DATA_SET_ID)){
			testStepJavaObject.setDataSetId(testStepJsonObject.getString(QdosConstants.DATA_SET_ID));
		}
		if (testStepJsonObject.has(QdosConstants.STEP_ID)){
			testStepJavaObject.setStepId(testStepJsonObject.getString(QdosConstants.STEP_ID));
		}
		if (testStepJsonObject.has(QdosConstants.STEP_DESCRIPTION)){
			testStepJavaObject.setStepDescription(testStepJsonObject.getString(QdosConstants.STEP_DESCRIPTION));
		}
		if (testStepJsonObject.has(QdosConstants.STEP_KEYWORD)){
			testStepJavaObject.setStepKeyword(testStepJsonObject.getString(QdosConstants.STEP_KEYWORD));
		}
		if (testStepJsonObject.has(QdosConstants.STEP_STATUS)){
			testStepJavaObject.setStepStatus(testStepJsonObject.getString(QdosConstants.STEP_STATUS));
		}
		if (testStepJsonObject.has(QdosConstants.PROCEED_ON_FAIL)){
			testStepJavaObject.setProceedOnFail(testStepJsonObject.getString(QdosConstants.PROCEED_ON_FAIL));
		}
		if (testStepJsonObject.has(QdosConstants.STEP_STATUS_CLASS)){
			testStepJavaObject.setStepStatusClass(testStepJsonObject.getString(QdosConstants.STEP_STATUS_CLASS));
		}
		if (testStepJsonObject.has(QdosConstants.STEP_SCREENSHOT)){
			testStepJavaObject.setStepScreenShot(testStepJsonObject.getString(QdosConstants.STEP_SCREENSHOT));
		}
		if (testStepJsonObject.has(QdosConstants.SYSTEM_MESSAGE)){
			testStepJavaObject.setSystemMessage(testStepJsonObject.getString(QdosConstants.SYSTEM_MESSAGE));
		}
		if (testStepJsonObject.has(QdosConstants.STEP_PAGE_STATS)){
			testStepJavaObject.setStepPageStats(testStepJsonObject.getString(QdosConstants.STEP_PAGE_STATS));
		}
		if (testStepJsonObject.has(QdosConstants.DURATION)){
			testStepJavaObject.setDuration(testStepJsonObject.getLong(QdosConstants.DURATION));
		}
		return testStepJavaObject;
	}
	
	private Object processTestSuite(HttpServletRequest request, String status) {
		String email = request.getParameter(User.FIELDS.EMAIL);
		String activeAPIkey = request.getParameter(User.FIELDS.ACTIVE_API_KEY);
		String siteName = request.getParameter(QdosReport.FIELDS.SITE_NAME);
		String versionNumberWithPrefix = request.getParameter(QdosReport.FIELDS.VERSION_NUMBER);
		String versionNumber = versionNumberWithPrefix.substring(QdosReport.FIELDS.VERSION_NAME_PREFIX.length());
		String suiteName = request.getParameter(QdosConstants.SUITE_NAME);
		String startTime = request.getParameter(QdosConstants.START_TIME);
		String endTime = request.getParameter(QdosConstants.END_TIME);
		String duration = request.getParameter(QdosConstants.SUITE_DURATION);
		String siteDuration = request.getParameter(QdosConstants.SITE_DURATION);
		User user = new User();
		user.setEmail(email);
		user.setDefaultAPIkey(activeAPIkey);
		
		logger.debug("HANDLING processTestSuite() REQUEST : " + user);
		User validUser = userDao.getUserDetailsViaEmailAndDefaultApiKey(user);
		if (validUser!=null){
			TestSuiteReport testSuiteReport = new TestSuiteReport();
			testSuiteReport.setSuiteName(suiteName);
			testSuiteReport.setReport(new Report());
			testSuiteReport.getReport().setTestSuiteDetails(new TestSuiteDetails());
			testSuiteReport.getReport().getTestSuiteDetails().setDuration(Long.parseLong(duration));
			QdosReport qdosReport = new QdosReport();
			qdosReport.setEmail(email);
			qdosReport.setSiteName(siteName);
			qdosReport.setVersionNumber(Long.parseLong(versionNumber));
			qdosReport.setTotalTime(Long.parseLong(siteDuration));
			if (status.equals(QdosStatus.REAL_TIME_STATUS.NEW)){
				testSuiteReport.getReport().getTestSuiteDetails().setStartTime(startTime);
				testSuiteReport.getReport().getTestSuiteDetails().setEndTime(QdosStatus.REAL_TIME_STATUS.RUNNING);
				testSuiteReport.getReport().getTestSuiteDetails().setSummary(new ResultSummary());
				testSuiteReport.getReport().getTestSuiteDetails().getSummary().setAborted(0L);
				testSuiteReport.getReport().getTestSuiteDetails().getSummary().setFailed(0L);
				testSuiteReport.getReport().getTestSuiteDetails().getSummary().setPassed(0L);
				testSuiteReport.getReport().getTestSuiteDetails().getSummary().setManual(0L);
				testSuiteReport.getReport().getTestSuiteDetails().getSummary().setTotal(0L);
				if (qdosReportDao.startTestSuiteReport(qdosReport, testSuiteReport)){
					Response customResponse = new Response();
					customResponse.setMessage(ResponseCodes.SUCCESS.message);
					customResponse.setStatus(ResponseCodes.SUCCESS.code);
					return customResponse;
				} else {
					logger.error("Test Suite not updated in Suite Name = "+suiteName+" :: under Site = "+siteName+" :: in Version = "+versionNumber);
					throw new InternalServerErrorException(ResponseCodes.INTERNAL_ERROR.message);
				}
			} else {
				testSuiteReport.getReport().getTestSuiteDetails().setEndTime(endTime);
				if (qdosReportDao.finishTestSuiteReport(qdosReport, suiteName, testSuiteReport)){
					Response customResponse = new Response();
					customResponse.setMessage(ResponseCodes.SUCCESS.message);
					customResponse.setStatus(ResponseCodes.SUCCESS.code);
					return customResponse;
				} else {
					logger.error("Test Suite not updated in Suite Name = "+suiteName+" :: under Site = "+siteName+" :: in Version = "+versionNumber);
					throw new InternalServerErrorException(ResponseCodes.INTERNAL_ERROR.message);
				}
			}
			
			
			
		} else {
			throw new InvalidActiveAPIException(ResponseCodes.INVALID_ACTIVE_API_KEY.message);
		}
	}
	
}
