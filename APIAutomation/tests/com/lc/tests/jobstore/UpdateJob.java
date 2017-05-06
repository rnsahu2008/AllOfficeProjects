package com.lc.tests.jobstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.JobStoreCoreComponents;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

import static com.lc.constants.Constants_JobsStore.*;

public class UpdateJob extends JobStoreCoreComponents{
	List<String> jobIds = new ArrayList<String>();
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@Test
	public void updateJobById(){
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
			dataInPost.put("ApplyUrl", "http://www.jobtap.com/job?=" + RandomStringUtils.randomNumeric(4));
			dataInPost.put("StringHash", "");			
			
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			checkSuccessCode(response.code);
			String id = response.message.replace("\"", "");
			
			url = BASE_URL_JOBS + "/v1/jobs/" + id;
			response = HttpService.get(url, new HashMap<String, String>());
			JSONObject responseJson = new JSONObject(response.message); 
			checkSuccessCode(response.code);			
			
			String puid = responseJson.getString("PUID");
			JSONObject json = new JSONObject();
			json.put("PUID", puid);
			json.put("Title", "Title Edited");
			json.put("Description", "Description Edited");
			json.put("CountryCD", "US");
			json.put("PostalCode", "PostalCode Edited");
			json.put("EmploymentTypeCD", "EmploymentTypeCD Edited");
			json.put("SourceCD", "JTR");
			json.put("City", "City Edited");
			json.put("State", "State Edited");
			json.put("Company", "Company Edited");
			json.put("PostedOn", "2015-04-06 00:00:00");
			json.put("ApplyUrl", "www.jobtap.com/job?=27Edited");
			
			response = HttpService.put(url, new HashMap<String, String>(), json.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals(id, respJson , "id");
			assertEquals("Success", respJson , "status");
			
			response = HttpService.get(url, null);
			respJson = new JSONObject(response.message);
			assertEquals(puid, respJson , "PUID");
			assertTrue(respJson.getString("Title").contains("Edited"), "Title not updated::::Response is-->> " + response.message);
			assertTrue(respJson.getString("Description").contains("Edited"), "Description not updated::::Response is-->> " + response.message);
			assertTrue(respJson.getString("PostalCode").contains("Edited"), "PostalCode not updated::::Response is-->> " + response.message);
			assertTrue(respJson.getString("EmploymentTypeCD").contains("Edited"), "EmploymentTypeCD not updated::::Response is-->> " + response.message);
			assertTrue(respJson.getString("State").contains("Edited"), "State not updated::::Response is-->> " + response.message);
			assertTrue(respJson.getString("Company").contains("Edited"), "Company not updated::::Response is-->> " + response.message);
			assertTrue(respJson.getString("ApplyUrl").contains("Edited"), "ApplyUrl not updated::::Response is-->> " + response.message);
			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
}
