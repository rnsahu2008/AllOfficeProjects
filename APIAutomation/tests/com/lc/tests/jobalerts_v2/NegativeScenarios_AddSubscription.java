package com.lc.tests.jobalerts_v2;

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

public class NegativeScenarios_AddSubscription extends JobAlertsCoreComponents_V2{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@Test 
	public void addNewSubscriptionAndGetDetails(){
		try {
			/*
			 * create new subscription by POST (Adds the subscription)
			 */
			String sourceCD = "LCAUS";
			JSONArray dataToPost = randomSubscription(sourceCD , null , null);				
			String url = BASE_URL + SUBSCRIBER_URL_V2;
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			String invalidId = "4be6c086-64cb-42e3-9f66-26c1699daf5bee";		
			
			JSONObject newSubscriptionJson = addSubscriptionDataV2();
			String newUrl = BASE_URL + SUBSCRIBER_URL_V2 + "/" + invalidId + "/subscriptions";
			response = HttpService.post(newUrl, headers, newSubscriptionJson.toString());
			assertResponseCode(response.code, 200);
			assertEquals("null", response.message, "Not getting NULL in response");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
}
