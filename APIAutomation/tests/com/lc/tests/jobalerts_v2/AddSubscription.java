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

import java.text.SimpleDateFormat;
import java.util.*;

import static com.lc.constants.Constants_JobAlerts.*;

public class AddSubscription extends JobAlertsCoreComponents_V2{
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@Test ////(dataProviderClass = JobAlerts_DP.class , dataProvider = "getSourceCD")
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
			JSONObject respJson = new JSONArray(response.message).getJSONObject(0);
			String subscriberId = respJson.getString("subscriber_id");			
			
			String statusCd = respJson.getString("status_cd");
		
			JSONObject newSubscriptionJson = this.getHardcodedSubscriptionData();
			String newUrl = BASE_URL + SUBSCRIBER_URL_V2 + "/" + subscriberId + "/subscriptions";
			response = HttpService.post(newUrl, headers, newSubscriptionJson.toString());
			checkSuccessCode(response.code);
			
			JSONObject actualjsonObject = new JSONObject(response.message); 			
						
			assertEquals(statusCd, actualjsonObject.getString("status_cd"), "status_cd not same");
			assertEquals(newSubscriptionJson.getString("subscription_type_cd"), actualjsonObject.getString("subscription_type_cd"), "subscription_type_cd not same");	
			assertEquals(newSubscriptionJson.getString("Referrer"), actualjsonObject.getString("Referrer"), "Referrer not same");
			String getDate = actualjsonObject.getString("subscribed_on");
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date date = formatter.parse(getDate);
			
			Date todayDate = new Date();
			String todatDateInString = formatter.format(todayDate);			
			Date todatDateInExpecetdFormat = formatter.parse(todatDateInString);		
		    assertTrue(date.equals(todatDateInExpecetdFormat), "date time is mis match ...");
     		String sid= actualjsonObject.getString("subscription_id");
			JSONArray subsArrexpected = newSubscriptionJson.getJSONArray("subscription_options");
			JSONArray subsArrActual = actualjsonObject.getJSONArray("subscription_options");			
			
			JSONObject subscription_optionsexpected = subsArrexpected.getJSONObject(0);
			JSONObject subscription_optionsactual = subsArrActual.getJSONObject(0);
			
		    assertEquals(subscription_optionsexpected.getString("option_value"), subscription_optionsactual.getString("option_value"), "(\"option_value\") not equal");
		    assertEquals(subscription_optionsexpected.getString("option_cd"), subscription_optionsactual.getString("option_cd"), "(\"option_cd\") not equal");
			
			url = BASE_URL + SUBSCRIBER_URL_V2  + "/" + respJson.getString("subscriber_id"); 
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			
			url = BASE_URL + SUBSCRIBER_URL_V2 + "/" + subscriberId + "/subscriptions" + "/" + sid;
			response = HttpService.get(url, headers);
			assertNotNull(response.message,"response is null");
			checkSuccessCode(response.code);
			
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	public JSONObject getHardcodedSubscriptionData(){
		try {
			JSONObject json = new JSONObject();
			json.put("subscription_type_cd", "JBALRT");
			json.put("Referrer", "seo");
			
			JSONObject subsOption1 = new JSONObject();
			subsOption1.put("option_cd", "JQRY");
			subsOption1.put("option_value", "Manager");
			
			JSONObject subsOption2 = new JSONObject();
			subsOption2.put("option_cd", "LOCN");
			subsOption2.put("option_value", "New York");
			
			JSONObject subsOption3 = new JSONObject();
			subsOption3.put("option_cd", "FREQ");
			subsOption3.put("option_value", "Daily");		
			
			JSONArray subscription_Options = new JSONArray();
			subscription_Options.put(subsOption1);
			subscription_Options.put(subsOption2);
			subscription_Options.put(subsOption3);
			json.put("subscription_options", subscription_Options);
			
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
