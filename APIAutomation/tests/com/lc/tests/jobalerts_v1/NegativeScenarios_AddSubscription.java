package com.lc.tests.jobalerts_v1;

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

public class NegativeScenarios_AddSubscription extends JobAlertsCoreComponents_V1{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@Test 
	public void addNewSubscriptionAndGetDetailsWithInvalidId(){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			String sourceCD = "LCAUS";
			JSONArray dataToPost = randomSubscription(sourceCD , null , null);			
			String url = BASE_URL + SUBSCRIBER_URL_V1;
			ResponseBean response = HttpService.post(url, null, dataToPost.toString());
			checkSuccessCode(response.code);
			String invalidId = "4be6c086-64cb-42e3-9f66-26c1699daf5bee";
	
			/*
			 * 	Check when invalid id is passed 
			 *	POST /v1/subscribers/{id}/subscriptions Adds the subscription 
			 */ 
			
			String loc = "newyork" + RandomStringUtils.randomAlphanumeric(4);
			String jqry = "manager" + RandomStringUtils.randomAlphanumeric(4);
			JSONObject newSubscriptionJson = newSubscriptionWithSubscriptionID("0" , loc , jqry);
			String newUrl = BASE_URL + "/v1/subscribers/" + invalidId + "/subscriptions";
			response = HttpService.post(newUrl, null, newSubscriptionJson.toString());
			checkSuccessCode(response.code);
			assertEquals("false", response.message, "Not getting FALSE value in response");
			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
}
