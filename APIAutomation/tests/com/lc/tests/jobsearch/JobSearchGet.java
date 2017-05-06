package com.lc.tests.jobsearch;

import static com.lc.constants.Constants_JobSearch.BASE_URL;
import static com.lc.constants.Constants_JobSearch.JOB_SEARCH;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.JobSearchCoreComponents;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

public class JobSearchGet extends JobSearchCoreComponents{
	String jobId = "";
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test
	public void jobSearchById(){
		try {
			String getUrl = BASE_URL + JOB_SEARCH + "?ip=138.91.242.92&useragent=cece&q=performance%20architect&l=san%20francisco";
			ResponseBean response = HttpService.get(getUrl, null);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			JSONArray allJobs = respJson.getJSONArray("Jobs");
			JSONObject singleJob = allJobs.getJSONObject(0);
			jobId = singleJob.getString("ID");
			getUrl = BASE_URL + JOB_SEARCH + "/" + jobId;
			response = HttpService.get(getUrl, null);
			respJson = new JSONObject(response.message);
			assertNotNull(respJson, "JobTitle");
			assertNotNull(respJson, "Company");
			assertNotNull(respJson, "Source");
			assertNotNull(respJson, "Url");
			assertNotNull(respJson, "City");
			assertNotNull(respJson, "State");
			assertNotNull(respJson, "Country");
			assertNotNull(respJson, "Date");
			assertNotNull(respJson, "Description");
			assertNotNull(respJson, "PostalCode");
			assertNotNull(respJson, "IsHighlighted");
			assertNotNull(respJson, "StatusCD");
			assertNotNull(respJson, "PUID");
			assertEquals(jobId, respJson, "ID");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test (dependsOnMethods = "jobSearchById")
	public void jobSearchRecommend(){
		try {
			String url = BASE_URL + JOB_SEARCH + "/" + jobId + "/recommended/" + "?count=1";
			ResponseBean response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			assertTrue(new JSONArray(response.message).length()==1, "Not getting any recommended jobs");
			JSONArray arr = new JSONArray(response.message);
			JSONObject respJson = arr.getJSONObject(0);
			assertNotNull(respJson, "JobTitle");
			assertNotNull(respJson, "Company");
			assertNotNull(respJson, "Source");
			assertNotNull(respJson, "Url");
			assertNotNull(respJson, "City");
			assertNotNull(respJson, "State");
			assertNotNull(respJson, "Country");
			assertNotNull(respJson, "Date");
			assertNotNull(respJson, "Description");
			assertNotNull(respJson, "PostalCode");
			assertNotNull(respJson, "IsHighlighted");
			assertNotNull(respJson, "StatusCD");
			assertNotNull(respJson, "PUID");
			assertNotNull(respJson, "ID");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	//@Test
	public void jobSearchByQueryParameters(){
		try{
			String url = BASE_URL + JOB_SEARCH + "/" + jobId + "/recommended/" + "?count=1";
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

}
