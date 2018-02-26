package com.systematictesting.daolayer.beans;

public class TestStep {

	private String dataSetId;
	private String stepId;
	private String stepDescription;
	private String stepKeyword;
	private String proceedOnFail;
	private String stepPageStats;
	private Long duration;
	private String systemMessage;
	private String stepScreenShot;
	private String stepStatusClass;
	private String stepStatus;
	
	public String getDataSetId() {
		return dataSetId;
	}
	public void setDataSetId(String dataSetId) {
		this.dataSetId = dataSetId;
	}
	public String getStepId() {
		return stepId;
	}
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	public String getStepDescription() {
		return stepDescription;
	}
	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}
	public String getStepKeyword() {
		return stepKeyword;
	}
	public void setStepKeyword(String stepKeyword) {
		this.stepKeyword = stepKeyword;
	}
	public String getProceedOnFail() {
		return proceedOnFail;
	}
	public void setProceedOnFail(String proceedOnFail) {
		this.proceedOnFail = proceedOnFail;
	}
	public String getStepPageStats() {
		return stepPageStats;
	}
	public void setStepPageStats(String stepPageStats) {
		this.stepPageStats = stepPageStats;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getSystemMessage() {
		return systemMessage;
	}
	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}
	public String getStepScreenShot() {
		return stepScreenShot;
	}
	public void setStepScreenShot(String stepScreenShot) {
		this.stepScreenShot = stepScreenShot;
	}
	public String getStepStatusClass() {
		return stepStatusClass;
	}
	public void setStepStatusClass(String stepStatusClass) {
		this.stepStatusClass = stepStatusClass;
	}
	public String getStepStatus() {
		return stepStatus;
	}
	public void setStepStatus(String stepStatus) {
		this.stepStatus = stepStatus;
	}
	@Override
	public String toString() {
		return "TestStep [dataSetId=" + dataSetId + ", stepId=" + stepId + ", stepDescription=" + stepDescription
				+ ", stepKeyword=" + stepKeyword + ", proceedOnFail=" + proceedOnFail + ", stepPageStats="
				+ stepPageStats + ", duration=" + duration + ", systemMessage=" + systemMessage + ", stepScreenShot="
				+ stepScreenShot + ", stepStatusClass=" + stepStatusClass + ", stepStatus=" + stepStatus + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataSetId == null) ? 0 : dataSetId.hashCode());
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((proceedOnFail == null) ? 0 : proceedOnFail.hashCode());
		result = prime * result + ((stepDescription == null) ? 0 : stepDescription.hashCode());
		result = prime * result + ((stepId == null) ? 0 : stepId.hashCode());
		result = prime * result + ((stepKeyword == null) ? 0 : stepKeyword.hashCode());
		result = prime * result + ((stepPageStats == null) ? 0 : stepPageStats.hashCode());
		result = prime * result + ((stepScreenShot == null) ? 0 : stepScreenShot.hashCode());
		result = prime * result + ((stepStatus == null) ? 0 : stepStatus.hashCode());
		result = prime * result + ((stepStatusClass == null) ? 0 : stepStatusClass.hashCode());
		result = prime * result + ((systemMessage == null) ? 0 : systemMessage.hashCode());
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
		TestStep other = (TestStep) obj;
		if (dataSetId == null) {
			if (other.dataSetId != null)
				return false;
		} else if (!dataSetId.equals(other.dataSetId))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (proceedOnFail == null) {
			if (other.proceedOnFail != null)
				return false;
		} else if (!proceedOnFail.equals(other.proceedOnFail))
			return false;
		if (stepDescription == null) {
			if (other.stepDescription != null)
				return false;
		} else if (!stepDescription.equals(other.stepDescription))
			return false;
		if (stepId == null) {
			if (other.stepId != null)
				return false;
		} else if (!stepId.equals(other.stepId))
			return false;
		if (stepKeyword == null) {
			if (other.stepKeyword != null)
				return false;
		} else if (!stepKeyword.equals(other.stepKeyword))
			return false;
		if (stepPageStats == null) {
			if (other.stepPageStats != null)
				return false;
		} else if (!stepPageStats.equals(other.stepPageStats))
			return false;
		if (stepScreenShot == null) {
			if (other.stepScreenShot != null)
				return false;
		} else if (!stepScreenShot.equals(other.stepScreenShot))
			return false;
		if (stepStatus == null) {
			if (other.stepStatus != null)
				return false;
		} else if (!stepStatus.equals(other.stepStatus))
			return false;
		if (stepStatusClass == null) {
			if (other.stepStatusClass != null)
				return false;
		} else if (!stepStatusClass.equals(other.stepStatusClass))
			return false;
		if (systemMessage == null) {
			if (other.systemMessage != null)
				return false;
		} else if (!systemMessage.equals(other.systemMessage))
			return false;
		return true;
	}
}
