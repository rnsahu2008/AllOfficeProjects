package com.lc.utils;

import java.util.HashMap;

public class HeaderManager {
	public static HashMap<String, String> getHeaders(String clientCD, String authorization, String client_request_reference){
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", authorization);
		headers.put("client_request_reference", client_request_reference);
		headers.put("ClientCD", clientCD);
		return headers;
	}

}
