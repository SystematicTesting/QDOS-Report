package com.systematictesting.daolayer.beans.response;

public class UserCatalogVersion {
	String versionNumber;
	String createTime;
	int totalNumberOfSuites;
	int totalNumberOfTestCases;
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getTotalNumberOfSuites() {
		return totalNumberOfSuites;
	}
	public void setTotalNumberOfSuites(int totalNumberOfSuites) {
		this.totalNumberOfSuites = totalNumberOfSuites;
	}
	public int getTotalNumberOfTestCases() {
		return totalNumberOfTestCases;
	}
	public void setTotalNumberOfTestCases(int totalNumberOfTestCases) {
		this.totalNumberOfTestCases = totalNumberOfTestCases;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + totalNumberOfSuites;
		result = prime * result + totalNumberOfTestCases;
		result = prime * result + ((versionNumber == null) ? 0 : versionNumber.hashCode());
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
		UserCatalogVersion other = (UserCatalogVersion) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (totalNumberOfSuites != other.totalNumberOfSuites)
			return false;
		if (totalNumberOfTestCases != other.totalNumberOfTestCases)
			return false;
		if (versionNumber == null) {
			if (other.versionNumber != null)
				return false;
		} else if (!versionNumber.equals(other.versionNumber))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "UserCatalogVersion [versionNumber=" + versionNumber + ", createTime=" + createTime
				+ ", totalNumberOfSuites=" + totalNumberOfSuites + ", totalNumberOfTestCases=" + totalNumberOfTestCases
				+ "]";
	}
}
