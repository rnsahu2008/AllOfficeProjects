package com.lc.tests.ecom;

import static com.lc.constants.Constants_Ecom.BASE_URL;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
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
import com.lc.json.SubscriptionJson;
import com.lc.utils.HeaderManager;

public class LinkedSubscription extends EComCoreComponents
{
	String customerId = "";
	String transaction_type;
	String cuid = "";

	@BeforeMethod
	public void init()
	{
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	//Test Linked subscription charge now false with different - 2 sets of data
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "lsChargeNowFalse")
	public void linkedSubscriptionWithChargeNowFalse(HashMap<String, String> row) {
		try {
			linkedSubscription(row, "AUTH", false);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test Linked subscription charge now true with different - 2 sets of data
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "lsChargeNowTrue")
	public void linkedSubscriptionWithChargeNowTrue(HashMap<String, String> row) {
		try {
			linkedSubscription(row, "SALE", true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test that child subscription can be deactivated 
	@SuppressWarnings("rawtypes")
	@Test
	public void linkedSubsWithChildDeactivated() {
		try {
			cancelSubscription("10117", 1.0);
			JSONObject linkedSubs = new JSONObject();
			linkedSubs.put("link_type_cd", "CHAN");
			linkedSubs.put("link_to_subscription_id", Integer.parseInt(subscriptionId));

			String query_session = "select top (10) * from [Ecommerce].[dbo].[Plan] where ClientSKUID = '10034'";
			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
			Map tmpData_session = list.get(0);
			int planId = Integer.parseInt(tmpData_session.get("PlanID").toString());

			JSONObject json = SubscriptionJson.subscriptionData("10034", planId, null, 1.95, "RB 3.00 INIT IM TO 3.00 MNTH", cuid, "182.71.90.126", "rajan@livecareer.com", true, linkedSubs);
			json.put("credit_card", JSONObject.NULL);
			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.post(url, headers, json.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			JSONArray transactionArr = respJson.getJSONArray("transaction");
			assertTrue(transactionArr.length(), 1, "Issue with number of transactions");
			
			//Gateway Check 
			verifyTransaction(respJson, "SALE", false);
			
			//Getting child subscription id 
			subscriptionId = ((JSONObject)respJson.get("subscription")).get("id").toString(); 
			deactivateSubscription();			
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test that parent subscription can be deactivated 
	@SuppressWarnings("rawtypes")
	@Test
	public void linkedSubsWithParentDeactivated() {
		try {
			cancelSubscription("10117", 1.0);
			JSONObject linkedSubs = new JSONObject();
			linkedSubs.put("link_type_cd", "CHAN");
			linkedSubs.put("link_to_subscription_id", Integer.parseInt(subscriptionId));

			String query_session = "select top (10) * from [Ecommerce].[dbo].[Plan] where ClientSKUID = '10117'";
			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
			Map tmpData_session = list.get(0);
			int planId = Integer.parseInt(tmpData_session.get("PlanID").toString());

			JSONObject json = SubscriptionJson.subscriptionData("10117", planId, null, 1.95, "RB 2.95 INIT 3D TO 34.95 MNTH", cuid, "182.71.90.126", "rajan@livecareer.com", false, linkedSubs);
			json.put("credit_card", JSONObject.NULL);
			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.post(url, headers, json.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			
			//Gateway Check 
			JSONObject respJson = new JSONObject(response.message);
			verifyTransaction(respJson, "SALE", false);
			
			deactivateSubscription();
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test canceling of linked subscription 
	@SuppressWarnings("rawtypes")
	@Test(dataProviderClass=Ecom_DP.class, dataProvider="cancelLinkedSubscription")
	public void cancelLinkedSubscription(HashMap<String, String> excelDataRow)
	{
		try {
			String childSubscriptionId = "";
			if(excelDataRow.get("childSubscriptionId").equalsIgnoreCase(""))
				childSubscriptionId = linkedSubscription();
			else
				childSubscriptionId = excelDataRow.get("childSubscriptionId").equalsIgnoreCase("null") ? null : excelDataRow.get("childSubscriptionId");

			JSONObject cancelSub = new JSONObject();
			cancelSub.put("mode", excelDataRow.get("mode"));
			cancelSub.put("reason_cd", excelDataRow.get("reason_cd"));
			cancelSub.put("reason_message", excelDataRow.get("reason_message"));
			cancelSub.put("expiry_date", getExpiryDate(excelDataRow.get("expiry_date")));			
			String url = BASE_URL + "/v1/subscriptions/" + childSubscriptionId + "/cancel";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");		
			ResponseBean response = HttpService.patch(url, headers, cancelSub.toString());
			System.out.println("Response: " + response.message);
			
			if(excelDataRow.get("ResponseMessage").equalsIgnoreCase(""))  //Positive scenarios (where ResonseCode and ResponseMessage are BLANK in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, "Getting NULL as response");
				JSONObject respJson = new JSONObject(response.message);
				JSONObject subscriptionJson = (JSONObject) respJson.get("Subscription");
				String child_subscription_status_cd = subscriptionJson.getString("subscription_status_cd");
				String next_billing_date = subscriptionJson.getString("next_billing_date");
				String parent_subscription_status = getSubcriptionStatusBySubscriptionId(subscriptionId);
				String query_session = "select * from SubscriptionEventProperty where SubscriptionEventID in (select SubscriptionEventID from SubscriptionEvent where SubscriptionID = "	+ childSubscriptionId + ") and PropertyCD = 'mode'";
				List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
				Map tmpData_session = list.get(0);
				int renewal_count = subscriptionJson.getInt("renewal_count");
				assertTrue(renewal_count, 0, excelDataRow.get("TestCaseName") + " test got failed. Failure reason : renewal_count not correct");
				assertEquals(child_subscription_status_cd, "EXPD", excelDataRow.get("TestCaseName") + " test got failed. Failure reason : child status not correct");
				assertEquals(parent_subscription_status, "EXPR", excelDataRow.get("TestCaseName") + " test got failed. Failure reason : parent status not correct");
				assertEquals(next_billing_date, "null", excelDataRow.get("TestCaseName") + " test got failed. Failure reason : Next billing date not correct");
				assertEquals(excelDataRow.get("mode"), tmpData_session.get("PropertyValue").toString(), excelDataRow.get("TestCaseName") + " test got failed. Failure reason : mode set in SubscriptionEventProperty table is incorrect");
			}
			else //Negative scenarios (using invalid values for parameters)
			{
				assertTrue(Integer.parseInt(excelDataRow.get("ResponseCode")), response.code, excelDataRow.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch");
				if(!response.message.contains("ModelState"))
				assertEquals(excelDataRow.get("ResponseMessage"), response.message, excelDataRow.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
			}

		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test that linked subscription can be deactivated
	@Test(dataProviderClass=Ecom_DP.class, dataProvider="getDeactivateSubscriptionData")
	public void deactivateLinkedSubscription(HashMap<String, String> excelDataRow)
	{
		try 
		{
			if(excelDataRow.get("SubscriptionId").equalsIgnoreCase(""))
				subscriptionId = linkedSubscription();
			else
				subscriptionId = excelDataRow.get("SubscriptionId").equalsIgnoreCase("null") ? null : excelDataRow.get("SubscriptionId");
			
			JSONObject json = new JSONObject();
			json.put("reason_cd", excelDataRow.get("reason_cd"));

			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/deactivate";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.patch(url, headers, json.toString());
			
			if(excelDataRow.get("ResponseMessage").equalsIgnoreCase(""))  //Positive scenarios (where ResonseCode and ResponseMessage are BLANK in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, "Getting NULL as response");
				JSONObject respJson = new JSONObject(response.message);
				assertEquals(respJson.get("EventStatus").toString(), "RFND", excelDataRow.get("TestCaseName") + " test got failed. Failure reason : EventStatus not correct");
				assertEquals(respJson.get("subscription_status_cd").toString(), "EXPD", excelDataRow.get("TestCaseName") + " test got failed. Failure reason : subscription_status_cd incorrect");
			}
			else  //Negative scenarios (using invalid values for parameters)
			{
				assertTrue(Integer.parseInt(excelDataRow.get("ResponseCode")), response.code, excelDataRow.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch");
				if(!response.message.contains("ModelState"))
				assertEquals(excelDataRow.get("ResponseMessage"), response.message, excelDataRow.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
			}
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	//Test linked subscription with invalid CC parameters
	@SuppressWarnings("rawtypes")
	@Test(dataProviderClass=Ecom_DP.class, dataProvider="creditCardLinkedSubscription")
	public void linkedSubscriptionWithInvalidCCDetails(HashMap<String, String> excelDataRow)
	{
		try {
			cancelSubscription("10117",1.95);
			JSONObject linkedSubs = new JSONObject();
			linkedSubs.put("link_type_cd", "CHAN");
			linkedSubs.put("link_to_subscription_id", Integer.parseInt(subscriptionId));

			String query_session = "select top (10) * from [Ecommerce].[dbo].[Plan] where ClientSKUID = '10023'";
			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
			Map tmpData_session = list.get(0);
			Integer planId = Integer.parseInt(tmpData_session.get("PlanID").toString());		

			JSONObject json = SubscriptionJson.subscriptionData("10023", planId, null, 95.4, "RB 2.95 INIT 3D TO 34.95 MNTH", cuid, "182.71.90.126", "rajan@livecareer.com", Boolean.parseBoolean(excelDataRow.get("charge_now")), linkedSubs);
			json.put("credit_card", getInvalidCCJson(excelDataRow));

			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.post(url, headers, json.toString());
			JSONObject respJson = new JSONObject(response.message);

			if(excelDataRow.get("FailCode").equalsIgnoreCase("")) //Scenarios (where FailCode is BLANK in the excel sheet)
			{
				assertTrue(Integer.parseInt(excelDataRow.get("ResponseCode")), response.code, excelDataRow.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch");
				if(!response.message.contains("ModelState"))
					assertEquals(excelDataRow.get("ResponseMessage"), response.message, excelDataRow.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
			}
			else //Scenarios (where FailCode is mentioned in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, excelDataRow.get("TestCaseName") + " test got failed. Failure reason :Getting NULL as response");
				assertJsonValueEquality(respJson, "status", "FAIL");
				assertJsonValueEquality(respJson, "mode", "DEMO");
				assertJsonValueEquality(respJson, "fail_code", excelDataRow.get("FailCode"));
				assertJsonValueEquality(respJson, "fail_message", excelDataRow.get("FailMessage"));
			}			

		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	//Test linked subscription without credit card details
	@SuppressWarnings("rawtypes")
	@Test(dataProviderClass=Ecom_DP.class, dataProvider="linkedSubsWithoutCC")
	public void linkedSubscriptionWithoutCC(HashMap<String, String> excelDataRow)
	{
		try {
			cancelSubscription("10117",1.95);
			JSONObject linkedSubs = new JSONObject();
			linkedSubs.put("link_type_cd", "CHAN");
			linkedSubs.put("link_to_subscription_id", Integer.parseInt(subscriptionId));

			String query_session = "select top (10) * from [Ecommerce].[dbo].[Plan] where ClientSKUID = '" + excelDataRow.get("sku_id")+"'";
			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
			Map tmpData_session = list.get(0);
			Integer planId = Integer.parseInt(tmpData_session.get("PlanID").toString());		

			JSONObject json = SubscriptionJson.subscriptionData(excelDataRow.get("sku_id"), planId, null, Double.parseDouble(excelDataRow.get("amount")), "RB 2.95 INIT 3D TO 34.95 MNTH", cuid, "182.71.90.126", "rajan@livecareer.com", Boolean.parseBoolean(excelDataRow.get("charge_now")), linkedSubs);
			json.put("credit_card", JSONObject.NULL);

			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.post(url, headers, json.toString());
			JSONObject respJson = new JSONObject(response.message);

			if(excelDataRow.get("ResponseMessage").equalsIgnoreCase("")) //Positive scenarios (Where ResponseCode and ResponseMessage are BLANK in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, "Getting NULL as response");
				assertJsonValueEquality(respJson, "status", "SUCC");
				assertJsonValueEquality(respJson, "mode", "DEMO");
				
				//Gateway Check 
				String transType = Boolean.parseBoolean(excelDataRow.get("charge_now")) ? "SALE" : "AUTH";
				verifyTransaction(respJson, transType, false);
			}
			else  //Negative scenarios (using invalid values for parameters)
			{
				assertTrue(Integer.parseInt(excelDataRow.get("ResponseCode")), response.code, excelDataRow.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch");
				assertEquals(excelDataRow.get("ResponseMessage"), response.message, excelDataRow.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
			}	

		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test utility method to deactivate subscription
	private void deactivateSubscription() {
		try {
			JSONObject json = new JSONObject();
			json.put("reason_cd", "RFND");
			json.put("reason_message", "api test");
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/deactivate";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.patch(url, headers, json.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertEquals(respJson.get("EventStatus").toString(), "RFND", "EventStatus not correct");
			assertEquals(respJson.get("subscription_status_cd").toString(), "EXPD", "subscription_status_cd incorrect");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
	}

	/**
	 * //Test utility method to create subscription
	 * @param skuId SKU ID
	 * @param amount of the plan selected
	 */
	private void subscribeWithCCDetails(String skuId, Double amount) {
		try {
			ResponseBean response = createSubscriptionBySkuId(skuId, amount, "LCAUS", getFakeCCDetails());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");
			JSONObject cc = (JSONObject) respJson.get("subscription");
			subscriptionId = cc.get("id").toString();
			customerId = cc.get("customer_id").toString();
			JSONObject cust = (JSONObject) respJson.get("customer");
			cuid = cust.get("cuid").toString();
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
	}

	/**
	 * Test utility method to cancel subscription
	 * @param skuId skuId SKU ID
	 * @param amount of the plan selected
	 */
	private void cancelSubscription(String skuId, Double amount) {
		try {			
			subscribeWithCCDetails(skuId, amount);
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/cancel";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			JSONObject cancelSub = new JSONObject();
			cancelSub.put("expiry_date", getExpiryDate("Today'sDate"));	
			ResponseBean response = HttpService.patch(url, headers, cancelSub.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			JSONObject subscriptionJson = (JSONObject) respJson.get("Subscription");
			String subscription_status_cd = subscriptionJson.get("subscription_status_cd").toString();
			String next_billing_date = subscriptionJson.get("next_billing_date").toString();
			int renewal_count = subscriptionJson.getInt("renewal_count");
			assertTrue(renewal_count, 0, "renewal_count not correct");
			assertEquals(subscription_status_cd, "EXPR", "Status not correct");
			assertEquals(next_billing_date, "null", "Next billing date not correct");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
	}

	/**
	 * Test utility method to parse String value to integer
	 * @param value value to be parsed
	 * @return parsed integer value
	 */
	private Integer parseValue(String value)
	{
		if(value.equalsIgnoreCase("null"))
			return null;
		else
			return Integer.parseInt(value);
	}

	/**
	 * Set credit card based on user input mentioned in excel data sheet
	 * @param value credit card mentioned in excel data sheet
	 * @param json JSON
	 * @throws JSONException
	 */
	private void setCreditCard(String value, JSONObject json) throws JSONException
	{
		switch(value)
		{
		case "FakeCC" :
			json.put("credit_card", getFakeCCDetails());
			break;
		case "FakeFailCC" :
			json.put("credit_card", getFakeFailCCDetails());
			break;
		default :
			json.put("credit_card", JSONObject.NULL);
			break;
		}
	}

	/**
	 * Test common method to create linked subscriptions and doing assertions 
	 * @param dataRow excel data row
	 * @param transactionType Transaction Type whether AUTH or SALE
	 * @param chargeNow set charge now depending on the requirement
	 * @throws JSONException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	private void linkedSubscription(HashMap<String, String> dataRow, String transactionType, Boolean chargeNow)
	{
		try {
			cancelSubscription(dataRow.get("parent_sku_id"), Double.parseDouble(dataRow.get("parent_amount")));
			String ipAddress = dataRow.get("child_ip_address").equalsIgnoreCase("") ? "182.71.90.126" : dataRow.get("ip_address");
			String startDate = dataRow.get("child_start_date").equalsIgnoreCase("") ? null : dataRow.get("child_start_date");

			Integer linkedSubscriptionId;
			if(dataRow.get("linked_subs_id").equalsIgnoreCase(""))
				linkedSubscriptionId = Integer.parseInt(subscriptionId);
			else
				linkedSubscriptionId = parseValue(dataRow.get("linked_subs_id"));

			Integer planId;
			if(dataRow.get("child_plan_id").equalsIgnoreCase(""))
			{
				String query_session = "select top (10) * from [Ecommerce].[dbo].[Plan] where ClientSKUID = '" + dataRow.get("child_sku_id")+ "'";
				List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
				Map tmpData_session = list.get(0);
				planId = Integer.parseInt(tmpData_session.get("PlanID").toString());			
			}
			else
				planId = parseValue(dataRow.get("child_plan_id"));

			JSONObject linkedSubs = new JSONObject();
			linkedSubs.put("link_type_cd", "CHAN");
			linkedSubs.put("link_to_subscription_id", linkedSubscriptionId);

			JSONObject json = SubscriptionJson.subscriptionData(dataRow.get("child_sku_id"), planId, startDate, Double.parseDouble(dataRow.get("child_amount")), "RB 2.95 INIT 3D TO 34.95 MNTH", cuid, ipAddress, "rajan@livecareer.com", chargeNow, linkedSubs);
			setCreditCard(dataRow.get("child_credit_card"), json);

			if(!dataRow.get("customer_id").equalsIgnoreCase(""))
			{
				customerId = dataRow.get("customer_id");
			}

			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.post(url, headers, json.toString());

			if(dataRow.get("ResponseMessage").equalsIgnoreCase(""))  //Positive scenarios (where ResonseCode and ResponseMessage are BLANK in the excel sheet)
			{
				checkSuccessCode(response.code);
				assertNotNull(response.message, "Getting NULL as response");
				JSONObject respJson = new JSONObject(response.message);

				//If credit card passed is null then transaction object will be empty in the response message, so no need to check transaction type
				if(!dataRow.get("child_credit_card").equalsIgnoreCase("nullCC"))
				{
					transaction_type = respJson.getJSONArray("transaction").getJSONObject(0).getString("transaction_type");
					assertEquals(transactionType, transaction_type, dataRow.get("TestCaseName") + " test got failed. Failure reason : transaction_type mismatch");
				}
				String subscription_status_cd = ((JSONObject)respJson.get("subscription")).get("subscription_status_cd").toString(); 
				assertEquals("PEND", subscription_status_cd, dataRow.get("TestCaseName") + " test got failed. Failure reason : subscription_status_cd mismatch");
				
				//Gateway Check 
				verifyTransaction(respJson, transactionType, false);
			}
			else //Negative scenarios (using invalid values for parameters)
			{
				assertTrue(Integer.parseInt(dataRow.get("ResponseCode")), response.code, dataRow.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch");
				if(!response.message.contains("ModelState"))
					assertEquals(dataRow.get("ResponseMessage"), response.message, dataRow.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
			}
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
	}

	/**
	 * Test utility method to create linked subscription
	 * @return linked subscription id
	 */
	@SuppressWarnings("rawtypes")
	private String linkedSubscription()
	{
		try {
				cancelSubscription("10117",1.95);
				JSONObject linkedSubs = new JSONObject();
				linkedSubs.put("link_type_cd", "CHAN");
				linkedSubs.put("link_to_subscription_id", Integer.parseInt(subscriptionId));

				String query_session = "select top (10) * from [Ecommerce].[dbo].[Plan] where ClientSKUID = '10023'";
				List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
				Map tmpData_session = list.get(0);
				Integer planId = Integer.parseInt(tmpData_session.get("PlanID").toString());		

				JSONObject json = SubscriptionJson.subscriptionData("10023", planId, null, 95.4, "RB 2.95 INIT 3D TO 34.95 MNTH", cuid, "182.71.90.126", "rajan@livecareer.com", false, linkedSubs);
				json.put("credit_card", getFakeCCDetails());

				String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
				HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
				ResponseBean response = HttpService.post(url, headers, json.toString());
				checkSuccessCode(response.code);
				assertNotNull(response.message, "Getting NULL as response");
				JSONObject respJson = new JSONObject(response.message);
				assertJsonValueEquality(respJson, "status", "SUCC");
				assertJsonValueEquality(respJson, "mode", "DEMO");
				
				//Gateway Check 
				verifyTransaction(respJson, "AUTH", false);
				
				JSONObject subscription = (JSONObject) respJson.get("subscription");
				return subscription.get("id").toString();

			} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}

		return null;
	}
	
}
