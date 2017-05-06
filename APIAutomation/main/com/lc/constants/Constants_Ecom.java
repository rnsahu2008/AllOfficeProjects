package com.lc.constants;

import com.lc.utils.ReadProperties;

public class Constants_Ecom {
	public static final String ECOMAUTH_URL = ReadProperties.getauthURL();
	public static final String BASE_URL = ReadProperties.getbaseURL();
	public static final String SUBSCRIBE_URL = "/v1/subscriptions";
	public static final String PLAN_LIST_URL = "/v1/plans";
	public static final String PAYMENT_URL = "/v1/payments";
	public static final String SEARCH_URL = "/v1/customers/search";
	public static final String REFUND_DECLINE_URL = "/v1/refundqueues/?action=decline";
	public static final String REFUND_PROCESS_URL = "/v1/refundqueues/?action=process";
	public static final String REFUND_UNDO_URL = "/v1/refundqueues/?action=undo";
	public static final String[] mailRecipients = {"rajendra.gola@bold.com"};
}
