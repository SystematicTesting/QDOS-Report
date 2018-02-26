package com.systematictesting.daolayer.beans;

import java.util.ArrayList;
import java.util.List;

public class TestCase {

	private String testCaseId;
	private String testCaseName;
	private String startTime;
	private String endTime;
	private Long duration;
	private String status;
	private String statusClass;
	private List<TestStep> testStepsData = new ArrayList<TestStep>();
	private String videoFile;
	
	public String getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}
	public String getTestCaseName() {
		return testCaseName;
	}
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
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
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusClass() {
		return statusClass;
	}
	public void setStatusClass(String statusClass) {
		this.statusClass = statusClass;
	}
	public List<TestStep> getTestStepsData() {
		return testStepsData;
	}
	public void setTestStepsData(List<TestStep> testStepsData) {
		this.testStepsData = testStepsData;
	}
	public String getVideoFile() {
		return videoFile;
	}
	public void setVideoFile(String videoFile) {
		this.videoFile = videoFile;
	}
	@Override
	public String toString() {
		return "TestCase [testCaseId=" + testCaseId + ", testCaseName=" + testCaseName + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", duration=" + duration + ", status=" + status + ", statusClass="
				+ statusClass + ", testStepsData=" + testStepsData + ", videoFile=" + videoFile + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((statusClass == null) ? 0 : statusClass.hashCode());
		result = prime * result + ((testCaseId == null) ? 0 : testCaseId.hashCode());
		result = prime * result + ((testCaseName == null) ? 0 : testCaseName.hashCode());
		result = prime * result + ((testStepsData == null) ? 0 : testStepsData.hashCode());
		result = prime * result + ((videoFile == null) ? 0 : videoFile.hashCode());
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
		TestCase other = (TestCase) obj;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
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
		if (statusClass == null) {
			if (other.statusClass != null)
				return false;
		} else if (!statusClass.equals(other.statusClass))
			return false;
		if (testCaseId == null) {
			if (other.testCaseId != null)
				return false;
		} else if (!testCaseId.equals(other.testCaseId))
			return false;
		if (testCaseName == null) {
			if (other.testCaseName != null)
				return false;
		} else if (!testCaseName.equals(other.testCaseName))
			return false;
		if (testStepsData == null) {
			if (other.testStepsData != null)
				return false;
		} else if (!testStepsData.equals(other.testStepsData))
			return false;
		if (videoFile == null) {
			if (other.videoFile != null)
				return false;
		} else if (!videoFile.equals(other.videoFile))
			return false;
		return true;
	}
}
