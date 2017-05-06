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
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

import static com.lc.constants.Constants_JobAlerts.*;

public class NegativeScenarios_GetSubscriberSubscription extends JobAlertsCoreComponents_V2{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@Test
	public void createSubscriberByPOSTAndCheckByGET(){
		try {
			/*
			 * create new subscriber by POST (Adds the subscribers)
			 */
			String sourceCD = "LCAUS";
			String invalidSourceCD = "LCAUK";
			String loc = "sanfrancisco" + RandomStringUtils.randomAlphabetic(4);
			String jqry = "manager" + RandomStringUtils.randomAlphabetic(4);
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
				
			/*
			 * check newly created subscriber by GET (Gets the subscriber by email and source)
			 */
			JSONObject dataWithWrongEmail = new JSONObject();
			dataWithWrongEmail.put("EmailAddress", "wrongemail@livecareer.com");
			dataWithWrongEmail.put("Source", sourceCD);
			JSONObject param = new JSONObject();
			param.put("subscriberemailsource", dataWithWrongEmail);
			String queryParam = URLEncoder.encode(dataWithWrongEmail.toString(), "UTF-8");
			url = BASE_URL + SUBSCRIBER_URL_V2 + "?subscriberemailsource=" + queryParam; 
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertEquals("null", response.message, "Not getting NULL in response message");
			
			JSONObject dataWithWrongSourceCD = new JSONObject();
			dataWithWrongSourceCD.put("EmailAddress", inputJson.getString("Email"));
			dataWithWrongSourceCD.put("Source", invalidSourceCD);
			param = new JSONObject();
			param.put("subscriberemailsource", dataWithWrongSourceCD);
			queryParam = URLEncoder.encode(dataWithWrongSourceCD.toString(), "UTF-8");
			url = BASE_URL + SUBSCRIBER_URL_V2 + "?subscriberemailsource=" + queryParam; 
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertEquals("null", response.message, "Not getting NULL in response message");
			
			/*
			 *check newly created subscription by GET (Gets the subscriber by identifier and source) 
			 */
			String invalidId = "f6f6f219-e75a-4ee3-ac99-24570f2a72afddffr";
			String invalidSid = "48336633";
			url = BASE_URL + SUBSCRIBER_URL_V2  + "/" + invalidId; 
			response = HttpService.get(url, headers);
			assertEquals(404, response.code, "Not getting 404 error code");
			assertEquals("", response.message, "Not getting '' in response message");
			
			/*
			 *check newly created subscription by GET (Gets the subscription by SubscriptionId) 
			 */
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + subscriberId + "/subscriptions/" + invalidSid;
			response = HttpService.get(url, headers);
			assertResponseCode(response.code, 404);
			
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + invalidId + "/subscriptions/" + subscription_id;
			response = HttpService.get(url, headers);
			assertResponseCode(response.code, 404);
			} 
		catch (Exception e) {
			fail("");
		}
		s_assert.assertAll();
	}
	
}