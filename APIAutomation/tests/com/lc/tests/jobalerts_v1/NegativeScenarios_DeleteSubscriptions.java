package com.lc.tests.jobalerts_v1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.JobAlertsCoreComponents_V1;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.JSONValueFinder;

import static com.lc.constants.Constants_JobAlerts.*;

public class NegativeScenarios_DeleteSubscriptions extends JobAlertsCoreComponents_V1{

	String sourceCD = "LCAUS";
	String invalidSourceCD = "LCAUK";
	
	String invalidId = "4be6c086-64cb-42e3-9f66-26c1699daf5bdfre";
	String invalidSid = "4833666";
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test 
	public void v1_deleteSubscriptionByIdAndSID(){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String id = respJson.getString("subscriber_id");
			String sid = JSONValueFinder.getJsonValue(response.message, "subscription_id");
			
			/*
			 * 	Delete with invalid sid
			 * 	DELETE /v1/subscribers/{id}/subscriptions/{sid}/cancel Deactivates the subscriptionid for particular subscriber unique id
			 */
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + "subscriptions/" + invalidSid + "/cancel";
			response = HttpService.delete(url, null);
			checkSuccessCode(response.code);
			assertEquals("false", response.message, "Not getting FALSE value in response");
			
			/*
			 * 	Delete with invalid Id
			 * 	DELETE /v1/subscribers/{id}/subscriptions/{sid}/cancel Deactivates the subscriptionid for particular subscriber unique id
			 */
	
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + invalidId + "/" + "subscriptions/" + sid + "/cancel";
			response = HttpService.delete(url, null);
			checkSuccessCode(response.code);
			assertEquals("false", response.message, "Not getting FALSE value in response");
			
			/*
			 * 	Verify the delete operation by
			 * 	GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			JSONObject subsJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			assertEquals("ACTV", subsJson.getString("status_cd"), "status_cd wrongly changed to INAC");			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	
	@Test
	public void v1_deleteSubscriptionByIdSIDAndType(){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String id = respJson.getString("subscriber_id");
			String sid = JSONValueFinder.getJsonValue(response.message, "subscription_id");
			
			/*
			 * 	DELETE /v1/subscribers/{id}/subscriptions/{sid}/cancel/{type} Deactivates the subscriptionid for particular subscriber unique id
			 */
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + "subscriptions/" + invalidSid + "/cancel" + "/" + "EMAIL";
			response = HttpService.delete(url, null);
			checkSuccessCode(response.code);
			assertEquals("false", response.message, "Not getting FALSE value in response");
			
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + invalidId + "/" + "subscriptions/" + sid + "/cancel" + "/" + "EMAIL";
			response = HttpService.delete(url, null);
			checkSuccessCode(response.code);
			assertEquals("false", response.message, "Not getting FALSE value in response");
			
			/*
			 * 	Verify the delete operation by
			 * 	GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			JSONObject subsJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			assertEquals("ACTV", subsJson.getString("status_cd"), "status_cd wrongly changed to INAC");			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test 
	public void v1_deleteSubscriptionBySIDSourceCDAndType(){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String id = respJson.getString("subscriber_id");
			String sid = JSONValueFinder.getJsonValue(response.message, "subscription_id");
			
			/*
			 * 	DELETE /v1/subscribers/{id}/{sourceCD}/subscriptions/{sid}/cancel/{type} Deactivates the subscriptionid for particular subscriber unique id.
			 */
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD + "/" + "subscriptions/" + invalidSid + "/cancel" + "/" + "EMAIL";
			response = HttpService.delete(url, null);
			checkSuccessCode(response.code);
			assertEquals("false", response.message, "Not getting FALSE value in response");
			
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + invalidId + "/" + sourceCD + "/" + "subscriptions/" + sid + "/cancel" + "/" + "EMAIL";
			response = HttpService.delete(url, null);
			checkSuccessCode(response.code);
			assertEquals("false", response.message, "Not getting FALSE value in response");
			/*
			 * 	Verify the delete operation by
			 * 	GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			JSONObject subsJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			assertEquals("ACTV", subsJson.getString("status_cd"), "status_cd wrongly changed to INAC");			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
}
