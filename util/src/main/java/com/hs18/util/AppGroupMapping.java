package com.hs18.util;

import java.util.List;

public class AppGroupMapping {
	public String appName;
	List<String> group , browser;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public List<String> getGroup() {
		return group;
	}

	public void setGroup(List<String> group) {
		this.group = group;
	}

	public List<String> getBrowser() {
		return browser;
	}

	public void setBrowser(List<String> browser) {
		this.browser = browser;
	}

}
