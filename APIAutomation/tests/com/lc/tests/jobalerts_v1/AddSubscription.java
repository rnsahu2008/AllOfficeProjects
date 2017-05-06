package com.lc.tests.jobalerts_v1;

import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.JobAlertsCoreComponents_V1;
import com.lc.dataprovider.JobAlerts_DP;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

import static com.lc.constants.Constants_JobAlerts.*;

public class AddSubscription extends JobAlertsCoreComponents_V1{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void addNewSubscriptionAndGetDetails(String sourceCD){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			JSONArray dataToPost = randomSubscription(sourceCD , "new york" , "manager");
			JSONObject inputJson = dataToPost.getJSONObject(0);
			JSONObject subscriptionsData = inputJson.getJSONArray("Subscriptions").getJSONObject(0);
			
			String url = BASE_URL + SUBSCRIBER_URL_V1;
			ResponseBean response = HttpService.post(url, null, dataToPost.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String id = respJson.getString("subscriber_id");
			JSONObject subscriptionsRespJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			String subscriptionId = subscriptionsRespJson.getString("subscription_id");
			System.out.println(respJson.getString("subscriber_id"));
			
			assertNotNull(respJson.getString("subscriber_id"), "subscriber_id not generated");
			assertEquals(inputJson.getString("IP_Address"), respJson.getString("ip_address"), "ip_address mismatch in request and response");
			assertEquals(inputJson.getString("Status_CD"), respJson.getString("status_cd"), "status_cd mismatch in request and response");
			assertEquals(inputJson.getString("Source_CD"), respJson.getString("source_cd"), "source_cd mismatch in request and response");
			assertEquals(inputJson.getString("SourceUID"), respJson.getString("sourceuid"), "sourceuid mismatch in request and response");
			assertEquals(subscriptionsData.getString("Status_CD"), subscriptionsRespJson.getString("status_cd"), "status_cd mismatch in request and response");
			assertEquals(subscriptionsData.getString("Subscription_Type_CD"), subscriptionsRespJson.getString("subscription_type_cd"), "subscription_type_cd mismatch in request and response");
		
			/*
			 *	POST /v1/subscribers/{id}/subscriptions Adds the subscription 
			 */ 
			
			String loc = "newyork" + RandomStringUtils.randomAlphanumeric(4);
			String jqry = "manager" + RandomStringUtils.randomAlphanumeric(4);
			JSONObject newSubscriptionJson = newSubscriptionWithSubscriptionID("0" , loc , jqry);
			String newUrl = BASE_URL + "/v1/subscribers/" + id + "/subscriptions";
			response = HttpService.post(newUrl, null, newSubscriptionJson.toString());
			checkSuccessCode(response.code);
			assertEquals("true", response.message, "Not getting TRUE value in response");
			
			/*
			 * GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD ; 
			response = HttpService.get(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			JSONArray subsArr = respJson.getJSONArray("subscriptions");
			String newSubscriptionId = subsArr.getJSONObject(1).getString("subscription_id");
			assertTrue(2, subsArr.length(), "subscription list size not correct");
			JSONArray newSubscriptionAdded = subsArr.getJSONObject(1).getJSONArray("subscription_options");
			assertTrue(newSubscriptionAdded.toString().contains(loc), "Location not added");
			assertTrue(newSubscriptionAdded.toString().contains(jqry), "JQRY not added");
			assertTrue(newSubscriptionAdded.toString().contains("Weekly"), "Frequency not added");
			assertTrue(!subscriptionId.equals(newSubscriptionId), "Getting same subscriber value in newly added subscription");
			//weekly, subscription_id not null and not same
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
}
