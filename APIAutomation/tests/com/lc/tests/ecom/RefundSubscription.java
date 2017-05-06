package com.lc.tests.ecom;
import static com.lc.constants.Constants_Ecom.BASE_URL;
import static com.lc.constants.Constants_Ecom.PAYMENT_URL;
import static com.lc.constants.Constants_Ecom.REFUND_DECLINE_URL;
import static com.lc.constants.Constants_Ecom.REFUND_PROCESS_URL;
import static com.lc.constants.Constants_Ecom.REFUND_UNDO_URL;

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
import com.lc.json.PaymentJson;
import com.lc.utils.HeaderManager;

public class RefundSubscription extends EComCoreComponents {
	String subscriptionId = "";
	String paymentitemId = "";
	String transactionId = "";
	int refundrequestId;
	String subscriptionGateway = "";

	@BeforeMethod
	public void init() {
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	//Test refund payment by subscription and transaction id
	@Test
	public void refundSubscriptionBySubscriptionAndTransactionId() {
		try {
			createSubscription();
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/transactions/" + transactionId + "/refund";
			TestCommon(url, 0.20);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test refund payment by payment and transaction id
	@Test
	public void refundSubscriptionByPaymentAndTransactionId() {
		try {
			createPaymentWithOnePLANAndTwoSNGL();
			String url = BASE_URL + "/v1/paymentitems/" + paymentitemId + "/transactions/" + transactionId + "/refund";
			TestCommon(url, 0.10);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test that refund payment is queued/initiated
	@Test
	public void refundQueuePaymentSubscription() {
		refundQueuePayment();
	}

	//Test that refund can be declined
	@Test
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
	@Test
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
	@Test
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

	//Test refund payment by invalid sets of values
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "refundSubscription")
	public void refundSubscriptionWithInvalidValues(HashMap<String, String> excelDataRow) {
		try {
			createSubscription();
			if(!excelDataRow.get("subscriptionId").equalsIgnoreCase(""))
				subscriptionId = excelDataRow.get("subscriptionId");

			if(!excelDataRow.get("transactionId").equalsIgnoreCase(""))
				transactionId = excelDataRow.get("transactionId");

			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/transactions/" + transactionId + "/refund";

			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			JSONObject json = new JSONObject();
			json.put("reason_cd", excelDataRow.get("reason_cd"));
			json.put("reason_message", "api test");
			json.put("amount", Double.parseDouble(excelDataRow.get("amount")));
			ResponseBean response = HttpService.post(url, headers, json.toString());
			assertTrue(Integer.parseInt(excelDataRow.get("ResponseCode")), response.code, excelDataRow.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch");
			if(!response.message.contains("ModelState"))
			assertEquals(excelDataRow.get("ResponseMessage"), response.message, excelDataRow.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Test utility method to create subscription
	private void createSubscription() {
		try {
			ResponseBean response = createSubscriptionBySkuId("10117", 1.95, "LCAUS", getFakeCCDetails());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");
			System.out.println(respJson);
			JSONObject cc = (JSONObject) respJson.get("subscription");
			subscriptionId = cc.get("id").toString();
			JSONObject transactionarray = (JSONObject) respJson.getJSONArray("transaction").getJSONObject(0);
			paymentitemId = transactionarray.getJSONArray("payment_item").getJSONObject(0).getString("id");
			transactionId = transactionarray.get("id").toString();
			
			//Verify Gateway and Gateway_Reference
			verifyTransaction(respJson, "SALE", false);
			
			String query_gateway = "select GatewayCD as Gateway from BusinessCase where BusinessCaseID = (select BusinessCaseID as BusinessCaseID from [transaction] where transactionid = " + transactionId + ") and ClientCD = 'LCAUS'";
			Map list_SubscriptionGateway = DbUtil.getInstance("Ecommerce").getResultSet(query_gateway); 
			subscriptionGateway = list_SubscriptionGateway.get("Gateway").toString();
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
	}

	//create payment with one plan two singles 
	private void createPaymentWithOnePLANAndTwoSNGL() {
		try {
			JSONObject json = new JSONObject();
			JSONArray paymentArr = new JSONArray();
			JSONObject payment1 = new JSONObject();
			JSONObject payment2 = new JSONObject();
			JSONObject payment3 = new JSONObject();
			payment1 = PaymentJson.paymentData("10117", 0, 1.95, 1.95, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			payment2 = PaymentJson.paymentData("685795", 0, 10, 10, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			payment3 = PaymentJson.paymentData("842754", 0, 10, 10, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			paymentArr.put(payment1);
			paymentArr.put(payment2);
			paymentArr.put(payment3);
			json.put("email", "reg_lc_n_27jan@livecareer.com");
			json.put("payment", paymentArr);
			json.put("credit_card", getFakeCCDetails());
			json.put("amount", 21.95);
			json.put("currency", "USD");
			json.put("cuid", "69901642");
			json.put("ip_address", "182.71.90.126");
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1122");
			ResponseBean response = HttpService.post(BASE_URL + PAYMENT_URL, headers, json.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");
			JSONArray transactionArr = (JSONArray) respJson.get("transaction");
			transactionId = transactionArr.getJSONObject(0).getString("id");
			JSONArray paymentItem = (JSONArray) transactionArr.getJSONObject(0).get("payment_item");
			paymentitemId = paymentItem.getJSONObject(0).getString("id");
			
			//Verify Gateway and Gateway_Reference
			verifyTransaction(respJson, "SALE", false);
			
			String query_gateway = "select GatewayCD as Gateway from BusinessCase where BusinessCaseID = (select BusinessCaseID as BusinessCaseID from [transaction] where transactionid = " + transactionId + ") and ClientCD = 'LCAUS'";
			Map list_SubscriptionGateway = DbUtil.getInstance("Ecommerce").getResultSet(query_gateway); 
			subscriptionGateway = list_SubscriptionGateway.get("Gateway").toString();
			
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
	}

	//Test utility method to post refund request and assert expected against actual
	private void refundQueuePayment()
	{
		try {
			createPaymentWithOnePLANAndTwoSNGL();
			String url = BASE_URL + "/v1/paymentitems/" + paymentitemId + "/transactions/" + transactionId + "/refundqueue";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
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
		HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
		JSONObject json = new JSONObject();
		JSONArray refund = new JSONArray();
		System.out.println(refundrequestId);
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

	/**
	 * Test utility method to post the refund request and assert expected values against actual 
	 * @param url complete url of the resource
	 * @param amount refund amount 
	 * @throws JSONException
	 * @throws IOException
	 */
	private void TestCommon(String url, Double amount) throws JSONException, IOException
	{
		HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
		
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
		
		//Gateway Check 
		System.out.println("Refund transaction JSON is " + transactionJson);
		String gateway = transactionJson.getString("gateway");
		String gateway_reference = transactionJson.getString("gateway_reference");
		
		assertEquals(subscriptionGateway, gateway, "Gateway mismatch, expecetd : " + subscriptionGateway + " but actual " +gateway);
		if(subscriptionGateway.equalsIgnoreCase("WCRD"))
		{
			assertTrue(!gateway_reference.matches("^[0-9]{16}$"), "gateway_reference is not of WCRD");
		}
		else if (subscriptionGateway == "ADYN")
		{
			assertTrue(gateway_reference.matches("^[0-9]{16}$"), "gateway_reference is not of ADYN");
		}
	}
}
