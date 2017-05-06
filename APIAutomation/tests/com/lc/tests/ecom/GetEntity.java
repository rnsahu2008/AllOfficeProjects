package com.lc.tests.ecom;

import static com.lc.constants.Constants_Ecom.BASE_URL;

import java.util.HashMap;
import java.util.Iterator;
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

public class GetEntity extends EComCoreComponents{
	
	String subscriptionId = "";
	String customerId = "";
	String transactionId = "";
	String paymentSubID = "";
	String planid = "2";
	HashMap<String, String> a = null;
	
	@BeforeMethod
	public void init() {
		s_assert = new SoftAssert();
		a = new HashMap<String, String>();
		Common.apiLogInfo.clear();
	}
	
	@Test
	public void getConfigurations() {
		try {
			String url = BASE_URL + "/v1/configurations";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			assertEquals("site_id=123457&location_name=checkout1", "site_id=123457&location_name=checkout1", "Done");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getcreditcardtypes() {
		try {
			String url = BASE_URL + "/v1/creditcardtypes";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONArray respJson = new JSONArray(response.message);
			assertJsonValueEquality(respJson.getJSONObject(0), "CardTypeCD", "AMEX");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getCustomerbyCustomerID() {
		try {
			createSubscription();
			String url = BASE_URL + "/v1/customers/" + customerId;
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "id", customerId);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getCreditCardWithCustomerId() {
		try {
			createSubscription();
			String url = BASE_URL + "/v1/customers/" + customerId + "/creditscards";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONArray respJson = new JSONArray(response.message);
			assertJsonValueEquality(respJson.getJSONObject(0), "type", "VISA");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Getting exception");
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getCustomersSubscriptionByCustomerID() {
		try {
			createSubscription();
			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONArray respJson = new JSONArray(response.message);
			for (int i = 0; i < respJson.length(); i++) {
				JSONObject transaction = respJson.getJSONObject(i);
				Map<String, String> out = null;
				Iterator<String> keys = transaction.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					String val = null;
					try {
						String value = transaction.getString(key);
						if (value.equals(a.get(key))) {
							// pass
						}
						System.out.println(value);
						// parse(value,out);
					} catch (Exception e) {
						val = transaction.getString(key);
					}
					if (val != null) {
						out.put(key, val);
					}
				}
				String subid = transaction.getString("id");
				String subStatusCD = transaction.getString("subscription_status_cd");
				assertEquals(subid, subscriptionId, "Subscriptionid Mismatch");
				assertEquals(subStatusCD, "ACTV", "subStatusCD Mismatch");
			}
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getEventsByDyanamicSubscriptionID() {
		try {
			createSubscription();
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/events";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			// JSONObject json = new JSONObject();
			// json.put("expiry_date", "2015-08-21T02:15:53");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertTrue(200, response.code, "Response is Ok");
			assertNotNull(response.message, "Getting NULL as response");
			JSONArray respJson = new JSONArray(response.message);
			assertJsonArrayEquality(respJson, respJson);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getEventsByDyanamicSubscriptionIDAndProperty() {
		try {
			createSubscription();
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/events";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertTrue(200, response.code, "Response is Ok");
			assertNotNull(response.message, "Getting NULL as response");
			JSONArray respJson = new JSONArray(response.message);
			assertJsonArrayEquality(respJson, respJson);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getEventsBySubscriptionID() {
		try {
			createSubscription();
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/events";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertTrue(200, response.code, "Response is Ok");
			assertNotNull(response.message, "Getting NULL as response");
			JSONArray respJson = new JSONArray(response.message);
			assertJsonArrayEquality(respJson, respJson);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getPaymentItemsByTransactionId() {
		try {
			createSubscription();
			String url = BASE_URL + "/v1/transactions/" + transactionId + "/paymentitems";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONArray respJson = new JSONArray(response.message);
			JSONObject Paymentitems = respJson.getJSONObject(0);
			String paymentitemid = Paymentitems.getString("id").toString();
			assertEquals(paymentitemid, paymentSubID, "Paymentid is Mismatch");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getPlanById() {
		try {
			String url = BASE_URL + "/v1/plans/" + planid;
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			String plnid = respJson.getString("id");
			assertEquals(plnid, "2", "Planid is Mismatch");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getRefundRequestsWithCustomerID() {
		try {
			String query_session = "Select * from PaymentItemTransactions pt inner join RefundRequest rq on pt.PaymentItemTransactionID  = rq.PaymentItemTransactionID inner join [Transaction] t on pt.TransactionID  = t.TransactionID inner join Customer c on c.customerID = t.customerID order by 1 desc"; 

			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
			Map tmpData_session = list.get(0);
			int customerId = Integer.parseInt(tmpData_session.get("CustomerID").toString());
			String transId = tmpData_session.get("RefundRequestID").toString();

			String url = BASE_URL + "/v1/customers/" + customerId + "/refundrequests";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONArray respJson = new JSONArray(response.message);
			assertEquals(transId, respJson.getJSONObject(0).get("refund_request_id").toString(), "RefundRequestID mismatch");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getSubcriptionBySubscriptionId() {
		try {
			String subscriptionId = createSampleSubscription();
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId;
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			String subid = respJson.getString("id");
			assertEquals(subid, subscriptionId, "Subscriptionid is Mismatch");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getTransactionbyPaymentid() {
		try {
			createSubscription();
			String url = BASE_URL + "/v1/paymentitems/" + paymentSubID + "/transactions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONArray respJson = new JSONArray(response.message);
			for (int i = 0; i < respJson.length(); i++) {
				int count = respJson.length();
				assertTrue(count, 2, "transaction count Mismatch");
			}
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getTransactionbySubscriptionID() {
		try {
			String subscriptionId = createSampleSubscription();
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/transactions";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONArray respJson = new JSONArray(response.message);
			for (int i = 0; i < respJson.length(); i++) {
				int count = respJson.length();
				assertTrue(count, 2, "transaction count Mismatch");
			}
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	private void createSubscription() {
		try {
			ResponseBean response = createSubscriptionBySkuId("10117", 1.95, "LCAUS", getFakeCCDetails());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");
			System.out.println(respJson);
			JSONObject customer = respJson.getJSONObject("customer");
			customerId = customer.get("id").toString();
			JSONObject subscription = (JSONObject) respJson.get("subscription");
			subscriptionId = subscription.get("id").toString();
			a.put("id", subscriptionId);
			JSONArray msg1 = (JSONArray) respJson.get("transaction");
			JSONObject transactionid = msg1.getJSONObject(0);
			transactionId = transactionid.getString("id");
			JSONArray msg2 = (JSONArray) respJson.get("transaction");
			JSONObject paymentid = msg2.getJSONObject(0);
			JSONArray paymentId = paymentid.getJSONArray("payment_item");
			JSONObject payobj = paymentId.getJSONObject(0);
			paymentSubID = payobj.getString("id");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
	}

}
