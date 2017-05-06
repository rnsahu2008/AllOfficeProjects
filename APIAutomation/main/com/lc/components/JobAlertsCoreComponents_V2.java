package com.lc.components;

import static com.lc.constants.Constants_JobAlerts.*;

import java.net.Inet4Address;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.lc.assertion.AssertionUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.ReadProperties;

public class JobAlertsCoreComponents_V2 extends AssertionUtil{	
	public HashMap<String, String> headers = new HashMap<String, String>();
	@Parameters("Environment")
	@BeforeTest
	public void setupEnvironment(String env){
		ReadProperties.setupEnvironmentProperties(env,"res/jobalertsdata/config.json");
		environment = env;
		
		try {
			headers.put("Authorization", "Bearer 9bbFqW_wkpqYvpze240NFSJr3y083m3CwyihusDBWQcevpCtTIVO9ej03-_dWAs6k6LTKvQ7LDdO_n0-WCFCRsbLXxD01Pqv95YB1XFTBQ7d9tS5CPbAsOhIOdIpCFUuSyyrDN_XfOq6UznoTqFXSUP9eV8LomsvDzqFOXPHCUSz3sQmS6WK0929csT4pQ9Rd54kG3q5HQf7N_KnumuI4-dgrr2mD2a12dyfMmfuubMRHthDNoghD2kHWvF2EwPI");
			String url = BASE_URL + HEART_BEAT_URL_V1;
			ResponseBean response = HttpService.get(url, new HashMap<String, String>());
			if (response.code==200 && response.message.contains("Successful. Your request has been handled successfully")){
				//assertTrue(true , "");
			}
			else{
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static JSONArray randomSubscription(String sourceCD, String loc , String jqry){
		try {
			if(StringUtils.isEmpty(loc)){
				loc = "NewJersey";
			}
			if(StringUtils.isEmpty(jqry)){
				jqry = "QA Manager";
			}
			JSONArray finalArr = new JSONArray();
			JSONObject parentJson = new JSONObject();
			//parentJson.put("Subscriber_ID", "");
			parentJson.put("Email", "api_automation_" + UUID.randomUUID().toString() + "@livecareer.com");
			parentJson.put("IP_Address", Inet4Address.getLocalHost().getHostAddress());
			//parentJson.put("Status_CD", "ACTV");
			parentJson.put("Source_CD", sourceCD);
			parentJson.put("SourceUID", RandomStringUtils.randomNumeric(10));
			
			JSONObject subscriptionsJson = new JSONObject();
			//subscriptionsJson.put("Subscription_ID", 0);
			subscriptionsJson.put("Referrer", "seo");
			//subscriptionsJson.put("Status_CD", "ACTV");
			subscriptionsJson.put("Subscription_Type_CD", "JBALRT");
			
			JSONArray subscription_Options = new JSONArray();
			
			JSONObject subsOption1 = new JSONObject();
			subsOption1.put("Option_CD", "JQRY");
			subsOption1.put("Option_Value", jqry);
			
			JSONObject subsOption2 = new JSONObject();
			subsOption2.put("Option_CD", "LOCN");
			subsOption2.put("Option_Value", loc);
			
			JSONObject subsOption3 = new JSONObject();
			subsOption3.put("Option_CD", "FREQ");
			subsOption3.put("Option_Value", "Daily");		
			
			subscription_Options.put(subsOption1);
			subscription_Options.put(subsOption2);
			subscription_Options.put(subsOption3);
			subscriptionsJson.put("Subscription_Options", subscription_Options);
			parentJson.put("Subscriptions", new JSONArray().put(subscriptionsJson));
			
			finalArr.put(parentJson);
			return finalArr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static JSONArray subscriptionWithSubscriptionIdEmailUidAndOptions(String sourceCD, String subscriptionID , String email , String sourceUid , String loc , String jqry){
		try {
			JSONArray finalArr = new JSONArray();
			JSONObject parentJson = new JSONObject();
			//parentJson.put("Subscriber_ID", "");
			parentJson.put("Email", email);
			parentJson.put("IP_Address", Inet4Address.getLocalHost().getHostAddress());
			//parentJson.put("Status_CD", "ACTV");
			parentJson.put("Source_CD", sourceCD);
			parentJson.put("SourceUID", sourceUid);
			
			JSONObject subscriptionsJson = new JSONObject();
			subscriptionsJson.put("Subscription_ID", subscriptionID);
			//subscriptionsJson.put("Referrer", "seo");
			//subscriptionsJson.put("Status_CD", "ACTV");
			subscriptionsJson.put("Subscription_Type_CD", "JBALRT");
			
			JSONArray subscription_Options = new JSONArray();
			
			JSONObject subsOption1 = new JSONObject();
			subsOption1.put("Option_CD", "JQRY");
			subsOption1.put("Option_Value", jqry);
			
			JSONObject subsOption2 = new JSONObject();
			subsOption2.put("Option_CD", "LOCN");
			subsOption2.put("Option_Value", loc);
			
			JSONObject subsOption3 = new JSONObject();
			subsOption3.put("Option_CD", "FREQ");
			subsOption3.put("Option_Value", "Daily");		
			
			subscription_Options.put(subsOption1);
			subscription_Options.put(subsOption2);
			subscription_Options.put(subsOption3);
			subscriptionsJson.put("Subscription_Options", subscription_Options);
			parentJson.put("Subscriptions", new JSONArray().put(subscriptionsJson));
			
			finalArr.put(parentJson);
			return finalArr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject newSubscriptionWithSubscriptionID(String subscriptionId , String loc , String jqry){
		try {
			JSONObject json = new JSONObject();
			json.put("Subscription_ID", subscriptionId);
			json.put("Subscribed_On", "03/06/2015");
			json.put("Status_CD", "ACTV");
			json.put("Subscription_Type_CD", "JBALRT");
			json.put("Referrer", "seo");
			JSONArray subscription_Options = new JSONArray();
			
			JSONObject subsOption1 = new JSONObject();
			subsOption1.put("Option_CD", "JQRY");
			subsOption1.put("Option_Value", jqry);
			
			JSONObject subsOption2 = new JSONObject();
			subsOption2.put("Option_CD", "LOCN");
			subsOption2.put("Option_Value", loc);
			
			JSONObject subsOption3 = new JSONObject();
			subsOption3.put("Option_CD", "FREQ");
			subsOption3.put("Option_Value", "Weekly");		
			
			subscription_Options.put(subsOption1);
			subscription_Options.put(subsOption2);
			subscription_Options.put(subsOption3);
			json.put("Subscription_Options", subscription_Options);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject randomSubscription(String sourceCD, String subscriberId , String email , String sourceUid){
		try {
			JSONObject parentJson = new JSONObject();
			parentJson.put("Subscriber_ID", subscriberId);
			parentJson.put("Email", email);
			parentJson.put("IP_Address", Inet4Address.getLocalHost().getHostAddress());
			parentJson.put("Status_CD", "INAC");
			parentJson.put("Source_CD", sourceCD);
			parentJson.put("SourceUID", sourceUid);			
			return parentJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject addSubscriptionDataV2(){
		try {
			JSONObject json = new JSONObject();
			json.put("Subscription_Type_CD", "JBALRT");
			json.put("Referrer", "seo");
			
			JSONObject subsOption1 = new JSONObject();
			subsOption1.put("Option_CD", "JQRY");
			subsOption1.put("Option_Value", "Manager");
			
			JSONObject subsOption2 = new JSONObject();
			subsOption2.put("Option_CD", "LOCN");
			subsOption2.put("Option_Value", "New York");
			
			JSONObject subsOption3 = new JSONObject();
			subsOption3.put("Option_CD", "FREQ");
			subsOption3.put("Option_Value", "Daily");		
			
			JSONArray subscription_Options = new JSONArray();
			subscription_Options.put(subsOption1);
			subscription_Options.put(subsOption2);
			subscription_Options.put(subsOption3);
			json.put("Subscription_Options", subscription_Options);
			
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResponseBean createRandomSubscriber(String sourceCD){
		try {
			JSONArray dataToPost = randomSubscription(sourceCD , null , null);
			String url = BASE_URL + SUBSCRIBER_URL_V1;
			ResponseBean response = HttpService.post(url, new HashMap<String, String>(), dataToPost.toString());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
