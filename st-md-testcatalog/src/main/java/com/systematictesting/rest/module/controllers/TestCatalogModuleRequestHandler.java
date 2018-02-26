package com.systematictesting.rest.module.controllers;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.systematictesting.daolayer.beans.RequestedTestSuites;
import com.systematictesting.daolayer.beans.TestSuite;
import com.systematictesting.daolayer.beans.response.DetailedCatalogVersion;
import com.systematictesting.daolayer.beans.response.DetailedSharedCatalogs;
import com.systematictesting.daolayer.beans.response.UserCatalogVersion;
import com.systematictesting.daolayer.constants.Status;
import com.systematictesting.daolayer.constants.TestCatalogConstants;
import com.systematictesting.daolayer.constants.TestCatalogResponseCodes;
import com.systematictesting.daolayer.constants.TestCatalogStatus;
import com.systematictesting.daolayer.entity.Catalog;
import com.systematictesting.daolayer.entity.SingleTestSuite;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.entity.UserSite;
import com.systematictesting.daolayer.exceptions.MissingMandatoryAPIParameters;
import com.systematictesting.daolayer.exceptions.SessionNotValidException;
import com.systematictesting.daolayer.services.CatalogDao;
import com.systematictesting.daolayer.services.SingleTestSuiteDao;
import com.systematictesting.daolayer.services.UserDao;
import com.systematictesting.daolayer.services.UserSiteDao;
import com.systematictesting.rest.core.beans.Response;
import com.systematictesting.rest.core.controllers.AbstractController;
import com.systematictesting.rest.core.utils.TimeFormatHandler;
import com.systematictesting.services.core.SystemPropertiesService;

@Controller
@RequestMapping("/module/testcatalog/sreport")
public class TestCatalogModuleRequestHandler extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(TestCatalogModuleRequestHandler.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserSiteDao userSiteDao;

	@Autowired
	private CatalogDao catalogDao;

	@Autowired
	private SingleTestSuiteDao singleTestSuiteDao;
	
	@Autowired
	private SystemPropertiesService systemPropertiesService;
	
	@Autowired
	ServletContext servletContext;

	@RequestMapping(value = "/testSuitesLibrary.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object getTestSuitesLibrary(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		String siteName = request.getParameter(SingleTestSuite.FIELDS.SITE_NAME);
		SingleTestSuite singleTestSuite = new SingleTestSuite();
		singleTestSuite.setEmail(user.getEmail());
		singleTestSuite.setSiteName(siteName);
		List<SingleTestSuite> testSuitesLibrary = singleTestSuiteDao.getAllTestSuites(singleTestSuite);
		for (SingleTestSuite item : testSuitesLibrary) {
			item.setLastmodifiedtime((System.currentTimeMillis() - item.getLastmodifiedtime()) / 1000);
		}
		return testSuitesLibrary;
	}
	
	@RequestMapping(value = "/singleTestSuiteFromLibrary.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object getSingleTestSuiteFromLibrary(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		String siteName = request.getParameter(SingleTestSuite.FIELDS.SITE_NAME);
		String suiteName = request.getParameter(TestCatalogConstants.SUITE_NAME);
		SingleTestSuite singleTestSuite = new SingleTestSuite();
		singleTestSuite.setEmail(user.getEmail());
		singleTestSuite.setSiteName(siteName);
		singleTestSuite.setTestSuite(new TestSuite());
		singleTestSuite.getTestSuite().setSuiteName(suiteName);
		SingleTestSuite singleTestSuiteFromLibrary = singleTestSuiteDao.getSingleTestSuite(singleTestSuite);
		singleTestSuiteFromLibrary.setLastmodifiedtime((System.currentTimeMillis() - singleTestSuiteFromLibrary.getLastmodifiedtime()) / 1000);
		singleTestSuiteFromLibrary.setCreatetime((System.currentTimeMillis() - singleTestSuiteFromLibrary.getCreatetime()) / 1000);
		return singleTestSuiteFromLibrary;
	}

	@RequestMapping(value = "/downloadTestSuite.rest", method = RequestMethod.POST, produces = { "application/*" }, consumes = { "text/plain", "application/*" })
	public void downloadTestSuite(@RequestBody SingleTestSuite singleTestSuite, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to download TestSuites excel file.");
		}
		
		String defaultCatalogLocation = systemPropertiesService.getProperties().getProperty(TestCatalogConstants.BOOTUP_TEST_CATALOG_ROOT_PATH);
		String absoluteDirectoryLocationOfExcelSheet = defaultCatalogLocation + singleTestSuite.getFileLocation();
		String absoluteFilePathOfTestSuiteExcelSheet = absoluteDirectoryLocationOfExcelSheet + TestCatalogConstants.FILE_SEPARATOR + singleTestSuite.getFileName();
		logger.debug("DOWNLOADING FILE : "+absoluteFilePathOfTestSuiteExcelSheet);
		int length = 0;
		byte[] byteBuffer = new byte[4096];
		DataInputStream in = new DataInputStream(new FileInputStream(absoluteFilePathOfTestSuiteExcelSheet));
		
		String mime = servletContext.getMimeType(absoluteFilePathOfTestSuiteExcelSheet);
		response.setContentType(mime);
		
		ServletOutputStream outStream = response.getOutputStream();

		while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
			outStream.write(byteBuffer, 0, length);
		}
		in.close();
		outStream.close();
	}

	@RequestMapping(value = "/commitCatalog.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object commitCatalog(@RequestBody RequestedTestSuites requestedTestSuites, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to commit the list of TestSuites Library.");
		}
		List<SingleTestSuite> testSuitesLibraryToBeCommitted = requestedTestSuites.getTestSuitesLibrary();
		Catalog catalog = new Catalog();
		catalog.setEmail(user.getEmail());
		catalog.setSiteName(requestedTestSuites.getSiteName());
		catalog.setStatus(TestCatalogStatus.CATALOG_STATUS.ACTIVE);
		Catalog savedCatalog = catalogDao.getLatestVersionOfCatalog(catalog);
		if (savedCatalog == null) {
			catalog.setVersionNumber(1L);
		} else {
			catalog.setVersionNumber(savedCatalog.getVersionNumber() + 1);
		}
		catalog.setCatalogSuites(testSuitesLibraryToBeCommitted);
		Response customResponse = new Response();
		if (catalogDao.addCatalog(catalog)) {
			for (SingleTestSuite singleTestSuite : testSuitesLibraryToBeCommitted) {
				singleTestSuiteDao.deleteTestSuite(singleTestSuite);
			}
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}

	@RequestMapping(value = "/deleteTestSuitesLibrary.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object deleteTestSuitesLibrary(@RequestBody RequestedTestSuites requestedTestSuites, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to delete the list of TestSuites Library.");
		}
		List<SingleTestSuite> testSuitesLibraryToBeDeleted = requestedTestSuites.getTestSuitesLibrary();
		Response customResponse = new Response();
		if (singleTestSuiteDao.deleteTestSuites(testSuitesLibraryToBeDeleted)) {
			customResponse.setMessage(TestCatalogResponseCodes.SUCCESS.message);
			customResponse.setStatus(TestCatalogResponseCodes.SUCCESS.code);
		} else {
			customResponse.setMessage(TestCatalogResponseCodes.INTERNAL_ERROR.message);
			customResponse.setStatus(TestCatalogResponseCodes.INTERNAL_ERROR.code);
		}
		return customResponse;
	}

	@RequestMapping(value = "/allVersionsOfCatalog.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object getAllVersionsOfCatalog(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		if (user.getEmail() == null) {
			throw new SessionNotValidException("User Session is not valid to delete the list of TestSuites Library.");
		}
		String siteName = request.getParameter(UserSite.FIELDS.SITE_NAME);
		String emailOfSiteOwner = request.getParameter(UserSite.FIELDS.EMAIL);
		Catalog catalogReq = new Catalog();
		catalogReq.setEmail(emailOfSiteOwner);
		catalogReq.setSiteName(siteName);
		List<Catalog> versionsOfCatalog = catalogDao.getVersionHistoryOfCatalog(catalogReq);
		Map<String,List<SingleTestSuite>> versionVsListOfTestSuites = assembleVersionVsTestSuitesMap(versionsOfCatalog);
		return versionVsListOfTestSuites;
	}
	
	private Map<String, List<SingleTestSuite>> assembleVersionVsTestSuitesMap(List<Catalog> versionsOfCatalog) {
		Map<String, List<SingleTestSuite>> result = new HashMap<String, List<SingleTestSuite>>();
		for(Catalog catalogItem : versionsOfCatalog){
			result.put(Catalog.FIELDS.VERSION_NAME_PREFIX+catalogItem.getVersionNumber(), catalogItem.getCatalogSuites());
		}
		return result;
	}

	@RequestMapping(value = "/detailedSharedCatalogs.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object getDetailedSharedCatalogs(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		List<DetailedSharedCatalogs> listDetailedSharedCatalogs = new ArrayList<DetailedSharedCatalogs>();
		String siteStatus = request.getParameter(UserSite.FIELDS.STATUS);
		List<UserSite> userSiteList = userSiteDao.getSharedUserSites(null, siteStatus);
		assembleResponseList(listDetailedSharedCatalogs, siteStatus, userSiteList, user);
		return listDetailedSharedCatalogs;
	}
	
	@RequestMapping(value = "/userSharedCatalogs.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody Object getUserSharedCatalogs(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		List<DetailedSharedCatalogs> listDetailedSharedCatalogs = new ArrayList<DetailedSharedCatalogs>();
		String siteStatus = request.getParameter(UserSite.FIELDS.STATUS);
		String email = request.getParameter(User.FIELDS.EMAIL);
		List<UserSite> userSiteList = userSiteDao.getSharedUserSites(email, siteStatus);
		assembleResponseList(listDetailedSharedCatalogs, siteStatus, userSiteList, user);
		return listDetailedSharedCatalogs;
	}
	
	@RequestMapping(value = "/userAllSites.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object userAllSites(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<DetailedSharedCatalogs> listDetailedSharedSites = new ArrayList<DetailedSharedCatalogs>();
		String siteStatus = request.getParameter(UserSite.FIELDS.STATUS);
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		List<UserSite> userSiteList = userSiteDao.getAllUserSites(user.getEmail(), siteStatus);
		assembleResponseList(listDetailedSharedSites, siteStatus, userSiteList, null);
		return listDetailedSharedSites;
	}

	private void assembleResponseList(List<DetailedSharedCatalogs> listDetailedSharedCatalogs, String siteStatus, List<UserSite> userSiteList, User requestorUser) {
		logger.debug("userSiteList = " + userSiteList);
		String companyName = null;
		if (requestorUser != null) {
			User fetchedUser = userDao.getUserDetailsViaEmail(requestorUser);
			if (fetchedUser != null && fetchedUser.getCompanyname() != null) {
				companyName = fetchedUser.getCompanyname();
			}
		}

		for (UserSite userSite : userSiteList) {
			User userReq = new User();
			userReq.setEmail(userSite.getEmail());
			User savedUser = userDao.getUserDetailsViaEmail(userReq);

			if (companyName==null || 
					userSite.getShareStatus().equals(Status.SHARE_STATUS.PUBLIC) || 
					(userSite.getShareStatus().equals(Status.SHARE_STATUS.COMPANY) && 
							savedUser!=null && 
							savedUser.getCompanyname().equalsIgnoreCase(companyName)) ){
				Catalog catalog = new Catalog();
				catalog.setEmail(userSite.getEmail());
				catalog.setSiteName(userSite.getSiteName());
				catalog.setStatus(TestCatalogStatus.CATALOG_STATUS.ACTIVE);
				Catalog savedCatalog = catalogDao.getLatestVersionOfCatalog(catalog);
				if (savedUser != null) {
					DetailedSharedCatalogs objDetailedSharedCatalog = new DetailedSharedCatalogs();
					objDetailedSharedCatalog.setEmail(userSite.getEmail());
					objDetailedSharedCatalog.setSiteName(userSite.getSiteName());
					objDetailedSharedCatalog.setOwnerName(savedUser.getFirstname() + " " + savedUser.getLastname());
					objDetailedSharedCatalog.setDescription(userSite.getDescription());
					objDetailedSharedCatalog.setCreateTime(TimeFormatHandler.convertTimeStamp(userSite.getCreatetime()));
					if (savedCatalog != null) {
						objDetailedSharedCatalog.setVersion(Catalog.FIELDS.VERSION_NAME_PREFIX + savedCatalog.getVersionNumber());
						objDetailedSharedCatalog.setLastModifiedTime(TimeFormatHandler.convertTimeStamp(savedCatalog.getLastmodifiedtime()));
						objDetailedSharedCatalog.setNumberOfCatalogSuites((long) savedCatalog.getCatalogSuites().size());
					} else {
						objDetailedSharedCatalog.setVersion("NULL");
						objDetailedSharedCatalog.setLastModifiedTime(TimeFormatHandler.convertTimeStamp(userSite.getCreatetime()));
						objDetailedSharedCatalog.setNumberOfCatalogSuites(0L);
					}

					listDetailedSharedCatalogs.add(objDetailedSharedCatalog);
				}
			}
		}
	}
	
	@RequestMapping(value = "/versionHistoryOfUserSite.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object versionHistoryOfUserSite(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException,MissingMandatoryAPIParameters {
		String email = request.getParameter(User.FIELDS.EMAIL);
		String siteName = request.getParameter(TestCatalogConstants.SITE_NAME);
		String siteStatus = request.getParameter(Catalog.FIELDS.STATUS);
		String numberOfRecords = request.getParameter(TestCatalogConstants.NO_OF_RECORDS);
		int intNumberOfRecords = 10;
		try{
			if (numberOfRecords!=null){
				intNumberOfRecords = Integer.parseInt(numberOfRecords);
			}
		} catch(NumberFormatException e){
			e.printStackTrace();
			intNumberOfRecords = 10;
			logger.error("Number of Records to fetch is not a number in request.");
		}
		
		if (email==null && siteName==null){
			throw new MissingMandatoryAPIParameters("Mandatory parameters are missing in /versionHistoryOfUserSite.rest");
		}
		
		Catalog catalogReq = new Catalog();
		catalogReq.setEmail(email);
		catalogReq.setSiteName(siteName);
		catalogReq.setStatus(siteStatus);
		List<Catalog> resultList = catalogDao.getVersionHistoryOfCatalog(catalogReq);
		if (intNumberOfRecords>=resultList.size()){
			intNumberOfRecords = resultList.size();
		}
		List<UserCatalogVersion> listOfUserSiteVersions = new ArrayList<UserCatalogVersion>();
		List<Catalog> returnList = resultList.subList(0, intNumberOfRecords);
		for (Catalog catalog : returnList){
			UserCatalogVersion objUserCatalogVersion = new UserCatalogVersion();
			objUserCatalogVersion.setTotalNumberOfSuites(catalog.getCatalogSuites().size());
			int totalTestCases = 0;
			for (SingleTestSuite singleTestSuite : catalog.getCatalogSuites()){
				totalTestCases = totalTestCases + singleTestSuite.getTestSuite().getTestCaseArray().size();
			}
			objUserCatalogVersion.setTotalNumberOfTestCases(totalTestCases);
			objUserCatalogVersion.setCreateTime(TimeFormatHandler.convertTimeStamp(catalog.getCreatetime()));
			objUserCatalogVersion.setVersionNumber(Catalog.FIELDS.VERSION_NAME_PREFIX+catalog.getVersionNumber());
			listOfUserSiteVersions.add(objUserCatalogVersion);
		}
		Collections.reverse(listOfUserSiteVersions);
		return listOfUserSiteVersions;
	}
	
	@RequestMapping(value = "/versionDetailsOfSite.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object versionDetailsOfSite(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String email = request.getParameter(User.FIELDS.EMAIL);
		String siteName = request.getParameter(TestCatalogConstants.SITE_NAME);
		String siteStatus = request.getParameter(UserSite.FIELDS.STATUS);
		String versionNumberWithPrefix = request.getParameter(Catalog.FIELDS.VERSION_NUMBER);
		String versionNumber = versionNumberWithPrefix.substring(Catalog.FIELDS.VERSION_NAME_PREFIX.length());
		Catalog catalogReq = new Catalog();
		catalogReq.setEmail(email);
		catalogReq.setSiteName(siteName);
		catalogReq.setVersionNumber(Long.parseLong(versionNumber));
		catalogReq.setStatus(siteStatus);
		Catalog catalog = catalogDao.getVersionOfCatalog(catalogReq);
		
		DetailedCatalogVersion objDetailedCatalogVersion = new DetailedCatalogVersion();
		objDetailedCatalogVersion.setEmail(catalog.getEmail());
		objDetailedCatalogVersion.setSiteName(catalog.getSiteName());
		objDetailedCatalogVersion.setTestSuitesArray(catalog.getCatalogSuites());
		objDetailedCatalogVersion.setVersionNumber(Catalog.FIELDS.VERSION_NAME_PREFIX+catalog.getVersionNumber());
		objDetailedCatalogVersion.setCreateTime(TimeFormatHandler.convertTimeStamp(catalog.getCreatetime()));
		objDetailedCatalogVersion.setTotalTestSuites(catalog.getCatalogSuites().size());
		int totalTestCases = 0;
		for (SingleTestSuite singleTestSuite : catalog.getCatalogSuites()){
			totalTestCases = totalTestCases + singleTestSuite.getTestSuite().getTestCaseArray().size();
		}
		objDetailedCatalogVersion.setTotalTestCases(totalTestCases);
		
		return objDetailedCatalogVersion;
	}
}
