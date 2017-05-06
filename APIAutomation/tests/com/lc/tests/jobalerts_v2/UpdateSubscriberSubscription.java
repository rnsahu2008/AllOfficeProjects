package com.lc.tests.jobalerts_v2;

import static com.lc.constants.Constants_JobAlerts.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.JobAlertsCoreComponents_V2;
import com.lc.dataprovider.JobAlerts_DP;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.JSONValueFinder;

public class UpdateSubscriberSubscription extends JobAlertsCoreComponents_V2{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void editByIdSourceCD(String sourceCD){
		try {
			/*
			 * This is setup part
			 * POST /v2/subscribers Adds the subscribers			 
			 */
			JSONArray dataToPost = randomSubscription(sourceCD , null , null);
			String url = BASE_URL + SUBSCRIBER_URL_V2;
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			System.out.println(response.message);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String subscriberId = respJson.getString("subscriber_id");
			
			String alreadyAssignedEmail = respJson.getString("email");
			String alreadyAssignedSourceUid = respJson.getString("sourceuid");
			JSONObject dataToPut = randomSubscription(sourceCD, subscriberId , alreadyAssignedEmail , alreadyAssignedSourceUid);
			
			/*
			 * PUT /v2/subscribers/{id}/sources/{sourcecd} (Updates the subscriber)
			 */
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + subscriberId + "/sources/" + sourceCD ;
			response = HttpService.put(url, headers, dataToPut.toString());
			checkSuccessCode(response.code);
			assertEquals("true", response.message, "Not getting TRUE value in response");
			
			//check whether edit has been made successfully
			url = BASE_URL + SUBSCRIBER_URL_V2  + "/" + respJson.getString("subscriber_id"); 
			response = HttpService.get(url, headers);
			JSONObject resp = new JSONObject(response.message);
			assertEquals(alreadyAssignedEmail, resp.getString("email"), "email not same");
			assertEquals(alreadyAssignedSourceUid, resp.getString("sourceuid"), "sourceuid not same");
			assertEquals("INAC", resp.getString("status_cd"), "status_cd not same");
			assertEquals(sourceCD, resp.getString("source_cd"), "source_cd not same");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void editByIdSid(String sourceCD){
		try {
			/*
			 * This is setup part
			 * POST /v2/subscribers Adds the subscribers			 
			 */
			JSONArray dataToPost = randomSubscription(sourceCD , null , null);
			String url = BASE_URL + SUBSCRIBER_URL_V2;
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			System.out.println(response.message);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String id = respJson.getString("subscriber_id");
			String sid = JSONValueFinder.getJsonValue(response.message, "subscription_id");
			String loc = "newyork" + RandomStringUtils.randomAlphanumeric(4);
			String jqry = "manager" + RandomStringUtils.randomAlphanumeric(4);
			
			String alreadyAssignedEmail = respJson.getString("email");
			String alreadyAssignedSourceUid = respJson.getString("sourceuid");
			
			/*
			 * PUT /v2/subscribers/{id}/subscriptions/{sid} Updates the subscription
			 */
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + id + "/subscriptions" + "/" + sid;
			JSONObject dataToPut = newSubscriptionWithSubscriptionID(sid , loc , jqry);
			response = HttpService.put(url, headers, dataToPut.toString());
			checkSuccessCode(response.code);
			assertEquals("true", response.message, "Not getting TRUE value in response");
			
			//check whether edit has been made successfully
			url = BASE_URL + SUBSCRIBER_URL_V2  + "/" + respJson.getString("subscriber_id"); 
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			JSONObject resp = new JSONObject(response.message);
			assertEquals(alreadyAssignedEmail, resp.getString("email"), "email not same");
			assertEquals(alreadyAssignedSourceUid, resp.getString("sourceuid"), "sourceuid not same");
			assertEquals("ACTV", resp.getString("status_cd"), "status_cd not same");
			assertEquals(sourceCD, resp.getString("source_cd"), "source_cd not same");
			JSONArray subsArr = resp.getJSONArray("subscriptions");
			assertTrue(1, subsArr.length(), "subscription list size not correct");	
			JSONArray subscription_options = subsArr.getJSONObject(0).getJSONArray("subscription_options");
			assertTrue(subscription_options.toString().contains(loc), "location not updated");
			assertTrue(subscription_options.toString().contains(jqry), "JQRY not updated");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

}
