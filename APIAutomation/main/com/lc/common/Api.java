package com.lc.common;

public class Api {
	
	private String url;
	private String request;
	private int responseCode;
	private String responseMessage;
	
	public Api(String url, String request, int code, String message)
	{
		this.setUrl(url);
		this.setRequest(request);
		this.setResponseCode(code);
		this.setResponseMessage(message);
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
