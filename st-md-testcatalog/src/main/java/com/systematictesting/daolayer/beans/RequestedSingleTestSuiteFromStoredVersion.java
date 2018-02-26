package com.systematictesting.daolayer.beans;

import com.systematictesting.daolayer.entity.SingleTestSuite;

public class RequestedSingleTestSuiteFromStoredVersion {

	private String email;
	private String siteName;
	private SingleTestSuite singleTestSuite;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public SingleTestSuite getSingleTestSuite() {
		return singleTestSuite;
	}
	public void setSingleTestSuite(SingleTestSuite singleTestSuite) {
		this.singleTestSuite = singleTestSuite;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((singleTestSuite == null) ? 0 : singleTestSuite.hashCode());
		result = prime * result + ((siteName == null) ? 0 : siteName.hashCode());
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
		RequestedSingleTestSuiteFromStoredVersion other = (RequestedSingleTestSuiteFromStoredVersion) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (singleTestSuite == null) {
			if (other.singleTestSuite != null)
				return false;
		} else if (!singleTestSuite.equals(other.singleTestSuite))
			return false;
		if (siteName == null) {
			if (other.siteName != null)
				return false;
		} else if (!siteName.equals(other.siteName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RequestedSingleTestSuiteFromStoredVersion [email=" + email + ", siteName=" + siteName + ", singleTestSuite=" + singleTestSuite + "]";
	}
}
