package com.lc.constants;

import com.lc.utils.ReadProperties;

public class Constants_JobAlerts {
	public static final String BASE_URL = ReadProperties.getbaseURL();
	public static final String HEART_BEAT_URL_V1 = "/v1/heartbeat";
	public static final String SUBSCRIBER_URL_V1 = "/v1/subscribers";
	public static final String SUBSCRIPTION_URL_V1 = "/v1/subscriptions";
	
	public static final String HEART_BEAT_URL_V2 = "/v2/heartbeat";
	public static final String SUBSCRIBER_URL_V2 = "/v2/subscribers";
	public static final String SUBSCRIPTION_URL_V2 = "/v2/subscriptions";
	public static final String[] mailRecipients = {"sanjay.saini@bold.com" , "ankur.maurya@bold.com"};
	///public static final String[] mailRecipients = {"sanjay.saini@bold.com" , "ankur.maurya@bold.com" , "mathew.varghese@bold.com" , "simardeep.sethi@bold.com", "ankur.mittal@bold.com"};

	//public static final String[] mailRecipients = {"rakshit.jain@bold.com"};
	public static String environment;
}
