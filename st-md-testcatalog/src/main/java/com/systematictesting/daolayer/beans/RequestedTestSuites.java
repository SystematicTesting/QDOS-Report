package com.systematictesting.daolayer.beans;

import java.util.List;

import com.systematictesting.daolayer.entity.SingleTestSuite;

public class RequestedTestSuites {
	List<SingleTestSuite> testSuitesLibrary;
	String siteName;

	public List<SingleTestSuite> getTestSuitesLibrary() {
		return testSuitesLibrary;
	}

	public void setTestSuitesLibrary(List<SingleTestSuite> testSuitesLibrary) {
		this.testSuitesLibrary = testSuitesLibrary;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
}
