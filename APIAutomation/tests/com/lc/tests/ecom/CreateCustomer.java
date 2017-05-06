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
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.HeaderManager;

public class CreateCustomer extends EComCoreComponents {
	
	String subscriptionId = "";
	String customerId = "";
	String cuid = "";
	String email = "";
	
	@BeforeMethod
	public void init() {
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test
	public void createCustomer() {
		try {
			JSONObject json = new JSONObject();
			json.put("cuid", "884564");
			json.put("email", "rajan@livecareer.com");
			String url = BASE_URL + "/v1/customers";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.post(url, headers, json.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			// DB Check
			JSONObject custID = (JSONObject) respJson.get("customer");
			customerId = custID.get("id").toString();
			cuid = custID.get("cuid").toString();
			email = custID.get("email").toString();
			String query_session = "select * from [Ecommerce].[dbo].[customer] where customerid = " + customerId;
			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
			Map tmpData_session = list.get(0);
			assertEquals(tmpData_session.get("CUID").toString(), cuid, "cuid Mismatch");
			// DB Check end
			assertEquals(cuid, "884564", "CUID Mismatch");
			assertEquals(email, "rajan@livecareer.com", "EMAIL Mismatch");
			System.out.println(respJson);
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void createCustomerWithInvalidEmailId() {
		try {
			JSONObject json = new JSONObject();
			json.put("cuid", "884564");
			json.put("email", "rajanlivecareer.com");
			String url = BASE_URL + "/v1/customers";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.post(url, headers, json.toString());
			assertTrue(400, response.code, "Response code mismatch");
			assertNotNull(response.message, "Getting NULL as response");
			JSONObject respJson = new JSONObject(response.message);
			JSONObject modelState = respJson.getJSONObject("ModelState");
			String expectedErrMessage = "[\"E-mail is not valid\"]";
			assertEquals(expectedErrMessage, modelState.getString("request.email"), "message mismatch");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
}
