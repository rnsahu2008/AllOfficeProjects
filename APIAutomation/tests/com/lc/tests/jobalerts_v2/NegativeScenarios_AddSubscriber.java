package com.lc.tests.jobalerts_v2;

import java.net.Inet4Address;

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

import static com.lc.constants.Constants_JobAlerts.*;

public class NegativeScenarios_AddSubscriber extends JobAlertsCoreComponents_V2{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@Test 
	public void createSubscriberWithInvalidSourceCD(){
		try {
			/*
			 * create new subscriber by POST (Adds the subscribers)
			 */
			String invalidSourceCD = "MPPR";
			String loc = "sanfrancisco" + RandomStringUtils.randomAlphabetic(4);
			String jqry = "manager" + RandomStringUtils.randomAlphabetic(4);
			JSONArray dataToPost = randomSubscription(invalidSourceCD , loc , jqry);			
			String url = BASE_URL + SUBSCRIBER_URL_V2;
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 400);
					
		} catch (Exception e) {
			fail("");
		}
		s_assert.assertAll();
	}
	
	@Test
	public void createSubscriberWithRestrictedEmailDomain(){
		try {
			JSONArray dataToPost = new JSONArray();
			JSONObject parentJson = new JSONObject();
			//parentJson.put("Subscriber_ID", "");
			parentJson.put("Email", "api_automation_" + RandomStringUtils.randomNumeric(8) + "@livecareer.com");
			parentJson.put("IP_Address", Inet4Address.getLocalHost().getHostAddress());
			//parentJson.put("Status_CD", "ACTV");
			parentJson.put("Source_CD", "LCAUS");
			parentJson.put("SourceUID", RandomStringUtils.randomNumeric(5));
			
			JSONObject subscriptionsJson = new JSONObject();
			//subscriptionsJson.put("Subscription_ID", 0);
			subscriptionsJson.put("Referrer", "seo");
			//subscriptionsJson.put("Status_CD", "ACTV");
			subscriptionsJson.put("Subscription_Type_CD", "JBALRT");
			
			JSONArray subscription_Options = new JSONArray();

			JSONObject subsOption = new JSONObject();
			subsOption.put("Option_CD", "FREQ");
			subsOption.put("Option_Value", "Daily");		
			
			subscription_Options.put(subsOption);
			subscriptionsJson.put("Subscription_Options", subscription_Options);
			parentJson.put("Subscriptions", new JSONArray().put(subscriptionsJson));
			
			dataToPost.put(parentJson);	
			String url = BASE_URL + SUBSCRIBER_URL_V1;
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 400);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createSubscriberWithoutSubscriptionOption(){
		try {
			JSONArray dataToPost = new JSONArray();
			JSONObject parentJson = new JSONObject();
			//parentJson.put("Subscriber_ID", "");
			parentJson.put("Email", "api_automation_" + RandomStringUtils.randomNumeric(8) + "@livecareer.com");
			parentJson.put("IP_Address", Inet4Address.getLocalHost().getHostAddress());
			//parentJson.put("Status_CD", "ACTV");
			parentJson.put("Source_CD", "LCAUS");
			parentJson.put("SourceUID", RandomStringUtils.randomNumeric(5));
			
			JSONObject subscriptionsJson = new JSONObject();
			//subscriptionsJson.put("Subscription_ID", 0);
			subscriptionsJson.put("Referrer", "seo");
			//subscriptionsJson.put("Status_CD", "ACTV");
			subscriptionsJson.put("Subscription_Type_CD", "JBALRT");
			
			JSONArray subscription_Options = new JSONArray();
			
			JSONObject subsOption1 = new JSONObject();
			subsOption1.put("Option_CD", "JQRY");
			subsOption1.put("Option_Value", "");
			
			JSONObject subsOption2 = new JSONObject();
			subsOption2.put("Option_CD", "LOCN");
			subsOption2.put("Option_Value", "");
			
			JSONObject subsOption3 = new JSONObject();
			subsOption3.put("Option_CD", "FREQ");
			subsOption3.put("Option_Value", "");
			
			subscription_Options.put(subsOption1);
			subscription_Options.put(subsOption2);
			subscription_Options.put(subsOption3);
			subscriptionsJson.put("Subscription_Options", subscription_Options);
			parentJson.put("Subscriptions", new JSONArray().put(subscriptionsJson));
			
			dataToPost.put(parentJson);	
			String url = BASE_URL + SUBSCRIBER_URL_V2;
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			assertEquals(response.message, "[]" , "subscriber_id wrongly created");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void createTwoSubscriberWithSameData(){
		try {
			String sourceCD = "LCAUS";
			String loc = "sanfrancisco" + RandomStringUtils.randomAlphabetic(4);
			String jqry = "manager" + RandomStringUtils.randomAlphabetic(4);
			JSONArray dataToPost = randomSubscription(sourceCD , loc , jqry);			
			String url = BASE_URL + SUBSCRIBER_URL_V2;
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String subscriberId = respJson.getString("subscriber_id");		
			assertNotNull(subscriberId , "subscriber_id not generated");
			response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			respJson = new JSONArray(response.message).getJSONObject(0);
			assertTrue(respJson.getJSONArray("subscriptions").length()==1, "Response contains 2 subscriptions instead of 1");
			assertEquals(subscriberId, respJson.getString("subscriber_id"), "subscriber_id is wrongly modified");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
}