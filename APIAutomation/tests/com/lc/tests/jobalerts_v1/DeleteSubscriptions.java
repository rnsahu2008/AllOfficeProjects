package com.lc.tests.jobalerts_v1;

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

import static com.lc.constants.Constants_JobAlerts.*;

public class DeleteSubscriptions extends JobAlertsCoreComponents_V1{

	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
	public void deleteSubscriptionByIdAndSID(String sourceCD){
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
			 * 	DELETE /v1/subscribers/{id}/subscriptions/{sid}/cancel Deactivates the subscriptionid for particular subscriber unique id
			 */
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + "subscriptions/" + sid + "/cancel";
			response = HttpService.delete(url, null);
			
			/*
			 * 	Verify the delete operation by
			 * 	GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			//check subscriber status is INAC
			JSONObject subsJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			assertEquals("INAC", subsJson.getString("status_cd"), "status_cd not changed to INAC");			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	
	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "deleteData")
	public void deleteSubscriptionByIdSIDAndType(String sourceCD , String type){
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
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + "subscriptions/" + sid + "/cancel" + "/" + type;
			response = HttpService.delete(url, null);
			
			/*
			 * 	Verify the delete operation by
			 * 	GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			JSONObject subsJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			assertEquals("INAC", subsJson.getString("status_cd"), "status_cd not changed to INAC");	
			//check subscriber status is INAC
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test (dataProviderClass = JobAlerts_DP.class , dataProvider = "deleteData")
	public void deleteSubscriptionBySIDSourceCDAndType(String sourceCD , String type){
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
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD + "/" + "subscriptions/" + sid + "/cancel" + "/" + type;
			response = HttpService.delete(url, null);
			
			/*
			 * 	Verify the delete operation by
			 * 	GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			JSONObject subsJson = respJson.getJSONArray("subscriptions").getJSONObject(0);
			assertEquals("INAC", subsJson.getString("status_cd"), "status_cd not changed to INAC");	
			//check subscriber status is INAC
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

}
