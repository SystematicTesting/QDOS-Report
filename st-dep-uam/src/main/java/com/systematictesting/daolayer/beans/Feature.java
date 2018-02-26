package com.systematictesting.daolayer.beans;

public class Feature {

	private String automationCoverage = "AutomationCoverage";
	private String moreVersionHistory = "MoreVersionHistory";
	private String siteManager = "SiteManager";
	
	public String getAutomationCoverage() {
		return automationCoverage;
	}
	public void setAutomationCoverage(String automationCoverage) {
		this.automationCoverage = automationCoverage;
	}
	public String getMoreVersionHistory() {
		return moreVersionHistory;
	}
	public void setMoreVersionHistory(String moreVersionHistory) {
		this.moreVersionHistory = moreVersionHistory;
	}
	public String getSiteManager() {
		return siteManager;
	}
	public void setSiteManager(String siteManager) {
		this.siteManager = siteManager;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((automationCoverage == null) ? 0 : automationCoverage.hashCode());
		result = prime * result + ((moreVersionHistory == null) ? 0 : moreVersionHistory.hashCode());
		result = prime * result + ((siteManager == null) ? 0 : siteManager.hashCode());
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
		Feature other = (Feature) obj;
		if (automationCoverage == null) {
			if (other.automationCoverage != null)
				return false;
		} else if (!automationCoverage.equals(other.automationCoverage))
			return false;
		if (moreVersionHistory == null) {
			if (other.moreVersionHistory != null)
				return false;
		} else if (!moreVersionHistory.equals(other.moreVersionHistory))
			return false;
		if (siteManager == null) {
			if (other.siteManager != null)
				return false;
		} else if (!siteManager.equals(other.siteManager))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Feature [automationCoverage=" + automationCoverage
				+ ", moreVersionHistory=" + moreVersionHistory + ", siteManager="
				+ siteManager + "]";
	}

}
