package com.lc.tests.jobalerts_v1;

import static com.lc.constants.Constants_JobAlerts.*;

import java.util.HashMap;

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

public class DeleteSubscriber extends JobAlertsCoreComponents_V1{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void deleteSubscriberByIdAndSourceCD(String sourceCD){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			
			/*
			 * 	DELETE /v1/subscribers/{id}/{sourceCD}/cancel Unsubscribes the specified id for source.
			 */
			String id = respJson.getString("subscriber_id");
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD + "/cancel";
			response = HttpService.delete(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			assertEquals("true", response.message, "Not getting TRUE value in response");
			
			/*
			 * 	Verify the delete operation by
			 * 	GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			JSONObject subscriptionJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			assertEquals("INAC", respJson.getString("status_cd"), "Subscriber Status not changed to INAC");
			assertEquals("ACTV", subscriptionJson.getString("status_cd"), "Subscription Status wrongly changed to INAC");
			//subscription_status shall be actv
				
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "deleteData")
	public void deleteSubscriberByIdSourceCDAndType(String sourceCD , String type){
		try {
			/*
			 * 	POST /v1/subscribers Adds the subscribers
			 */			
			ResponseBean response = createRandomSubscriber(sourceCD);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			
			/*
			 * 	DELETE /v1/subscribers/{id}/{sourceCD}/cancel/{type} Unsubscribes the specified id for source
			 */
			String id = respJson.getString("subscriber_id");
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD + "/cancel/" + type;
			response = HttpService.delete(url, null);
			checkSuccessCode(response.code);
			assertEquals("true", response.message, "Not getting TRUE value in response");
			
			/*
			 * 	Verify the delete operation by
			 * 	GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			JSONObject subscriptionJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			assertEquals("INAC", respJson.getString("status_cd"), "Subscriber Status not changed to INAC");
			assertEquals("ACTV", subscriptionJson.getString("status_cd"), "Subscription Status wrongly changed to INAC");
			//subscription_status shall be actv	
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
}
