package com.lc.constants;

import com.lc.utils.ReadProperties;

public class Constants_JobsStore {
	public static final String AUTH_TOKEN_URL = ReadProperties.getauthURL();
	public static final String BASE_URL_JOBS = ReadProperties.getbaseURL();
	public static final String JOBS_URL = "/v1/jobs";
	public static final String[] mailRecipients = {"sanjay.saini@bold.com" , "ankur.maurya@bold.com" , "mathew.varghese@bold.com" , "simardeep.sethi@bold.com" , "ankur.mittal@bold.com"};
	//public static final String[] mailRecipients = {"rakshit.jain@bold.com"};
	public static long waitTime = 300000;
}
