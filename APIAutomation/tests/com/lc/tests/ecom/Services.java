package com.lc.tests.ecom;
import static com.lc.constants.Constants_Ecom.BASE_URL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.EComCoreComponents;
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.json.SubscriptionJson;
import com.lc.utils.HeaderManager;
import com.lc.utils.ReadProperties;

public class Services extends EComCoreComponents {

	String customerId = "";
	String cuid = "";

	@BeforeMethod
	public void init() {
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	//This test is used to test both recurring and suspension services
	@Test
	public void resumeSubscription()
	{
		String baseUrl = ReadProperties.getbaseURL();
		//Since recurring services are not setup on DEV environment so resume subscription can not be tested on Dev. It can only be tested on Regression and Sandbox.
		//Hence skip test when tests run on Dev environment
		if(baseUrl.contains("dev5"))
			throw new SkipException("Skipping this exception");

		try
		{
			suspendSubscription();

			//Run Notification Service - Notification service will run only on SANDBOX
			if(baseUrl.contains("sandbox"))
			{
				String notificationService_query = "Update [Ecommerce].[dbo].[Service] Set NextRun = GetDate() -1 where ServiceCD = 'SUBN'";
				DbUtil.getInstance("Ecommerce").executeNonQuery(notificationService_query);

				String subsNotification_query = "Select StatusCD from [Ecommerce].[dbo].[SubscriptionNotification] where SubscriptionId = "+ subscriptionId;
				String subsStatus = getActualValueAfterRetry(subsNotification_query, "StatusCD", "SUCC");
				
				assertEquals("SUCC", subsStatus, "Subscription Notification Status mismatch, expected : SUCC");
			}

			//Resume subscription
			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions/" + subscriptionId + "/resume";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			JSONObject resumeObj = new JSONObject();
			resumeObj.put("credit_card", getFakeCCDetails());
			resumeObj.put("amount", 59.40);
			resumeObj.put("descriptor", "RWZ 95.40 Annual");
			resumeObj.put("ip_address", "182.71.90.126");
			ResponseBean response = HttpService.post(url, headers, resumeObj.toString());
			System.out.println("Response: " + response.message);	
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");
			JSONObject subscription = respJson.getJSONObject("subscription");
			assertEquals("ACTV", subscription.get("subscription_status_cd").toString(), "Subscription status mismatch, expected : ACTV");
			JSONArray transaction = respJson.getJSONArray("transaction");
			assertEquals("59.4", transaction.getJSONObject(0).getString("amount"), "Subscription Amount mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void expiredServiceWithChargeNowFalse()
	{
		String baseUrl = ReadProperties.getbaseURL();
		//Since recurring services are not setup on DEV environment so resume subscription can not be tested on Dev. It can only be tested on Regression and Sandbox.
		//Hence skip test when tests run on Dev environment
		if(baseUrl.contains("dev5"))
			throw new SkipException("Skipping this exception");

		try
		{
			//create linked subscription
			Integer parentSubscriptionID = cancelSubscription("10117",1.95);
			JSONObject linkedSubs = new JSONObject();
			linkedSubs.put("link_type_cd", "CHAN");
			linkedSubs.put("link_to_subscription_id", Integer.parseInt(subscriptionId));

			Integer childPlanID = getPlanID("10004");	

			JSONObject json = SubscriptionJson.subscriptionData("10004", childPlanID, null, 59.95, "RB 2.95 INIT 3D TO 34.95 MNTH", cuid, "182.71.90.126", "rajan@livecareer.com", false, linkedSubs);
			json.put("credit_card", getFakeCCDetails());

			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.post(url, headers, json.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");
			JSONObject subscription = (JSONObject) respJson.get("subscription");
			Integer childSubscriptionID = subscription.getInt("id");

			//Verify Parent Subscription Status is EXPR and Child Subscription Status is PEND	
			assertEquals("EXPR", getSubcriptionStatusBySubscriptionId(parentSubscriptionID.toString()), "Parent Subscription Status Initial Mismatch");
			assertEquals("PEND", getSubcriptionStatusBySubscriptionId(childSubscriptionID.toString()), "Child Subscription Status Initial Mismatch");

			//Linked sub chargeNow_false   PEND Sub to start 
			String billingDate_query = "Update [Ecommerce].[dbo].[Subscription] Set NextBillingDate = GetDate() -1 , StartDate = GetDate() -1 where SubscriptionId = "+ childSubscriptionID;
			DbUtil.getInstance("Ecommerce").executeNonQuery(billingDate_query);

			//To set parent Subscription from EXPR to EXPD
			billingDate_query = "Update [Ecommerce].[dbo].[Subscription] Set ExpiryDate  = GetDate() -1 where SubscriptionId = "+ parentSubscriptionID;
			DbUtil.getInstance("Ecommerce").executeNonQuery(billingDate_query);

			DbUtil.getInstance("Ecommerce").executeStoredProcedure("dbo.sp_ExpireSubscription");

			//Run Recurring services
			String service_query = "Update [Ecommerce].[dbo].[Service] Set NextRun = GetDate() -1 where ServiceCD = 'RCRG'";
			DbUtil.getInstance("Ecommerce").executeNonQuery(service_query);

			String parent_subs_query = "select SubscriptionStatusCD from [Ecommerce].[dbo].[Subscription] where SubscriptionId = "+ parentSubscriptionID;
			List<Map> list_parent = DbUtil.getInstance("Ecommerce").getResultSetList(parent_subs_query);
			Map tmpData_session_parent = list_parent.get(0);

			String child_subs_query = "select SubscriptionStatusCD from [Ecommerce].[dbo].[Subscription] where SubscriptionId = "+ childSubscriptionID;
			List<Map> list_child = DbUtil.getInstance("Ecommerce").getResultSetList(child_subs_query);
			Map tmpData_session_child = list_child.get(0);

			for (int i = 0; i< 5; i++)
			{
				list_parent = DbUtil.getInstance("Ecommerce").getResultSetList(parent_subs_query);
				tmpData_session_parent = list_parent.get(0);
				list_child = DbUtil.getInstance("Ecommerce").getResultSetList(child_subs_query);
				tmpData_session_child = list_child.get(0);
				if(tmpData_session_parent.get("SubscriptionStatusCD").toString().equalsIgnoreCase("EXPD") && 
						tmpData_session_child.get("SubscriptionStatusCD").toString().equalsIgnoreCase("ACTV"))
					break;
				Thread.sleep(2000);
			}

			//Verify Parent Subscription Status is EXPD and Child Subscription Status is ACTV	
			assertEquals("EXPD", getSubcriptionStatusBySubscriptionId(parentSubscriptionID.toString()), "Parent Subscription Status Final Mismatch");
			assertEquals("ACTV", getSubcriptionStatusBySubscriptionId(childSubscriptionID.toString()), "Child Subscription Status Final Mismatch");

			//To set renewal count to 1
			billingDate_query = "Update [Ecommerce].[dbo].[Subscription] Set NextBillingDate = GetDate() -1 where SubscriptionId = "+ childSubscriptionID;
			DbUtil.getInstance("Ecommerce").executeNonQuery(billingDate_query);

			//Run Recurring services
			service_query = "Update [Ecommerce].[dbo].[Service] Set NextRun = GetDate() -1 where ServiceCD = 'RCRG'";
			DbUtil.getInstance("Ecommerce").executeNonQuery(service_query);

			String subsRetry_query = "select RenewalCount from [Ecommerce].[dbo].[Subscription] where SubscriptionId = "+ childSubscriptionID;
			String actualRenewalCount = getActualValueAfterRetry(subsRetry_query, "RenewalCount", "1");
			assertEquals("1", actualRenewalCount, "Renewal Count mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test
	public void expiredServiceWithChargeNowTrue()
	{
		String baseUrl = ReadProperties.getbaseURL();
		//Since recurring services are not setup on DEV environment so resume subscription can not be tested on Dev. It can only be tested on Regression and Sandbox.
		//Hence skip test when tests run on Dev environment
		if(baseUrl.contains("dev5"))
			throw new SkipException("Skipping this exception");

		try
		{
			//create linked subscription
			Integer parentSubscriptionID = cancelSubscription("10117",1.95);
			JSONObject linkedSubs = new JSONObject();
			linkedSubs.put("link_type_cd", "CHAN");
			linkedSubs.put("link_to_subscription_id", Integer.parseInt(subscriptionId));

			Integer childPlanID = getPlanID("10004");	

			JSONObject json = SubscriptionJson.subscriptionData("10004", childPlanID, null, 59.95, "RB 2.95 INIT 3D TO 34.95 MNTH", cuid, "182.71.90.126", "rajan@livecareer.com", true, linkedSubs);
			json.put("credit_card", getFakeCCDetails());

			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.post(url, headers, json.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");
			JSONObject subscription = (JSONObject) respJson.get("subscription");
			Integer childSubscriptionID = subscription.getInt("id");

			//Verify Parent Subscription Status is EXPR and Child Subscription Status is PEND	
			assertEquals("EXPR", getSubcriptionStatusBySubscriptionId(parentSubscriptionID.toString()), "Parent Subscription Status Mismatch");
			assertEquals("PEND", getSubcriptionStatusBySubscriptionId(childSubscriptionID.toString()), "Child Subscription Status Mismatch");

			//Linked sub chargeNow_true   PEND Sub to start 
			String billingDate_query = "Update [Ecommerce].[dbo].[Subscription] Set StartDate = GetDate() -1 where SubscriptionId = "+ childSubscriptionID;
			DbUtil.getInstance("Ecommerce").executeNonQuery(billingDate_query);

			//Run Recurring services
			String service_query = "Update [Ecommerce].[dbo].[Service] Set NextRun = GetDate() -1 where ServiceCD = 'RCRG'";
			DbUtil.getInstance("Ecommerce").executeNonQuery(service_query);

			//Check SBST status
			String subsEvent_query = "select * from [Ecommerce].[dbo].[SubscriptionEvent] where SubscriptionId = "+ childSubscriptionID + "order by 6 desc";
			String actualEventCD = getActualValueAfterRetry(subsEvent_query, "EventCD", "SBST");
			
			assertEquals("SBST", actualEventCD, "EventCD Mismatch");

			//Update NextBillingDate
			billingDate_query = "Update [Ecommerce].[dbo].[Subscription] Set NextBillingDate = GetDate() -1 where SubscriptionId = "+ childSubscriptionID;
			DbUtil.getInstance("Ecommerce").executeNonQuery(billingDate_query);

			//Run Recurring services
			service_query = "Update [Ecommerce].[dbo].[Service] Set NextRun = GetDate() -1 where ServiceCD = 'RCRG'";
			DbUtil.getInstance("Ecommerce").executeNonQuery(service_query);

			String child_subs_query = "select SubscriptionStatusCD from [Ecommerce].[dbo].[Subscription] where SubscriptionId = "+ childSubscriptionID;
			String actualSubsStatus = getActualValueAfterRetry(child_subs_query, "SubscriptionStatusCD", "ACTV");
			
			//Verify Child Subscription Status is ACTV	
			assertEquals("ACTV", actualSubsStatus, "Child Subscription Status Mismatch");

			String subsRetry_query = "select RenewalCount from [Ecommerce].[dbo].[Subscription] where SubscriptionId = "+ childSubscriptionID;
			String actualRenewalCount = getActualValueAfterRetry(subsRetry_query, "RenewalCount", "1");

			assertEquals("1", actualRenewalCount, "Renewal Count mismatch");

			//To set parent Subscription from EXPR to EXPD
			billingDate_query = "Update [Ecommerce].[dbo].[Subscription] Set ExpiryDate  = GetDate() -1 where SubscriptionId = "+ parentSubscriptionID;
			DbUtil.getInstance("Ecommerce").executeNonQuery(billingDate_query);		

			DbUtil.getInstance("Ecommerce").executeStoredProcedure("dbo.sp_ExpireSubscription");

			//Verify Parent Subscription Status is EXPD
			assertEquals("EXPD", getSubcriptionStatusBySubscriptionId(parentSubscriptionID.toString()), "Parent Subscription Status Mismatch");

			//Run Notification Service - Notification service will run only on SANDBOX
			if(baseUrl.contains("sandbox"))
			{
				String notificationService_query = "Update [Ecommerce].[dbo].[Service] Set NextRun = GetDate() -1 where ServiceCD = 'SUBN'";
				DbUtil.getInstance("Ecommerce").executeNonQuery(notificationService_query);

				String subsNotification_query_child = "Select StatusCD from [Ecommerce].[dbo].[SubscriptionNotification] where SubscriptionId = "+ childSubscriptionID;
				String childSubsStatus = getActualValueAfterRetry(subsNotification_query_child, "StatusCD", "SUCC");
				
				assertEquals("SUCC", childSubsStatus, "Child Subscription Notification mismatch");

				String subsNotification_query_parent = "Select StatusCD from [Ecommerce].[dbo].[SubscriptionNotification] where SubscriptionId = "+ parentSubscriptionID;
				String parentSubsStatus = getActualValueAfterRetry(subsNotification_query_parent, "StatusCD", "SUCC");
				
				assertEquals("SUCC", parentSubsStatus, "Parent Subscription Notification mismatch");
			}
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	/**
	 * Test utility method to suspend subscription
	 */
	@SuppressWarnings("rawtypes")
	private void suspendSubscription()
	{
		try
		{
			subscriptionId = createSampleSubscription();

			String subs_query = "select * from [Ecommerce].[dbo].[Subscription] where SubscriptionId = "+ subscriptionId;
			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(subs_query);
			Map tmpData_session = list.get(0);
			customerId = tmpData_session.get("CustomerID").toString();

			String billingDate_query = "Update [Ecommerce].[dbo].[Subscription] Set NextBillingDate = GetDate() -1 , Amount = 1300 where SubscriptionId = "+ subscriptionId;
			DbUtil.getInstance("Ecommerce").executeNonQuery(billingDate_query);

			String service_query = "Update [Ecommerce].[dbo].[Service] Set NextRun = GetDate() -1 where ServiceCD = 'RCRG'";
			DbUtil.getInstance("Ecommerce").executeNonQuery(service_query);

			//Recurring and Retry Status check
			String actualRetryStatus = getActualValueAfterRetry(subs_query, "SubscriptionStatusCD", "RTRY");
			
			assertEquals("RTRY", actualRetryStatus, "Subscription status mismatch, expected : RTRY");	

			String subsRetry_query = "select * from [Ecommerce].[dbo].[SubscriptionRetry] where SubscriptionId = "+ subscriptionId;
			list = DbUtil.getInstance("Ecommerce").getResultSetList(subsRetry_query);
			tmpData_session = list.get(0);
			assertEquals("0", tmpData_session.get("FailCount").toString(), "Faild Count mismatch");		

			String retry_query = "Update [Ecommerce].[dbo].[SubscriptionRetry] Set NextRetryDate = GetDate() -1 where SubscriptionId = "+ subscriptionId;

			verifyFailCount("1", retry_query, service_query, subsRetry_query);
			verifyFailCount("2", retry_query, service_query, subsRetry_query);
			verifyFailCount("3", retry_query, service_query, subsRetry_query);
			verifyFailCount("4", retry_query, service_query, subsRetry_query);
			verifyFailCount("5", retry_query, service_query, subsRetry_query);
			verifyFailCount("6", retry_query, service_query, subsRetry_query);
			verifyFailCount("7", retry_query, service_query, subsRetry_query);
			verifyFailCount("8", retry_query, service_query, subsRetry_query);

			String actualSubsStatus = getActualValueAfterRetry(subs_query, "SubscriptionStatusCD", "SUSP");
			
			assertEquals("SUSP", actualSubsStatus, "Subscription status mismatch, expected : SUSP");
			assertTrue(tmpData_session.get("NextBillingDate") == null, "NextBillingDate mismatch");
			assertTrue(tmpData_session.get("ExpiryDate") == null, "ExpiryDate mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test utility method to verify retry fail count
	 * @param expectedFailCount expected fail count
	 * @param retry_query suscription retry select query
	 * @param service_query service select query 
	 * @param subsRetry_query subscription retry update query
	 */
	private void verifyFailCount(String expectedFailCount, String retry_query, String service_query, String subsRetry_query)
	{
		try
		{
			DbUtil.getInstance("Ecommerce").executeNonQuery(retry_query);
			DbUtil.getInstance("Ecommerce").executeNonQuery(service_query);		

			String actualFailCount = getActualValueAfterRetry(subsRetry_query, "FailCount", expectedFailCount);
			assertEquals(expectedFailCount, actualFailCount, "Fail Count mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * //Test utility method to create subscription
	 * @param skuId SKU ID
	 * @param amount of the plan selected
	 */
	private Integer subscribeWithCCDetails(String skuId, Double amount) {
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
			JSONObject subscription = (JSONObject) respJson.get("subscription");
			return subscription.getInt("id");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		return 0;
	}

	/**
	 * Test utility method to cancel subscription
	 * @param skuId skuId SKU ID
	 * @param amount of the plan selected
	 */
	private Integer cancelSubscription(String skuId, Double amount) {
		Integer parentSubscriptionID = subscribeWithCCDetails(skuId, amount);
		try {			
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
		return parentSubscriptionID;
	}

	@SuppressWarnings("rawtypes")
	private String getActualValueAfterRetry(String query, String key, String value)
	{
		String actualValue = "";
		try
		{
			for (int i = 0; i< 5; i++)
			{
				List<Map >list = DbUtil.getInstance("Ecommerce").getResultSetList(query);
				Map tmpData_session = list.get(0);
				actualValue = tmpData_session.get(key).toString();
				if(actualValue.equalsIgnoreCase(value))
					break;
				Thread.sleep(2000);
			}
		}catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();		
		}
		return value;

	}
}
