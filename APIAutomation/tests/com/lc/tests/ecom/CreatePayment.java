package com.lc.tests.ecom;
import static com.lc.constants.Constants_Ecom.BASE_URL;
import static com.lc.constants.Constants_Ecom.PAYMENT_URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

public class CreatePayment extends EComCoreComponents 
{	
	String paymentId = "";
	String transactionId = "";
	String subscriptionId = "";
	String customerId = "";
	String cuid = "";

	@BeforeMethod
	public void init() 
	{
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	//Create payment for complementary one plan 
	@Test
	public void CompOnePlan() 
	{
		try 
		{
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment = PaymentJson.paymentData("10024", 0, 0, 0, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			paymentList.add(payment);
			TestCommon(json, paymentList, 0, false);
		} 
		catch (Exception e) 
		{
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Create payment for complementary one plan and one single
	@Test
	public void CompOnePlanOneSNGL() {
		try {

			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment1 = PaymentJson.paymentData("10024", 0, 0, 0, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			JSONObject payment2 = PaymentJson.paymentData("685795", 0, 1.95, 1.95, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			paymentList.add(payment1);
			paymentList.add(payment2);	
			TestCommon(json, paymentList, 1.95, true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Create payment for complementary one plan and two single
	@Test
	public void CompOnePlanTwoSNGL() {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment1 = PaymentJson.paymentData("10024", 0, 0, 0, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			JSONObject payment2 = PaymentJson.paymentData("685795", 0, 1.95, 1.95, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			JSONObject payment3 = PaymentJson.paymentData("8795874", 0, 1, 1, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			paymentList.add(payment1);
			paymentList.add(payment2);	
			paymentList.add(payment3);	
			TestCommon(json, paymentList, 2.95, true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Create payment for one plan and one single
	@Test
	public void OnePlanOneSNGL() {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment1 = PaymentJson.paymentData("10117", 0, 1, 1, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			JSONObject payment2 = PaymentJson.paymentData("685795", 0, 1, 1, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			paymentList.add(payment1);
			paymentList.add(payment2);	
			TestCommon(json, paymentList, 2, true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Create payment for one plan and one single with invalid data
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "getOnePlanOneSNGLInvalidData")
	public void OnePlanOneSNGLWithInvalidData(HashMap<String, String> data) {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment1 = PaymentJson.paymentData("10117", 0, 1, 1, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			JSONObject payment2 = PaymentJson.paymentData("685795", 0, 1, 1, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			paymentList.add(payment1);
			paymentList.add(payment2);	
			TestCommon(json, paymentList, Double.parseDouble(data.get("amount")), getFakeCCDetails(), data);			
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Create payment for two plan and one single
	@Test
	public void TwoPlanOneSNGL() {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment1 = PaymentJson.paymentData("10117", 0, 1.95, 1.95, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			JSONObject payment2 = PaymentJson.paymentData("10041", 0, 9.95, 9.95, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			JSONObject payment3 = PaymentJson.paymentData("54545", 0, 1.95, 1.95, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			paymentList.add(payment1);
			paymentList.add(payment2);	
			paymentList.add(payment3);	
			TestCommon(json, paymentList, 13.85, true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Create payment for one plan and two single
	@Test
	public void OnePlanAndTwoSNGL() {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment1 = PaymentJson.paymentData("10117", 0, 1.95, 1.95, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			JSONObject payment2 = PaymentJson.paymentData("685795", 0, 10, 10, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			JSONObject payment3 = PaymentJson.paymentData("842754", 0, 10, 10, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			paymentList.add(payment1);
			paymentList.add(payment2);	
			paymentList.add(payment3);	
			TestCommon(json, paymentList, 21.95, true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Create payment for one plan and NO single
	@Test
	public void OnePlanAndNoSNGL() {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment = PaymentJson.paymentData("10117", 0, 1.95, 1.95, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			paymentList.add(payment);
			TestCommon(json, paymentList, 1.95, true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Create payment for one plan with invalid data
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "getOnePlanInvalidData")
	public void OnePlanWithInvalidData(HashMap<String, String> data) {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment = PaymentJson.paymentData("10117", 0, Double.parseDouble(data.get("price")), Double.parseDouble(data.get("PriceAndQtyMultiply")), 
					Integer.parseInt(data.get("Qty")), "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			paymentList.add(payment);
			TestCommon(json, paymentList, Double.parseDouble(data.get("amount")), getFakeCCDetails(), data);			
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Create payment for one plan with invalid credit card details
	@Test(dataProviderClass = Ecom_DP.class, dataProvider = "getOnePlanInvalidCCData")
	public void OnePlanWithInvalidCCData(HashMap<String, String> ccDetails) {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment = PaymentJson.paymentData("10117", 0, 1.95, 1.95, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			paymentList.add(payment);

			JSONObject ccJson = getInvalidCCJson(ccDetails);
			TestCommon(json, paymentList, 1.95, ccJson, ccDetails);

		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}

	//Create payment for one single plan
	@Test
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

	//Create payment for two plan and one single for existing customer 
	@Test
	public void existingCustIdWithTwoPlanOneSNGL() {
		try {
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment1 = PaymentJson.paymentData("10117", 0, 1.95, 1.95, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			JSONObject payment2 = PaymentJson.paymentData("10028", 0, 0, 0, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			JSONObject payment3 = PaymentJson.paymentData("456456", 0, 1, 1, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			paymentList.add(payment1);
			paymentList.add(payment2);	
			paymentList.add(payment3);	
			TestCommon(json, paymentList, 2.95, true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	//Payment of One Plan with Auth amount 
	@Test
	public void OnePlanWithAuthAmount() {
		try {		
			verifyDefaultAuthAmount("WCRD", "1.0000");
			verifyDefaultAuthAmount("ADYN", "0.0000");
		
			String query_skuId = "Select top 1* from [Ecommerce].[dbo].[Plan] where Amount > 0 and FrequencyInterval > 0 and (Cycles is Null or Cycles > 0 )and TrialAmount = 0 and TrialSplitAmount = 0 and TrialDays > 0 and ClientCD = 'LCAUS'";
			Map skuID = DbUtil.getInstance("Ecommerce").getResultSet(query_skuId);
			
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment = PaymentJson.paymentData(skuID.get("ClientSKUID").toString(), 0, 0, 0, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "PLAN");
			paymentList.add(payment);
			
			ResponseBean response = getPaymentResponse(json, paymentList, 0);
			JSONObject respJson = new JSONObject(response.message);

			verifyTransaction(respJson, "AUTH", true);
			
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	//Payment of One SNGL with Auth amount 
	@Test
	public void OneSNGLWithAuthAmount() {
		try 
		{
			verifyDefaultAuthAmount("WCRD", "1.0000");
			verifyDefaultAuthAmount("ADYN", "0.0000");
			
			JSONObject json = new JSONObject();
			List<JSONObject> paymentList = new ArrayList<JSONObject>();
			JSONObject payment = PaymentJson.paymentData("87821", 0, 0, 0, 1, "ResumeBuilder 1 Month", JSONObject.NULL, "SNGL");
			paymentList.add(payment);
			
			ResponseBean response = getPaymentResponse(json, paymentList, 0);
			JSONObject respJson = new JSONObject(response.message);

			verifyTransaction(respJson, "AUTH", true);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
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
			json.put("currency", "USD");
			json.put("cuid", "69901642");
			json.put("ip_address", "182.71.90.126");
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1122");
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

			//Gateway Check 
			verifyTransaction(respJson, "SALE", false);

			// DB Check
			if(dbCheck)
			{
				JSONObject custID = (JSONObject) respJson.get("customer");
				customerId = custID.get("id").toString();
				JSONArray transactionArr = (JSONArray) respJson.get("transaction");
				transactionId = transactionArr.getJSONObject(0).getString("id");
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

	/**
	 * This method is used for creating data to post for creating payment and asserting actual and expected result
	 * @param instance of JSONObject
	 * @param instance of JSONObject
	 * @param list of Payment objects
	 * @param toatl amount of the plans
	 * @param HashMap data
	 */
	private void TestCommon(JSONObject json, List<JSONObject> payments, double amount, JSONObject ccData, HashMap<String, String> data)
	{
		try
		{
			ResponseBean response = getPaymentResponse(json, payments, amount);
			
			assertTrue(Integer.parseInt(data.get("ResponseCode")), response.code, data.get("TestCaseName") + " test got failed. Failure reason : Response code mismatch, ecpected " +data.get("ResponseCode")+ " and actual "+response.code);
			assertEquals(data.get("ResponseMessage"), response.message, data.get("TestCaseName") + " test got failed. Failure reason : Response message mismatch, ecpected " +data.get("ResponseMessage")+ " and actual "+response.message);
		}
		catch(Exception e)
		{
			fail(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
}
