package com.systematictesting.scheduled.tasks.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.systematictesting.daolayer.beans.TestCase;
import com.systematictesting.daolayer.beans.TestSuiteReport;
import com.systematictesting.daolayer.constants.QdosStatus;
import com.systematictesting.daolayer.entity.QdosReport;
import com.systematictesting.daolayer.services.QdosReportDao;
import com.systematictesting.scheduled.tasks.SiteVersionTaskService;

@Repository(SiteVersionTaskService.SERVICE_NAME)
public class SiteVersionTaskServiceImpl implements SiteVersionTaskService {

	private static final Logger logger = LoggerFactory.getLogger(SiteVersionTaskServiceImpl.class);
	private static final Long ALLOWED_HOURS_DIFFERENCE = 5*60*60*1000L;

	@Autowired
	private QdosReportDao qdosReportDao;
	
	@Override
	public void updateStatusOnStalledSiteVersion() {
		logger.debug("SiteVersionTaskImpl.class :: Starting the CRON JOB");
		boolean isUpdateRequired = false;
		QdosReport qdosReportReq = new QdosReport();
		qdosReportReq.setStatus(QdosStatus.REAL_TIME_STATUS.NEW);
		List<QdosReport> savedQdosReportList = qdosReportDao.getVersionHistoryOfSite(qdosReportReq);
		logger.debug("SiteVersionTaskImpl.class :: List of QdosReport :: "+savedQdosReportList);
		for (int index=0;index<savedQdosReportList.size();index++){
			QdosReport savedQdosReport = savedQdosReportList.get(index);
			logger.debug("@@@@@ SYSTEM CURRENT TIME = "+System.currentTimeMillis());
			logger.debug("@@@@@ LAST MODIFIED TIME = "+savedQdosReport.getLastmodifiedtime());
			logger.debug("@@@@@ TIME DIFFERENCE = "+(System.currentTimeMillis()-savedQdosReport.getLastmodifiedtime()));
			logger.debug("@@@@@ ALLOWED DIFFERENCE = "+ALLOWED_HOURS_DIFFERENCE);
			logger.debug("@@@@@ : Result = "+((System.currentTimeMillis()-savedQdosReport.getLastmodifiedtime())>ALLOWED_HOURS_DIFFERENCE));
			if ((System.currentTimeMillis()-savedQdosReport.getLastmodifiedtime())>ALLOWED_HOURS_DIFFERENCE){
				isUpdateRequired = true;
				logger.debug("SiteVersionTaskImpl.class :: ACTION NEEDED :: QdosReport :: "+savedQdosReport);
				savedQdosReport.setStatus(QdosStatus.REAL_TIME_STATUS.FINISHED);
				savedQdosReport.setVersionEndTime(QdosStatus.REAL_TIME_STATUS.ABORTED);
				for(int testSuiteIndex=0;testSuiteIndex<savedQdosReport.getTestSuitesReport().size();testSuiteIndex++){
					TestSuiteReport savedTestSuiteReportFromDB = savedQdosReport.getTestSuitesReport().get(testSuiteIndex);
					savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().setEndTime(QdosStatus.REAL_TIME_STATUS.ABORTED);
					logger.debug("SiteVersionTaskImpl.class :: ACTION NEEDED :: savedTestSuiteReportFromDB :: "+savedTestSuiteReportFromDB);
					for(int testCaseIndex=0;testCaseIndex<savedTestSuiteReportFromDB.getReport().getTestCaseArray().size();testCaseIndex++){
						TestCase savedTestCase = savedTestSuiteReportFromDB.getReport().getTestCaseArray().get(testCaseIndex);
						if (savedTestCase.getEndTime().equals(QdosStatus.REAL_TIME_STATUS.RUNNING)){
							savedTestCase.setEndTime(QdosStatus.TEST_CASE_STATUS.ABORTED);
							savedTestCase.setStatus(QdosStatus.TEST_CASE_STATUS.ABORTED);
							savedTestCase.setStatusClass(QdosStatus.TEST_CASE_STATUS.ABORTED.toLowerCase());
							
							savedQdosReport.getVersionReport().setAborted(savedQdosReport.getVersionReport().getAborted()+1);
							savedQdosReport.getVersionReport().setTotal(savedQdosReport.getVersionReport().getTotal()+1);
							savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().setTotal(savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().getTotal()+1);
							savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().setAborted(savedTestSuiteReportFromDB.getReport().getTestSuiteDetails().getSummary().getAborted()+1);
							
							logger.debug("SiteVersionTaskImpl.class :: ACTION NEEDED :: savedTestCase :: "+savedTestCase);
						}
					}
				}
				
			}
		}
		if (!isUpdateRequired){
			logger.info("SiteVersionTaskImpl.class :: CRON JOB HAS NOTHING TO UPDATE.");
		} else if (isUpdateRequired && qdosReportDao.saveListOfQdosReports(savedQdosReportList)){
			logger.info("SiteVersionTaskImpl.class :: CRON JOB SUCCESSFULLY FINISHED.");
		} else {
			logger.error("SiteVersionTaskImpl.class :: CRON JOB FAILED.");
		}
	}

}
