package com.lc.tests.jobalerts_v2;

import java.net.URLEncoder;

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

import static com.lc.constants.Constants_JobAlerts.*;

public class AddSubscriber extends JobAlertsCoreComponents_V2{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void createSubscriberByPOSTAndCheckByGET(String sourceCD){
		try {
			/*
			 * create new subscriber by POST (Adds the subscribers)
			 */
			String loc = "sanfrancisco";
			String jqry = "manager";
			JSONArray dataToPost = randomSubscription(sourceCD , loc , jqry);			
			String url = BASE_URL + SUBSCRIBER_URL_V2;
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			JSONObject subscriptionsRespJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			String subscription_id = subscriptionsRespJson.getString("subscription_id");
			String subscriberId = respJson.getString("subscriber_id");
			System.out.println(respJson.getString("subscriber_id"));
			
			JSONObject inputJson = dataToPost.getJSONObject(0);
			JSONObject inputSubscriptionsData = inputJson.getJSONArray("Subscriptions").getJSONObject(0);
			
			assertNotNull(respJson.getString("subscriber_id"), "subscriber_id not generated");
			assertEquals(inputJson.getString("IP_Address"), respJson.getString("ip_address"), "ip_address mismatch in request and response");
			assertEquals(respJson.getString("status_cd"), "ACTV", "status_cd mismatch in request and response");
			assertEquals(inputJson.getString("Source_CD"), respJson.getString("source_cd"), "source_cd mismatch in request and response");
			assertEquals(inputJson.getString("SourceUID"), respJson.getString("sourceuid"), "sourceuid mismatch in request and response");
			//assertEquals(inputSubscriptionsData.getString("Status_CD"), subscriptionsRespJson.getString("status_cd"), "status_cd mismatch in request and response");
			assertEquals(inputSubscriptionsData.getString("Subscription_Type_CD"), subscriptionsRespJson.getString("subscription_type_cd"), "subscription_type_cd mismatch in request and response");
			assertNotNull(subscriptionsRespJson.getString("subscription_id"), "subscription_id is null");
			assertNotNull(subscriptionsRespJson.getString("subscribed_on"), "subscribed_on is null");
			assertTrue(subscriptionsRespJson.getJSONArray("subscription_options").toString().contains(jqry), "Location Incorrect in response");
			assertTrue(subscriptionsRespJson.getJSONArray("subscription_options").toString().contains(loc), "Location Incorrect in response");
			assertEquals(inputSubscriptionsData.getString("Referrer"), subscriptionsRespJson.getString("Referrer"), "Not getting correct Referrer value");
			
			/*
			 * check newly created subscriber by GET (Gets the subscriber by email and source)
			 */
			JSONObject data = new JSONObject();
			data.put("EmailAddress", inputJson.getString("Email"));
			data.put("Source", sourceCD);
			JSONObject param = new JSONObject();
			param.put("subscriberemailsource", data);
			String queryParam = URLEncoder.encode(data.toString(), "UTF-8");
			url = BASE_URL + SUBSCRIBER_URL_V2 + "?subscriberemailsource=" + queryParam; 
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			subscriptionsRespJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			assertNotNull(respJson.getString("subscriber_id"), "subscriber_id not generated");
			assertEquals(inputJson.getString("IP_Address"), respJson.getString("ip_address"), "ip_address mismatch in request and response");
			assertEquals(respJson.getString("status_cd"), "ACTV", "status_cd mismatch in request and response");
			assertEquals(inputJson.getString("Source_CD"), respJson.getString("source_cd"), "source_cd mismatch in request and response");
			assertEquals(inputJson.getString("SourceUID"), respJson.getString("sourceuid"), "sourceuid mismatch in request and response");
			//assertEquals(inputSubscriptionsData.getString("Status_CD"), subscriptionsRespJson.getString("status_cd"), "status_cd mismatch in request and response");
			assertEquals(inputSubscriptionsData.getString("Subscription_Type_CD"), subscriptionsRespJson.getString("subscription_type_cd"), "subscription_type_cd mismatch in request and response");
			assertNotNull(subscriptionsRespJson.getString("subscription_id"), "subscription_id is null");
			assertNotNull(subscriptionsRespJson.getString("subscribed_on"), "subscribed_on is null");
			assertTrue(subscriptionsRespJson.getJSONArray("subscription_options").toString().contains(jqry), "Location Incorrect in response");
			assertTrue(subscriptionsRespJson.getJSONArray("subscription_options").toString().contains(loc), "Location Incorrect in response");
			assertEquals(inputSubscriptionsData.getString("Referrer"), subscriptionsRespJson.getString("Referrer"), "Not getting correct Referrer value");
			/*
			 *check newly created subscription by GET (Gets the subscriber by identifier and source) 
			 */
			
			url = BASE_URL + SUBSCRIBER_URL_V2  + "/" + respJson.getString("subscriber_id"); 
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			subscriptionsRespJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			assertNotNull(respJson.getString("subscriber_id"), "subscriber_id not generated");
			assertEquals(inputJson.getString("IP_Address"), respJson.getString("ip_address"), "ip_address mismatch in request and response");
			assertEquals(respJson.getString("status_cd"), "ACTV", "status_cd mismatch in request and response");
			assertEquals(inputJson.getString("Source_CD"), respJson.getString("source_cd"), "source_cd mismatch in request and response");
			assertEquals(inputJson.getString("SourceUID"), respJson.getString("sourceuid"), "sourceuid mismatch in request and response");
			//assertEquals(inputSubscriptionsData.getString("Status_CD"), subscriptionsRespJson.getString("status_cd"), "status_cd mismatch in request and response");
			assertEquals(inputSubscriptionsData.getString("Subscription_Type_CD"), subscriptionsRespJson.getString("subscription_type_cd"), "subscription_type_cd mismatch in request and response");
			assertNotNull(subscriptionsRespJson.getString("subscription_id"), "subscription_id is null");
			assertNotNull(subscriptionsRespJson.getString("subscribed_on"), "subscribed_on is null");
			assertTrue(subscriptionsRespJson.getJSONArray("subscription_options").toString().contains(jqry), "Location Incorrect in response");
			assertTrue(subscriptionsRespJson.getJSONArray("subscription_options").toString().contains(loc), "Location Incorrect in response");
			assertEquals(inputSubscriptionsData.getString("Referrer"), subscriptionsRespJson.getString("Referrer"), "Not getting correct Referrer value");
			/*
			 *check newly created subscription by GET (Gets the subscription by SubscriptionId) 
			 */
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + subscriberId + "/subscriptions/" + subscription_id;
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			assertEquals(subscription_id, respJson.getString("subscription_id"), "subscription_id is not correct in response");
			//assertNotNull(respJson.getString("subscribed_on"), "subscribed_on is null");
			assertEquals("ACTV", respJson.getString("status_cd"), "status_cd is not correct");
			assertEquals("JBALRT", respJson.getString("subscription_type_cd"), "subscription_type_cd is not correct");
			assertEquals(inputSubscriptionsData.getString("Referrer"), respJson.getString("Referrer"), "Not getting correct Referrer value");
			assertTrue(respJson.getJSONArray("subscription_options").toString().contains(loc), "Location not reflected in response");
			assertTrue(respJson.getJSONArray("subscription_options").toString().contains(jqry), "JQRY not reflected in response");			
		} catch (Exception e) {
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}
	
}