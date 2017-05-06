package com.lc.tests.jobalerts_v1;

import static com.lc.constants.Constants_JobAlerts.*;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;
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
import com.lc.utils.JSONValueFinder;

public class UpdateSubscriberSubscription extends JobAlertsCoreComponents_V1{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void editByIdEmailSourceCD(String sourceCD){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			
			/*
			 * 	PUT /v1/subscribers/{email}/{id}/{sourceCD} Updates the subscriber email by id and source
			 */
			String id = respJson.getString("subscriber_id");
			String newEmail =  "api"+UUID.randomUUID().toString()+ "@gmail.com";
			
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + URLEncoder.encode(newEmail, "UTF-8") + "/" + id + "/" + sourceCD;
			response = HttpService.put(url, null, null);
			checkSuccessCode(response.code);
			assertEquals("true", response.message, "Not getting TRUE value in response");
			
			/*
			 * 	Verify the update operation by
			 * 	GET /v1/subscribers/{email}/source/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + "/v1/subscribers/" + URLEncoder.encode(newEmail, "UTF-8") + "/source/" + sourceCD;
			response = HttpService.get(url, null);
			JSONObject resp = new JSONObject(response.message);
			assertEquals(id, resp.getString("subscriber_id"), "subscriber_id is not same");
			assertEquals(newEmail, resp.getString("email"), "email is not same");				
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void editBySubscriberDetailsEmailSourceCD(String sourceCD){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			JSONObject subscription = (JSONObject) respJson.getJSONArray("subscriptions").get(0);
			String subscriptionId = subscription.getString("subscription_id");
			
			String alreadyAssignedEmail = respJson.getString("email");
			String alreadyAssignedSourceUid = respJson.getString("sourceuid");
			String locEdited = "newyork" + RandomStringUtils.randomAlphanumeric(4);
			String jqrydited = "manager" + RandomStringUtils.randomAlphanumeric(4);
			String freqEdited = "Weekly";
			JSONArray subscriptionData = subscriptionWithSubscriptionIdEmailUidAndOptions(sourceCD, subscriptionId , alreadyAssignedEmail , alreadyAssignedSourceUid , locEdited , jqrydited);	
			String dataToPut = subscriptionData.getJSONObject(0).toString().replace("Daily", freqEdited);
			
			/*
			 * PUT /v1/subscribers/{email}/source/{sourceCD} Updates the subscriber by email and source	
			 */
			String url = BASE_URL + "/v1/subscribers/" + URLEncoder.encode(alreadyAssignedEmail, "UTF-8") + "/source/" + sourceCD ;
			response = HttpService.put(url, new HashMap<String, String>(), dataToPut);
			checkSuccessCode(response.code);
			assertEquals("true", response.message, "Not getting TRUE value in response");
			
			/*
			 * 	Verify the update operation by
			 * 	GET /v1/subscribers/{email}/source/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + URLEncoder.encode(alreadyAssignedEmail, "UTF-8") + "/source/" + sourceCD;
			response = HttpService.get(url, new HashMap<String, String>());
			JSONObject resp = new JSONObject(response.message);
			assertEquals(alreadyAssignedEmail, resp.getString("email"), "email not same");
			assertEquals(alreadyAssignedSourceUid, resp.getString("sourceuid"), "sourceuid not same");
			assertEquals("ACTV", resp.getString("status_cd"), "status_cd not same");
			assertEquals(sourceCD, resp.getString("source_cd"), "source_cd not same");
			
			JSONArray subsArr = resp.getJSONArray("subscriptions");
			assertTrue(1, subsArr.length(), "subscription list size not correct");			
			JSONArray newlyAddedSubscription = subsArr.getJSONObject(0).getJSONArray("subscription_options");
			assertTrue(newlyAddedSubscription.toString().contains(locEdited), "Location not edited");
			assertTrue(newlyAddedSubscription.toString().contains(jqrydited), "JQRY not edited");
			assertTrue(newlyAddedSubscription.toString().contains(freqEdited), "FREQ not edited");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void editByIdSourceCD(String sourceCD){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String id = respJson.getString("subscriber_id");
			JSONObject subscription = (JSONObject) respJson.getJSONArray("subscriptions").get(0);
			String subscriptionId = subscription.getString("subscription_id");
			
			String alreadyAssignedEmail = respJson.getString("email");
			String alreadyAssignedSourceUid = respJson.getString("sourceuid");
			String locEdited = "new york";
			String jqrydited = "Teacher";
			JSONArray dataToPut = subscriptionWithSubscriptionIdEmailUidAndOptions(sourceCD, subscriptionId , alreadyAssignedEmail , alreadyAssignedSourceUid , locEdited , jqrydited);
			
			/*
			 * 	PUT /v1/subscribers/{id}/{sourceCD} Updates the subscriber
			 */
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD ;
			response = HttpService.put(url, null, dataToPut.getJSONObject(0).toString());
			checkSuccessCode(response.code);
			assertEquals("true", response.message, "Not getting TRUE value in response");
			
			/*
			 * 	Verify the update operation by
			 * 	GET /v1/subscribers/{email}/source/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + "/v1/subscribers/" + URLEncoder.encode(alreadyAssignedEmail, "UTF-8") + "/source/" + sourceCD;
			response = HttpService.get(url, null);
			JSONObject resp = new JSONObject(response.message);
			assertEquals(alreadyAssignedEmail, resp.getString("email"), "email not same");
			assertEquals(alreadyAssignedSourceUid, resp.getString("sourceuid"), "sourceuid not same");
			assertEquals("ACTV", resp.getString("status_cd"), "status_cd not same");
			assertEquals(sourceCD, resp.getString("source_cd"), "source_cd not same");
			
			JSONArray subsArr = resp.getJSONArray("subscriptions");
			assertTrue(1, subsArr.length(), "subscription list size not correct");			
			
			JSONArray newlyAddedSubscription = subsArr.getJSONObject(0).getJSONArray("subscription_options");
			assertTrue(newlyAddedSubscription.toString().contains(locEdited), "Location not edited");
			assertTrue(newlyAddedSubscription.toString().contains(jqrydited), "JQRY not edited");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void editByIdSID(String sourceCD){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String id = respJson.getString("subscriber_id");
			String sid = JSONValueFinder.getJsonValue(response.message, "subscription_id");
			String loc = "new york";
			String jqry = "QA Manager";
			
			/*
			 * 	PUT /v1/subscribers/{id}/subscriptions/{sid} Updates the subscription
			 */
			String putUrl = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/subscriptions" + "/" + sid;
			JSONObject dataToPut = newSubscriptionWithSubscriptionID(sid , loc , jqry);
			response = HttpService.put(putUrl, new HashMap<String, String>(), dataToPut.toString());
			checkSuccessCode(response.code);
			assertEquals("true", response.message, "Not getting TRUE value in response");
			
			/*
			 * 	Verify the update operation by
			 * 	GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source.
			 */
			String url = BASE_URL + "/v1/subscribers/" + id + "/" + sourceCD ; 
			response = HttpService.get(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			JSONArray subsArr = respJson.getJSONArray("subscriptions");
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
