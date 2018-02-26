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

import com.systematictesting.daolayer.beans.ResultSummary;
import com.systematictesting.daolayer.beans.TestCase;
import com.systematictesting.daolayer.beans.TestStep;
import com.systematictesting.daolayer.beans.TestSuiteReport;
import com.systematictesting.daolayer.constants.QdosStatus;
import com.systematictesting.daolayer.constants.ResponseCodes;
import com.systematictesting.daolayer.entity.QdosReport;
import com.systematictesting.daolayer.exceptions.EmailNotValidException;
import com.systematictesting.daolayer.services.QdosReportDao;

@Repository(QdosReportDao.SERVICE_NAME)
public class QdosReportDaoImpl implements QdosReportDao {

	private static final Logger logger = LoggerFactory.getLogger(QdosReportDaoImpl.class);
	

	@Autowired
	private MongoOperations mongoOperation;
	
	
	@Override
	public String startSiteVersion(QdosReport qdosReport) {
		QdosReport savedQdosReport = queryQdosReportViaEmailAndSiteName(qdosReport);
		qdosReport.setStatus(QdosStatus.REAL_TIME_STATUS.NEW);
		qdosReport.setVersionEndTime(QdosStatus.REAL_TIME_STATUS.RUNNING);
		qdosReport.setVersionReport(new ResultSummary());
		qdosReport.getVersionReport().setAborted(0L);
		qdosReport.getVersionReport().setFailed(0L);
		qdosReport.getVersionReport().setPassed(0L);
		qdosReport.getVersionReport().setManual(0L);
		qdosReport.getVersionReport().setTotal(0L);
		qdosReport.setCreatetime(System.currentTimeMillis());
		qdosReport.setLastmodifiedtime(System.currentTimeMillis());
		qdosReport.setTotalTime(0L);
		if (savedQdosReport!=null){
			qdosReport.setVersionNumber(savedQdosReport.getVersionNumber()+1);
		} else {
			qdosReport.setVersionNumber(1L);
		}
		try {
			mongoOperation.save(qdosReport);
			logger.debug("QdosReport Saving Completed Successfully : startSiteVersion");
			return QdosReport.FIELDS.VERSION_NAME_PREFIX+qdosReport.getVersionNumber();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while adding user in DB : ", e);
			throw e;
		}
	}
	
	@Override
	public boolean finishSiteVersion(QdosReport qdosReport) {
		qdosReport.setStatus(QdosStatus.REAL_TIME_STATUS.NEW);
		QdosReport savedQdosReport = queryQdosReportForParticularVersion(qdosReport);
		if (savedQdosReport!=null) {
			savedQdosReport.setStatus(QdosStatus.REAL_TIME_STATUS.FINISHED);
			savedQdosReport.setVersionEndTime(qdosReport.getVersionEndTime());
			savedQdosReport.setTotalTime(qdosReport.getTotalTime());
			try {
				savedQdosReport.setLastmodifiedtime(System.currentTimeMillis());
				mongoOperation.save(savedQdosReport);
				logger.debug("QdosReport Saving Completed Successfully : finishSiteVersion");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured while adding user in DB : ", e);
				throw e;
			}
		} else {
			logger.error("SiteVersion with STATUS = NEW is not present in DB.");
			return false;
		}
		
	}

	@Override
	public boolean updateTestCaseInTestSuite(String startOrFinishStatus, QdosReport qdosReport, String suiteName, Long suiteDuration, TestCase testCase) {
		qdosReport.setStatus(QdosStatus.REAL_TIME_STATUS.NEW);
		QdosReport savedQdosReport = queryQdosReportForParticularVersion(qdosReport);
		logger.debug("updateTestCaseInTestSuite() :: savedQdosReport = "+savedQdosReport);
		if (savedQdosReport!=null){
			TestSuiteReport savedTestSuiteReportFromDB = getSavedTestSuiteReport(suiteName, savedQdosReport);
			if (savedTestSuiteReportFromDB!=null){
				savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().setDuration(suiteDuration);
				if (testCase.getStatus().equalsIgnoreCase(QdosStatus.TEST_CASE_STATUS.PASS)){
					savedQdosReport.getVersionReport().setPassed(savedQdosReport.getVersionReport().getPassed()+1);
					savedQdosReport.getVersionReport().setTotal(savedQdosReport.getVersionReport().getTotal()+1);
					savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().setTotal(savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().getTotal()+1);
					savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().setPassed(savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().getPassed()+1);
				} else if (testCase.getStatus().equalsIgnoreCase(QdosStatus.TEST_CASE_STATUS.FAIL)){
					savedQdosReport.getVersionReport().setFailed(savedQdosReport.getVersionReport().getFailed()+1);
					savedQdosReport.getVersionReport().setTotal(savedQdosReport.getVersionReport().getTotal()+1);
					savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().setFailed(savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().getFailed()+1);
					savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().setTotal(savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().getTotal()+1);
				} else if (testCase.getStatus().equalsIgnoreCase(QdosStatus.TEST_CASE_STATUS.MANUAL)){
					savedQdosReport.getVersionReport().setManual(savedQdosReport.getVersionReport().getManual()+1);
					savedQdosReport.getVersionReport().setTotal(savedQdosReport.getVersionReport().getTotal()+1);
					savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().setManual(savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().getManual()+1);
					savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().setTotal(savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().getTotal()+1);
				} else if (testCase.getStatus().equalsIgnoreCase(QdosStatus.TEST_CASE_STATUS.ABORTED)){
					savedQdosReport.getVersionReport().setAborted(savedQdosReport.getVersionReport().getAborted()+1);
					savedQdosReport.getVersionReport().setTotal(savedQdosReport.getVersionReport().getTotal()+1);
					savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().setTotal(savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().getTotal()+1);
					savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().setAborted(savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().getAborted()+1);
				}
				if (startOrFinishStatus.equals(QdosStatus.REAL_TIME_STATUS.NEW)){
					savedTestSuiteReportFromDB.getReport().getTestCaseArray().add(testCase);
				} else {
					for(int index=0;index<savedTestSuiteReportFromDB.getReport().getTestCaseArray().size();index++){
						TestCase savedTestCase = savedTestSuiteReportFromDB.getReport().getTestCaseArray().get(index);
						if (savedTestCase.getTestCaseId().equals(testCase.getTestCaseId())){
							savedTestCase.setDuration(testCase.getDuration());
							savedTestCase.setEndTime(testCase.getEndTime());
							savedTestCase.setStatus(testCase.getStatus());
							savedTestCase.setVideoFile(testCase.getVideoFile());
							savedTestCase.setStatusClass(testCase.getStatusClass());
							break;
						}
					}
				}
				try {
					savedQdosReport.setTotalTime(qdosReport.getTotalTime());
					savedQdosReport.setLastmodifiedtime(System.currentTimeMillis());
					mongoOperation.save(savedQdosReport);
					logger.debug("TestCase in TestSuite Saving Completed Successfully : updateTestCaseInTestSuite");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while adding user in DB : ", e);
					throw e;
				}
			} else {
				logger.error("TestSuiteReport with SuiteName = "+suiteName+" :: is not present in QdosReport = "+qdosReport);
				return false;
			}
			
		} else {
			logger.error("QdosReport with STATUS = NEW is not present in DB.");
			return false;
		}
	}

	@Override
	public boolean updateTestStepInTestCase(QdosReport qdosReport, String suiteName, Long suiteDuration, String testCaseId, Long testCaseDuration, TestStep testStepJavaObject) {
		qdosReport.setStatus(QdosStatus.REAL_TIME_STATUS.NEW);
		logger.debug("updateTestStepInTestCase() : Test Case ID ="+testCaseId);
		logger.debug("updateTestStepInTestCase() : qdosReport ="+qdosReport);
		QdosReport savedQdosReport = queryQdosReportForParticularVersion(qdosReport);
		logger.debug("updateTestStepInTestCase() : savedQdosReport ="+savedQdosReport);
		if (savedQdosReport!=null){
			TestSuiteReport savedTestSuiteReportFromDB = getSavedTestSuiteReport(suiteName, savedQdosReport);
			logger.debug("updateTestStepInTestCase() : savedTestSuiteReportFromDB ="+savedTestSuiteReportFromDB);
			if (savedTestSuiteReportFromDB!=null){
				savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().setDuration(suiteDuration);
				for(int index=0;index < savedTestSuiteReportFromDB.getReport().getTestCaseArray().size(); index++){
					TestCase testCase = savedTestSuiteReportFromDB.getReport().getTestCaseArray().get(index);
					if (testCase.getTestCaseId().equals(testCaseId)){
						logger.debug("updateTestStepInTestCase() : testCase ="+testCase);
						testCase.setDuration(testCaseDuration);
						testCase.getTestStepsData().add(testStepJavaObject);
						logger.debug("updateTestStepInTestCase() : testStepJavaObject ="+testStepJavaObject);
						logger.debug("updateTestStepInTestCase() : AFTER ADDING TEST STEP : testCase ="+testCase);
						try {
							savedQdosReport.setTotalTime(qdosReport.getTotalTime());
							savedQdosReport.setLastmodifiedtime(System.currentTimeMillis());
							mongoOperation.save(savedQdosReport);
							logger.debug("updateTestStepInTestCase() :: TestStep added Successfully in Test Case ID ="+testCaseId);
							return true;
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("Exception occured while adding user in DB : ", e);
							throw e;
						}
					}
				}
				logger.error("TestCase with ID = "+testCaseId+" :: and SuiteName = "+suiteName+" :: is not present in QdosReport = "+qdosReport);
				return false;
			} else {
				logger.error("TestSuiteReport with SuiteName = "+suiteName+" :: is not present in QdosReport = "+qdosReport);
				return false;
			}
		} else {
			logger.error("QdosReport with STATUS = NEW is not present in DB.");
			return false;
		}
	}

	@Override
	public boolean startTestSuiteReport(QdosReport qdosReport, TestSuiteReport testSuiteReport) {
		qdosReport.setStatus(QdosStatus.REAL_TIME_STATUS.NEW);
		QdosReport savedQdosReport = queryQdosReportForParticularVersion(qdosReport);
		
		if (savedQdosReport!=null){
			savedQdosReport.getTestSuitesReport().add(testSuiteReport);
			savedQdosReport.setTotalTime(qdosReport.getTotalTime());
			try {
				savedQdosReport.setLastmodifiedtime(System.currentTimeMillis());
				mongoOperation.save(savedQdosReport);
				logger.debug("TestSuiteReport Saving Completed Successfully : startTestSuiteReport");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occured while adding user in DB : ", e);
				throw e;
			}
		} else {
			logger.error("QdosReport with STATUS = NEW is not present in DB.");
			return false;
		}
	}

	@Override
	public boolean finishTestSuiteReport(QdosReport qdosReport, String suiteName, TestSuiteReport testSuiteReport) {
		qdosReport.setStatus(QdosStatus.REAL_TIME_STATUS.NEW);
		QdosReport savedQdosReport = queryQdosReportForParticularVersion(qdosReport);
		
		if (savedQdosReport!=null){
			TestSuiteReport savedTestSuiteReportFromDB = getSavedTestSuiteReport(suiteName, savedQdosReport);
			if (savedTestSuiteReportFromDB!=null){
				savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().setEndTime(testSuiteReport.getReport().getTestSuiteDetails().getEndTime());
				savedQdosReport.setTotalTime(qdosReport.getTotalTime());
				try {
					savedQdosReport.setLastmodifiedtime(System.currentTimeMillis());
					mongoOperation.save(savedQdosReport);
					logger.debug("TestSuiteReport Saving Completed Successfully : finishTestSuiteReport");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occured while adding user in DB : ", e);
					throw e;
				}
			} else {
				logger.error("TestSuiteReport with SuiteName = "+suiteName+" :: is not present in QdosReport = "+qdosReport);
				return false;
			}
			
		} else {
			logger.error("QdosReport with STATUS = NEW is not present in DB.");
			return false;
		}
	}

	private TestSuiteReport getSavedTestSuiteReport(String suiteName, QdosReport savedQdosReport) {
		for (int index=0;index<savedQdosReport.getTestSuitesReport().size();index++){
			TestSuiteReport testSuiteReport = savedQdosReport.getTestSuitesReport().get(index);
			if (testSuiteReport.getSuiteName().equals(suiteName)){
				return testSuiteReport;
			}
		}
		return null;
	}
	
	@Override
	public QdosReport queryQdosReportForParticularVersion(QdosReport qdosReport) {
		Query searchQdosReportQuery = new Query();
		searchQdosReportQuery.addCriteria(Criteria.where(QdosReport.FIELDS.EMAIL).is(qdosReport.getEmail()));
		searchQdosReportQuery.addCriteria(Criteria.where(QdosReport.FIELDS.SITE_NAME).is(qdosReport.getSiteName()));
		searchQdosReportQuery.addCriteria(Criteria.where(QdosReport.FIELDS.VERSION_NUMBER).is(qdosReport.getVersionNumber()));
		if (qdosReport.getStatus()!=null){
			searchQdosReportQuery.addCriteria(Criteria.where(QdosReport.FIELDS.STATUS).is(qdosReport.getStatus()));
		}
		QdosReport savedQdosReport = null;
		try {
			savedQdosReport = mongoOperation.findOne(searchQdosReportQuery, QdosReport.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering user in DB : ", e);
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
		return savedQdosReport;
	}

	@Override
	public QdosReport queryQdosReportViaEmailAndSiteName(QdosReport qdosReport) {
		Query searchQdosReportQuery = new Query();
		searchQdosReportQuery.addCriteria(Criteria.where(QdosReport.FIELDS.EMAIL).is(qdosReport.getEmail()));
		searchQdosReportQuery.addCriteria(Criteria.where(QdosReport.FIELDS.SITE_NAME).is(qdosReport.getSiteName()));
		searchQdosReportQuery.with(new Sort(Sort.Direction.DESC, QdosReport.FIELDS.VERSION_NUMBER));
		searchQdosReportQuery.limit(1);
		
		QdosReport savedQdosReport = null;
		try {
			savedQdosReport = mongoOperation.findOne(searchQdosReportQuery, QdosReport.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering user in DB : ", e);
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
		return savedQdosReport;
	}

	@Override
	public QdosReport getLatestVersionOfSite(QdosReport qdosReport) {
		QdosReport savedQdosReport = queryQdosReportViaEmailAndSiteName(qdosReport);
		return savedQdosReport;
	}

	@Override
	public QdosReport getVersionOfSite(QdosReport qdosReportReq) {
		return queryQdosReportForParticularVersion(qdosReportReq);
	}

	@Override
	public List<QdosReport> getVersionHistoryOfSite(QdosReport qdosReportReq) {
		Query searchQdosReportQuery = new Query();
		if (qdosReportReq.getEmail()!=null){
			searchQdosReportQuery.addCriteria(Criteria.where(QdosReport.FIELDS.EMAIL).is(qdosReportReq.getEmail()));
		}
		if (qdosReportReq.getSiteName()!=null) {
			searchQdosReportQuery.addCriteria(Criteria.where(QdosReport.FIELDS.SITE_NAME).is(qdosReportReq.getSiteName()));
		}
		if (qdosReportReq.getStatus()!=null){
			searchQdosReportQuery.addCriteria(Criteria.where(QdosReport.FIELDS.STATUS).is(qdosReportReq.getStatus()));
		}
		searchQdosReportQuery.with(new Sort(Sort.Direction.DESC, QdosReport.FIELDS.VERSION_NUMBER));
		List<QdosReport> savedQdosReportVersionsList = new ArrayList<QdosReport>();
		try {
			savedQdosReportVersionsList = mongoOperation.find(searchQdosReportQuery, QdosReport.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured while quering user in DB : ", e);
			throw new EmailNotValidException(ResponseCodes.EMAIL_NOT_VALID.message);
		}
		return savedQdosReportVersionsList;
	}

	@Override
	public boolean saveListOfQdosReports(List<QdosReport> qdosReports) {
		boolean result = false;
		for (QdosReport savedQdosReport : qdosReports){
			try {
				mongoOperation.save(savedQdosReport);
				result = true;
				logger.debug("QdosReport Saving Completed Successfully");
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
