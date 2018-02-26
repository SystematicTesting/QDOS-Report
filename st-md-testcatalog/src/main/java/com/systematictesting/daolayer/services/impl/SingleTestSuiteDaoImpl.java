package com.systematictesting.daolayer.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.systematictesting.daolayer.beans.DataSet;
import com.systematictesting.daolayer.beans.FieldName;
import com.systematictesting.daolayer.beans.TestCaseData;
import com.systematictesting.daolayer.beans.TestCaseDataSchema;
import com.systematictesting.daolayer.beans.TestStepData;
import com.systematictesting.daolayer.constants.TestCatalogConstants;
import com.systematictesting.daolayer.entity.SingleTestSuite;
import com.systematictesting.daolayer.exceptions.InternalServerErrorException;
import com.systematictesting.daolayer.exceptions.MissingMandatoryAPIParameters;
import com.systematictesting.daolayer.exceptions.TestSuiteNameNotPresentException;
import com.systematictesting.daolayer.services.SingleTestSuiteDao;
import com.systematictesting.services.core.SystemPropertiesService;

@Repository(SingleTestSuiteDao.SERVICE_NAME)
public class SingleTestSuiteDaoImpl implements SingleTestSuiteDao {
	private static final Logger logger = LoggerFactory.getLogger(SingleTestSuiteDaoImpl.class);

	@Autowired
	private SystemPropertiesService systemPropertiesService;

	@Autowired
	private MongoOperations mongoOperation;

	@Override
	public boolean addProperty(SingleTestSuite requestedSingleTestSuite, Map<String, String> elementProperty) {
		if (StringUtils.isNotBlank(requestedSingleTestSuite.getEmail())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getSiteName())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getTestSuite().getSuiteName())) {
			SingleTestSuite savedSingleTestSuite = getSingleTestSuite(requestedSingleTestSuite);
			if (savedSingleTestSuite != null) {
				Set<String> requestedKeyName = elementProperty.keySet();
				for(String keyName : requestedKeyName){
					savedSingleTestSuite.getTestSuite().getKeyValuePairs().put(keyName, elementProperty.get(keyName));
				}
				savedSingleTestSuite.setLastmodifiedtime(System.currentTimeMillis());
				try {
					mongoOperation.save(savedSingleTestSuite);
					logger.debug(" Element Property added Successfully against email=" + savedSingleTestSuite.getEmail());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while adding TestCase in DB : ", e);
					throw e;
				}
			} else {
				throw new TestSuiteNameNotPresentException(
						"Test Suite Name not present against this sitename. Please change the TestSuite Name.");
			}
		} else {
			throw new MissingMandatoryAPIParameters(
					"Mandatory parameters are missing while storing TestCase files against email.");
		}
	}
	
	@Override
	public boolean deleteProperty(SingleTestSuite requestedSingleTestSuite, Map<String, String> elementProperty) {
		if (StringUtils.isNotBlank(requestedSingleTestSuite.getEmail())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getSiteName())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getTestSuite().getSuiteName())) {
			//TODO
			SingleTestSuite savedSingleTestSuite = getSingleTestSuite(requestedSingleTestSuite);
			if (savedSingleTestSuite != null) {
				Set<String> requestedKeyName = elementProperty.keySet();
				for(String keyName : requestedKeyName){
					if (savedSingleTestSuite.getTestSuite().getKeyValuePairs().containsKey(keyName)){
						savedSingleTestSuite.getTestSuite().getKeyValuePairs().remove(keyName);
					}
				}
				savedSingleTestSuite.setLastmodifiedtime(System.currentTimeMillis());
				try {
					mongoOperation.save(savedSingleTestSuite);
					logger.debug(" Element Property added Successfully against email=" + savedSingleTestSuite.getEmail());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while adding TestCase in DB : ", e);
					throw e;
				}
			} else {
				throw new TestSuiteNameNotPresentException(
						"Test Suite Name not present against this sitename. Please change the TestSuite Name.");
			}
		} else {
			throw new MissingMandatoryAPIParameters(
					"Mandatory parameters are missing while storing TestCase files against email.");
		}
	}
	
	@Override
	public boolean addTestCase(SingleTestSuite requestedTestSuite, TestCaseData requestedTestCaseData) {
		if (StringUtils.isNotBlank(requestedTestSuite.getEmail())
				&& StringUtils.isNotBlank(requestedTestSuite.getSiteName())
				&& StringUtils.isNotBlank(requestedTestSuite.getTestSuite().getSuiteName())) {
			SingleTestSuite savedSingleTestSuite = getSingleTestSuite(requestedTestSuite);
			if (savedSingleTestSuite != null) {
				boolean isTestCasePresent = false;
				for (TestCaseData testCase : savedSingleTestSuite.getTestSuite().getTestCaseArray()){
					if (testCase.getTestCaseId().equals(requestedTestCaseData.getTestCaseId())){
						testCase.setTestCaseMode(requestedTestCaseData.getTestCaseMode());
						testCase.setTestCaseName(requestedTestCaseData.getTestCaseName());
						testCase.setTestCaseType(requestedTestCaseData.getTestCaseType());
						isTestCasePresent = true;
						break;
					}
				}
				if (!isTestCasePresent){
					savedSingleTestSuite.getTestSuite().getTestCaseArray().add(requestedTestCaseData);
				}
				savedSingleTestSuite.setLastmodifiedtime(System.currentTimeMillis());
				try {
					mongoOperation.save(savedSingleTestSuite);
					logger.debug(" Completed Successfully against email=" + savedSingleTestSuite.getEmail());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while adding TestCase in DB : ", e);
					throw e;
				}
			} else {
				throw new TestSuiteNameNotPresentException(
						"Test Suite Name not present against this sitename. Please change the TestSuite Name.");
			}
		} else {
			throw new MissingMandatoryAPIParameters(
					"Mandatory parameters are missing while storing TestCase files against email.");
		}
	}

	@Override
	public boolean deleteTestCases(SingleTestSuite requestedTestSuite, List<TestCaseData> requestedTestCases) {
		if (StringUtils.isNotBlank(requestedTestSuite.getEmail())
				&& StringUtils.isNotBlank(requestedTestSuite.getSiteName())
				&& StringUtils.isNotBlank(requestedTestSuite.getTestSuite().getSuiteName())) {
			SingleTestSuite savedSingleTestSuite = getSingleTestSuite(requestedTestSuite);
			if (savedSingleTestSuite != null) {
				savedSingleTestSuite.getTestSuite().getTestCaseArray().removeAll(requestedTestCases);
				savedSingleTestSuite.setLastmodifiedtime(System.currentTimeMillis());
				try {
					mongoOperation.save(savedSingleTestSuite);
					logger.debug("Deleted Successfully against email=" + savedSingleTestSuite.getEmail());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while deleting TestCases in DB : ", e);
					throw e;
				}
			} else {
				throw new TestSuiteNameNotPresentException(
						"Test Suite Name not present against this sitename. Please change the TestSuite Name.");
			}
		} else {
			throw new MissingMandatoryAPIParameters(
					"Mandatory parameters are missing while storing TestCase files against email.");
		}
	}
	
	@Override
	public boolean addTestCaseDataSchema(SingleTestSuite requestedTestSuite, String testCaseId, TestCaseDataSchema requestedTestCaseDataSchema) {
		if (StringUtils.isNotBlank(requestedTestSuite.getEmail())
				&& StringUtils.isNotBlank(requestedTestSuite.getSiteName())
				&& StringUtils.isNotBlank(requestedTestSuite.getTestSuite().getSuiteName())) {
			SingleTestSuite savedSingleTestSuite = getSingleTestSuite(requestedTestSuite);
			if (savedSingleTestSuite != null) {
				TestCaseData testCase = getTestCaseFromTestSuite(testCaseId, savedSingleTestSuite);
				if (testCase.getDataSets()==null){
					testCase.setDataSets(new HashMap<String, ArrayList<DataSet>>());
				}
				Set<String> fieldNames = new HashSet<String>(testCase.getDataSets().keySet());
				for (String fieldNameKey: fieldNames){
					if (!requestedTestCaseDataSchema.getFieldNames().contains(fieldNameKey)){
						testCase.getDataSets().remove(fieldNameKey);
					}
				}
				for (FieldName fieldName: requestedTestCaseDataSchema.getFieldNames()){
					if (!testCase.getDataSets().containsKey(fieldName.getText())){
						testCase.getDataSets().put(fieldName.getText(), new ArrayList<DataSet>());
					}
				}
				if (testCase.getDataSets().size()==0){
					testCase.setDataSets(null);
				}
				savedSingleTestSuite.setLastmodifiedtime(System.currentTimeMillis());
				try {
					mongoOperation.save(savedSingleTestSuite);
					logger.debug("Data Fields Added Successfully against email=" + savedSingleTestSuite.getEmail());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while deleting TestCases in DB : ", e);
					throw e;
				}
			} else {
				throw new TestSuiteNameNotPresentException(
						"Test Suite Name not present against this sitename. Please change the TestSuite Name.");
			}
		} else {
			throw new MissingMandatoryAPIParameters(
					"Mandatory parameters are missing while storing TestCase files against email.");
		}
	}
	
	@Override
	public boolean addTestCaseDataSet(SingleTestSuite requestedSingleTestSuite, String requestedTestCaseId,Map<String, DataSet> dataSetMap) {
		if (StringUtils.isNotBlank(requestedSingleTestSuite.getEmail())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getSiteName())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getTestSuite().getSuiteName())) {
			SingleTestSuite savedSingleTestSuite = getSingleTestSuite(requestedSingleTestSuite);
			if (savedSingleTestSuite != null) {
				TestCaseData testCase = getTestCaseFromTestSuite(requestedTestCaseId, savedSingleTestSuite);
				
				Set<String> fieldNames = new HashSet<String>(testCase.getDataSets().keySet());
				for (String fieldNameKey: fieldNames){
					int indexToDelete = -1;
					for(int index=0;index<testCase.getDataSets().get(fieldNameKey).size();index++){
						if (testCase.getDataSets().get(fieldNameKey).get(index).getId().equals(dataSetMap.get(fieldNameKey).getId())){
							indexToDelete = index;
							break;
						}
					}
					if (indexToDelete!=-1){
						testCase.getDataSets().get(fieldNameKey).remove(indexToDelete);
					}
					testCase.getDataSets().get(fieldNameKey).add(dataSetMap.get(fieldNameKey));
				}
				
				savedSingleTestSuite.setLastmodifiedtime(System.currentTimeMillis());
				try {
					mongoOperation.save(savedSingleTestSuite);
					logger.debug("Data Set Updated Successfully against email=" + savedSingleTestSuite.getEmail());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while deleting TestCases in DB : ", e);
					throw e;
				}
			} else {
				throw new TestSuiteNameNotPresentException(
						"Test Suite Name not present against this sitename. Please change the TestSuite Name.");
			}
		} else {
			throw new MissingMandatoryAPIParameters(
					"Mandatory parameters are missing while storing TestCase files against email.");
		}
	}
	
	@Override
	public boolean addTestStep(SingleTestSuite requestedSingleTestSuite, String requestedTestCaseId, TestStepData testStep) {
		if (StringUtils.isNotBlank(requestedSingleTestSuite.getEmail())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getSiteName())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getTestSuite().getSuiteName())) {
			SingleTestSuite savedSingleTestSuite = getSingleTestSuite(requestedSingleTestSuite);
			if (savedSingleTestSuite != null) {
				TestCaseData testCase = getTestCaseFromTestSuite(requestedTestCaseId, savedSingleTestSuite);
				boolean isTestStepPresent = false;
				for(TestStepData savedTestStep : testCase.getTestStepsData()){
					if (savedTestStep.getStepId().equals(testStep.getStepId())){
						isTestStepPresent = true;
						savedTestStep.setElementKey(testStep.getElementKey());
						savedTestStep.setElementType(testStep.getElementType());
						savedTestStep.setElementValue(testStep.getElementValue());
						savedTestStep.setProceedOnFail(testStep.getProceedOnFail());
						savedTestStep.setStepDescription(testStep.getStepDescription());
						savedTestStep.setStepKeyword(testStep.getStepKeyword());
						break;
					}
				}
				if (!isTestStepPresent){
					testCase.getTestStepsData().add(testStep);
				}
				testCase.setCompleteStatus(TestCatalogConstants.COMPLETE);
				if (StringUtils.isNotBlank(testStep.getElementKey()) && !savedSingleTestSuite.getTestSuite().getKeyValuePairs().containsKey(testStep.getElementKey())){
					savedSingleTestSuite.getTestSuite().getKeyValuePairs().put(testStep.getElementKey(), TestCatalogConstants.INCOMPLETE);
				}
				
				savedSingleTestSuite.setLastmodifiedtime(System.currentTimeMillis());
				try {
					mongoOperation.save(savedSingleTestSuite);
					logger.debug("Test Step Updated Successfully against email=" + savedSingleTestSuite.getEmail());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while deleting TestCases in DB : ", e);
					throw e;
				}
			} else {
				throw new TestSuiteNameNotPresentException(
						"Test Suite Name not present against this sitename. Please change the TestSuite Name.");
			}
		} else {
			throw new MissingMandatoryAPIParameters(
					"Mandatory parameters are missing while storing TestCase files against email.");
		}
	}
	
	@Override
	public boolean deleteTestStep(SingleTestSuite requestedSingleTestSuite, String requestedTestCaseId, TestStepData testStep) {
		if (StringUtils.isNotBlank(requestedSingleTestSuite.getEmail())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getSiteName())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getTestSuite().getSuiteName())) {
			SingleTestSuite savedSingleTestSuite = getSingleTestSuite(requestedSingleTestSuite);
			if (savedSingleTestSuite != null) {
				TestCaseData testCase = getTestCaseFromTestSuite(requestedTestCaseId, savedSingleTestSuite);
				testCase.getTestStepsData().remove(testStep);
				if (testCase.getTestStepsData().size()==0){
					testCase.setCompleteStatus(TestCatalogConstants.INCOMPLETE);
				}
				savedSingleTestSuite.setLastmodifiedtime(System.currentTimeMillis());
				try {
					mongoOperation.save(savedSingleTestSuite);
					logger.debug("Test Step Deleted Successfully against email=" + savedSingleTestSuite.getEmail());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while deleting TestCases in DB : ", e);
					throw e;
				}
			} else {
				throw new TestSuiteNameNotPresentException(
						"Test Suite Name not present against this sitename. Please change the TestSuite Name.");
			}
		} else {
			throw new MissingMandatoryAPIParameters(
					"Mandatory parameters are missing while storing TestCase files against email.");
		}
	}
	
	@Override
	public boolean deleteTestCaseDataSet(SingleTestSuite requestedSingleTestSuite, String requestedTestCaseId, DataSet dataSet) {
		if (StringUtils.isNotBlank(requestedSingleTestSuite.getEmail())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getSiteName())
				&& StringUtils.isNotBlank(requestedSingleTestSuite.getTestSuite().getSuiteName())) {
			SingleTestSuite savedSingleTestSuite = getSingleTestSuite(requestedSingleTestSuite);
			if (savedSingleTestSuite != null) {
				TestCaseData testCase = getTestCaseFromTestSuite(requestedTestCaseId, savedSingleTestSuite);
				
				Set<String> fieldNames = new HashSet<String>(testCase.getDataSets().keySet());
				for (String fieldNameKey: fieldNames){
					int indexToDelete = -1;
					for(int index=0;index<testCase.getDataSets().get(fieldNameKey).size();index++){
						if (testCase.getDataSets().get(fieldNameKey).get(index).getId().equals(dataSet.getId())){
							indexToDelete = index;
							break;
						}
					}
					if (indexToDelete!=-1){
						testCase.getDataSets().get(fieldNameKey).remove(indexToDelete);
					}
				}
				
				savedSingleTestSuite.setLastmodifiedtime(System.currentTimeMillis());
				try {
					mongoOperation.save(savedSingleTestSuite);
					logger.debug("Data Set Deleted Successfully against email=" + savedSingleTestSuite.getEmail());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while deleting TestCases in DB : ", e);
					throw e;
				}
			} else {
				throw new TestSuiteNameNotPresentException(
						"Test Suite Name not present against this sitename. Please change the TestSuite Name.");
			}
		} else {
			throw new MissingMandatoryAPIParameters(
					"Mandatory parameters are missing while storing TestCase files against email.");
		}
	}

	private TestCaseData getTestCaseFromTestSuite(String testCaseId, SingleTestSuite savedSingleTestSuite) {
		TestCaseData testCase = null;
		for (int index=0;index<savedSingleTestSuite.getTestSuite().getTestCaseArray().size();index++){
			if (savedSingleTestSuite.getTestSuite().getTestCaseArray().get(index).getTestCaseId().equals(testCaseId)){
				testCase = savedSingleTestSuite.getTestSuite().getTestCaseArray().get(index);
			}
		}
		return testCase;
	}

	@Override
	public boolean addTestSuite(SingleTestSuite newTestSuite) {
		if (StringUtils.isNotBlank(newTestSuite.getFileLocation()) && StringUtils.isNotBlank(newTestSuite.getFileName())
				&& StringUtils.isNotBlank(newTestSuite.getEmail()) && StringUtils.isNotBlank(newTestSuite.getSiteName())
				&& StringUtils.isNotBlank(newTestSuite.getSourceEmail())
				&& StringUtils.isNotBlank(newTestSuite.getSourceType())) {
			newTestSuite.setLastmodifiedtime(System.currentTimeMillis());
			SingleTestSuite savedSingleTestSuite = getSingleTestSuite(newTestSuite);
			if (savedSingleTestSuite == null) {
				try {
					mongoOperation.save(newTestSuite);
					logger.debug("SingleTestSuite Saving fileName = " + newTestSuite.getFileName()
							+ " Completed Successfully against email=" + newTestSuite.getEmail());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while adding SingleTestSuite in DB : ", e);
					throw e;
				}
			} else {
				try {
					savedSingleTestSuite.getTestSuite()
							.setSuiteDescription(newTestSuite.getTestSuite().getSuiteDescription());
					mongoOperation.save(savedSingleTestSuite);
					logger.debug(
							"SingleTestSuite updated Successfully against email=" + savedSingleTestSuite.getEmail());
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while updating SingleTestSuite in DB : ", e);
					throw e;
				}
			}
		} else {
			throw new MissingMandatoryAPIParameters(
					"Mandatory parameters are missing while storing TestSuite files against email.");
		}
	}

	@Override
	public List<SingleTestSuite> getAllTestSuites(SingleTestSuite singleTestSuite) {
		Query searchSingleTestSuiteQuery = new Query();
		searchSingleTestSuiteQuery
				.addCriteria(Criteria.where(SingleTestSuite.FIELDS.EMAIL).is(singleTestSuite.getEmail()));
		searchSingleTestSuiteQuery
				.addCriteria(Criteria.where(SingleTestSuite.FIELDS.SITE_NAME).is(singleTestSuite.getSiteName()));
		searchSingleTestSuiteQuery.with(new Sort(Sort.Direction.DESC, SingleTestSuite.FIELDS.LAST_MODIFIED_TIME));
		List<SingleTestSuite> savedListOfSingleTestSuites = null;
		try {
			savedListOfSingleTestSuites = mongoOperation.find(searchSingleTestSuiteQuery, SingleTestSuite.class);
			for (SingleTestSuite testSuite : savedListOfSingleTestSuites){
				String testSuiteCompleteStatus = TestCatalogConstants.COMPLETE; 
				if (testSuite.getTestSuite().getTestCaseArray().size()>0){
					for (TestCaseData testCase : testSuite.getTestSuite().getTestCaseArray()){
						if (TestCatalogConstants.INCOMPLETE.equals(testCase.getCompleteStatus())){
							testSuiteCompleteStatus = TestCatalogConstants.INCOMPLETE;
							break;
						}
					}
				} else {
					testSuiteCompleteStatus = TestCatalogConstants.INCOMPLETE;
				}
				testSuite.setCompleteStatus(testSuiteCompleteStatus);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering SingleTestSuite in DB : ", e);
			throw new InternalServerErrorException(
					"No SingleTestSuite available against email = " + singleTestSuite.getEmail());
		}
		return savedListOfSingleTestSuites;
	}

	@Override
	public boolean deleteTestSuite(SingleTestSuite singleTestSuite) {
		Query searchListTestSuiteQuery = new Query();
		searchListTestSuiteQuery
				.addCriteria(Criteria.where(SingleTestSuite.FIELDS.EMAIL).is(singleTestSuite.getEmail()));
		searchListTestSuiteQuery
				.addCriteria(Criteria.where(SingleTestSuite.FIELDS.SITE_NAME).is(singleTestSuite.getSiteName()));
		searchListTestSuiteQuery.addCriteria(Criteria.where(SingleTestSuite.FIELDS.ID).is(singleTestSuite.getId()));
		try {
			SingleTestSuite deletedSingleTestSuites = mongoOperation.findAndRemove(searchListTestSuiteQuery,
					SingleTestSuite.class);
			logger.debug("SingleTestSuite is Deleted Successfully : " + deletedSingleTestSuites.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering SingleTestSuite in DB : ", e);
			throw new InternalServerErrorException(
					"No SingleTestSuite available against email = " + singleTestSuite.getEmail());
		}
	}

	@Override
	public boolean deleteTestSuites(List<SingleTestSuite> listOfSingleTestSuites) throws IOException {
		for (SingleTestSuite singleTestSuite : listOfSingleTestSuites) {
			if (!deleteTestSuite(singleTestSuite)) {
				logger.debug("SingleTestSuite is FAILED to DELETE : " + singleTestSuite.toString());
				return false;
			}
			String defaultCatalogLocation = systemPropertiesService.getProperties()
					.getProperty(TestCatalogConstants.BOOTUP_TEST_CATALOG_ROOT_PATH);
			String absoluteDirectoryLocationOfExcelSheet = defaultCatalogLocation + singleTestSuite.getFileLocation();
			String absoluteFilePathOfTestSuiteExcelSheet = absoluteDirectoryLocationOfExcelSheet
					+ TestCatalogConstants.FILE_SEPARATOR + singleTestSuite.getFileName();
			File excelFile = new File(absoluteFilePathOfTestSuiteExcelSheet);
			if (excelFile.exists()) {
				logger.debug("DELETING EXCEL FILE : " + absoluteFilePathOfTestSuiteExcelSheet);
				excelFile.delete();
				File location = new File(absoluteDirectoryLocationOfExcelSheet);
				if (location.exists()) {
					logger.debug("DELETING EXCEL FILE DIRECTORY : " + absoluteDirectoryLocationOfExcelSheet);
					location.delete();
				}
			}
		}
		return true;
	}

	@Override
	public SingleTestSuite getSingleTestSuite(SingleTestSuite singleTestSuite) {
		Query searchSingleTestSuiteQuery = new Query();
		searchSingleTestSuiteQuery
				.addCriteria(Criteria.where(SingleTestSuite.FIELDS.EMAIL).is(singleTestSuite.getEmail()));
		searchSingleTestSuiteQuery
				.addCriteria(Criteria.where(SingleTestSuite.FIELDS.SITE_NAME).is(singleTestSuite.getSiteName()));
		searchSingleTestSuiteQuery.with(new Sort(Sort.Direction.DESC, SingleTestSuite.FIELDS.LAST_MODIFIED_TIME));
		List<SingleTestSuite> savedListOfSingleTestSuites = null;
		try {
			savedListOfSingleTestSuites = mongoOperation.find(searchSingleTestSuiteQuery, SingleTestSuite.class);
			if (savedListOfSingleTestSuites != null) {
				for (SingleTestSuite savedSingleTestSuite : savedListOfSingleTestSuites) {
					if (savedSingleTestSuite.getTestSuite().getSuiteName()
							.equals(singleTestSuite.getTestSuite().getSuiteName())) {
						return savedSingleTestSuite;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering SingleTestSuite in DB : ", e);
			throw new InternalServerErrorException(
					"No SingleTestSuite available against email = " + singleTestSuite.getEmail());
		}
		return null;
	}

}
