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
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

import static com.lc.constants.Constants_JobAlerts.*;

public class NegativeScenarios_GetSubscriberSubscription extends JobAlertsCoreComponents_V1{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@Test
	public void getSubscriberWithInvalidParameters(){
		try {
			/*
			 * create new subscription by POST
			 */
			String sourceCD = "LCAUS";
			String loc = "sanfrancisco" + RandomStringUtils.randomAlphabetic(4);
			String jqry = "manager" + RandomStringUtils.randomAlphabetic(4);
			JSONArray dataToPost = randomSubscription(sourceCD , loc , jqry);			
			String url = BASE_URL + SUBSCRIBER_URL_V1;
			ResponseBean response = HttpService.post(url, null, dataToPost.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);			
			JSONObject inputJson = dataToPost.getJSONObject(0);
						
			/*
			 * Check GET when sourceCD is invalid
			 * check newly created subscription by GET (by email and sourceCD)
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + inputJson.getString("Email") + "/source/" + "LCAUK"; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			assertEquals("null", response.message, "Not getting null in response for Invalid sourceCD");
			
			/*
			 * Check GET when email is invalid
			 * check newly created subscription by GET (by email and sourceCD)
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + "wrongemail@lc.com" + "/source/" + sourceCD; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			assertEquals("null", response.message, "Not getting null in response for Invalid email");
			
			
			/*
			 * Check GET when sourceCD is invalid
			 *check newly created subscription by GET (by Id and sourceCD) 
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1  + "/" + respJson.getString("subscriber_id") + "/" + "LCAUK"; 
			response = HttpService.get(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			assertEquals("null", response.message, "Not getting null in response for Invalid sourceCD");
					
			
			/*
			 * Check GET when id is invalid
			 *check newly created subscription by GET (by Id and sourceCD) 
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1  + "/" + RandomStringUtils.randomNumeric(8) + "/" + sourceCD; 
			response = HttpService.get(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			assertEquals("null", response.message, "Not getting null in response for Invalid ID");
			
			/*
			 * Check GET when ID is incorrect
			 *check newly created subscription by GET (by Id and sourceCD) 
			 */
			url = BASE_URL + SUBSCRIPTION_URL_V1 + "/" + RandomStringUtils.randomNumeric(8);
			response = HttpService.get(url, null);
			assertResponseCode(response.code, 404);
					
		} catch (Exception e) {
			fail("");
		}
		s_assert.assertAll();
	}
	
}