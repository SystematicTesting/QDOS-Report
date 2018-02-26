package com.systematictesting.daolayer.beans;

import java.util.ArrayList;
import java.util.List;

public class Report {

	private TestSuiteDetails testSuiteDetails;
	private List<TestCase> testCaseArray = new ArrayList<TestCase>();
	public TestSuiteDetails getTestSuiteDetails() {
		return testSuiteDetails;
	}
	public void setTestSuiteDetails(TestSuiteDetails testSuiteDetails) {
		this.testSuiteDetails = testSuiteDetails;
	}
	public List<TestCase> getTestCaseArray() {
		return testCaseArray;
	}
	public void setTestCaseArray(List<TestCase> testCaseArray) {
		this.testCaseArray = testCaseArray;
	}
	@Override
	public String toString() {
		return "Report [testSuiteDetails=" + testSuiteDetails + ", testCaseArray=" + testCaseArray + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((testCaseArray == null) ? 0 : testCaseArray.hashCode());
		result = prime * result + ((testSuiteDetails == null) ? 0 : testSuiteDetails.hashCode());
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
		Report other = (Report) obj;
		if (testCaseArray == null) {
			if (other.testCaseArray != null)
				return false;
		} else if (!testCaseArray.equals(other.testCaseArray))
			return false;
		if (testSuiteDetails == null) {
			if (other.testSuiteDetails != null)
				return false;
		} else if (!testSuiteDetails.equals(other.testSuiteDetails))
			return false;
		return true;
	}
}
