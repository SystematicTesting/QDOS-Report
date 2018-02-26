package com.systematictesting.daolayer.beans.response;

public class DetailedSharedSites {

	private String email;
	private String ownerName;
	private String siteName;
	private String status;
	private String shareStatus;
	private String startTime;
	private String endTime;
	private String version;
	private String catalogVersion;
	private String browser;
	private Long totalTime;
	private String operatingSystem;
	private Long pass;
	private Long fail;
	private Long manual;
	private Long aborted;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShareStatus() {
		return shareStatus;
	}
	public void setShareStatus(String shareStatus) {
		this.shareStatus = shareStatus;
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	public Long getPass() {
		return pass;
	}
	public void setPass(Long pass) {
		this.pass = pass;
	}
	public Long getFail() {
		return fail;
	}
	public void setFail(Long fail) {
		this.fail = fail;
	}
	public Long getAborted() {
		return aborted;
	}
	public void setAborted(Long aborted) {
		this.aborted = aborted;
	}
	public Long getManual() {
		return manual;
	}
	public void setManual(Long manual) {
		this.manual = manual;
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
		result = prime * result + ((aborted == null) ? 0 : aborted.hashCode());
		result = prime * result + ((browser == null) ? 0 : browser.hashCode());
		result = prime * result + ((catalogVersion == null) ? 0 : catalogVersion.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((fail == null) ? 0 : fail.hashCode());
		result = prime * result + ((manual == null) ? 0 : manual.hashCode());
		result = prime * result + ((operatingSystem == null) ? 0 : operatingSystem.hashCode());
		result = prime * result + ((ownerName == null) ? 0 : ownerName.hashCode());
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		result = prime * result + ((shareStatus == null) ? 0 : shareStatus.hashCode());
		result = prime * result + ((siteName == null) ? 0 : siteName.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((totalTime == null) ? 0 : totalTime.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		DetailedSharedSites other = (DetailedSharedSites) obj;
		if (aborted == null) {
			if (other.aborted != null)
				return false;
		} else if (!aborted.equals(other.aborted))
			return false;
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
		if (fail == null) {
			if (other.fail != null)
				return false;
		} else if (!fail.equals(other.fail))
			return false;
		if (manual == null) {
			if (other.manual != null)
				return false;
		} else if (!manual.equals(other.manual))
			return false;
		if (operatingSystem == null) {
			if (other.operatingSystem != null)
				return false;
		} else if (!operatingSystem.equals(other.operatingSystem))
			return false;
		if (ownerName == null) {
			if (other.ownerName != null)
				return false;
		} else if (!ownerName.equals(other.ownerName))
			return false;
		if (pass == null) {
			if (other.pass != null)
				return false;
		} else if (!pass.equals(other.pass))
			return false;
		if (shareStatus == null) {
			if (other.shareStatus != null)
				return false;
		} else if (!shareStatus.equals(other.shareStatus))
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
		if (totalTime == null) {
			if (other.totalTime != null)
				return false;
		} else if (!totalTime.equals(other.totalTime))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DetailedSharedSites [email=" + email + ", ownerName=" + ownerName + ", siteName=" + siteName + ", status=" + status + ", shareStatus=" + shareStatus + ", startTime=" + startTime + ", endTime=" + endTime + ", version=" + version + ", catalogVersion=" + catalogVersion + ", browser=" + browser + ", totalTime=" + totalTime + ", operatingSystem=" + operatingSystem + ", pass=" + pass + ", fail=" + fail + ", manual=" + manual + ", aborted=" + aborted + "]";
	}
	
}
