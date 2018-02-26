/**
 * Copyright (c) Apr 15, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.rest.core.beans;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.systematictesting.daolayer.constants.UserConstants;

public class GraphTemplate {
	private String startTime;
	private String endTime;
	private String duration;
	private String environment;
	private String graphUrl;
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
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getGraphUrl() {
		return graphUrl;
	}
	public void setGraphUrl(String suiteId, String passCount, String failCount, String manual, String abortedCount) {
		StringBuilder sb = new StringBuilder("http://chart.apis.google.com/chart?chs=400x300&chdlp=b&cht=p3");
		try {
			sb.append("&chf=").append(URLEncoder.encode("bg,s,FFDBD2",UserConstants.UTF_8_ENCODING));
			sb.append("&chtt=").append(URLEncoder.encode("SUITE ID :"+suiteId,UserConstants.UTF_8_ENCODING));
			sb.append("&chdl=").append(URLEncoder.encode("PASS:"+passCount+"|FAIL:"+failCount+"|MANUAL:"+manual+"|ABORTED:"+abortedCount,UserConstants.UTF_8_ENCODING));
			sb.append("&chd=").append(URLEncoder.encode("t:"+passCount+","+failCount+","+manual+","+abortedCount,UserConstants.UTF_8_ENCODING));
			sb.append("&chco=").append(URLEncoder.encode("00A500,FF0000,A3D4A3,FFA500",UserConstants.UTF_8_ENCODING));
			this.graphUrl = sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			this.graphUrl = null;
		}
	}
}
