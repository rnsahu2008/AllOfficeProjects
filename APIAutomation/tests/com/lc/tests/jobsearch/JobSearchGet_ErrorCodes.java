package com.lc.tests.jobsearch;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.JobSearchCoreComponents;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

import static com.lc.constants.Constants_JobSearch.*;
public class JobSearchGet_ErrorCodes extends JobSearchCoreComponents{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test
	public void getJobsErrorCodes_412_1(){
		try {
			String url = BASE_URL + JOB_SEARCH + "?ip=&useragent=firefox&q=manager";
			ResponseBean response = HttpService.get(url, null);
			JSONObject jsonResp = new JSONObject(response.message);
			checkSuccessCode(response.code);
			assertEquals("false", jsonResp, "IsValidRequest");
			assertEquals("412", jsonResp, "HttpStatusCode");
			assertEquals("The parameter ip is required.", jsonResp, "Reason");
			assertEquals("Try using a parameter ip=ip.", jsonResp, "Hint");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getJobsErrorCodes_412_2(){
		try {
			String url = BASE_URL + JOB_SEARCH + "?ip=138.91.242.92&useragent=&q=manager";
			ResponseBean response = HttpService.get(url, null);
			JSONObject jsonResp = new JSONObject(response.message);
			checkSuccessCode(response.code);
			assertEquals("false", jsonResp, "IsValidRequest");
			assertEquals("412", jsonResp, "HttpStatusCode");
			assertEquals("The parameter useragent is required.", jsonResp, "Reason");
			assertEquals("Try using a parameter useragent=browser.", jsonResp, "Hint");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getJobsErrorCodes_412_3(){
		try {
			String url = BASE_URL + JOB_SEARCH + "?ip=138.91.242.92&useragent=firefox";
			ResponseBean response = HttpService.get(url, null);
			JSONObject jsonResp = new JSONObject(response.message);
			checkSuccessCode(response.code);
			assertEquals("false", jsonResp, "IsValidRequest");
			assertEquals("412", jsonResp, "HttpStatusCode");
			assertEquals("At minimum a location, keyword, or category is required for a valid request.", jsonResp, "Reason");
			assertEquals("Try using a parameter q=jobQuery or l=location.", jsonResp, "Hint");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getJobsErrorCodes_412_4(){
		try {
			String url = BASE_URL + JOB_SEARCH + "?ip=138.91.242.92&useragent=firefox&q=manager&so=wrongvalue";
			ResponseBean response = HttpService.get(url, null);
			JSONObject jsonResp = new JSONObject(response.message);
			checkSuccessCode(response.code);
			assertEquals("false", jsonResp, "IsValidRequest");
			assertEquals("412", jsonResp, "HttpStatusCode");
			assertEquals("The value parameter so is not valid.", jsonResp, "Reason");
			assertEquals("Try using a parameter so=stored.", jsonResp, "Hint");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getJobsNullResponse(){
		try {
			String url = BASE_URL + JOB_SEARCH + "/11111111";
			ResponseBean response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			assertEquals("null", response.message, "Not getting NULL in response");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

}
