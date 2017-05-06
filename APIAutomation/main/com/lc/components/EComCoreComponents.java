package com.lc.components;

import static com.lc.constants.Constants_Ecom.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.lc.assertion.AssertionUtil;
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.json.CreditCardJson;
import com.lc.json.SubscriptionJson;
import com.lc.utils.HeaderManager;
import com.lc.utils.ReadProperties;

public class EComCoreComponents extends AssertionUtil{
	public static String access_token = "";	
	public String subscriptionId = "";
	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public boolean switchFunc = false;
	public int wcrdWeight = 100;
	public int adynWeight = 0;
	
	@Parameters(value = { "Environment", "Switch", "Wcrd", "Adyn" })
	@BeforeTest
	public void setupEnvironment(String env, boolean swch, int wcrd, int adyn){
		ReadProperties.setupEnvironmentProperties(env,"res/ecomdata/config.json");
		generateAccessToken();
		switchFunc = swch;
		wcrdWeight = wcrd;
		adynWeight = adyn;
		setWeight(wcrdWeight, adynWeight);
	}
	
/*@BeforeTest
	public void setUp(){
		ReadProperties.setAuthURL("https://auth.livecareer.com/oAuth/Token");
		ReadProperties.setBaseURL("http://api-sandbox-ecom.livecareer.com");
		ReadProperties.setSqlHost("67.22.105.3");
		ReadProperties.setSqlUserName("qa_ro");
		ReadProperties.setSqlPasswd("ec0mm3rc3q@");
		ReadProperties.setSqlPort("1433");
		generateAccessToken();
	}	
*/
	public void generateAccessToken(){
		try {
			String dataToPost = "grant_type=client_credentials&client_id=LCAUS&client_secret=bGl2ZWNhcmVlciByb2NrcyE=&response_type=token:";
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD", "LCAUS");
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
	
	public void setWeight(Integer wcrd, Integer adyn)
	{
		String query_wcrd = "update BusinessCaseCurrency set weight = " + wcrd + " where BusinessCaseID = (Select BusinessCaseID from dbo.BusinessCase where ClientCD in ('LCAUS') and GatewayCD = 'WCRD' and CurrencyCD = 'USD')";
		DbUtil.getInstance("Ecommerce").executeNonQuery(query_wcrd);
		String query_adyn = "update BusinessCaseCurrency set weight = " + adyn + " where BusinessCaseID = (Select BusinessCaseID from dbo.BusinessCase where ClientCD in ('LCAUS') and GatewayCD = 'ADYN' and CurrencyCD = 'USD')";
		DbUtil.getInstance("Ecommerce").executeNonQuery(query_adyn);
	}
	
	public static ResponseBean createSubscriptionBySkuId(String skuId , double amount , String clientCD , JSONObject ccDetails){
		try {
			JSONObject json = getSubscriptionJsonBySkuId(skuId , amount);
			if(ccDetails.length()==0){
				json.put("credit_card", JSONObject.NULL);
			}
			else{
				json.put("credit_card", ccDetails);
			}			
			HashMap<String, String> headers = HeaderManager.getHeaders(clientCD, access_token, "1234");			
			ResponseBean response = HttpService.post(BASE_URL + "/" + SUBSCRIBE_URL, headers, json.toString());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ResponseBean createSubscriptionBySkuId(String skuId , double amount , String clientCD , JSONObject ccDetails , String customerId){
		try {
			JSONObject json = getSubscriptionJsonBySkuId(skuId , amount);
			if(ccDetails.length()==0){
				json.put("credit_card", JSONObject.NULL);
			}
			else{
				json.put("credit_card", ccDetails);
			}			
			String url = BASE_URL + "/v1/customers/" + customerId + "/subscriptions";
			HashMap<String, String> headers = HeaderManager.getHeaders(clientCD, access_token, "1234");			
			ResponseBean response = HttpService.post(url, headers, json.toString());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject getSubscriptionJsonBySkuId(String skuId , double amount){
		try {
			JSONObject json = new JSONObject();
			HashMap<String, String> hMap = new HashMap<String , String>();
			String query = "select * from [Ecommerce].[dbo].[Plan] where ClientSKUID = '" + skuId + "'";
			hMap = DbUtil.getInstance("Ecommerce").getResultSet(query); 
			json.put("sku_id", skuId);
			json.put("plan_id", Integer.parseInt(hMap.get("PlanID").toString()));
			json.put("amount", amount);
			json.put("descriptor", hMap.get("Description"));
			
			json.put("start_date", JSONObject.NULL);
			json.put("cuid", "88503817");
			json.put("ip_address", "141.71.90.126");
			json.put("email", "rajan@livecareer.com");
			json.put("charge_now", JSONObject.NULL);
			json.put("linked_subscription", JSONObject.NULL);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject subscribeWithCCDetailsUtil(){
		try {
			JSONObject ccJson = getFakeCCDetails();
			JSONObject json = SubscriptionJson.subscriptionData("10117", 60, null, 1.95, "CV 1.95 INIT 14D TO 22.95 MNTH", "69935748", "182.71.90.126",
														"rajan@livecareer.com", false, null);
			json.put("credit_card", ccJson);
			
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.post(SUBSCRIBE_URL, headers, json.toString());
			return new JSONObject(response.message);
			} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject getFakeCCDetails(){
		return CreditCardJson.ccData(0, 0, "livecareer", "VISA", "5424000000100015", 2016, 06, 200, "542400", 
				"2015-03-24T15:57:05.1379094+05:30", "2015-03-24T15:57:05.1539163+05:30");
	}
	
	public static JSONObject getPrepaidCCDetails(){
		return CreditCardJson.ccData(0, 0, "Rajan", "VISA", "4385000000104001", 2020, 10, 200, "438587", 
				"2015-01-27T02:00:08.013203-05:00", "2015-01-27T02:00:08.028828-05:00");
	}

	public static JSONObject getFakeFailCCDetails(){
		return CreditCardJson.ccData(0, 0, "Rajan", "MASTERCARD", "54000000400005", 2020, 10, 200, "544440", 
							"2015-01-27T02:00:08.013203-05:00", "2015-01-27T02:00:08.028828-05:00");
	}
	
	//This method is used for creating invalid credit card JSONObject
	public static JSONObject getInvalidCCJson(HashMap<String, String> ccData) throws JSONException
	{
		JSONObject ccJson = new JSONObject();
		ccJson.put("id", 0);
		ccJson.put("CustomerID", 0);
		ccJson.put("cardholder_name", ccData.get("CardHolderName"));
		ccJson.put("type", ccData.get("CardType"));
		ccJson.put("number", ccData.get("CardNumber"));
		ccJson.put("expire_year", ccData.get("ExpiryYear"));
		ccJson.put("expire_month", ccData.get("ExpiryMonth"));
		ccJson.put("cvv", ccData.get("CVV"));
		ccJson.put("bin", ccData.get("Bin"));
		ccJson.put("created_on", ccData.get("CreatedOn"));
		ccJson.put("ModifiedOn", ccData.get("ModifiedOn"));
		 
		return ccJson;
	}
	
	//This is used for setting integer value to JSONObject parameter by converting it into Integer value
	public static void setIntegerValue(String key, HashMap<String, String> data, JSONObject json) throws JSONException
	{
		if(data.get(key).equalsIgnoreCase("null"))
			json.put(key, JSONObject.NULL);
		else
			json.put(key, Integer.parseInt(data.get(key)));
	}
	
	//This is used for setting double value to JSONObject parameter by converting it into Integer value
	public static void setDoubleValue(String key, HashMap<String, String> data, JSONObject json) throws JSONException
	{
		if(data.get(key).equalsIgnoreCase("null"))
			json.put(key, JSONObject.NULL);
		else
			json.put(key, Double.parseDouble(data.get(key)));
	}
	
	//This is used for setting string value to JSONObject parameter 
	public static void setStringValue(String key, HashMap<String, String> data, JSONObject json) throws JSONException
	{
		if(data.get(key).equalsIgnoreCase("null"))
			json.put(key, JSONObject.NULL);
		else
			json.put(key, data.get(key));
	}
	
	/**
	* This method is used for creating subscription JSON object 
	* @param data in the form of HashMap<String, String>, pass this to set key value pairs to form JSON object 
	* @param credit card JSONObject
	* @return create subscription JSON object
	*/
	public static JSONObject createSubscriptionJsonObject(HashMap<String, String> data, JSONObject ccDetail) throws JSONException
	{	
		JSONObject json = new JSONObject();
		
		if(data.get("plan_id").equalsIgnoreCase(""))
		{
			String skuId = data.get("sku_id");
			String query = "select * from [Ecommerce].[dbo].[Plan] where ClientSKUID = " + skuId;
			HashMap<String, String> hMap = DbUtil.getInstance("Ecommerce").getResultSet(query); 
			json.put("plan_id", Integer.parseInt(hMap.get("PlanID").toString()));
		}	
		else
			setIntegerValue("plan_id", data, json);
		
		setIntegerValue("sku_id", data, json);
		setDoubleValue("amount", data, json);		
		json.put("descriptor", "CLB " + data.get("amount") + " Week");
		setStringValue("start_date", data, json);
		setStringValue("cuid", data, json);
		setStringValue("ip_address", data, json);
		setStringValue("email", data, json);
		setStringValue("charge_now", data, json);
		setStringValue("linked_subscription", data, json);
		
		if(ccDetail.length()==0){
			json.put("credit_card", JSONObject.NULL);
		}
		else{
			json.put("credit_card", ccDetail);
		}	
		
		return json;
	}
	
	/**
	* This method is used for creating subscription JSON object with invalid credit card details
	* @param credit card details JSON Object 
	* @return create subscription JSON object
	*/
	public static JSONObject createSubscriptionJsonObjectWithInvalidCC(JSONObject ccData) throws JSONException
	{
		JSONObject json = new JSONObject();

		json.put("sku_id", 10117);
		String query = "select * from [Ecommerce].[dbo].[Plan] where ClientSKUID = 10117";
		HashMap<String, String> hMap = DbUtil.getInstance("Ecommerce").getResultSet(query); 
		json.put("plan_id", Integer.parseInt(hMap.get("PlanID").toString()));
		json.put("amount", 1.95);
		json.put("descriptor", "CLB 1.95 Plan");		
		json.put("start_date", JSONObject.NULL);
		json.put("cuid", "88503817");
		json.put("ip_address", "141.71.90.126");
		json.put("email", "rajan@livecareer.com");
		json.put("charge_now", JSONObject.NULL);
		json.put("linked_subscription", JSONObject.NULL);
		
		json.put("credit_card", ccData);
		
		return json;
	}
	
	/**
	 * This method creates subscription 
	 * @return subscription id
	*/
	public String createSampleSubscription()
	{
		try 
		{
			ResponseBean response = createSubscriptionBySkuId("10117", 1.95, "LCAUS", getFakeCCDetails());
			
			System.out.println("Response: " + response.message);
			
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			assertJsonValueEquality(respJson, "status", "SUCC");
			assertJsonValueEquality(respJson, "mode", "DEMO");

			JSONObject cc = (JSONObject) respJson.get("subscription");
			return cc.getString("id");
		} 
		catch (Exception e) 
		{
			fail("Getting exception::" + e.getMessage());
		}
		
		return null;
	}
	
	public String cancelSampleSubscription() 
	{
		try 
		{
			subscriptionId = createSampleSubscription();
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/cancel";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
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
			return subscriptionId;
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		
		return null;
	}
	
	//This method is used to fetch expiry date depending on the input provided for example, PastDate, FutureDate and Today's Date
	public String getExpiryDate(String data)
	{
		String date = "";
		switch(data)
		{
			case "PastDate" :
				date = addDays(new Date(), format, -30).toString();
				break;
			case "FutureDate" :
				date = addDays(new Date(), format, 120).toString();
				break;
			case "Today'sDate" : 
				date = addDays(new Date(), format, 0).toString();
				break;
			default: break;
		}
		return date;
	}

	//This method is used to return Date in given format after adding days in todays date
	public String addDays(Date date, SimpleDateFormat simpleDateFormat, Integer numberOfDays) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.DAY_OF_MONTH, numberOfDays);

	    return simpleDateFormat.format(cal.getTime());
	}
	
	public String getSubcriptionStatusBySubscriptionId(String subscriptionId) {
		try {
			String subscription_status = "";
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId;
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			subscription_status = respJson.getString("subscription_status_cd");
			return subscription_status;
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		return subscriptionId;
	}
	
	@SuppressWarnings("rawtypes")
	public Integer getPlanID(String sku_id)
	{
		String query_session = "select top (10) * from [Ecommerce].[dbo].[Plan] where ClientSKUID = '" + sku_id + "'";
		List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
		Map tmpData_session = list.get(0);
		return Integer.parseInt(tmpData_session.get("PlanID").toString());	
	}
	
	public void verifyTransaction(JSONObject respJson, String transactionType, boolean checkForAuthAmount)
	{
		try
		{
			System.out.println(respJson.getJSONArray("transaction"));
			if(respJson.getJSONArray("transaction").length() != 0)
			{
				for(int i = 0; i < respJson.getJSONArray("transaction").length(); i++)
				{
					int transactionRow = i+1;
					String gateway = respJson.getJSONArray("transaction").getJSONObject(i).getString("gateway");
					String gateway_reference = respJson.getJSONArray("transaction").getJSONObject(i).getString("gateway_reference");
					String transaction_type = respJson.getJSONArray("transaction").getJSONObject(i).getString("transaction_type");
					String result = respJson.getJSONArray("transaction").getJSONObject(i).getString("result");
					String amount = respJson.getJSONArray("transaction").getJSONObject(i).getString("amount");
					
					if(switchFunc && (wcrdWeight == 100 || adynWeight == 0))
					{
						if(result.equalsIgnoreCase("FAIL"))
						{
							for(int j = i+1; j < respJson.getJSONArray("transaction").length(); j++)
							{
								gateway = respJson.getJSONArray("transaction").getJSONObject(j).getString("gateway");
								gateway_reference = respJson.getJSONArray("transaction").getJSONObject(j).getString("gateway_reference");
								transaction_type = respJson.getJSONArray("transaction").getJSONObject(j).getString("transaction_type");
								amount = respJson.getJSONArray("transaction").getJSONObject(i).getString("amount");
								result = respJson.getJSONArray("transaction").getJSONObject(j).getString("result");
								
								assertEquals("ADYN", gateway, "Gateway mismatch in transaction row " + j + ", expecetd : ADYN but actual " +gateway);
								assertTrue(gateway_reference.matches("^[0-9]{16}$"), "gateway_reference is not of ADYN in transaction row " +j);
								assertEquals(transactionType, transaction_type, "Transaction type mismatch in transaction row " + j + ", expecetd : SALE but actual " +transaction_type);
								assertEquals(result, "SUCC", "transaction result not succ");
								
								if(checkForAuthAmount)
								assertEquals("0.0", amount, "amount mismatch, expected : 0.0 and actual " + amount);
							}
							break;
						}
						else
						{
							assertEquals("WCRD", gateway, "Gateway mismatch in transaction row " + transactionRow + ", expecetd : WCRD but actual " +gateway);
							assertTrue(!gateway_reference.matches("^[0-9]{16}$"), "gateway_reference is not of WCRD in transaction row " +transactionRow);
							assertEquals(transactionType, transaction_type, "Transaction type mismatch in transaction row " + transactionRow + ", expecetd : SALE but actual " +transaction_type);
							
							if(checkForAuthAmount)
								assertEquals("1.0", amount, "amount mismatch, expected : 1.0 and actual " + amount);
						}
					}
					else if (switchFunc && (wcrdWeight == 0 || adynWeight == 100))
					{
						if(result.equalsIgnoreCase("FAIL"))
						{
							for(int j = i+1; j < respJson.getJSONArray("transaction").length(); j++)
							{
								gateway = respJson.getJSONArray("transaction").getJSONObject(j).getString("gateway");
								gateway_reference = respJson.getJSONArray("transaction").getJSONObject(j).getString("gateway_reference");
								transaction_type = respJson.getJSONArray("transaction").getJSONObject(j).getString("transaction_type");
								result = respJson.getJSONArray("transaction").getJSONObject(j).getString("result");
								
								assertEquals("WCRD", gateway, "Gateway mismatch in transaction row " + j + ", expecetd : WCRD but actual " +gateway);
								assertTrue(!gateway_reference.matches("^[0-9]{16}$"), "gateway_reference is not of WCRD in transaction row " +j);
								assertEquals(transactionType, transaction_type, "Transaction type mismatch in transaction row " + j + ", expecetd : SALE but actual " +transaction_type);
								assertEquals(result, "SUCC", "transaction result not succ");
								
								if(checkForAuthAmount)
									assertEquals("1.0", amount, "amount mismatch, expected : 1.0 and actual " + amount);
							}
							break;
						}
						else
						{
							assertEquals("ADYN", gateway, "Gateway mismatch in transaction row " + transactionRow + ", expecetd : ADYN but actual " +gateway);
							assertTrue(gateway_reference.matches("^[0-9]{16}$"), "gateway_reference is not of ADYN in transaction row " +transactionRow);
							assertEquals(transactionType, transaction_type, "Transaction type mismatch in transaction row " + transactionRow + ", expecetd : SALE but actual " +transaction_type);
							
							if(checkForAuthAmount)
								assertEquals("0.0", amount, "amount mismatch, expected : 0.0 and actual " + amount);
						}
					}
					else if (!switchFunc && (wcrdWeight == 100 || adynWeight == 0))
					{
						assertEquals("WCRD", gateway, "Gateway mismatch in transaction row " + transactionRow + ", expecetd : WCRD but actual " +gateway);
						assertTrue(!gateway_reference.matches("^[0-9]{16}$"), "gateway_reference is not of WCRD in transaction row " +transactionRow);
						assertEquals(transactionType, transaction_type, "Transaction type mismatch in transaction row " + transactionRow + ", expecetd : SALE but actual " +transaction_type);
						assertEquals(result, "SUCC", "transaction result not succ");
						
						if(checkForAuthAmount)
							assertEquals("1.0", amount, "amount mismatch, expected : 1.0 and actual " + amount);
					}
					else if (!switchFunc && (wcrdWeight == 0 || adynWeight == 100))
					{
						assertEquals("ADYN", gateway, "Gateway mismatch in transaction row " + transactionRow + ", expecetd : ADYN but actual " +gateway);
						assertTrue(gateway_reference.matches("^[0-9]{16}$"), "gateway_reference is not of ADYN in transaction row " +transactionRow);
						assertEquals(transactionType, transaction_type, "Transaction type mismatch in transaction row " + transactionRow + ", expecetd : SALE but actual " +transaction_type);
						assertEquals(result, "SUCC", "transaction result not succ");
						
						if(checkForAuthAmount)
							assertEquals("0.0", amount, "amount mismatch, expected : 0.0 and actual " + amount);	
					}
				}
			}
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void verifyDefaultAuthAmount(String paymentGateway, String amount)
	{
		String query_wcrd = "select DefaultAuthAMount as Amount from dbo.Gateway where GatewayCD = '" + paymentGateway  + "'";
		Map amountWcrd = DbUtil.getInstance("Ecommerce").getResultSet(query_wcrd);
		assertEquals(amount, amountWcrd.get("Amount").toString(), "DefaultAuthAMount mismatch, expecetd : 1.00 but actual " +amountWcrd.get("Amount"));
	}
}
