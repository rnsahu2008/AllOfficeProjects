package com.lc.tests.ecom;
import static com.lc.constants.Constants_Ecom.BASE_URL;

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

public class PostProperties extends EComCoreComponents {
	String subscriptionId = "";
	String eventId = "";
	String cuid = "";
	
	@BeforeMethod
	public void init() {
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void postProperties() {
		try {
			JSONObject json = new JSONObject();
			JSONArray properties = new JSONArray();
			JSONObject pro1 = new JSONObject();
			JSONObject pro2 = new JSONObject();
			pro1.put("key", "rajan");
			pro1.put("value", "testvalue1");
			pro2.put("key", "rajan");
			pro2.put("value", "testvalue2");
			properties.put(pro1);
			properties.put(pro2);
			json.put("properties", properties);
			String query_session = "select top (10) * from [Ecommerce].[dbo].[SubscriptionEvent] order by 1 desc";
			List<Map> list = DbUtil.getInstance("Ecommerce").getResultSetList(query_session);
			Map tmpData_session = list.get(0);
			subscriptionId = tmpData_session.get("SubscriptionID").toString();
			eventId = tmpData_session.get("SubscriptionEventID").toString();
			String url = BASE_URL + "/v1/subscriptions/" + subscriptionId + "/events/" + eventId + "/properties";
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
			ResponseBean response = HttpService.post(url, headers, json.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "Getting NULL as response");
			assertTrue(200, response.code, "Response is Ok");
		} catch (Exception e) {
			fail("Getting exception::" + e.getMessage());
		}
		s_assert.assertAll();
	}
}
