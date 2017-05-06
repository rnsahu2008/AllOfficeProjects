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
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.JSONValueFinder;

public class NegativeScenarios_UpdateSubscriberSubscription extends JobAlertsCoreComponents_V2{
	String sourceCD = "LCAUS";
	String invalidSourceCD = "LCAUK";
	String invalidId = "4be6c086-64cb-42e3-9f66-26c1699daf5bkjkdh";
	String invalidSid = "99999";
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@Test
	public void editByIdSourceCD(){
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
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + subscriberId + "/sources/" + invalidSourceCD ;
			response = HttpService.put(url, headers, dataToPut.toString());
			assertResponseCode(response.code, 400);
			assertEquals("{\"Message\":\"Failed. Your request couldn't be handled. SourceCD provided in Subscriber does not match input SourceCD parameter. Please provide a valid subscriber.\"}", response.message, "Not getting valid error message");
			
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + invalidId + "/sources/" + sourceCD ;
			response = HttpService.put(url, headers, dataToPut.toString());
			assertResponseCode(response.code, 400);
			assertEquals("{\"Message\":\"Failed. Your request couldn't be handled. SourceUID provided in Subscriber does not match input SourceUID parameter. Please provide a valid subscriber.\"}", response.message, "Not getting valid error message");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test 
	public void editByIdSid(){
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
			
			/*
			 * PUT /v2/subscribers/{id}/subscriptions/{sid} Updates the subscription
			 */
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + id + "/subscriptions" + "/" + invalidSid;
			JSONObject dataToPut = newSubscriptionWithSubscriptionID(sid , loc , jqry);
			response = HttpService.put(url, headers, dataToPut.toString());
			assertResponseCode(response.code, 400);
			
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + invalidId + "/subscriptions" + "/" + sid;
			 dataToPut = newSubscriptionWithSubscriptionID(sid , loc , jqry);
			response = HttpService.put(url, headers, dataToPut.toString());
			assertResponseCode(response.code, 400);
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

}
