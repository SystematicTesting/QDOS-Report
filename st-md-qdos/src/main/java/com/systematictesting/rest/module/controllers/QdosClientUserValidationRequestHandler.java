package com.systematictesting.rest.module.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.systematictesting.daolayer.beans.AwsCreds;
import com.systematictesting.daolayer.beans.response.QdosReportStartVersion;
import com.systematictesting.daolayer.constants.QdosConstants;
import com.systematictesting.daolayer.constants.ResponseCodes;
import com.systematictesting.daolayer.constants.Status;
import com.systematictesting.daolayer.entity.Catalog;
import com.systematictesting.daolayer.entity.QdosReport;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.entity.UserSite;
import com.systematictesting.daolayer.exceptions.InternalServerErrorException;
import com.systematictesting.daolayer.exceptions.InvalidActiveAPIException;
import com.systematictesting.daolayer.exceptions.SiteNameNotActiveException;
import com.systematictesting.daolayer.exceptions.UserSiteNotPresentException;
import com.systematictesting.daolayer.services.CatalogDao;
import com.systematictesting.daolayer.services.QdosReportDao;
import com.systematictesting.daolayer.services.UserDao;
import com.systematictesting.daolayer.services.UserSiteDao;
import com.systematictesting.rest.core.beans.Response;
import com.systematictesting.rest.core.controllers.AbstractController;
import com.systematictesting.services.core.SystemPropertiesService;

@Controller
@RequestMapping("/module/qdos/user")
public class QdosClientUserValidationRequestHandler extends AbstractController{
	private static final Logger logger = LoggerFactory.getLogger(QdosClientUserValidationRequestHandler.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserSiteDao userSiteDao;
	
	@Autowired
	private QdosReportDao qdosReportDao;
	
	@Autowired
	private CatalogDao catalogDao;
	
	@Autowired
	private SystemPropertiesService systemPropertiesService;
	
	@RequestMapping(value = "/startSiteVersion.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object startSiteVersion(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		String email = request.getParameter(User.FIELDS.EMAIL);
		String activeAPIkey = request.getParameter(User.FIELDS.ACTIVE_API_KEY);
		String siteName = request.getParameter(QdosReport.FIELDS.SITE_NAME);
		String operatingSystem = request.getParameter(QdosReport.FIELDS.OPERATING_SYSTEM);
		String browser = request.getParameter(QdosReport.FIELDS.BROWSER);
		String versionStartTime = request.getParameter(QdosConstants.START_TIME);
		String catalogVersion = request.getParameter(QdosConstants.CATALOG_VERSION);
		
		User user = new User();
		user.setEmail(email);
		user.setDefaultAPIkey(activeAPIkey);
		
		logger.debug("HANDLING /startSiteVersion.rest REQUEST : " + user);
		User validUser = userDao.getUserDetailsViaEmailAndDefaultApiKey(user);
		if (validUser!=null & validUser.getStatus().equals(Status.USER_STATUS.ACTIVE)){
			logger.debug("RESPONSE /startSiteVersion.rest : " + validUser);
			UserSite userSite = new UserSite();
			userSite.setEmail(validUser.getEmail());
			userSite.setSiteName(siteName);
			UserSite validUserSite = userSiteDao.getUserSite(userSite);
			if (validUserSite!=null && validUserSite.getStatus().equals(Status.SITE_STATUS.ACTIVE)){
				return setNextSiteVersionInNEWMode(email, siteName, operatingSystem, browser, versionStartTime, catalogVersion);
			} else if (validUserSite!=null && validUserSite.getStatus().equals(Status.SITE_STATUS.DELETED)) {
				throw new SiteNameNotActiveException(ResponseCodes.SITE_NAME_NOT_ACTIVE.message);
			} else {
				logger.error("Requested User Site is NOT PRESENT in userSite table.");
				throw new UserSiteNotPresentException(ResponseCodes.USER_SITE_NOT_PRESENT.message);
			}
			
		} else {
			throw new InvalidActiveAPIException(ResponseCodes.INVALID_ACTIVE_API_KEY.message);
		}
	}
	
	@RequestMapping(value = "/finishSiteVersion.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object finishSiteVersion(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, JSONException {
		String email = request.getParameter(User.FIELDS.EMAIL);
		String activeAPIkey = request.getParameter(User.FIELDS.ACTIVE_API_KEY);
		String siteName = request.getParameter(QdosReport.FIELDS.SITE_NAME);
		String browser = request.getParameter(QdosReport.FIELDS.BROWSER);
		String versionNumberWithPrefix = request.getParameter(QdosReport.FIELDS.VERSION_NUMBER);
		String versionNumber = versionNumberWithPrefix.substring(QdosReport.FIELDS.VERSION_NAME_PREFIX.length());
		String versionEndTime = request.getParameter(QdosConstants.END_TIME);
		String totalTime = request.getParameter(QdosConstants.SITE_DURATION);
		
		User user = new User();
		user.setEmail(email);
		user.setDefaultAPIkey(activeAPIkey);
		
		logger.debug("HANDLING /finishSiteVersion.rest REQUEST : " + user);
		User validUser = userDao.getUserDetailsViaEmailAndDefaultApiKey(user);
		if (validUser!=null){
			QdosReport qdosReport = new QdosReport();
			qdosReport.setEmail(email);
			qdosReport.setSiteName(siteName);
			qdosReport.setBrowser(browser);
			qdosReport.setVersionNumber(Long.parseLong(versionNumber));
			qdosReport.setVersionEndTime(versionEndTime);
			qdosReport.setTotalTime(Long.parseLong(totalTime));
			
			if (qdosReportDao.finishSiteVersion(qdosReport)) {
				Response customResponse = new Response();
				customResponse.setMessage(ResponseCodes.SUCCESS.message);
				customResponse.setStatus(ResponseCodes.SUCCESS.code);
				return customResponse;
			} else {
				logger.error("SiteName and its version FAILED to set in FINISH MODE.");
				throw new InternalServerErrorException(ResponseCodes.INTERNAL_ERROR.message);
			}
		} else {
			throw new InvalidActiveAPIException(ResponseCodes.INVALID_ACTIVE_API_KEY.message);
		}
	}
	
	

	private Object setNextSiteVersionInNEWMode(String email, String siteName, String operatingSystem, String browser, String versionStartTime, String catalogVersion) throws IOException {
		
		Catalog savedCatalog = null;
		Catalog catalogReq = new Catalog();
		catalogReq.setEmail(email);
		catalogReq.setSiteName(siteName);
		Long cVersionNumber = null;
		try{
			if (catalogVersion.startsWith(Catalog.FIELDS.VERSION_NAME_PREFIX)){
				cVersionNumber = Long.parseLong(catalogVersion.substring(1));
			} else {
				cVersionNumber = Long.parseLong(catalogVersion);
			}
			catalogReq.setVersionNumber(cVersionNumber);
			savedCatalog = catalogDao.getVersionOfCatalog(catalogReq);
		} catch (Exception e){
			savedCatalog = catalogDao.getLatestVersionOfCatalog(catalogReq);
		}
		
		if (savedCatalog.getCatalogSuites().size()>0){
			QdosReport qdosReport = new QdosReport();
			qdosReport.setEmail(email);
			qdosReport.setSiteName(siteName);
			qdosReport.setBrowser(browser);
			qdosReport.setOperatingSystem(operatingSystem);
			qdosReport.setVersionStartTime(versionStartTime);
			qdosReport.setCatalogVersion(catalogVersion);
			
			String versionNumber = qdosReportDao.startSiteVersion(qdosReport);
			if (versionNumber!=null && versionNumber.length()>0){
				AwsCreds awsCredentials = null;
				String awsAccessKey = systemPropertiesService.getProperties().getProperty(QdosConstants.AWS_ACCESSKEY);
				String awsSecretKey = systemPropertiesService.getProperties().getProperty(QdosConstants.AWS_SECRETKEY);
				String awsFolderName = systemPropertiesService.getProperties().getProperty(QdosConstants.AWS_S3_FOLDERNAME);
				String awsBucketName = systemPropertiesService.getProperties().getProperty(QdosConstants.AWS_S3_BUCKETNAME);
				String awsRegionName = systemPropertiesService.getProperties().getProperty(QdosConstants.AWS_S3_REGION);
				String awsHostingUrl = systemPropertiesService.getProperties().getProperty(QdosConstants.AWS_S3_HOSTING_URL);
				if (StringUtils.isNotBlank(awsAccessKey) && StringUtils.isNotBlank(awsSecretKey) 
						&& StringUtils.isNotBlank(awsFolderName) && StringUtils.isNotBlank(awsBucketName)
						&& StringUtils.isNotBlank(awsRegionName) && StringUtils.isNotBlank(awsHostingUrl)){
					awsCredentials = new AwsCreds(awsAccessKey, awsSecretKey, awsBucketName, awsFolderName, awsRegionName, awsHostingUrl);
				}
				
				QdosReportStartVersion response = new QdosReportStartVersion();
				response.setCatalogSuites(savedCatalog.getCatalogSuites());
				response.setCatalogVersionNumber(Catalog.FIELDS.VERSION_NAME_PREFIX+savedCatalog.getVersionNumber());
				response.setNextQdosVersionNumber(versionNumber);
				response.setAwsCredentials(awsCredentials);
				return response;
			} else {
				logger.error("SiteName and its version FAILED to set in NEW MODE.");
				throw new InternalServerErrorException(ResponseCodes.INTERNAL_ERROR.message);
			}
		} else {
			logger.error("Catalog Suites are empty.");
			throw new InternalServerErrorException(ResponseCodes.NO_CATALOG_SUITES_AVAILABLE.message);
		}
	}
}
