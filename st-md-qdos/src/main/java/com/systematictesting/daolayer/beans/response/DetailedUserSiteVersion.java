package com.systematictesting.daolayer.beans.response;

import java.util.ArrayList;
import java.util.List;

import com.systematictesting.daolayer.beans.ResultSummary;
import com.systematictesting.daolayer.beans.TestSuiteReport;

public class DetailedUserSiteVersion {

	private String email;
	private List<TestSuiteReport> testSuitesReport = new ArrayList<TestSuiteReport>();
	private String siteName;
	private String versionNumber;
	private String catalogVersion;
	private String browser;
	private String operatingSystem;
	private String startTime;
	private String endTime;
	private Long totalTime;
	private ResultSummary versionReport;
	private String status;
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
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((operatingSystem == null) ? 0 : operatingSystem.hashCode());
		result = prime * result + ((siteName == null) ? 0 : siteName.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((testSuitesReport == null) ? 0 : testSuitesReport.hashCode());
		result = prime * result + ((totalTime == null) ? 0 : totalTime.hashCode());
		result = prime * result + ((versionNumber == null) ? 0 : versionNumber.hashCode());
		result = prime * result + ((versionReport == null) ? 0 : versionReport.hashCode());
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
		DetailedUserSiteVersion other = (DetailedUserSiteVersion) obj;
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
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
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
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
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
		return true;
	}
	@Override
	public String toString() {
		return "DetailedUserSiteVersion [email=" + email + ", testSuitesReport=" + testSuitesReport + ", siteName=" + siteName + ", versionNumber=" + versionNumber + ", catalogVersion=" + catalogVersion + ", browser=" + browser + ", operatingSystem=" + operatingSystem + ", startTime=" + startTime + ", endTime=" + endTime + ", totalTime=" + totalTime + ", versionReport=" + versionReport + ", status=" + status + "]";
	}
	
}
