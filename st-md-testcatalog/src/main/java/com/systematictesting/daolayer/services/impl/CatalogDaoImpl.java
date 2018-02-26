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
import com.systematictesting.daolayer.entity.Catalog;
import com.systematictesting.daolayer.exceptions.EmailNotValidException;
import com.systematictesting.daolayer.exceptions.MissingMandatoryAPIParameters;
import com.systematictesting.daolayer.services.CatalogDao;

@Repository(CatalogDao.SERVICE_NAME)
public class CatalogDaoImpl implements CatalogDao {
	private static final Logger logger = LoggerFactory.getLogger(CatalogDaoImpl.class);

	@Autowired
	private MongoOperations mongoOperation;

	@Override
	public Catalog getLatestVersionOfCatalog(Catalog catalogReq) {
		Catalog savedCatalog = queryCatalogViaEmailAndSiteName(catalogReq);
		return savedCatalog;
	}

	@Override
	public Catalog getVersionOfCatalog(Catalog catalogReq) {
		return queryCatalogForParticularVersion(catalogReq);
	}
	
	@Override
	public List<Catalog> getVersionHistoryOfCatalog(Catalog catalogReq) {
		Query searchCatalogQuery = new Query();
		if (catalogReq.getEmail()!=null){
			searchCatalogQuery.addCriteria(Criteria.where(Catalog.FIELDS.EMAIL).is(catalogReq.getEmail()));
		}
		if (catalogReq.getSiteName()!=null) {
			searchCatalogQuery.addCriteria(Criteria.where(Catalog.FIELDS.SITE_NAME).is(catalogReq.getSiteName()));
		}
		if (catalogReq.getStatus()!=null){
			searchCatalogQuery.addCriteria(Criteria.where(Catalog.FIELDS.STATUS).is(catalogReq.getStatus()));
		}
		searchCatalogQuery.with(new Sort(Sort.Direction.DESC, Catalog.FIELDS.VERSION_NUMBER));
		List<Catalog> savedCatalogVersionsList = new ArrayList<Catalog>();
		try {
			savedCatalogVersionsList = mongoOperation.find(searchCatalogQuery, Catalog.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering user in DB : ", e);
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
		return savedCatalogVersionsList;
	}


	@Override
	public Catalog queryCatalogViaEmailAndSiteName(Catalog catalogReq) {
		Query searchCatalogQuery = new Query();
		searchCatalogQuery.addCriteria(Criteria.where(Catalog.FIELDS.EMAIL).is(catalogReq.getEmail()));
		searchCatalogQuery.addCriteria(Criteria.where(Catalog.FIELDS.SITE_NAME).is(catalogReq.getSiteName()));
		searchCatalogQuery.with(new Sort(Sort.Direction.DESC, Catalog.FIELDS.VERSION_NUMBER));
		searchCatalogQuery.limit(1);

		Catalog savedCatalog = null;
		try {
			savedCatalog = mongoOperation.findOne(searchCatalogQuery, Catalog.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering user in DB : ", e);
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
		return savedCatalog;
	}
	
	@Override
	public Catalog queryCatalogForParticularVersion(Catalog catalogReq) {
		Query searchCatalogQuery = new Query();
		searchCatalogQuery.addCriteria(Criteria.where(Catalog.FIELDS.EMAIL).is(catalogReq.getEmail()));
		searchCatalogQuery.addCriteria(Criteria.where(Catalog.FIELDS.SITE_NAME).is(catalogReq.getSiteName()));
		searchCatalogQuery.addCriteria(Criteria.where(Catalog.FIELDS.VERSION_NUMBER).is(catalogReq.getVersionNumber()));
		if (catalogReq.getStatus()!=null){
			searchCatalogQuery.addCriteria(Criteria.where(Catalog.FIELDS.STATUS).is(catalogReq.getStatus()));
		}
		Catalog savedCatalog = null;
		try {
			savedCatalog = mongoOperation.findOne(searchCatalogQuery, Catalog.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering user in DB : ", e);
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
		return savedCatalog;
	}

	@Override
	public boolean addCatalog(Catalog catalog) {
		if (catalog.getCatalogSuites().size() > 0 && catalog.getEmail() != null && catalog.getSiteName() != null) {
			catalog.setCreatetime(System.currentTimeMillis());
			catalog.setLastmodifiedtime(System.currentTimeMillis());
			try {
				mongoOperation.save(catalog);
				logger.debug("Catalog against siteName = " + catalog.getSiteName() + " committed Successfully");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured while adding SingleTestSuite in DB : ", e);
				throw e;
			}
		} else {
			throw new MissingMandatoryAPIParameters("Mandatory parameters are missing while storing Catalog.");
		}

	}

}
