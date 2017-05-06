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
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

public class NegativeScenarios_DeleteSubscriber extends JobAlertsCoreComponents_V1{
	
	String sourceCD = "LCAUS";
	String invalidSourceCD = "LCAUK";
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test
	public void deleteSubscriberByIdAndSourceCD(){
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
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + invalidSourceCD + "/cancel";
			response = HttpService.delete(url, new HashMap<String, String>());
			checkSuccessCode(response.code);
			assertEquals("false", response.message, "Not getting FALSE value in response");
			
			/*
			 * 	Verify the delete operation by
			 * 	GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			assertEquals("ACTV", respJson.getString("status_cd"), "Status wrongly changed to INAC");
				
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void deleteSubscriberByIdSourceCDAndType(){
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
			String url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + invalidSourceCD + "/cancel/" + "EMAIL";
			response = HttpService.delete(url, null);
			checkSuccessCode(response.code);
			assertEquals("false", response.message, "Not getting FALSE value in response");
			
			/*
			 * 	Verify the delete operation by
			 * 	GET /v1/subscribers/{id}/{sourceCD} Gets the subscriber by identifier and source.
			 */
			url = BASE_URL + SUBSCRIBER_URL_V1 + "/" + id + "/" + sourceCD; 
			response = HttpService.get(url, null);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			assertEquals("ACTV", respJson.getString("status_cd"), "Status wrongly changed to INAC");
				
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
}
