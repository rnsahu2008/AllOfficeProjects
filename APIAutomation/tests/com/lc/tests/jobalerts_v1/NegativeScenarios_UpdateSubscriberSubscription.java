package com.lc.tests.jobalerts_v1;

import static com.lc.constants.Constants_JobAlerts.*;

import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
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

public class NegativeScenarios_UpdateSubscriberSubscription extends JobAlertsCoreComponents_V1{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test
	public void editEmailWithOldValue_PutEmailIdSourceCD(){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			String sourceCD = "LCAUS";
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String oldEmail = respJson.getString("email");
			
			/*
			 * 	Try to edit subscriber with old email ID
			 * 	PUT /v1/subscribers/{email}/{id}/{sourceCD} Updates the subscriber email by id and source
			 */
			String id = respJson.getString("subscriber_id");
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + URLEncoder.encode(oldEmail, "UTF-8") + "/" + id + "/" + sourceCD;
			response = HttpService.put(url, null, null);
			assertResponseCode(response.code, 400);
			assertEquals("{\"Message\":\"Failed. Your request couldn't be handled. EmailAddress and SourceCode combination already exists.\"}", response.message, "Invalid response message");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	

	
	@Test
	public void editEmailWithInvalidSubscriberId_PutEmailIdSourceCD(){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			String sourceCD = "LCAUS";
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			String newEmailId =  "api_automation_" + RandomStringUtils.randomNumeric(8) + "@livecareer.com";
			
			/*
			 * 	Try to edit subscriber with old email ID
			 * 	PUT /v1/subscribers/{email}/{id}/{sourceCD} Updates the subscriber email by id and source
			 */
			String invalidId = "7824f171-6428-4a20-9428-eedb462dc6d32";
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + URLEncoder.encode(newEmailId, "UTF-8") + "/" + invalidId + "/" + sourceCD;
			response = HttpService.put(url, null, null);
			assertResponseCode(response.code, 400);
			assertEquals("{\"Message\":\"Failed. Your request couldn't be handled. id and sourceCD combination doesn't exist.\"}", response.message, "Invalid response message");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void editEmailWithInvalidSourceCD_PutEmailIdSourceCD(){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			String sourceCD = "LCAUS";
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String newEmailId =  "api_automation_" + RandomStringUtils.randomNumeric(8) + "@livecareer.com";
			
			/*
			 * 	Try to edit subscriber with invalid sourceCD
			 * 	PUT /v1/subscribers/{email}/{id}/{sourceCD} Updates the subscriber email by id and source
			 */
			String id = respJson.getString("subscriber_id");
			String invalidSourceCD = "LCAUK";
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + URLEncoder.encode(newEmailId, "UTF-8") + "/" + id + "/" + invalidSourceCD;
			response = HttpService.put(url, null, null);
			assertResponseCode(response.code, 400);
			assertEquals("{\"Message\":\"Failed. Your request couldn't be handled. id and sourceCD combination doesn't exist.\"}", response.message, "Invalid response message");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void editSubscriberDataWithInvalidEmail_PutEmailSourceCD(){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			String sourceCD = "LCAUS";
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			JSONObject subscription = (JSONObject) respJson.getJSONArray("subscriptions").get(0);
			String subscriptionId = subscription.getString("subscription_id");
			
			String alreadyAssignedEmail = respJson.getString("email");
			String alreadyAssignedSourceUid = respJson.getString("sourceuid");
			/*
			 * 	Try to edit subscriber with old email ID
			 * 	PUT /v1/subscribers/{email}/{id}/{sourceCD} Updates the subscriber email by id and source
			 */
			String locEdited = "newyork" + RandomStringUtils.randomAlphanumeric(4);
			String jqrydited = "manager" + RandomStringUtils.randomAlphanumeric(4);
			String freqEdited = "Weekly";
			JSONArray subscriptionData = subscriptionWithSubscriptionIdEmailUidAndOptions(sourceCD, subscriptionId , alreadyAssignedEmail , alreadyAssignedSourceUid , locEdited , jqrydited);	
			String dataToPut = subscriptionData.getJSONObject(0).toString().replace("Daily", freqEdited);
			
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + "wrongemail@livecareer.com" + "/source/" + sourceCD;
			response = HttpService.put(url, null, dataToPut);
			assertResponseCode(response.code, 400);
			assertEquals("{\"Message\":\"Failed. Your request couldn't be handled. Input EmailAddress does not match with Subscriber EmailAddress. Please provide a valid subscriber.\"}", response.message, "Invalid response message");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void editSubscriberDataWithInvalidSourceCD_PutEmailSourceCD(){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			String sourceCD = "LCAUS";
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			JSONObject subscription = (JSONObject) respJson.getJSONArray("subscriptions").get(0);
			String subscriptionId = subscription.getString("subscription_id");
			
			String alreadyAssignedEmail = respJson.getString("email");
			String alreadyAssignedSourceUid = respJson.getString("sourceuid");
			/*
			 * 	Try to edit subscriber with old email ID
			 * 	PUT /v1/subscribers/{email}/{id}/{sourceCD} Updates the subscriber email by id and source
			 */
			String locEdited = "newyork" + RandomStringUtils.randomAlphanumeric(4);
			String jqrydited = "manager" + RandomStringUtils.randomAlphanumeric(4);
			String freqEdited = "Weekly";
			JSONArray subscriptionData = subscriptionWithSubscriptionIdEmailUidAndOptions(sourceCD, subscriptionId , alreadyAssignedEmail , alreadyAssignedSourceUid , locEdited , jqrydited);	
			String dataToPut = subscriptionData.getJSONObject(0).toString().replace("Daily", freqEdited);
			
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + alreadyAssignedEmail + "/source/" + "LCAUK";
			response = HttpService.put(url, null, dataToPut);
			assertResponseCode(response.code, 400);
			assertEquals("{\"Message\":\"Failed. Your request couldn't be handled. Input SourceCD does not match with Subscriber SourceCD. Please provide a valid subscriber.\"}", response.message, "Invalid response message");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void editSubscriptionWithInvalidId_PutSourceCDId(){
		try {
			String sourceCD = "LCAUS";
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			JSONObject subscription = (JSONObject) respJson.getJSONArray("subscriptions").get(0);
			String subscriptionId = subscription.getString("subscription_id");
			
			String alreadyAssignedEmail = respJson.getString("email");
			String alreadyAssignedSourceUid = respJson.getString("sourceuid");
			String locEdited = "newyork" + RandomStringUtils.randomAlphanumeric(4);
			String jqrydited = "manager" + RandomStringUtils.randomAlphanumeric(4);
			JSONArray dataToPut = subscriptionWithSubscriptionIdEmailUidAndOptions(sourceCD, subscriptionId , alreadyAssignedEmail , alreadyAssignedSourceUid , locEdited , jqrydited);
			
			/*
			 * 	PUT /v1/subscribers/{id}/{sourceCD} Updates the subscriber
			 */
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + "123456" + "/" + sourceCD ;
			response = HttpService.put(url, null, dataToPut.getJSONObject(0).toString());
			assertResponseCode(response.code, 400);
			assertEquals("{\"Message\":\"Failed. Your request couldn't be handled. SourceUID provided in Subscriber does not match input SourceUID parameter. Please provide a valid subscriber.\"}", response.message, "Invalid response message");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void editSubscriptionWithInvalidSourceCD_PutSourceCDId(){
		try {
			String sourceCD = "LCAUS";
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String id = respJson.getString("subscriber_id");
			JSONObject subscription = (JSONObject) respJson.getJSONArray("subscriptions").get(0);
			String subscriptionId = subscription.getString("subscription_id");
			
			String alreadyAssignedEmail = respJson.getString("email");
			String alreadyAssignedSourceUid = respJson.getString("sourceuid");
			String locEdited = "newyork" + RandomStringUtils.randomAlphanumeric(4);
			String jqrydited = "manager" + RandomStringUtils.randomAlphanumeric(4);
			JSONArray dataToPut = subscriptionWithSubscriptionIdEmailUidAndOptions(sourceCD, subscriptionId , alreadyAssignedEmail , alreadyAssignedSourceUid , locEdited , jqrydited);
			
			/*
			 * 	PUT /v1/subscribers/{id}/{sourceCD} Updates the subscriber
			 */
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + "LCAUK" ;
			response = HttpService.put(url, null, dataToPut.getJSONObject(0).toString());
			assertResponseCode(response.code, 400);
			assertEquals("{\"Message\":\"Failed. Your request couldn't be handled. SourceCD provided in Subscriber does not match input SourceCD parameter. Please provide a valid subscriber.\"}", response.message, "Invalid response message");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test
	public void editSubscriptionWithInvalidIdAndSid_PutIdSid(){
		try {
			String sourceCD = "LCAUS";
			String invalidId = "4be6c086-64cb-42e3-9f66-26c1699daf5bre";
			String invalidSid = "12345";
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String id = respJson.getString("subscriber_id");
			String sid = JSONValueFinder.getJsonValue(response.message, "subscription_id");
			String loc = "new york";
			String jqry = "manager";
			
			/*
			 * 	PUT /v1/subscribers/{id}/subscriptions/{sid} Updates the subscription
			 */
			String putUrl = BASE_URL + SUBSCRIBER_URL_V1 + "/" + invalidId + "/subscriptions" + "/" + sid;
			JSONObject dataToPut = newSubscriptionWithSubscriptionID(sid , loc , jqry);
			response = HttpService.put(putUrl, new HashMap<String, String>(), dataToPut.toString());
			checkSuccessCode(response.code);
			assertEquals("false", response.message, "Getting wrong value 'true'in response");
			
			putUrl = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/subscriptions" + "/" + invalidSid;
			dataToPut = newSubscriptionWithSubscriptionID(sid , loc , jqry);
			response = HttpService.put(putUrl, new HashMap<String, String>(), dataToPut.toString());
			checkSuccessCode(response.code);
			assertEquals("false", response.message, "Getting wrong value 'true'in response");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
}
