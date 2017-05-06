package com.lc.tests.ecom;
import static com.lc.constants.Constants_Ecom.BASE_URL;

import java.util.HashMap;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.EComCoreComponents;
import com.lc.dataprovider.Ecom_DP;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.HeaderManager;

public class DeactivateSubscription extends EComCoreComponents {
	String subscriptionId = "";
	
	@BeforeMethod
	public void init() {
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	//Test that subscription can be deactivated provided values for parameters are passed
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "getDeactivateSubscriptionData")
	public void deactivateSubscription(HashMap<String, String> data) 
	{
		try 
		{
			if(data.get("SubscriptionId").equalsIgnoreCase(""))
				subscriptionId = cancelSampleSubscription();
			else
				subscriptionId = data.get("SubscriptionId").equalsIgnoreCase("null") ? null : data.get("SubscriptionId");
			
			JSONObject json = new JSONObject();
			json.put("reason_cd", data.get("reason_cd"));

			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/deactivate";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.patch(url, headers, json.toString());
			
			if(data.get("ResponseMessage").equalsIgnoreCase(""))  //Positive scenarios (where ResonseCode and ResponseMessage are BLANK in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, "Getting NULL as response");
				JSONObject respJson = new JSONObject(response.message);
				assertEquals(respJson.get("EventStatus").toString(), "RFND", data.get("TestCaseName") + " test got failed. Failure reason : EventStatus not correct");
				assertEquals(respJson.get("subscription_status_cd").toString(), "EXPD", data.get("TestCaseName") + " test got failed. Failure reason : subscription_status_cd incorrect");
			}
			else  //Negative scenarios (using invalid values for parameters)
			{
				assertTrue(Integer.parseInt(data.get("ResponseCode")), response.code, data.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch");
				if(!response.message.contains("ModelState"))
				assertEquals(data.get("ResponseMessage"), response.message, data.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
			}
			
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
}
