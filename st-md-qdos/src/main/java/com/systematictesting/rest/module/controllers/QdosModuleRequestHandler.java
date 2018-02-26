package com.systematictesting.rest.module.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.systematictesting.daolayer.beans.response.DetailedSharedSites;
import com.systematictesting.daolayer.beans.response.DetailedUserSiteVersion;
import com.systematictesting.daolayer.beans.response.UserSiteVersion;
import com.systematictesting.daolayer.constants.QdosConstants;
import com.systematictesting.daolayer.constants.Status;
import com.systematictesting.daolayer.entity.QdosReport;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.entity.UserSite;
import com.systematictesting.daolayer.exceptions.MissingMandatoryAPIParameters;
import com.systematictesting.daolayer.services.QdosReportDao;
import com.systematictesting.daolayer.services.UserDao;
import com.systematictesting.daolayer.services.UserSiteDao;
import com.systematictesting.rest.core.controllers.AbstractController;

@Controller
@RequestMapping("/module/qdos/sreport")
public class QdosModuleRequestHandler extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(QdosModuleRequestHandler.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserSiteDao userSiteDao;

	@Autowired
	private QdosReportDao qdosReportDao;
	
	@RequestMapping(value = "/detailedSharedSites.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object detailedSharedSites(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		List<DetailedSharedSites> listDetailedSharedSites = new ArrayList<DetailedSharedSites>();
		String siteStatus = request.getParameter(UserSite.FIELDS.STATUS);
		List<UserSite> userSiteList = userSiteDao.getSharedUserSites(null, siteStatus);
		assembleResponseList(listDetailedSharedSites, siteStatus, userSiteList, user);
		return listDetailedSharedSites;
	}
	
	
	@RequestMapping(value = "/userSharedSites.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object userSharedSites(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		List<DetailedSharedSites> listDetailedSharedSites = new ArrayList<DetailedSharedSites>();
		String siteStatus = request.getParameter(UserSite.FIELDS.STATUS);
		String email = request.getParameter(User.FIELDS.EMAIL);
		List<UserSite> userSiteList = userSiteDao.getSharedUserSites(email, siteStatus);
		assembleResponseList(listDetailedSharedSites, siteStatus, userSiteList, user);
		return listDetailedSharedSites;
	}

	@RequestMapping(value = "/userAllSites.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object userAllSites(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<DetailedSharedSites> listDetailedSharedSites = new ArrayList<DetailedSharedSites>();
		String siteStatus = request.getParameter(UserSite.FIELDS.STATUS);
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		List<UserSite> userSiteList = userSiteDao.getAllUserSites(user.getEmail(), siteStatus);
		assembleResponseList(listDetailedSharedSites, siteStatus, userSiteList, null);
		return listDetailedSharedSites;
	}

	private void assembleResponseList(List<DetailedSharedSites> listDetailedSharedSites, String siteStatus, List<UserSite> userSiteList, User requestorUser) {
		logger.debug("userSiteList = "+userSiteList);
		String companyName = null;
		if (requestorUser!=null){
			User fetchedUser = userDao.getUserDetailsViaEmail(requestorUser);
			if (fetchedUser!=null && fetchedUser.getCompanyname()!=null){
				companyName = fetchedUser.getCompanyname();
			}
		}
		
		for (UserSite userSite : userSiteList){
			User userReq = new User();
			userReq.setEmail(userSite.getEmail());
			User savedUser= userDao.getUserDetailsViaEmail(userReq);
			
			if (companyName==null || 
					userSite.getShareStatus().equals(Status.SHARE_STATUS.PUBLIC) || 
					(userSite.getShareStatus().equals(Status.SHARE_STATUS.COMPANY) && 
							savedUser!=null && 
							savedUser.getCompanyname().equalsIgnoreCase(companyName)) ){
				QdosReport qdosReportReq = new QdosReport();
				qdosReportReq.setEmail(userSite.getEmail());
				qdosReportReq.setSiteName(userSite.getSiteName());
				QdosReport savedQdosReport = qdosReportDao.getLatestVersionOfSite(qdosReportReq);
				if (savedQdosReport!=null && savedUser!=null){
					DetailedSharedSites objDetailedSharedSites = new DetailedSharedSites();
					objDetailedSharedSites.setEmail(userSite.getEmail());
					objDetailedSharedSites.setStatus(siteStatus);
					objDetailedSharedSites.setShareStatus(userSite.getShareStatus());
					objDetailedSharedSites.setSiteName(userSite.getSiteName());
					objDetailedSharedSites.setOwnerName(savedUser.getFirstname()+" "+savedUser.getLastname());
					objDetailedSharedSites.setAborted(savedQdosReport.getVersionReport().getAborted());
					objDetailedSharedSites.setPass(savedQdosReport.getVersionReport().getPassed());
					objDetailedSharedSites.setManual(savedQdosReport.getVersionReport().getManual());
					objDetailedSharedSites.setFail(savedQdosReport.getVersionReport().getFailed());
					objDetailedSharedSites.setStartTime(savedQdosReport.getVersionStartTime());
					objDetailedSharedSites.setEndTime(savedQdosReport.getVersionEndTime());
					objDetailedSharedSites.setVersion(QdosReport.FIELDS.VERSION_NAME_PREFIX+savedQdosReport.getVersionNumber());
					objDetailedSharedSites.setBrowser(savedQdosReport.getBrowser());
					objDetailedSharedSites.setTotalTime(savedQdosReport.getTotalTime());
					objDetailedSharedSites.setOperatingSystem(savedQdosReport.getOperatingSystem());
					objDetailedSharedSites.setCatalogVersion(savedQdosReport.getCatalogVersion());
					
					listDetailedSharedSites.add(objDetailedSharedSites);
				}
			}
		}
	}
	
	
	@RequestMapping(value = "/versionDetailsOfSite.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object versionDetailsOfSite(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String email = request.getParameter(User.FIELDS.EMAIL);
		String siteName = request.getParameter(QdosConstants.SITE_NAME);
		String siteStatus = request.getParameter(UserSite.FIELDS.STATUS);
		String versionNumberWithPrefix = request.getParameter(QdosReport.FIELDS.VERSION_NUMBER);
		String versionNumber = versionNumberWithPrefix.substring(QdosReport.FIELDS.VERSION_NAME_PREFIX.length());
		QdosReport qdosReportReq = new QdosReport();
		qdosReportReq.setEmail(email);
		qdosReportReq.setSiteName(siteName);
		qdosReportReq.setVersionNumber(Long.parseLong(versionNumber));
		qdosReportReq.setStatus(siteStatus);
		QdosReport qdosReport = qdosReportDao.getVersionOfSite(qdosReportReq);
		
		DetailedUserSiteVersion objDetailedUserSiteVersion = new DetailedUserSiteVersion();
		objDetailedUserSiteVersion.setBrowser(qdosReport.getBrowser());
		objDetailedUserSiteVersion.setEmail(qdosReport.getEmail());
		objDetailedUserSiteVersion.setOperatingSystem(qdosReport.getOperatingSystem());
		objDetailedUserSiteVersion.setCatalogVersion(qdosReport.getCatalogVersion());
		objDetailedUserSiteVersion.setSiteName(qdosReport.getSiteName());
		objDetailedUserSiteVersion.setStatus(qdosReport.getStatus());
		objDetailedUserSiteVersion.setTestSuitesReport(qdosReport.getTestSuitesReport());
		objDetailedUserSiteVersion.setTotalTime(qdosReport.getTotalTime());
		objDetailedUserSiteVersion.setEndTime(qdosReport.getVersionEndTime());
		objDetailedUserSiteVersion.setVersionNumber(QdosReport.FIELDS.VERSION_NAME_PREFIX+qdosReport.getVersionNumber());
		objDetailedUserSiteVersion.setVersionReport(qdosReport.getVersionReport());
		objDetailedUserSiteVersion.setStartTime(qdosReport.getVersionStartTime());
		return objDetailedUserSiteVersion;
	}
	
	@RequestMapping(value = "/versionHistoryOfUserSite.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object versionHistoryOfUserSite(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException,MissingMandatoryAPIParameters {
		String email = request.getParameter(User.FIELDS.EMAIL);
		String siteName = request.getParameter(QdosConstants.SITE_NAME);
		String siteStatus = request.getParameter(QdosReport.FIELDS.STATUS);
		String numberOfRecords = request.getParameter(QdosConstants.NO_OF_RECORDS);
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
		
		QdosReport qdosReportReq = new QdosReport();
		qdosReportReq.setEmail(email);
		qdosReportReq.setSiteName(siteName);
		qdosReportReq.setStatus(siteStatus);
		List<QdosReport> resultList = qdosReportDao.getVersionHistoryOfSite(qdosReportReq);
		if (intNumberOfRecords>=resultList.size()){
			intNumberOfRecords = resultList.size();
		}
		List<UserSiteVersion> listOfUserSiteVersions = new ArrayList<UserSiteVersion>();
		List<QdosReport> returnList = resultList.subList(0, intNumberOfRecords);
		for (QdosReport qdosReport : returnList){
			UserSiteVersion objUserSiteVersion = new UserSiteVersion();
			objUserSiteVersion.setBrowser(qdosReport.getBrowser());
			objUserSiteVersion.setOperatingSystem(qdosReport.getOperatingSystem());
			objUserSiteVersion.setCatalogVersion(qdosReport.getCatalogVersion());
			objUserSiteVersion.setStartTime(qdosReport.getVersionStartTime());
			objUserSiteVersion.setTotalTime(qdosReport.getTotalTime());
			objUserSiteVersion.setVersionNumber(QdosReport.FIELDS.VERSION_NAME_PREFIX+qdosReport.getVersionNumber());
			objUserSiteVersion.setVersionReport(qdosReport.getVersionReport());
			listOfUserSiteVersions.add(objUserSiteVersion);
		}
		Collections.reverse(listOfUserSiteVersions);
		return listOfUserSiteVersions;
	}

}
