package com.systematictesting.rest.core.controllers;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
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

import com.systematictesting.daolayer.constants.ResponseCodes;
import com.systematictesting.daolayer.constants.Status;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.daolayer.entity.UserSite;
import com.systematictesting.daolayer.entity.UserSite;
import com.systematictesting.daolayer.exceptions.InternalServerErrorException;
import com.systematictesting.daolayer.services.UserSiteDao;
import com.systematictesting.rest.core.beans.Response;

@Controller
@RequestMapping("/userSite")
public class UserSiteRequestHandler extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(UserSiteRequestHandler.class);

	@Autowired
	private UserSiteDao userSiteDao;

	@RequestMapping(value = "/add.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object addUserSite(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ParseException {
		logger.debug("HANDLING /userSite/add.rest");
		User userFromApiKey = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		String siteName = request.getParameter(UserSite.FIELDS.SITE_NAME);
		String description = request.getParameter(UserSite.FIELDS.DESCRIPTION);
		String shareStatus = request.getParameter(UserSite.FIELDS.SHARE_STATUS);
		UserSite userSite = new UserSite();
		userSite.setSiteName(siteName);
		userSite.setDescription(description);
		if (shareStatus.equalsIgnoreCase(Status.SHARE_STATUS.PUBLIC)){
			userSite.setShareStatus(Status.SHARE_STATUS.PUBLIC);
		} else if (shareStatus.equalsIgnoreCase(Status.SHARE_STATUS.COMPANY)){
			userSite.setShareStatus(Status.SHARE_STATUS.COMPANY);
		}
		
		userSite.setEmail(userFromApiKey.getEmail());
		
		logger.debug("HANDLING /userSite/add.rest REQUEST : " + userFromApiKey);
		logger.debug("HANDLING /userSite/add.rest LICENSE OBJECT : " + userSite);
		
		Response customResponse = new Response();
		if (userSiteDao.addUserSite(userSite)) {
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
		}
		logger.debug("RESPONSE /userSite/add.rest : " + customResponse);
		return customResponse;
		
	}
	
	@RequestMapping(value = "/saveMySitesStatus.rest", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object saveMySitesStatus(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<UserSite> userSites = new ArrayList<UserSite>();
		int index=0;
		while(true){
			String email = request.getParameter(UserSite.FIELDS.EMAIL+"-"+index);
			String siteName = request.getParameter(UserSite.FIELDS.SITE_NAME+"-"+index);
			String shareStatus = request.getParameter(UserSite.FIELDS.SHARE_STATUS+"-"+index);
			String siteStatus = request.getParameter(UserSite.FIELDS.STATUS+"-"+index);
			if (email!=null 
					&& siteName!=null 
					&& shareStatus!=null 
					&& (shareStatus.equals(Status.SHARE_STATUS.COMPANY) || shareStatus.equals(Status.SHARE_STATUS.PRIVATE) || shareStatus.equals(Status.SHARE_STATUS.PUBLIC))
					&& siteStatus!=null){
				UserSite userSite = new UserSite();
				userSite.setEmail(email);
				userSite.setSiteName(siteName);
				userSite.setShareStatus(shareStatus);
				userSite.setStatus(siteStatus);
				userSites.add(userSite);
			}
			if (email==null){
				break;
			}
			index++;
		}
		if (userSiteDao.saveUserSitesShareStatus(userSites)){
			Response customResponse = new Response();
			customResponse.setMessage(ResponseCodes.SUCCESS.message);
			customResponse.setStatus(ResponseCodes.SUCCESS.code);
			return customResponse;
		} else {
			throw new InternalServerErrorException(ResponseCodes.INTERNAL_ERROR.message);
		}
	}
	
	@RequestMapping(value = "/mysites.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object mysites(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(request);
		String siteStatus = request.getParameter(UserSite.FIELDS.STATUS);
		String shareStatus = request.getParameter(UserSite.FIELDS.SHARE_STATUS);
		List<UserSite> userSiteList = userSiteDao.getFilterdUserSites(user.getEmail(), siteStatus,shareStatus);
		return userSiteList;
	}
	
	@RequestMapping(value = "/sharedSites.rest", method = RequestMethod.GET, produces = { "application/json" }, consumes = { "text/plain", "application/*" })
	public @ResponseBody
	Object sharedSites(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String siteStatus = request.getParameter(UserSite.FIELDS.STATUS);
		List<UserSite> userSiteList = userSiteDao.getSharedUserSites(null, siteStatus);
		return userSiteList;
	}


}
