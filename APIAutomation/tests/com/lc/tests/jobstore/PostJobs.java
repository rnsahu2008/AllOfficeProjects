package com.lc.tests.jobstore;

import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.JobStoreCoreComponents;
import com.lc.dataprovider.JobStore_DP;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

import static com.lc.constants.Constants_JobsStore.*;

public class PostJobs extends JobStoreCoreComponents{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test (dataProviderClass = JobStore_DP.class , dataProvider = "getSourceCD")
	public void createJobWithAllSourceCD(String sourceCD){
		try {
			JSONObject dataInPost = new JSONObject();
			dataInPost.put("PUID", RandomStringUtils.randomNumeric(8));
			dataInPost.put("Title", RandomStringUtils.randomAlphanumeric(8));
			dataInPost.put("Description", RandomStringUtils.randomAlphanumeric(8));
			dataInPost.put("City", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("State", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("PostalCode", RandomStringUtils.randomAlphanumeric(6));
			dataInPost.put("CountryCD", "US");
			dataInPost.put("StatusCD", "ACTV");
			dataInPost.put("SourceCD", sourceCD);
			dataInPost.put("Company", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("PostedOn", "2015-04-06 00:00:00");
			dataInPost.put("EmploymentTypeCD", "1234test1234");
			dataInPost.put("ApplyUrl", "https://jobtap.com/?abc=true");
			dataInPost.put("StringHash", "");			
			
			/*
			 * POST /v1/jobs Creates a specified job.
			 */
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL in response");
			String id = response.message.replace("\"", "");
			System.out.println(sourceCD);
			System.out.println(id);
			
			/*
			 * GET /v1/jobs/{id} Gets a job with specified job Id.
			 */
			url = BASE_URL_JOBS + "/v1/jobs/" + id;
			response = HttpService.get(url, new HashMap<String, String>());
			
			JSONObject responseJson = new JSONObject(response.message); 
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL in response");
			assertEquals(dataInPost.getString("PUID") , responseJson.getString("PUID") , "PUID mismatch");
			assertEquals(dataInPost.getString("Title") , responseJson.getString("Title") , "Title mismatch");
			assertEquals(dataInPost.getString("Description") , responseJson.getString("Description") , "Description mismatch");
			assertEquals(dataInPost.getString("City") , responseJson.getString("City") , "City mismatch");
			assertEquals(dataInPost.getString("State") , responseJson.getString("State") , "State mismatch");
			assertEquals(dataInPost.getString("PostalCode") , responseJson.getString("PostalCode") , "PostalCode mismatch");
			assertEquals(dataInPost.getString("CountryCD") , responseJson.getString("CountryCD") , "CountryCD mismatch");
			assertEquals(dataInPost.getString("StatusCD") , responseJson.getString("StatusCD") , "StatusCD mismatch");
			assertEquals(dataInPost.getString("SourceCD") , responseJson.getString("SourceCD") , "SourceCD mismatch");
			assertEquals(dataInPost.getString("Company") , responseJson.getString("Company") , "Company mismatch");
			assertEquals(dataInPost.getString("EmploymentTypeCD") , responseJson.getString("EmploymentTypeCD") , "EmploymentTypeCD mismatch");
			assertTrue(responseJson.getString("ApplyUrl").contains(dataInPost.getString("ApplyUrl")) , "ApplyUrl mismatch");
			
			assertNotNull(responseJson.getString("ID"), "ID is null");
			assertNotNull(responseJson.getString("PostedOn"), "PostedOn is null");
			assertNotNull(responseJson.getString("TimeStamp"), "TimeStamp is null");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test (dataProviderClass = JobStore_DP.class , dataProvider = "postJobData")
	public void createJobWithSeedData(HashMap<String, String> list){
		try {
			JSONObject dataInPost = createJobJson(list);
			dataInPost.put("PUID", RandomStringUtils.randomNumeric(8));
			//System.out.println(jobData.toString());
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL in response");
			String id = response.message.replace("\"", "");
			
			url = BASE_URL_JOBS + "/v1/jobs?id=" + id +  "&co=" + dataInPost.getString("CountryCD");
			response = HttpService.get(url, new HashMap<String, String>());
			
			JSONObject responseJson = new JSONArray(response.message).getJSONObject(0); 
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL in response");
			assertEquals(dataInPost.getString("PUID") , responseJson.getString("PUID") , "PUID mismatch");
			assertEquals(dataInPost.getString("Title") , responseJson.getString("Title") , "Title mismatch");
			assertEquals(dataInPost.getString("Description") , responseJson.getString("Description") , "Description mismatch");
			assertEquals(dataInPost.getString("City") , responseJson.getString("City") , "City mismatch");
			assertEquals(dataInPost.getString("State") , responseJson.getString("State") , "State mismatch");
			assertEquals(dataInPost.getString("PostalCode") , responseJson.getString("PostalCode") , "PostalCode mismatch");
			assertEquals(dataInPost.getString("CountryCD") , responseJson.getString("CountryCD") , "CountryCD mismatch");
			assertEquals(dataInPost.getString("StatusCD") , responseJson.getString("StatusCD") , "StatusCD mismatch");
			assertEquals(dataInPost.getString("SourceCD") , responseJson.getString("SourceCD") , "SourceCD mismatch");
			assertEquals(dataInPost.getString("Company") , responseJson.getString("Company") , "Company mismatch");
			assertEquals(dataInPost.getString("EmploymentTypeCD") , responseJson.getString("EmploymentTypeCD") , "EmploymentTypeCD mismatch");
			assertTrue(responseJson.getString("ApplyUrl").contains(dataInPost.getString("ApplyUrl")) , "ApplyUrl mismatch");
			
			assertNotNull(responseJson.getString("ID"), "ID is null");
			assertNotNull(responseJson.getString("PostedOn"), "PostedOn is null");
			assertNotNull(responseJson.getString("TimeStamp"), "TimeStamp is null");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//@Test
	public void createJobWithNewData(){
		try {
			String[] checkPoints = {"Title" , "Description" , "Company"};
			JSONObject dataInPost = new JSONObject();
			dataInPost.put("PUID", RandomStringUtils.randomNumeric(8));
			dataInPost.put("Title", RandomStringUtils.randomAlphanumeric(8));
			dataInPost.put("Description", RandomStringUtils.randomAlphanumeric(8));
			dataInPost.put("City", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("State", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("PostalCode", RandomStringUtils.randomAlphanumeric(6));
			dataInPost.put("CountryCD", "US");
			dataInPost.put("StatusCD", "ACTV");
			dataInPost.put("SourceCD", "JTR");
			dataInPost.put("Company", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("PostedOn", "2015-04-06 00:00:00");
			dataInPost.put("EmploymentTypeCD", "1234test1234");
			dataInPost.put("ApplyUrl", "https://jobtap.com/?abc=true");
			dataInPost.put("StringHash", "");			
			
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL in response");
			String id = response.message.replace("\"", "");
			Thread.sleep(waitTime);
			for (String cp : checkPoints) {
				url = BASE_URL_JOBS + "/v1/jobs?ip=160.125.24.25&q=" + dataInPost.getString(cp);
				response = HttpService.get(url, new HashMap<String, String>());
				checkSuccessCode(response.code);
				assertNotNull(response.message, "Getting NULL in response");
				JSONObject respJson = new JSONObject(response.message);
				JSONArray respArray = respJson.getJSONArray("ResultList");
				assertTrue(respArray.length()!=0, "JObs list count is 0");
				for(int index=0 ; index<respArray.length() ; index++){
					JSONObject json = respArray.getJSONObject(index);
					assertEquals(id, json, "ID");
					assertEquals(dataInPost.getString("Title"), json , "Title");
					assertEquals(dataInPost.getString("Description"), json , "Description");
					assertEquals(dataInPost.getString("City"), json , "City");
					assertEquals(dataInPost.getString("State"), json , "State");
					assertEquals(dataInPost.getString("PostalCode"), json , "PostalCode");
					assertEquals(dataInPost.getString("CountryCD"), json , "CountryCD");
					assertEquals(dataInPost.getString("StatusCD"), json , "StatusCD");
					assertEquals(dataInPost.getString("SourceCD"), json , "SourceCD");
					assertEquals(dataInPost.getString("Company"), json , "Company");
					assertEquals(dataInPost.getString("ApplyUrl") + "&sid=", json , "ApplyUrl");					
					assertTrue(json.getBoolean("IsHighlighted")==false, "IsHighlighted is wrongly shown as TRUE");
					assertTrue(!StringUtils.isBlank(json.getString("TimeStamp")), "TimeStamp is BLANK");
					assertTrue(!StringUtils.isBlank(json.getString("PostedOn")), "PostedOn is BLANK");
					assertEquals(dataInPost.getString("EmploymentTypeCD"), json , "EmploymentTypeCD");					
				}
			}
			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	

	
}
