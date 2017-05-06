package com.lc.tests.jobstore;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.JobStoreCoreComponents;
import static com.lc.constants.Constants_JobsStore.*;
import java.util.HashMap;
import org.testng.annotations.Test;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

import org.testng.annotations.DataProvider;
public class PostJobs_Negative  extends JobStoreCoreComponents {

	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test(dataProvider = "getnullandemptystring")
	public void NegativeScenarios_AllInvalidProperty(String input){		
		try {
			JSONObject dataInPost = new JSONObject();
			dataInPost.put("PUID", input);
			dataInPost.put("Title", input);
			dataInPost.put("Description", input);
			dataInPost.put("City", input);
			dataInPost.put("State", input);
			dataInPost.put("PostalCode", RandomStringUtils.randomAlphanumeric(6));
			dataInPost.put("CountryCD", "US");
			dataInPost.put("StatusCD", input);
			dataInPost.put("SourceCD", input);
			dataInPost.put("Company", input);
			dataInPost.put("PostedOn", input);
			dataInPost.put("EmploymentTypeCD", "1234test1234");
			dataInPost.put("ApplyUrl", input);
			dataInPost.put("StringHash", input);			
			
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(dataProvider = "getnullandemptystring")
	public void NegativeScenarios_Invalid_PUID(String input){		
		try {
			JSONObject dataInPost = this.getInvalidJSONObject(input, "PUID");					
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
					
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
		
	@Test(dataProvider = "getnullandemptystring")
	public void NegativeScenarios_Invalid_Title(String input){		
		try {
			JSONObject dataInPost = this.getInvalidJSONObject(input, "Title");								
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
			} 
		catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(dataProvider = "getnullandemptystring")
	public void NegativeScenarios_Invalid_Description(String input){		
		try {
			JSONObject dataInPost = this.getInvalidJSONObject(input, "Description");								
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
			} 
		catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(dataProvider = "getnullandemptystring")
	public void NegativeScenarios_Invalid_City(String input){		
		try {
			JSONObject dataInPost = this.getInvalidJSONObject(input, "City");			
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
			} 
		catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(dataProvider = "getnullandemptystring")
	public void NegativeScenarios_Invalid_State(String input){		
		try {
			JSONObject dataInPost = this.getInvalidJSONObject(input, "State");
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
			}
		catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(dataProvider = "getnullandemptystring")
	public void NegativeScenarios_Invalid_StatusCD(String input){		
		try {
			JSONObject dataInPost = this.getInvalidJSONObject(input, "StatusCD");
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
		}
		catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(dataProvider = "getNullAndEmptyStringForSourceCD")
	public void NegativeScenarios_Invalid_SourceCD(String input){		
		try {
			JSONObject dataInPost = this.getInvalidJSONObject(input, "SourceCD");
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(dataProvider = "getnullandemptystring")
	public void NegativeScenarios_Invalid_Company(String input){		
		try {
			JSONObject dataInPost = this.getInvalidJSONObject(input, "Company");
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(dataProvider = "getnullandemptystring")
	public void NegativeScenarios_Invalid_PostedOn(String input){		
		try {
			JSONObject dataInPost = this.getInvalidJSONObject(input, "PostedOn");
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");		
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(dataProvider = "getnullandemptystring")
	public void NegativeScenarios_Invalid_ApplyUrl(String input){		
		try {
			JSONObject dataInPost = this.getInvalidJSONObject(input, "ApplyUrl");
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");		
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@DataProvider(name = "getnullandemptystring")
	public Object[][] getnullandemptystring()
	{
		return new Object[][] { { "" }, { "NULL" } , { "   " },{ null }};
	}
	
	@DataProvider(name = "getNullAndEmptyStringForSourceCD")
	public Object[][] getNullAndEmptyStringForSourceCD()
	{
		//// Property is missing from post request is null is passed.The
		//// behaviour of the SourceCD is little bit different ,if property is missing then it takes default
		//// values of JTR .which might be not right, need to discuss.
		return new Object[][] { { "" }, { "NULL" } , { "   " }};
		////return new Object[][] { { "" }, { "NULL" } , { "   " },{ null }};
	}
	
	private JSONObject getInvalidJSONObject(String input,String properNameToInvalidate)
	{
		JSONObject dataInPost = new JSONObject();
		try {

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
		if(input=="NULL")
		{
			dataInPost.put(properNameToInvalidate, JSONObject.NULL);
		}
		else
		{
			dataInPost.put(properNameToInvalidate, input);	
		}

		}
		catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		return dataInPost;
	}
}
