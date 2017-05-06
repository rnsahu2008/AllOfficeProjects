package com.lc.tests.ecom;

import static com.lc.constants.Constants_Ecom.BASE_URL;
import static com.lc.constants.Constants_Ecom.SUBSCRIBE_URL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.EComCoreComponents;
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.HeaderManager;
import com.lc.utils.JSONValueFinder;
import com.lc.dataprovider.Ecom_DP;;

public class CreateSubscription extends EComCoreComponents
{
	String customerId = "";
	String subscriptionId = "";

	@BeforeMethod
	public void init()
	{
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	//Test that subscription is created provided correct values for parameters are passed
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "getSubscriptionData")
	public void createSubscription(HashMap<String, String> data)
	{	
		try
		{	
			JSONObject json = createSubscriptionJsonObject(data, getFakeCCDetails());
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");			
			ResponseBean response = HttpService.post(BASE_URL + "/" + SUBSCRIBE_URL, headers, json.toString());
			JSONObject respJson = new JSONObject(response.message);

			if(data.get("ResponseMessage").equalsIgnoreCase(""))  //Positive scenarios (where ResonseCode and ResponseMessage are BLANK in the excel sheet)
			{
				//Get customer id and subscription id from subscription response message 
				JSONObject custID = (JSONObject) respJson.get("customer");
				customerId = custID.get("id").toString();
				JSONObject SubID = (JSONObject) respJson.get("subscription");
				subscriptionId = SubID.get("id").toString();

				//Assets subscription id is same in subscription response message and subscription table
				String query_session = "select * from [Ecommerce].[dbo].[Subscription] where customerid = "+ customerId;
				List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
				Map tmpData_session = list.get(0);
				assertEquals(tmpData_session.get("SubscriptionID").toString(), subscriptionId, "subscriptionId Mismatch");

				//Assert status and mode 
				assertJsonValueEquality(respJson, "status", "SUCC");
				assertJsonValueEquality(respJson, "mode", "DEMO");	
				
				//Gateway Check 
				verifyTransaction(respJson, "SALE", false);
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
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//Test that subscription can not be created when invalid values for credit card parameters are passed
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "getCreditCardData")
	public void createSubscriptionWithInvalidCCParameters(HashMap<String, String> ccDetails)
	{	
		try
		{	
			JSONObject ccJson = getInvalidCCJson(ccDetails);
			JSONObject json = createSubscriptionJsonObjectWithInvalidCC(ccJson);

			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");			
			ResponseBean response = HttpService.post(BASE_URL + "/" + SUBSCRIBE_URL, headers, json.toString());

			JSONObject respJson = new JSONObject(response.message);

			if(ccDetails.get("FailCode").equalsIgnoreCase("")) //Scenarios (where FailCode is BLANK in the excel sheet)
			{
				assertTrue(Integer.parseInt(ccDetails.get("ResponseCode")), response.code, ccDetails.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch, expected " +ccDetails.get("ResponseCode") + " and actual " +response.code);
				if(!response.message.contains("ModelState"))
					assertEquals(ccDetails.get("ResponseMessage"), response.message, ccDetails.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
			}
			else //Scenarios (where FailCode is mentioned in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, ccDetails.get("TestCaseName") + " test got failed. Failure reason :Getting NULL as response");
				assertJsonValueEquality(respJson, "status", "FAIL");
				assertJsonValueEquality(respJson, "mode", "DEMO");
				assertJsonValueEquality(respJson, "fail_code", ccDetails.get("FailCode"));
				assertJsonValueEquality(respJson, "fail_message", ccDetails.get("FailMessage"));
			}			
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//Test that few subscription can be created when credit card details are not passed
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "getSubscriptionWithoutCC")
	public void createSubscriptionWithoutCC(HashMap<String, String> data) 
	{
		try 
		{					
			JSONObject json = createSubscriptionJsonObject(data, new JSONObject());				
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");					
			ResponseBean response = HttpService.post(BASE_URL + "/" + SUBSCRIBE_URL, headers, json.toString());
			JSONObject respJson = new JSONObject(response.message);

			if(data.get("ResponseMessage").equalsIgnoreCase("")) //Positive scenarios (Where ResponseCode and ResponseMessage are BLANK in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, "Getting NULL as response");
				assertJsonValueEquality(respJson, "status", "SUCC");
				assertJsonValueEquality(respJson, "mode", "DEMO");
			}
			else  //Negative scenarios (using invalid values for parameters)
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

	//Test that subscription can not be created when invalid credit card details are used for payment
	@Test
	public void createSubscriptionByFakeFailCCDetails() 
	{
		try 
		{
			ResponseBean response = createSubscriptionBySkuId("10117", 1.95, "LCAUS", getFakeFailCCDetails());			
			JSONObject respJson = new JSONObject(response.message);			
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");		
			assertJsonValueEquality(respJson, "status", "FAIL");
			assertJsonValueEquality(respJson, "mode", "DEMO");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test that subscription can be created for existing customer with NO credit card details 
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "csWithExistingCustomer")
	public void createSubscriptionsForExistingCustomer(HashMap<String, String> data) 
	{
		try 
		{	
			if(data.get("customer_id").equalsIgnoreCase(""))
			{
				ResponseBean resp = createSubscriptionBySkuId("10117", 1.95, "LCAUS", getFakeCCDetails());
				customerId = JSONValueFinder.getJsonValue(resp.message, "CustomerID");
			}
			else
			{
				customerId = data.get("customer_id");
			}

			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			JSONObject json = getSubscriptionJsonBySkuId("10117", 1.95);			
			json.put("credit_card", JSONObject.NULL);
			ResponseBean response = HttpService.post(url, headers, json.toString());

			if(data.get("ResponseMessage").equalsIgnoreCase(""))  //Positive scenarios (where ResonseCode and ResponseMessage are BLANK in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, "Getting NULL as response");
				JSONObject respJson = new JSONObject(response.message);
				assertJsonValueEquality(respJson, "status", "SUCC");
				assertJsonValueEquality(respJson, "mode", "DEMO");

				//DB Verification
				customerId = ((JSONObject) respJson.get("customer")).get("id").toString();
				subscriptionId = ((JSONObject) respJson.get("subscription")).get("id").toString();

				String query_session = "select * from [Ecommerce].[dbo].[Subscription] where customerid = " + customerId;
				List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
				Map tmpData_session = list.get(1);

				assertEquals(tmpData_session.get("SubscriptionID").toString(), subscriptionId, "subscriptionId Mismatch");

				//Gateway Check 
				verifyTransaction(respJson, "SALE", false);
			}
			else //Negative scenarios (using invalid values for parameters)
			{
				assertTrue(Integer.parseInt(data.get("ResponseCode")), response.code, data.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch");
				assertEquals(data.get("ResponseMessage"), response.message, data.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
			}
		} 
		catch (Exception e) 
		{
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test that subscription can not be created for existing customer when invalid data is provided
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "getSubscriptionData")
	public void createSubscriptionsForExistingCustomerWithInvalidData(HashMap<String, String> data) 
	{
		try 
		{
			ResponseBean resp = createSubscriptionBySkuId("10117", 1.95, "LCAUS", getFakeCCDetails());
			customerId = JSONValueFinder.getJsonValue(resp.message, "CustomerID");
			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			JSONObject json = createSubscriptionJsonObject(data, new JSONObject());	
			ResponseBean response = HttpService.post(url, headers, json.toString());
			JSONObject respJson = new JSONObject(response.message);

			if(data.get("ResponseMessage").equalsIgnoreCase(""))  //Positive scenarios (where ResonseCode and ResponseMessage are BLANK in the excel sheet)
			{
				//Get customer id and subscription id from subscription response message 
				JSONObject custID = (JSONObject) respJson.get("customer");
				customerId = custID.get("id").toString();
				JSONObject SubID = (JSONObject) respJson.get("subscription");
				subscriptionId = SubID.get("id").toString();

				//Assets subscription id is same in subscription response message and subscription table
				String query_session = "select * from [Ecommerce].[dbo].[Subscription] where customerid = "+ customerId;
				List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
				Map tmpData_session = list.get(1);
				assertEquals(tmpData_session.get("SubscriptionID").toString(), subscriptionId, "subscriptionId Mismatch");

				//Assert status and mode 
				assertJsonValueEquality(respJson, "status", "SUCC");
				assertJsonValueEquality(respJson, "mode", "DEMO");
				
				//Gateway Check 
				verifyTransaction(respJson, "SALE", false);
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

	//Test that subscription can not be created for existing customer when invalid credit card details are provided
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "getCreditCardData")
	public void createSubscriptionsForExistingCustomerWithInvalidCC(HashMap<String, String> ccDetails) 
	{
		try 
		{
			ResponseBean resp = createSubscriptionBySkuId("10117", 1.95, "LCAUS", getFakeCCDetails());
			customerId = JSONValueFinder.getJsonValue(resp.message, "CustomerID");
			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			JSONObject ccJson = getInvalidCCJson(ccDetails);
			JSONObject json = createSubscriptionJsonObjectWithInvalidCC(ccJson);
			ResponseBean response = HttpService.post(url, headers, json.toString());
			JSONObject respJson = new JSONObject(response.message);

			if(ccDetails.get("FailCode").equalsIgnoreCase("")) //Scenarios (where FailCode is BLANK in the excel sheet)
			{
				assertTrue(Integer.parseInt(ccDetails.get("ResponseCode")), response.code, ccDetails.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch");
				if(!response.message.contains("ModelState"))
					assertEquals(ccDetails.get("ResponseMessage"), response.message, ccDetails.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
			}
			else //Scenarios (where FailCode is mentioned in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, ccDetails.get("TestCaseName") + " test got failed. Failure reason :Getting NULL as response");
				assertJsonValueEquality(respJson, "status", "FAIL");
				assertJsonValueEquality(respJson, "mode", "DEMO");
				assertJsonValueEquality(respJson, "fail_code", ccDetails.get("FailCode"));
				assertJsonValueEquality(respJson, "fail_message", ccDetails.get("FailMessage"));
			}			
		} 
		catch (Exception e) 
		{
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test subscription creation for existing customers when credit card details are not passed 
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "getSubscriptionWECDataWithoutCC")
	public void createSubscriptionsForExistingCustomerWithoutCC(HashMap<String, String> data) 
	{
		try 
		{
			ResponseBean resp = createSubscriptionBySkuId("10117", 1.95, "LCAUS", getFakeCCDetails());
			customerId = JSONValueFinder.getJsonValue(resp.message, "CustomerID");
			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			JSONObject json = getSubscriptionJsonBySkuId(data.get("sku_id"), Double.parseDouble(data.get("amount")));			
			json.put("credit_card", JSONObject.NULL);
			ResponseBean response = HttpService.post(url, headers, json.toString());
			JSONObject respJson = new JSONObject(response.message);

			if(data.get("ResponseMessage").equalsIgnoreCase("")) //Positive scenarios (Where ResponseCode and ResponseMessage are BLANK in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, "Getting NULL as response");
				assertJsonValueEquality(respJson, "status", "SUCC");
				assertJsonValueEquality(respJson, "mode", "DEMO");
				
				//Gateway Check 
				verifyTransaction(respJson, "SALE", false);
			}
			else  //Negative scenarios (using invalid values for parameters)
			{
				assertTrue(Integer.parseInt(data.get("ResponseCode")), response.code, data.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch");
				assertEquals(data.get("ResponseMessage"), response.message, data.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
			}
		} 
		catch (Exception e) 
		{
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	//create subscription with Auth amount
	@Test
	public void createSubscriptionWithAuthAmount()
	{
		try 
		{
			verifyDefaultAuthAmount("WCRD", "1.0000");
			verifyDefaultAuthAmount("ADYN", "0.0000");
		
			String query_skuId = "Select top 1* from [Ecommerce].[dbo].[Plan] where Amount > 0 and FrequencyInterval > 0 and (Cycles is Null or Cycles > 0 )and TrialAmount = 0 and TrialSplitAmount = 0 and TrialDays > 0 and ClientCD = 'LCAUS'";
			Map skuID = DbUtil.getInstance("Ecommerce").getResultSet(query_skuId);
			
			ResponseBean response = createSubscriptionBySkuId(skuID.get("ClientSKUID").toString(), 0, "LCAUS", getFakeCCDetails());
			
			System.out.println("Response: " + response.message);
			
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");
			
			String result = respJson.getJSONArray("transaction").getJSONObject(0).getString("result").toString();
			String transaction_type = respJson.getJSONArray("transaction").getJSONObject(0).getString("transaction_type").toString();
			String amount = respJson.getJSONArray("transaction").getJSONObject(0).getString("amount").toString();
			String gateway = respJson.getJSONArray("transaction").getJSONObject(0).getString("gateway").toString();
			String gateway_reference = respJson.getJSONArray("transaction").getJSONObject(0).getString("gateway_reference").toString();
			assertEquals(result, "SUCC", "transaction result not succ");
			assertEquals("AUTH", transaction_type, "Transaction type mismatch, expected : AUTH and actual " + transaction_type);
			
			if(wcrdWeight == 100 && adynWeight == 0)
			{
				assertEquals("1.0", amount, "amount mismatch, expected : 1 and actual " + amount);
				assertEquals("WCRD", gateway, "Gateway mismatch in transaction row, expecetd : WCRD but actual " +gateway);
				assertTrue(!gateway_reference.matches("^[0-9]{16}$"), "gateway_reference is not of WCRD in transaction row");
			}
			else if (wcrdWeight == 0 && adynWeight == 100)
			{
				assertEquals("0.0", amount, "amount mismatch, expected : 0 and actual " + amount);
				assertEquals("ADYN", gateway, "Gateway mismatch in transaction row, expecetd : ADYN but actual " +gateway);
				assertTrue(gateway_reference.matches("^[0-9]{16}$"), "gateway_reference is not of ADYN in transaction row");
			}
		} 
		catch (Exception e) 
		{
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

}
