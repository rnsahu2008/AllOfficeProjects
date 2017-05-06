package com.lc.tests.jobstore;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
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

public class GetJobs extends JobStoreCoreComponents{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test
	public void getJob_US_Default_CountryCode(){
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
			
			/*
			 * GET /v1/jobs Gets a job with specified job Ids.
			 */
			url = BASE_URL_JOBS + "/v1/jobs?" + "id=" + id;
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
	
	@Test (dataProviderClass = JobStore_DP.class , dataProvider = "getCountryCode")
	public void getJob_US_UK_CountryCode(String countryCode){
		try {
			JSONObject dataInPost = new JSONObject();
			dataInPost.put("PUID", RandomStringUtils.randomNumeric(8));
			dataInPost.put("Title", RandomStringUtils.randomAlphanumeric(8));
			dataInPost.put("Description", RandomStringUtils.randomAlphanumeric(8));
			dataInPost.put("City", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("State", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("PostalCode", RandomStringUtils.randomAlphanumeric(6));
			dataInPost.put("CountryCD", countryCode);
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
			
			/*
			 * GET /v1/jobs Gets a job with specified job Ids.
			 */
			url = BASE_URL_JOBS + "/v1/jobs?id=" + id + "&co=" + countryCode;
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
	
	@Test
	public void getJobById_US_Default_CountryCode(){
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

	
	@Test (dataProviderClass = JobStore_DP.class , dataProvider = "getCountryCode")
	public void getJobById_US_UK_CountryCode(String countryCode){
		try {
			JSONObject dataInPost = new JSONObject();
			dataInPost.put("PUID", RandomStringUtils.randomNumeric(8));
			dataInPost.put("Title", RandomStringUtils.randomAlphanumeric(8));
			dataInPost.put("Description", RandomStringUtils.randomAlphanumeric(8));
			dataInPost.put("City", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("State", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("PostalCode", RandomStringUtils.randomAlphanumeric(6));
			dataInPost.put("CountryCD", countryCode);
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
				
			/*
			 * GET /v1/jobs/{id} Gets a job with specified job Id.
			 */
			url = BASE_URL_JOBS + "/v1/jobs/" + id + "?co=" + countryCode;
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
	
	@Test (dataProviderClass = JobStore_DP.class , dataProvider = "getJobData" , priority = 1)
	public void getJobsWithSeedData(HashMap<String, String> list){
		try {
			String jobData = createUrl(list);
			String url = BASE_URL_JOBS + "/v1/jobs?" + jobData;
			ResponseBean response = HttpService.get(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL in response");
			JSONObject respJson = new JSONObject(response.message);
			JSONArray respArray = respJson.getJSONArray("ResultList");
			assertTrue(respArray.length()!=0, "JObs list count is 0");
			for(int index=0 ; index<respArray.length() ; index++){
				JSONObject json = respArray.getJSONObject(index);
				assertNotNull(json, "PUID");
				assertNotNull(json,"Title");
				assertNotNull(json,"Description");
				assertNotNull(json,"City");
				assertNotNull(json,"State");
				assertNotNull(json,"PostalCode"); //Not mandatory...In case of JTR its never null
				assertNotNull(json,"CountryCD");
				assertNotNull(json,"StatusCD");
				assertNotNull(json,"SourceCD");
				assertNotNull(json,"ID");
				assertNotNull(json,"Company");
				assertNotNull(json,"PostedOn");
				assertNotNull(json,"TimeStamp");
				assertNotNull(json,"ApplyUrl");
				assertNotNull(json,"Provider");
				//assertNotNull(json,"JobDescriptionScore");
				//assertNotNull(json,"EmploymentTypeCD");
				assertNotNull(json,"Coordinates");
				//assertNotNull(json,"Score");
				//assertNotNull(json,"HighlightingSnippet");
				assertNotNull(json,"IsHighlighted");
//				assertNotNull(json,"CommonSkills");
//				assertNotNull(json,"OtherSkills");
			}
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void checkLocationsWithPrefix(){
		try {
			String url = BASE_URL_JOBS + "/v1/jobs/locations?prefix=new";
			ResponseBean response = HttpService.get(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			JSONArray respArray = new JSONArray(response.message);
			int records = respArray.length();
			Random rnd = new Random();
			int countNum = rnd.nextInt(records) + 1;
			
			url = BASE_URL_JOBS + "/v1/jobs/locations?prefix=new&count=" + countNum;
			response = HttpService.get(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			assertNotNull(response.message,"Getting NULL in response");
			respArray = new JSONArray(response.message);
			assertTrue(respArray.length(), countNum, "Array count not equal to expected count of " + countNum);
			
			url = BASE_URL_JOBS + "/v1/jobs/locations?prefix=new";
			response = HttpService.get(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			assertNotNull(response.message,"Getting NULL in response");
			respArray = new JSONArray(response.message);
			assertTrue(respArray.length(), 10, "Array count not equal to 10");
			
			url = BASE_URL_JOBS + "/v1/jobs/locations?prefix=newyork";
			response = HttpService.get(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			assertNotNull(response.message,"Getting NULL in response");
			respArray = new JSONArray(response.message);
			assertTrue(respArray.length() > 0, "Array count is zero");
			
			url = BASE_URL_JOBS + "/v1/jobs/locations?prefix=washington";
			response = HttpService.get(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			assertNotNull(response.message,"Getting NULL in response");
			respArray = new JSONArray(response.message);
			assertTrue(respArray.length() > 0, "Array count is zero");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
		
	//@Test
	//Commit delay of 30 min
	public void checkTitles(){
		try {
			String title = RandomStringUtils.randomAlphabetic(8);
			for(int i = 0;i<10;i++){
			ResponseBean postResponse = createRandomJob(title);
			checkSuccessCode(postResponse.code);
			}
			for(int i=4 ; i<9 ; i++){
				String url = BASE_URL_JOBS + "/v1/jobs/titles?prefix=" + title.substring(0, i);
				ResponseBean getResponse = HttpService.get(url, new HashMap<String, String>());
				checkSuccessCode(getResponse.code);
				JSONArray arr = new JSONArray(getResponse.message);
				assertTrue(arr.length()>0, "Not getting Title in response::Response is-->> " + getResponse.message);
				assertTrue(arr.toString().toLowerCase().contains(title.toLowerCase()), "Expected Title not reflected in response::Response is-->> " + getResponse.message);
			}
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void spellCheckWithSingleFault(){
		try {
			String searchTerm = "mangerr";
			String url = BASE_URL_JOBS + "/v1/jobs/spellcheck?searchTerm="+ URLEncoder.encode(searchTerm, "UTF-8");
			ResponseBean response = HttpService.get(url , null);
			checkSuccessCode(response.code);
			assertNotNull(response.message,"Getting NULL in response");
			JSONObject spellChecks = new JSONObject(response.message).getJSONArray("SpellChecks").getJSONObject(0);
			JSONArray suggestions = spellChecks.getJSONArray("Suggestions");
			assertTrue(spellChecks.getString("Query").equals(searchTerm), "Query value is not correct in reponse | Response is-->> " + response.message);
			assertTrue(spellChecks.getInt("NumFound") > 0, "NumFound is not greater than 0 | Response is-->> " + response.message);
			assertTrue(suggestions.length() > 0, "Suggestions array is empty | Response is-->>  + response.message");
			JSONArray collations = new JSONObject(response.message).getJSONArray("Collations");
			assertTrue(collations.length() > 0, "Collation array is empty | Response is-->> " + response.message);			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test
	public void getJob_WithSearchOperation_Sr_OUT(){
		try {			
			String url = BASE_URL_JOBS + "/v1/jobs?" + "ip=10.10.10.2&sr=out";
		    ResponseBean response = HttpService.get(url, new HashMap<String, String>());			
			JSONObject responseJson = new JSONObject(response.message);
			JSONArray respArray = responseJson.getJSONArray("ResultList");
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL in response");
			assertTrue(respArray.length()!=0, "JObs list count is 0");
			for(int index=0 ; index<respArray.length() ; index++){
				JSONObject json = respArray.getJSONObject(index);
				assertNotNull(json, "PUID");
				assertNotNull(json,"Title");
				assertNotNull(json,"Description");
				assertNotNull(json,"StatusCD");
				assertNotNull(json,"SourceCD");			
				assertEquals(json.getString("SourceCD") , "OUT" , "SourceCD mismatch");
				assertEquals(json.getString("Company") , "Outsource.com" , "Company mismatch");
				assertNotNull(json,"ID");
				assertNotNull(json,"Company");			
				assertNotNull(json,"ApplyUrl");				
			}
			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
}
