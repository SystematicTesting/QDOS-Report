package com.systematictesting.daolayer.services;

import java.util.List;

import com.systematictesting.daolayer.beans.TestCase;
import com.systematictesting.daolayer.beans.TestStep;
import com.systematictesting.daolayer.beans.TestSuiteReport;
import com.systematictesting.daolayer.entity.QdosReport;

public interface QdosReportDao {

	String SERVICE_NAME = "QdosReportDao";
	String startSiteVersion(QdosReport qdosReport);
	boolean finishSiteVersion(QdosReport qdosReport);
	boolean updateTestCaseInTestSuite(String startOrFinishStatus, QdosReport qdosReport, String suiteName, Long suiteDuration, TestCase testCase);
	boolean updateTestStepInTestCase(QdosReport qdosReport, String suiteName, Long suiteDuration, String testCaseId, Long testCaseDuration, TestStep testStepJavaObject);
	boolean startTestSuiteReport(QdosReport qdosReport, TestSuiteReport testSuiteReport);
	boolean finishTestSuiteReport(QdosReport qdosReport, String suiteName, TestSuiteReport testSuiteReport);
	
	QdosReport getLatestVersionOfSite(QdosReport qdosReport);
	QdosReport getVersionOfSite(QdosReport qdosReportReq);
	List<QdosReport> getVersionHistoryOfSite(QdosReport qdosReportReq);
	
	QdosReport queryQdosReportViaEmailAndSiteName(QdosReport qdosReport);
	QdosReport queryQdosReportForParticularVersion(QdosReport qdosReport);
	
	boolean saveListOfQdosReports(List<QdosReport> qdosReports);
}
