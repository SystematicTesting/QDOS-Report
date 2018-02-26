package com.systematictesting.daolayer.services;

import java.util.List;

import com.systematictesting.daolayer.entity.Catalog;

public interface CatalogDao {
	String SERVICE_NAME = "CatalogDao";
	
	Catalog getLatestVersionOfCatalog(Catalog catalogReq);
	Catalog getVersionOfCatalog(Catalog catalogReq);
	List<Catalog> getVersionHistoryOfCatalog(Catalog catalogReq);
	
	Catalog queryCatalogViaEmailAndSiteName(Catalog catalogReq);
	Catalog queryCatalogForParticularVersion(Catalog catalogReq);
	
	boolean addCatalog(Catalog catalog);
	
}
