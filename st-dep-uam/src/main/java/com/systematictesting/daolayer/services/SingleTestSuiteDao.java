package com.systematictesting.daolayer.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.systematictesting.daolayer.beans.DataSet;
import com.systematictesting.daolayer.beans.TestCaseData;
import com.systematictesting.daolayer.beans.TestCaseDataSchema;
import com.systematictesting.daolayer.beans.TestStepData;
import com.systematictesting.daolayer.entity.SingleTestSuite;

public interface SingleTestSuiteDao {
	String SERVICE_NAME = "TempTestSuitesDao";
	
	boolean addTestSuite(SingleTestSuite newTestSuite);
	boolean addTestCase(SingleTestSuite requestedTestSuite, TestCaseData requestedTestCaseData);
	boolean addTestCaseDataSchema(SingleTestSuite requestedTestSuite, String testCaseId, TestCaseDataSchema requestedTestCaseDataSchema);
	boolean addTestCaseDataSet(SingleTestSuite requestedSingleTestSuite, String requestedTestCaseId, Map<String, DataSet> dataSetMap);
	boolean deleteTestCases(SingleTestSuite requestedTestSuite, List<TestCaseData> requestedTestCases);
	boolean deleteTestSuite(SingleTestSuite newTestSuite);
	boolean deleteTestSuites(List<SingleTestSuite> listOfSingleTestSuites) throws IOException;
	List<SingleTestSuite> getAllTestSuites(SingleTestSuite singleTestSuite);
	SingleTestSuite getSingleTestSuite(SingleTestSuite singleTestSuite);
	boolean deleteTestCaseDataSet(SingleTestSuite requestedSingleTestSuite, String requestedTestCaseId, DataSet dataSet);
	boolean addTestStep(SingleTestSuite requestedSingleTestSuite, String requestedTestCaseId, TestStepData testStep);
	boolean deleteTestStep(SingleTestSuite requestedSingleTestSuite, String requestedTestCaseId, TestStepData testStep);
	boolean addProperty(SingleTestSuite requestedSingleTestSuite, Map<String, String> elementProperty);
	boolean deleteProperty(SingleTestSuite requestedSingleTestSuite, Map<String, String> elementProperty);
	
	
}
