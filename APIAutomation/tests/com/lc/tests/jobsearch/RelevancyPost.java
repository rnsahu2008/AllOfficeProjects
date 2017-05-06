package com.lc.tests.jobsearch;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;

import static com.lc.constants.Constants_JobSearch.*;

import com.lc.common.Common;
import com.lc.components.JobSearchCoreComponents;
import com.lc.dataprovider.JobSearch_DP;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

public class RelevancyPost extends JobSearchCoreComponents{
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test(dataProviderClass = JobSearch_DP.class , dataProvider = "getRelevancyScore")
	public void postRelevancyWithIdAndScore(String score){
		try {
			String getUrl = BASE_URL + JOB_SEARCH + "?ip=138.91.242.92&useragent=firefox&q=qa%20manager";
			String source_appCD = "";
			ResponseBean response = HttpService.get(getUrl, null);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			
			String searchUID = respJson.getString("SearchUID");
			String jobId = respJson.getJSONArray("Jobs").getJSONObject(0).getString("ID");
			String postUrl = BASE_URL + JOB_SEARCH + "/" + jobId + "/relevancy/" + score;
			JSONObject json = createJobLogRequest(searchUID); 			
			response = HttpService.post(postUrl, null, json.toString());
			/*if(BASE_URL.contains("http://sandbox")){
				source_appCD = "JPSWS";
			}else{
				source_appCD = "JPSWT";
			}
			String eventUrl = "http://test-livecareer-eventstore.cloudapp.net:80/v1/Events?source_app_cd="+ source_appCD +"&event_type=JOB&source_app_uid=" + searchUID;
			response = HttpService.get(eventUrl, null);
			respJson = new JSONArray(response.message).getJSONObject(0);
			assertEquals(source_appCD, respJson, "SourceAppCD");
			assertEquals(searchUID, respJson, "SourceAppUID");
			assertEquals("JOB", respJson, "EventType");
			assertEquals("Relevancy", respJson, "EventSubType");
			assertEquals("true", respJson, "LogToSegment");
			assertNotNull(respJson, "EventTimestamp");*/
		} catch (Exception e) {
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}

}
