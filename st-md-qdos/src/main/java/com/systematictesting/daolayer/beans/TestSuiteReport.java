package com.systematictesting.daolayer.beans;

public class TestSuiteReport {
	private String suiteName;
	private Report report;
	public String getSuiteName() {
		return suiteName;
	}
	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((report == null) ? 0 : report.hashCode());
		result = prime * result + ((suiteName == null) ? 0 : suiteName.hashCode());
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
		TestSuiteReport other = (TestSuiteReport) obj;
		if (report == null) {
			if (other.report != null)
				return false;
		} else if (!report.equals(other.report))
			return false;
		if (suiteName == null) {
			if (other.suiteName != null)
				return false;
		} else if (!suiteName.equals(other.suiteName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TestSuiteReport [suiteName=" + suiteName + ", report=" + report + "]";
	}
}
