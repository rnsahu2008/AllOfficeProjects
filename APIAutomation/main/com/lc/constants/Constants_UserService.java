package com.lc.constants;

import com.lc.utils.ReadProperties;

public class Constants_UserService {
	
	    public static final String USERSERVICE_AUTH_URL = ReadProperties.getauthURL();
		public static final String BASE_URL = ReadProperties.getbaseURL();
		public static final String GET_USER_URL_V1 = BASE_URL + "/v1/users/";
		public static final String GET_USER_URL_V2 = BASE_URL + "/v2/users/";
		public static final String AUTHENTICATE_USER_URL = "/v1/sessions";
		public static final String[] mailRecipients = {"ankur.mittal@bold.com", "arnab@bold.com", "sanjay.saini@bold.com", "rakshit.jain@bold.com", "rajat.malik@bold.com", "rajendra.gola@bold.com", "amit.kumar@bold.com", "rajender@bold.com"};
		//public static final String[] mailRecipients = {"rajendra.gola@bold.com", "amit.kumar@bold.com"}; 
		public static final String createUserUrl = BASE_URL + "/v1/users/";
	}