package com.lc.constants;

import com.lc.utils.ReadProperties;

public class Constants_TrackingService {

	public static final String TRACKINGSERVICE_AUTH_URL = ReadProperties.getauthURL();
	public static final String BASE_URL = ReadProperties.getbaseURL(); 
	public static final String[] mailRecipients = {"ankur.mittal@bold.com", "arnab@bold.com", "sanjay.saini@bold.com", "rakshit.jain@bold.com", "vikas.singh@bold.com", "rajendra.gola@bold.com", "amit.kumar@bold.com", "rajender@bold.com"};
	//public static final String[] mailRecipients = {"rajendra.gola@bold.com", "amit.kumar@bold.com"};
	public static final String createVisitorUrl = BASE_URL + "/v1/visitors";
	public static final String createVisitUrl = BASE_URL + "/v1/visits";
	public static final String getVisitUrl = BASE_URL + "/v1/visits/";
	public static final String getPortalUrl = BASE_URL + "/v1/portals?domain=";
	public static final String getReferrer = BASE_URL + "/v1/referrers/";
}
