package com.systematictesting.daolayer.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.systematictesting.daolayer.beans.ResultSummary;
import com.systematictesting.daolayer.beans.TestSuiteReport;

@Document(collection = "Qdos")
public class QdosReport {

	public interface FIELDS {
		String ID = "id";
		String EMAIL = "email";
		String TEST_SUITES_REPORT = "testSuitesReport";
		String SITE_NAME = "siteName";
		String VERSION_NUMBER = "versionNumber";
		String VERSION_NAME_PREFIX = "S";
		String BROWSER = "browser";
		String OPERATING_SYSTEM = "operatingSystem";
		String VERSION_START_TIME = "versionStartTime";
		String VERSION_END_TIME = "versionEndTime";
		String TOTAL_TIME = "totalTime";
		String VERSION_REPORT = "versionReport";
		String STATUS = "status";
		String LAST_MODIFIED_TIME = "lastmodifiedtime";
		String CREATE_TIME = "createtime";
	}
	
	@Id
	private String id;
	
	@Indexed
	private String email;
	private List<TestSuiteReport> testSuitesReport = new ArrayList<TestSuiteReport>();
	@Indexed
	private String siteName;
	@Indexed
	private Long versionNumber;
	private String catalogVersion;
	private String browser;
	private String operatingSystem;
	private String versionStartTime;
	private String versionEndTime;
	private Long totalTime;
	private ResultSummary versionReport;
	private String status;
	private Long lastmodifiedtime;
	private Long createtime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<TestSuiteReport> getTestSuitesReport() {
		return testSuitesReport;
	}
	public void setTestSuitesReport(List<TestSuiteReport> testSuitesReport) {
		this.testSuitesReport = testSuitesReport;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public Long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}
	public ResultSummary getVersionReport() {
		return versionReport;
	}
	public void setVersionReport(ResultSummary versionReport) {
		this.versionReport = versionReport;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}
	
	public String getVersionStartTime() {
		return versionStartTime;
	}
	public void setVersionStartTime(String versionStartTime) {
		this.versionStartTime = versionStartTime;
	}
	public String getVersionEndTime() {
		return versionEndTime;
	}
	public void setVersionEndTime(String versionEndTime) {
		this.versionEndTime = versionEndTime;
	}
	public Long getLastmodifiedtime() {
		return lastmodifiedtime;
	}
	public void setLastmodifiedtime(Long lastmodifiedtime) {
		this.lastmodifiedtime = lastmodifiedtime;
	}
	public Long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}
	public String getOperatingSystem() {
		return operatingSystem;
	}
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	public String getCatalogVersion() {
		return catalogVersion;
	}
	public void setCatalogVersion(String catalogVersion) {
		this.catalogVersion = catalogVersion;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((browser == null) ? 0 : browser.hashCode());
		result = prime * result + ((catalogVersion == null) ? 0 : catalogVersion.hashCode());
		result = prime * result + ((createtime == null) ? 0 : createtime.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastmodifiedtime == null) ? 0 : lastmodifiedtime.hashCode());
		result = prime * result + ((operatingSystem == null) ? 0 : operatingSystem.hashCode());
		result = prime * result + ((siteName == null) ? 0 : siteName.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((testSuitesReport == null) ? 0 : testSuitesReport.hashCode());
		result = prime * result + ((totalTime == null) ? 0 : totalTime.hashCode());
		result = prime * result + ((versionEndTime == null) ? 0 : versionEndTime.hashCode());
		result = prime * result + ((versionNumber == null) ? 0 : versionNumber.hashCode());
		result = prime * result + ((versionReport == null) ? 0 : versionReport.hashCode());
		result = prime * result + ((versionStartTime == null) ? 0 : versionStartTime.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QdosReport other = (QdosReport) obj;
		if (browser == null) {
			if (other.browser != null)
				return false;
		} else if (!browser.equals(other.browser))
			return false;
		if (catalogVersion == null) {
			if (other.catalogVersion != null)
				return false;
		} else if (!catalogVersion.equals(other.catalogVersion))
			return false;
		if (createtime == null) {
			if (other.createtime != null)
				return false;
		} else if (!createtime.equals(other.createtime))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastmodifiedtime == null) {
			if (other.lastmodifiedtime != null)
				return false;
		} else if (!lastmodifiedtime.equals(other.lastmodifiedtime))
			return false;
		if (operatingSystem == null) {
			if (other.operatingSystem != null)
				return false;
		} else if (!operatingSystem.equals(other.operatingSystem))
			return false;
		if (siteName == null) {
			if (other.siteName != null)
				return false;
		} else if (!siteName.equals(other.siteName))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (testSuitesReport == null) {
			if (other.testSuitesReport != null)
				return false;
		} else if (!testSuitesReport.equals(other.testSuitesReport))
			return false;
		if (totalTime == null) {
			if (other.totalTime != null)
				return false;
		} else if (!totalTime.equals(other.totalTime))
			return false;
		if (versionEndTime == null) {
			if (other.versionEndTime != null)
				return false;
		} else if (!versionEndTime.equals(other.versionEndTime))
			return false;
		if (versionNumber == null) {
			if (other.versionNumber != null)
				return false;
		} else if (!versionNumber.equals(other.versionNumber))
			return false;
		if (versionReport == null) {
			if (other.versionReport != null)
				return false;
		} else if (!versionReport.equals(other.versionReport))
			return false;
		if (versionStartTime == null) {
			if (other.versionStartTime != null)
				return false;
		} else if (!versionStartTime.equals(other.versionStartTime))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "QdosReport [id=" + id + ", email=" + email + ", testSuitesReport=" + testSuitesReport + ", siteName=" + siteName + ", versionNumber=" + versionNumber + ", catalogVersion=" + catalogVersion + ", browser=" + browser + ", operatingSystem=" + operatingSystem + ", versionStartTime=" + versionStartTime + ", versionEndTime=" + versionEndTime + ", totalTime=" + totalTime + ", versionReport=" + versionReport + ", status=" + status + ", lastmodifiedtime=" + lastmodifiedtime + ", createtime=" + createtime + "]";
	}
	
}
