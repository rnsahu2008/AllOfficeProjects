package com.lc.tests.jobalerts_v2;

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
import com.lc.utils.JSONValueFinder;

import static com.lc.constants.Constants_JobAlerts.*;

public class DeleteSubscriptions extends JobAlertsCoreComponents_V2{

	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void deleteSubscriptionByIdSourceCDAndType(String sourceCD){
		try {
			/*
			 * This is setup part
			 * POST /v2/subscribers Adds the subscribers			 
			 */
			String loc = "new york";
			String jqry = "manager";
			JSONArray dataToPost = randomSubscription(sourceCD , loc , jqry);			
			String url = BASE_URL + SUBSCRIBER_URL_V2;
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			System.out.println(response.message);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String subscriberId = respJson.getString("subscriber_id");
			System.out.println(respJson.getString("subscriber_id"));
			
			/*
			 * DELETE /v2/subscribers/{id}/sources/{sourcecd}/unsubscribe Unsubscribes the specified id for source
			 */
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + subscriberId + "/" + "sources/" + sourceCD + "/unsubscribe?type=EMAIL";
			response = HttpService.delete(url, headers);
			checkSuccessCode(response.code);
			
			url = BASE_URL + SUBSCRIBER_URL_V2  + "/" + subscriberId; 
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			assertEquals("INAC", respJson.getString("status_cd"), "status_cd not changed to INAC");			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	
	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void deleteSubscriptionByIdSourceCDSidAndType(String sourceCD){
		try {
			/*
			 * This is setup part
			 * POST /v2/subscribers Adds the subscribers			 
			 */
			String loc = "new york";
			String jqry = "manager";
			JSONArray dataToPost = randomSubscription(sourceCD , loc , jqry);			
			String url = BASE_URL + SUBSCRIBER_URL_V2;
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			System.out.println(response.message);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String subscriberId = respJson.getString("subscriber_id");
			String sid = JSONValueFinder.getJsonValue(response.message, "subscription_id");
			System.out.println(respJson.getString("subscriber_id"));
			
			/*
			 * DELETE /v2/subscribers/{id}/sources/{sourcecd}/subscriptions/{sid}/unsubscribe 
			 * Unsubscribes the specified subscription for subscriberid and source
			 */
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + subscriberId + "/" + "sources/" + sourceCD + "/subscriptions/" + sid + "/unsubscribe?type=EMAIL";
			response = HttpService.delete(url, headers);
			checkSuccessCode(response.code);
			url = BASE_URL + SUBSCRIBER_URL_V2  + "/" + subscriberId; 
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			
			JSONObject subsJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			assertEquals("INAC", subsJson.getString("status_cd"), "status_cd not changed to INAC");			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
}
