package com.systematictesting.daolayer.beans.response;

import java.util.List;

import com.systematictesting.daolayer.entity.SingleTestSuite;

public class DetailedCatalogVersion {

	String versionNumber;
	String siteName;
	String email;
	String createTime;
	int totalTestSuites;
	int totalTestCases;
	List<SingleTestSuite> testSuitesArray;
	
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getTotalTestSuites() {
		return totalTestSuites;
	}
	public void setTotalTestSuites(int totalTestSuites) {
		this.totalTestSuites = totalTestSuites;
	}
	public int getTotalTestCases() {
		return totalTestCases;
	}
	public void setTotalTestCases(int totalTestCases) {
		this.totalTestCases = totalTestCases;
	}
	public List<SingleTestSuite> getTestSuitesArray() {
		return testSuitesArray;
	}
	public void setTestSuitesArray(List<SingleTestSuite> testSuitesArray) {
		this.testSuitesArray = testSuitesArray;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((siteName == null) ? 0 : siteName.hashCode());
		result = prime * result + ((testSuitesArray == null) ? 0 : testSuitesArray.hashCode());
		result = prime * result + totalTestCases;
		result = prime * result + totalTestSuites;
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
		DetailedCatalogVersion other = (DetailedCatalogVersion) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (siteName == null) {
			if (other.siteName != null)
				return false;
		} else if (!siteName.equals(other.siteName))
			return false;
		if (testSuitesArray == null) {
			if (other.testSuitesArray != null)
				return false;
		} else if (!testSuitesArray.equals(other.testSuitesArray))
			return false;
		if (totalTestCases != other.totalTestCases)
			return false;
		if (totalTestSuites != other.totalTestSuites)
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
		return "DetailedCatalogVersion [versionNumber=" + versionNumber + ", siteName=" + siteName + ", email=" + email + ", createTime=" + createTime + ", totalTestSuites=" + totalTestSuites + ", totalTestCases=" + totalTestCases + ", testSuitesArray=" + testSuitesArray + "]";
	}
}
