package com.systematictesting.daolayer.beans.response;

import com.systematictesting.daolayer.beans.ResultSummary;

public class UserSiteVersion {

	private String versionNumber;
	private String catalogVersion;
	private String browser;
	private String operatingSystem;
	private String startTime;
	private Long totalTime;
	private ResultSummary versionReport;
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public ResultSummary getVersionReport() {
		return versionReport;
	}
	public void setVersionReport(ResultSummary versionReport) {
		this.versionReport = versionReport;
	}
	public Long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
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
		result = prime * result + ((operatingSystem == null) ? 0 : operatingSystem.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
		UserSiteVersion other = (UserSiteVersion) obj;
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
		if (operatingSystem == null) {
			if (other.operatingSystem != null)
				return false;
		} else if (!operatingSystem.equals(other.operatingSystem))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
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
		return "UserSiteVersion [versionNumber=" + versionNumber + ", catalogVersion=" + catalogVersion + ", browser=" + browser + ", operatingSystem=" + operatingSystem + ", startTime=" + startTime + ", totalTime=" + totalTime + ", versionReport=" + versionReport + "]";
	}
	
}
