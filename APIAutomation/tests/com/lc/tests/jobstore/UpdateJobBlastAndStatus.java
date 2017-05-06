package com.lc.tests.jobstore;

import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.JobStoreCoreComponents;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

import static com.lc.constants.Constants_JobsStore.*;

public class UpdateJobBlastAndStatus extends JobStoreCoreComponents{

	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test
	public void checkJobBlast(){
		try {
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("PUID", RandomStringUtils.randomNumeric(8));
			dataToPost.put("Title", RandomStringUtils.randomAlphanumeric(8));
			dataToPost.put("Description", RandomStringUtils.randomAlphanumeric(8));
			dataToPost.put("City", RandomStringUtils.randomAlphabetic(8));
			dataToPost.put("State", RandomStringUtils.randomAlphabetic(8));
			dataToPost.put("PostalCode", RandomStringUtils.randomAlphanumeric(6));
			dataToPost.put("CountryCD", "US");
			dataToPost.put("StatusCD", "ACTV");
			dataToPost.put("SourceCD", "JTR");
			dataToPost.put("Company", RandomStringUtils.randomAlphabetic(8));
			dataToPost.put("PostedOn", "2015-04-06 00:00:00");
			dataToPost.put("EmploymentTypeCD", "1234test1234");
			dataToPost.put("ApplyUrl", "http://www.jobtap.com/job?=" + RandomStringUtils.randomNumeric(4));
			dataToPost.put("StringHash", "");			
			
			String jobPostUrl = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean postResponse = HttpService.post(jobPostUrl, new HashMap<String, String>() , dataToPost.toString());
			checkSuccessCode(postResponse.code);
			assertNotNull(postResponse.message, "Getting NULL in response");
			
			String blastUrl = BASE_URL_JOBS + "/v1/jobs/" + postResponse.message.replace("\"", "") + "/blast";
			JSONObject dataToPatch  = new JSONObject();
			String newUrl = dataToPost.getString("ApplyUrl") + RandomStringUtils.randomNumeric(8);
			dataToPatch.put("Days", 30);
			dataToPatch.put("ApplyUrl", newUrl);
			
			ResponseBean patchResp = HttpService.patch(blastUrl, new HashMap<String, String>() , dataToPatch.toString());
			JSONObject patchRespJson = new JSONObject(patchResp.message);
			checkSuccessCode(patchResp.code);
			assertNotNull(patchResp.message, "Getting NULL in response");
			
			assertEquals(postResponse.message.replace("\"", ""), patchRespJson , "id");
			assertEquals("Success", patchRespJson ,  "status");
			
			String getUrl = BASE_URL_JOBS + "/v1/jobs/" + patchRespJson.getString("id");
			ResponseBean getResponse = HttpService.get(getUrl, new HashMap<String, String>());
			JSONObject getResponseJson = new JSONObject(getResponse.message);
			checkSuccessCode(getResponse.code);
			assertNotNull(getResponse.message, "Getting NULL in response");
			assertEquals("true", getResponseJson , "IsHighlighted");
			assertEquals(newUrl, getResponseJson , "ApplyUrl");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void patchStatus(){
		try {
			ResponseBean jobPostResponse = createRandomJob("ACTV", "JTR");
			checkSuccessCode(jobPostResponse.code);
			String jobId = jobPostResponse.message.replace("\"", "");
			JSONObject patchStatusJson = new JSONObject();
			patchStatusJson.put("JobSource", "JTR");
			patchStatusJson.put("JobStatus", "INAC");
			patchStatusJson.put("JobIds", new JSONArray().put(0, jobId));
			
			String statusPatchUrl = BASE_URL_JOBS + "/v1/jobs/status";
			
			ResponseBean patchResponse = HttpService.patch(statusPatchUrl, new HashMap<String, String>(), patchStatusJson.toString());
			checkSuccessCode(patchResponse.code);
			
			ResponseBean getResponse = HttpService.get(BASE_URL_JOBS + "/v1/jobs/" + jobId, new HashMap<String, String>());
			JSONObject getResponseJson = new JSONObject(getResponse.message);
			checkSuccessCode(getResponse.code);
			assertEquals("INAC", getResponseJson , "StatusCD");
			
			patchStatusJson = new JSONObject();
			patchStatusJson.put("JobSource", "JTR");
			patchStatusJson.put("JobStatus", "ACTV");
			patchStatusJson.put("JobIds", new JSONArray().put(0, jobId));
			patchResponse = HttpService.patch(statusPatchUrl, new HashMap<String, String>(), patchStatusJson.toString());
			checkSuccessCode(patchResponse.code);
			
			getResponse = HttpService.get(BASE_URL_JOBS + "/v1/jobs/" + jobId, new HashMap<String, String>());
			getResponseJson = new JSONObject(getResponse.message);
			checkSuccessCode(getResponse.code);
			assertEquals("ACTV", getResponseJson , "StatusCD");
			
			//Check BULK update
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	
}
