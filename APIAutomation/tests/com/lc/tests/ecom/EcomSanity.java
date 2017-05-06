package com.lc.tests.ecom;

import static com.lc.constants.Constants_Ecom.BASE_URL;
import static com.lc.constants.Constants_Ecom.ECOMAUTH_URL;
import static com.lc.constants.Constants_Ecom.PAYMENT_URL;
import static com.lc.constants.Constants_Ecom.REFUND_DECLINE_URL;
import static com.lc.constants.Constants_Ecom.REFUND_PROCESS_URL;
import static com.lc.constants.Constants_Ecom.REFUND_UNDO_URL;
import static com.lc.constants.Constants_Ecom.SUBSCRIBE_URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.lc.assertion.AssertionUtil;
import com.lc.common.Common;
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.json.CreditCardJson;
import com.lc.json.PaymentJson;
import com.lc.json.SubscriptionJson;
import com.lc.utils.HeaderManager;
import com.lc.utils.ReadProperties;

public class EcomSanity extends AssertionUtil
{
	private static String access_token = "";	
	private String client_cd = "ADMEU"; 
	private HashMap<String, String> headers = null;
	private String subscription_Id = "";
	String currency = "";

	@Parameters(value = { "Environment", "ClientCD" })
	@BeforeTest //String env, String clientCD
	public void setupEnvironment(){
		ReadProperties.setupEnvironmentProperties("REG","res/ecomdata/config.json");
		//client_cd = clientCD;
		System.out.println("Executing Test suite for ClientCD : " + client_cd);
		generateAccessToken();
		if(client_cd.toLowerCase().equalsIgnoreCase("ADMUS"))
			headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
		else if(client_cd.toLowerCase().equalsIgnoreCase("ADMEU"))
			headers = HeaderManager.getHeaders("LCAES", access_token, "1234");
		else
			headers = HeaderManager.getHeaders(client_cd, access_token, "1234");
		HashMap<String, String> data = getSkuID();
		String skuId = data.get("parent_skuid");
		String query = "select CurrencyCD from [Ecommerce].[dbo].[Plan] where ClientSKUID = '" + skuId + "'";
		HashMap<String, String> hMap = DbUtil.getInstance("Ecommerce").getResultSet(query); 
		currency = hMap.get("CurrencyCD");
	}

	//generate access token based on client cd and client secret
	private void generateAccessToken(){
		try {
			String dataToPost = "grant_type=client_credentials&client_id=" + client_cd + "&client_secret=" + getClientSecret() + "&response_type=token:";
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD", client_cd);
			ResponseBean response = HttpService.post(ECOMAUTH_URL, headers, dataToPost);
			checkSuccessCode(response.code);
			String respString = response.message;
			JSONObject respJson = new JSONObject(respString);
			assertEquals(respJson.getString("token_type"), "bearer", "Issue with getting token_type");
			assertNotNull(respJson.getString("access_token"), "Issue with getting access token");
			assertNotNull(respJson.getString("refresh_token"), "Issue with getting refresh_token");
			assertNotNull(respJson.getString("expires_in"), "Issue with getting expires_in");
			access_token = "Bearer "+respJson.get("access_token");
			if(StringUtils.isEmpty(access_token)){
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	//get client secret based on clientCD
	private String getClientSecret()
	{
		switch(client_cd.toUpperCase())
		{
		case "LCAUS" :
			return "bGl2ZWNhcmVlciByb2NrcyE=";
		case "MPRUS" :
			return "bGl2ZWNhcmVlciByb2NrcyE=";
		case "LCAFR" :
			return "TmkAEvFVF0OFeUCI1av5nQ==";
		case "LCAES" :
			return "TmkAEvFVF0OFeUCI1av5nQ==";
		case "ADMUS" :
			return "bGl2ZWNhcmVlciByb2NrcyE=";
		case "ADMEU" :
			return "TmkAEvFVF0OFeUCI1av5nQ==";
		case "JEMPD" :
			return "bGl2ZWNhcmVlciByb2NrcyE=";
		case "MPIUS" :
			return "anJkdGF0YTI5MDcxOTA0";
		case "RHWEB" :
			return "dXAgdXAgYW5kIGF3YXk=";
		}
		return "";
	}

	//get skuid based on clientCD
	private HashMap<String, String> getSkuID()
	{
		HashMap<String, String> testData = new HashMap<String, String>();
		switch(client_cd.toUpperCase())
		{
			case "LCAUS" :
			{
				testData.put("parent_skuid", "10003");
				testData.put("child_skuid", "10117");
				return testData;
			}
			case "MPRUS" :
			{
				testData.put("parent_skuid", "10664");
				testData.put("child_skuid", "10663");
				return testData;
			}
			case "LCAFR" :
			{
				testData.put("parent_skuid", "10435");
				testData.put("child_skuid", "10330");
				return testData;
			}
			case "LCAES" :
			{
				testData.put("parent_skuid", "10329");
				testData.put("child_skuid", "10249");
				return testData;
			}
			case "ADMUS" :
			{
				testData.put("parent_skuid", "10003");
				testData.put("child_skuid", "10117");
				return testData;
			}
			case "ADMEU" :
			{
				testData.put("parent_skuid", "10329");
				testData.put("child_skuid", "10249");
				return testData;
			}
			case "JEMPD" :
			{
				testData.put("parent_skuid", "MR-US-S-M-50");
				testData.put("child_skuid", "MR-US-S-M-03");
				return testData;
			}
			case "MPIUS" :
			{
				testData.put("parent_skuid", "10669");
				testData.put("child_skuid", "10669");
				return testData;
			}
			case "RHWEB" :
			{
				testData.put("parent_skuid", "18");
				testData.put("child_skuid", "22");
				return testData;
			}
		}
		return null;
	}

	private JSONObject getFakeCCDetails(){
		return CreditCardJson.ccData(0, 0, "livecareer", "VISA", "5424000000100015", 2016, 06, 200, "542400", 
				"2015-03-24T15:57:05.1379094+05:30", "2015-03-24T15:57:05.1539163+05:30");
	}

	private ResponseBean getPaymentResponse(JSONObject json, List<JSONObject> payments, double amount)
	{
		ResponseBean response = null;
		try
		{
			JSONArray paymentArr = new JSONArray();
			Iterator<JSONObject> iterator = payments.iterator();

			while(iterator.hasNext())
			{
				paymentArr.put(iterator.next());
			}

			json.put("email", "reg_lc_n_27jan@livecareer.com");
			json.put("payment", paymentArr);
			json.put("credit_card", getFakeCCDetails());
			json.put("amount", amount);
			json.put("currency", currency);
			json.put("cuid", "69901642");
			json.put("ip_address", "182.71.90.126");
			response = HttpService.post(BASE_URL + PAYMENT_URL, headers, json.toString());
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * This method is used for creating data to post for creating payment and asserting actual and expected result
	 * @param instance of JSONObject
	 * @param instance of JSONObject
	 * @param list of Payment objects
	 * @param toatl amount of the plans
	 * @param database check 
	 */
	private void TestCommon(JSONObject json, List<JSONObject> payments, double amount, boolean dbCheck)
	{
		try
		{
			ResponseBean response = getPaymentResponse(json, payments, amount);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");

			// DB Check
			if(dbCheck)
			{
				JSONObject custID = (JSONObject) respJson.get("customer");
				String customerId = custID.get("id").toString();
				JSONArray transactionArr = (JSONArray) respJson.get("transaction");
				String transactionId = transactionArr.getJSONObject(0).getString("id");
				String query_session = "select * from [Ecommerce].[dbo].[customer] where customerid = " + customerId;
				List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
				Map tmpData_session = list.get(0);
				assertEquals(tmpData_session.get("CustomerID").toString(), customerId, "customerId Mismatch");
				String query_session1 = "select * from [Ecommerce].[dbo].[transaction] where customerid = " + customerId;
				List<Map> list1 = DbUtil.getInstance("Ecommerce").getResultSetList(query_session1);
				Map tmpData_session1 = list1.get(0);
				assertEquals(tmpData_session1.get("TransactionID").toString(), transactionId, "transactionid Mismatch");
			}
		}
		catch(Exception e)
		{
			fail(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private void linkedSubscription(boolean chargeNow, String transactionType)
	{
		try {

			//Create parent subscription
			String[] strArr = createSampleSubscription();

			//cancel parent subscription
			String url = BASE_URL + "/v1/subscriptions/" + strArr[1] + "/cancel";	
			JSONObject cancelSub = new JSONObject();
			cancelSub.put("expiry_date", new Date());				
			ResponseBean response = HttpService.patch(url, headers, cancelSub.toString());			
			checkSuccessCode(response.code);

			//Create linked subscription
			JSONObject linkedSubs = new JSONObject();
			linkedSubs.put("link_type_cd", "CHAN");
			linkedSubs.put("link_to_subscription_id", Integer.parseInt(strArr[1]));

			HashMap<String, String> data = getSkuID();
			String childSkuID = data.get("child_skuid");
			String query_session = "select PlanID, TrialAmount,Description from [Ecommerce].[dbo].[Plan] where ClientSKUID = '" + childSkuID + "'";
			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
			Map tmpData_session = list.get(0);
			Integer planId = Integer.parseInt(tmpData_session.get("PlanID").toString());	
			Double amt = Double.parseDouble(tmpData_session.get("TrialAmount").toString());

			JSONObject json = SubscriptionJson.subscriptionData(childSkuID, planId, null, amt, tmpData_session.get("Description").toString(), "88503817", "182.71.90.126", "rajan@livecareer.com", chargeNow, linkedSubs);
			json.put("credit_card", getFakeCCDetails());

			url = BASE_URL + "/v1/customers/" + strArr[0] + "/subscriptions";

			response = HttpService.post(url, headers, json.toString());

			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);	

			String transaction_type = respJson.getJSONArray("transaction").getJSONObject(0).getString("transaction_type");
			String subscription_status_cd = ((JSONObject)respJson.get("subscription")).get("subscription_status_cd").toString(); 
			assertEquals(transactionType, transaction_type, "transaction_type mismatch");
			assertEquals("PEND", subscription_status_cd, "subscription_status_cd mismatch");

			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
	}

	/**
	 * Test utility method to post the refund request and assert expected values against actual 
	 * @param url complete url of the resource
	 * @param amount refund amount 
	 * @throws JSONException
	 * @throws IOException
	 */
	private void TestCommon(String url, Double amount) throws JSONException, IOException
	{
		JSONObject json = new JSONObject();
		json.put("reason_cd", "RFND");
		json.put("reason_message", "api test");
		json.put("amount", amount);

		ResponseBean response = HttpService.post(url, headers, json.toString());

		checkSuccessCode(response.code);
		assertNotNull(response.message, "Getting NULL as response");
		JSONObject respJson = new JSONObject(response.message);

		assertJsonValueEquality(new JSONObject(response.message), "status", "SUCC");
		assertJsonValueEquality(new JSONObject(response.message), "mode", "DEMO");

		JSONObject transactionJson = (JSONObject) respJson.get("transaction");
		String result = transactionJson.getString("result").toString();
		assertEquals(result, "SUCC", "transaction not succ");
		String transaction_type = transactionJson.getString("transaction_type").toString();
		assertEquals(transaction_type, "RFND", "Refund done");
		Double actualAmount = transactionJson.getDouble("amount");
		assertEquals("-"+amount.toString(), actualAmount.toString(), "Refund amount mismatch");
	}

	//create payment with one plan two singles 
	private String[] createPaymentWithOnePLANAndTwoSNGL() {

		String[] strArr = null;

		try {
			JSONObject json = new JSONObject();
			JSONArray paymentArr = new JSONArray();
			JSONObject payment1 = new JSONObject();
			JSONObject payment2 = new JSONObject();
			JSONObject payment3 = new JSONObject();
			
			HashMap<String, String> data = getSkuID();
			String childSkuID = data.get("child_skuid");
			String query_session = "select PlanID, TrialAmount,Description from [Ecommerce].[dbo].[Plan] where ClientSKUID = '" + childSkuID + "'";
			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
			Map tmpData_session = list.get(0);	
			Double amt = Double.parseDouble(tmpData_session.get("TrialAmount").toString());
			
			payment1 = PaymentJson.paymentData(childSkuID, 0, amt, amt, 1, tmpData_session.get("Description").toString(), JSONObject.NULL, "PLAN");
			payment2 = PaymentJson.paymentData("685795", 0, 10, 10, 1, "SNGL 1", JSONObject.NULL, "SNGL");
			payment3 = PaymentJson.paymentData("842754", 0, 10, 10, 1, "SNGL 2", JSONObject.NULL, "SNGL");
			paymentArr.put(payment1);
			paymentArr.put(payment2);
			paymentArr.put(payment3);
			json.put("email", "reg_lc_n_27jan@livecareer.com");
			json.put("payment", paymentArr);
			json.put("credit_card", getFakeCCDetails());
			json.put("amount", amt+20);
			json.put("currency", currency);
			json.put("cuid", "69901642");
			json.put("ip_address", "182.71.90.126");

			ResponseBean response = HttpService.post(BASE_URL + PAYMENT_URL, headers, json.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");
			JSONArray transactionArr = (JSONArray) respJson.get("transaction");
			String transactionId = transactionArr.getJSONObject(0).getString("id");
			JSONArray paymentItem = (JSONArray) transactionArr.getJSONObject(0).get("payment_item");
			String paymentitemId = paymentItem.getJSONObject(0).getString("id");
			strArr = new String[] {transactionId, paymentitemId};
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		return strArr;
	}

	//Test utility method to post refund request and assert expected against actual
	private Integer refundQueuePayment()
	{
		Integer refundrequestId = 0;
		try {
			String[] strArr = createPaymentWithOnePLANAndTwoSNGL();
			String url = BASE_URL + "/v1/paymentitems/" + strArr[1] + "/transactions/" + strArr[0] + "/refundqueue";
			JSONObject json = new JSONObject();
			json.put("amount", 0.10);
			ResponseBean response = HttpService.post(url, headers, json.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject refund_request = new JSONObject(response.message).getJSONObject("refund_request");
			assertEquals("INIT", refund_request, "status_CD");
			refundrequestId = refund_request.getInt("refund_request_id");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		return refundrequestId;
	}

	/**
	 * test utility method to post the refund request and assert expected values against actual 
	 * @param url complete url of the resource
	 * @return refunds JSONArray
	 * @throws NumberFormatException
	 * @throws JSONException
	 * @throws IOException
	 */
	private JSONArray refundJArray(String url) throws NumberFormatException, JSONException, IOException
	{
		JSONObject json = new JSONObject();
		JSONArray refund = new JSONArray();
		Integer refundrequestId = refundQueuePayment();
		JSONObject refund_request_id_json = new JSONObject();
		refund_request_id_json.put("refund_request_id", refundrequestId);
		refund_request_id_json.put("reason_code", "Valid");
		refund_request_id_json.put("amount", new Double("0.10"));
		refund_request_id_json.put("guid", "4f3e9e44-4c7f-11e5-9355-005056a96a54");
		refund.put(refund_request_id_json);
		json.put("refunds", refund);
		ResponseBean response = HttpService.post(url, headers, json.toString());
		checkSuccessCode(response.code);
		assertNotNull(response.message, "Getting NULL as response");
		JSONArray refunds_get = new JSONObject(response.message).getJSONArray("refunds");
		assertJsonValueEquality(refunds_get.getJSONObject(0), "status", "SUCC");

		return refunds_get;
	}

	private String[] createSampleSubscription()
	{	
		String[] strArr = null;
		try
		{	
			JSONObject json = new JSONObject();
			HashMap<String, String> hMap = new HashMap<String , String>();
			HashMap<String, String> data = getSkuID();
			String skuId = data.get("parent_skuid");
			String query = "select PlanID, TrialAmount,Description from [Ecommerce].[dbo].[Plan] where ClientSKUID = '" + skuId + "'";
			hMap = DbUtil.getInstance("Ecommerce").getResultSet(query); 
			json.put("sku_id", skuId);
			json.put("plan_id", Integer.parseInt(hMap.get("PlanID")));
			json.put("amount", Double.parseDouble(hMap.get("TrialAmount")));
			json.put("descriptor", hMap.get("Description"));
			json.put("start_date", JSONObject.NULL);
			json.put("cuid", "88503817");
			json.put("ip_address", "141.71.90.126");
			json.put("email", "rajan@livecareer.com");
			json.put("charge_now", JSONObject.NULL);
			json.put("linked_subscription", JSONObject.NULL);
			json.put("credit_card", getFakeCCDetails());	

			ResponseBean response = HttpService.post(BASE_URL + "/" + SUBSCRIBE_URL, headers, json.toString());

			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);

			//Get customer id, subscription id, transaction id, payment id from subscription response message 
			JSONObject customer = (JSONObject) respJson.get("customer");
			String customerId = customer.get("id").toString();

			JSONObject subscription = (JSONObject) respJson.get("subscription");
			String subscriptionId = subscription.get("id").toString();

			JSONObject transactionarray = (JSONObject) respJson.getJSONArray("transaction").getJSONObject(0);
			String transactionId = transactionarray.get("id").toString();

			String paymentitemId = transactionarray.getJSONArray("payment_item").getJSONObject(0).getString("id");

			strArr = new String[] {customerId, subscriptionId, transactionId, paymentitemId};
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		return strArr;
	}

	@Test(priority=1)
	public void createSubscription()
	{	
		try
		{	
			JSONObject json = new JSONObject();
			HashMap<String, String> hMap = new HashMap<String , String>();
			HashMap<String, String> data = getSkuID();
			String skuId = data.get("parent_skuid");
			String query = "select PlanID, TrialAmount, Description from [Ecommerce].[dbo].[Plan] where ClientSKUID = '" + skuId + "'";
			hMap = DbUtil.getInstance("Ecommerce").getResultSet(query); 
			json.put("sku_id", skuId);
			json.put("plan_id", Integer.parseInt(hMap.get("PlanID")));
			json.put("amount", Double.parseDouble(hMap.get("TrialAmount")));
			json.put("descriptor", hMap.get("Description"));
			json.put("start_date", JSONObject.NULL);
			json.put("cuid", "88503817");
			json.put("ip_address", "141.71.90.126");
			json.put("email", "rajan@livecareer.com");
			json.put("charge_now", JSONObject.NULL);
			json.put("linked_subscription", JSONObject.NULL);
			json.put("credit_card", getFakeCCDetails());	

			ResponseBean response = HttpService.post(BASE_URL + "/" + SUBSCRIBE_URL, headers, json.toString());

			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);

			//Get customer id and subscription id from subscription response message 
			JSONObject custID = (JSONObject) respJson.get("customer");
			String customerId = custID.get("id").toString();
			JSONObject SubID = (JSONObject) respJson.get("subscription");
			subscription_Id = SubID.get("id").toString();

			//Assets subscription id is same in subscription response message and subscription table
			String query_session = "select * from [Ecommerce].[dbo].[Subscription] where customerid = "+ customerId;
			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
			Map tmpData_session = list.get(0);
			assertEquals(tmpData_session.get("SubscriptionID").toString(), subscription_Id, "subscriptionId Mismatch");

			//Assert status and mode 
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");	
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(priority=2)
	public void cancelSubscription() 
	{
		try 
		{
			String url = BASE_URL + "/v1/subscriptions/" + subscription_Id + "/cancel";
			ResponseBean response = HttpService.patch(url, headers, null);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			JSONObject subscriptionJson = (JSONObject) respJson.get("Subscription");
			String subscription_status_cd = subscriptionJson.getString("subscription_status_cd");
			String next_billing_date = subscriptionJson.getString("next_billing_date");
			int renewal_count = subscriptionJson.getInt("renewal_count");
			assertTrue(renewal_count, 0, "renewal_count not correct");
			assertEquals(subscription_status_cd, "EXPR", "Status not correct");
			assertEquals(next_billing_date, "null", "Next billing date not correct");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	@Test(priority=3)
	public void deactivateSubscription() 
	{
		try 
		{
			JSONObject json = new JSONObject();
			json.put("reason_cd", "RFND");

			String url = BASE_URL + "/v1/subscriptions/" + subscription_Id + "/deactivate";
			ResponseBean response = HttpService.patch(url, headers, json.toString());

			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertEquals(respJson.get("EventStatus").toString(), "RFND", "EventStatus mismatch");
			assertEquals(respJson.get("subscription_status_cd").toString(), "EXPD", "subscription_status_cd mismatch");

		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	@Test(priority=4)
	public void createCustomer() {
		try {
			JSONObject json = new JSONObject();
			json.put("cuid", "884564");
			json.put("email", "rajan@livecareer.com");
			String url = BASE_URL + "/v1/customers";
			ResponseBean response = HttpService.post(url, headers, json.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);

			// DB Check
			JSONObject custID = (JSONObject) respJson.get("customer");
			String customerId = custID.get("id").toString();
			String cuid = custID.get("cuid").toString();
			String email = custID.get("email").toString();
			String query_session = "select * from [Ecommerce].[dbo].[customer] where customerid = " + customerId;
			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
			Map tmpData_session = list.get(0);
			assertEquals(tmpData_session.get("CUID").toString(), cuid, "cuid Mismatch");

			assertEquals(cuid, "884564", "CUID Mismatch");
			assertEquals(email, "rajan@livecareer.com", "EMAIL Mismatch");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	@Test(priority=5)
	public void OnePlanOneSNGL() {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			HashMap<String, String> data = getSkuID();
			String skuId = data.get("parent_skuid");
			JSONObject payment1 = PaymentJson.paymentData(skuId, 0, 1, 1, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			JSONObject payment2 = PaymentJson.paymentData("685795", 0, 1, 1, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			paymentList.add(payment1);
			paymentList.add(payment2);	
			TestCommon(json, paymentList, 2, true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	@Test(priority=6)
	public void OnePlan() {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			HashMap<String, String> data = getSkuID();
			String skuId = data.get("parent_skuid");
			String query = "select PlanID, TrialAmount, Description from [Ecommerce].[dbo].[Plan] where ClientSKUID = '" + skuId + "'";
			HashMap<String, String> hMap = DbUtil.getInstance("Ecommerce").getResultSet(query); 
			Double amt = Double.parseDouble(hMap.get("TrialAmount"));
			JSONObject payment = PaymentJson.paymentData(skuId, 0, amt, amt, 1, hMap.get("Description").toString(), JSONObject.NULL, "PLAN");
			paymentList.add(payment);
			TestCommon(json, paymentList, amt, true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	@Test(priority=7)
	public void OneSNGL() {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment = PaymentJson.paymentData("842754", 0, 10, 10, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			paymentList.add(payment);
			TestCommon(json, paymentList, 10, true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test Linked subscription charge now false 
	@Test(priority=8)
	public void linkedSubscriptionWithChargeNowFalse() {
		try {
			linkedSubscription(false, "AUTH");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test Linked subscription charge now true 
	@Test(priority=9)
	public void linkedSubscriptionWithChargeNowTrue() {
		try {
			linkedSubscription(true, "SALE");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test refund payment by subscription and transaction id
	@Test(priority=10)
	public void refundSubscriptionBySubscriptionAndTransactionId() {
		try {
			String[] strArr = createSampleSubscription();
			String url = BASE_URL + "/v1/subscriptions/" + strArr[1] + "/transactions/" + strArr[2] + "/refund";
			TestCommon(url, 0.20);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test refund payment by payment and transaction id
	@Test(priority=11)
	public void refundSubscriptionByPaymentAndTransactionId() {
		try {
			String[] strArr = createPaymentWithOnePLANAndTwoSNGL();
			String url = BASE_URL + "/v1/paymentitems/" + strArr[1] + "/transactions/" + strArr[0] + "/refund";
			TestCommon(url, 0.10);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test that refund payment is queued/initiated
	@Test(priority=12)
	public void refundQueuePaymentSubscription() {
		refundQueuePayment();
	}

	//Test that refund can be declined
	@Test(priority=13)
	public void refundDecline() {
		try {
			refundQueuePayment();
			String url = BASE_URL + REFUND_DECLINE_URL;
			JSONArray refunds_get = refundJArray(url);
			assertJsonValueEquality(refunds_get.getJSONObject(0).getJSONObject("refund_request"), "status_CD", "DECL");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test that refund is processed
	@Test(priority=14)
	public void refundProcess() {
		try {
			refundQueuePayment();
			String url = BASE_URL + REFUND_PROCESS_URL;
			refundJArray(url);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test that refund can be undone
	@Test(priority=15)
	public void refundUndo() {
		try {
			refundQueuePayment();
			String url = BASE_URL + REFUND_UNDO_URL;
			JSONArray refunds_get = refundJArray(url);
			assertJsonValueEquality(refunds_get.getJSONObject(0).getJSONObject("refund_request"), "status_CD", "UNDO");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
}
