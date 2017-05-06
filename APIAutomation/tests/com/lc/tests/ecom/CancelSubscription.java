package com.lc.tests.ecom;

import static com.lc.constants.Constants_Ecom.BASE_URL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.EComCoreComponents;
import com.lc.dataprovider.Ecom_DP;
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.HeaderManager;

public class CancelSubscription extends EComCoreComponents
{
	String subscriptionId = "";
	
	@BeforeMethod
	public void init()
	{
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	//Test that subscription can be canceled provided valid parameter values are passed
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "getCancelSubscriptionData")
	public void cancelSubscription(HashMap<String, String> data) 
	{
		try 
		{
			if(data.get("SubscriptionId").equalsIgnoreCase(""))
				subscriptionId = createSampleSubscription();
			else
				subscriptionId = data.get("SubscriptionId").equalsIgnoreCase("null") ? null : data.get("SubscriptionId");

			JSONObject cancelSub = new JSONObject();
			cancelSub.put("mode", data.get("mode"));
			cancelSub.put("reason_cd", data.get("reason_cd"));
			cancelSub.put("reason_message", data.get("reason_message"));
			cancelSub.put("expiry_date", getExpiryDate(data.get("expiry_date")));			
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/cancel";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");		
			ResponseBean response = HttpService.patch(url, headers, cancelSub.toString());
			System.out.println("Response: " + response.message);
			
			if(data.get("ResponseMessage").equalsIgnoreCase(""))  //Positive scenarios (where ResonseCode and ResponseMessage are BLANK in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, "Getting NULL as response");
				JSONObject respJson = new JSONObject(response.message);
				JSONObject subscriptionJson = (JSONObject) respJson.get("Subscription");
				String subscription_status_cd = subscriptionJson.get("subscription_status_cd").toString();
				String next_billing_date = subscriptionJson.get("next_billing_date").toString();
				String query_session = "select * from SubscriptionEventProperty where SubscriptionEventID in (select SubscriptionEventID from SubscriptionEvent where SubscriptionID = "	+ subscriptionId + ") and PropertyCD = 'mode'";
				List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
				Map tmpData_session = list.get(0);
				int renewal_count = subscriptionJson.getInt("renewal_count");
				assertTrue(renewal_count, 0, data.get("TestCaseName") + " test got failed. Failure reason : renewal_count not correct");
				assertEquals(subscription_status_cd, "EXPR", data.get("TestCaseName") + " test got failed. Failure reason : Status not correct");
				assertEquals(next_billing_date, "null", data.get("TestCaseName") + " test got failed. Failure reason : Next billing date not correct");
				assertEquals(data.get("mode"), tmpData_session.get("PropertyValue").toString(), data.get("TestCaseName") + " test got failed. Failure reason : mode set in SubscriptionEventProperty table is incorrect");
			}
			else //Negative scenarios (using invalid values for parameters)
			{
				assertTrue(Integer.parseInt(data.get("ResponseCode")), response.code, data.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch");
				if(!response.message.contains("ModelState"))
				assertEquals(data.get("ResponseMessage"), response.message, data.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
			}
		} 
		catch (Exception e) 
		{
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

}
