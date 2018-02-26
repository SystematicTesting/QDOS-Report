package com.systematictesting.daolayer.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.systematictesting.daolayer.constants.ResponseCodes;
import com.systematictesting.daolayer.constants.Status;
import com.systematictesting.daolayer.entity.UserSite;
import com.systematictesting.daolayer.exceptions.EmailNotValidException;
import com.systematictesting.daolayer.exceptions.UserSiteAlreadyPresentException;
import com.systematictesting.daolayer.services.UserSiteDao;

@Repository(UserSiteDao.SERVICE_NAME)
public class UserSiteDaoImpl implements UserSiteDao{
	
	private static final Logger logger = LoggerFactory.getLogger(UserSiteDaoImpl.class);
	

	@Autowired
	private MongoOperations mongoOperation;


	@Override
	public boolean addUserSite(UserSite userSite) {
		UserSite savedUserSite = queryUserSiteViaEmailAndSiteName(userSite);
		if (savedUserSite==null){
			userSite.setCreatetime(System.currentTimeMillis());
			userSite.setLastmodifiedtime(System.currentTimeMillis());
			if (userSite.getShareStatus()==null){
				userSite.setShareStatus(Status.SHARE_STATUS.PRIVATE);
			}
			userSite.setStatus(Status.SITE_STATUS.ACTIVE);
			try {
				mongoOperation.save(userSite);
				logger.debug("UserSite Saving Completed Successfully");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured while adding user in DB : ", e);
				throw e;
			}
		} else {
			logger.error("UserSite already present in DB : ", userSite);
			throw new UserSiteAlreadyPresentException(ResponseCodes.USER_SITE_ALREADY_PRESENT.message);
		}
	}
	
	@Override
	public UserSite getUserSite(UserSite userSite) {
		return queryUserSiteViaEmailAndSiteName(userSite);
	}

	private UserSite queryUserSiteViaEmailAndSiteName(UserSite userSite) {
		Query searchUserSiteQuery = new Query();
		searchUserSiteQuery.addCriteria(Criteria.where(UserSite.FIELDS.EMAIL).is(userSite.getEmail()));
		searchUserSiteQuery.addCriteria(Criteria.where(UserSite.FIELDS.SITE_NAME).is(userSite.getSiteName()));
		UserSite savedUserSite = null;
		try {
			savedUserSite = mongoOperation.findOne(searchUserSiteQuery, UserSite.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering user in DB : ", e);
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
		return savedUserSite;
	}

	@Override
	public List<UserSite> getFilterdUserSites(String email, String siteStatus, String shareStatus) {
		Query searchUserSiteQuery = new Query();
		if (siteStatus!=null){
			searchUserSiteQuery.addCriteria(Criteria.where(UserSite.FIELDS.STATUS).is(siteStatus));
		}
		if (shareStatus!=null){
			searchUserSiteQuery.addCriteria(Criteria.where(UserSite.FIELDS.SHARE_STATUS).is(shareStatus));
		}
		if (email!=null){
			searchUserSiteQuery.addCriteria(Criteria.where(UserSite.FIELDS.EMAIL).is(email));
		}
		searchUserSiteQuery.with(new Sort(Sort.Direction.DESC, UserSite.FIELDS.LAST_MODIFIED_TIME));
		List<UserSite> userSiteList = new ArrayList<UserSite>();
		try {
			userSiteList = mongoOperation.find(searchUserSiteQuery, UserSite.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering user in DB : ", e);
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
		return userSiteList;
	}

	@Override
	public List<UserSite> getSharedUserSites(String email, String siteStatus) {
		Query searchUserSiteQuery = new Query();
		searchUserSiteQuery.addCriteria(Criteria.where(UserSite.FIELDS.SHARE_STATUS).exists(true).orOperator(Criteria.where(UserSite.FIELDS.SHARE_STATUS).is(Status.SHARE_STATUS.COMPANY),Criteria.where(UserSite.FIELDS.SHARE_STATUS).is(Status.SHARE_STATUS.PUBLIC)));
		if (siteStatus!=null){
			searchUserSiteQuery.addCriteria(Criteria.where(UserSite.FIELDS.STATUS).is(siteStatus));
		}
		if (email!=null){
			searchUserSiteQuery.addCriteria(Criteria.where(UserSite.FIELDS.EMAIL).is(email));
		}
		searchUserSiteQuery.with(new Sort(Sort.Direction.DESC, UserSite.FIELDS.LAST_MODIFIED_TIME));
		List<UserSite> userSiteList = new ArrayList<UserSite>();
		try {
			userSiteList = mongoOperation.find(searchUserSiteQuery, UserSite.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering user in DB : ", e);
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
		return userSiteList;
	}
	
	@Override
	public List<UserSite> getAllUserSites(String email, String siteStatus) {
		Query searchUserSiteQuery = new Query();
		if (siteStatus!=null){
			searchUserSiteQuery.addCriteria(Criteria.where(UserSite.FIELDS.STATUS).is(siteStatus));
		}
		if (email!=null){
			searchUserSiteQuery.addCriteria(Criteria.where(UserSite.FIELDS.EMAIL).is(email));
		}
		searchUserSiteQuery.with(new Sort(Sort.Direction.DESC, UserSite.FIELDS.LAST_MODIFIED_TIME));
		List<UserSite> userSiteList = new ArrayList<UserSite>();
		try {
			userSiteList = mongoOperation.find(searchUserSiteQuery, UserSite.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering user in DB : ", e);
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
		return userSiteList;
	}

	@Override
	public boolean saveUserSitesShareStatus(List<UserSite> userSites) {
		boolean result = false;
		for (UserSite userSite : userSites){
			UserSite savedUserSite = queryUserSiteViaEmailAndSiteName(userSite);
			try {
				savedUserSite.setShareStatus(userSite.getShareStatus());
				savedUserSite.setStatus(userSite.getStatus());
				savedUserSite.setLastmodifiedtime(System.currentTimeMillis());
				mongoOperation.save(savedUserSite);
				result = true;
				logger.debug("UserSite Saving Completed Successfully");
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
				logger.error("Exception occured while adding user in DB : ", e);
				throw e;
			}
		}
		return result;
	}

}
