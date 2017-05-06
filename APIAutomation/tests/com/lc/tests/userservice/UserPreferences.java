package com.lc.tests.userservice;

import static com.lc.constants.Constants_UserService.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.UserServiceCoreComponents;
import com.lc.dataprovider.UserService_DP;
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.HeaderManager;

public class UserPreferences extends UserServiceCoreComponents{
	
	private String partyId = "";
	HashMap<String, String> headers = null;
	String user_uid = "";
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@BeforeClass
	public void classInit()
	{
		headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
		partyId = setPartyID();
		user_uid = getUserUIDByPartyID(partyId);
	}
	
	@Test(dataProviderClass=UserService_DP.class, dataProvider="getUserUrl")
	public void postUserPreferences(String getUserUrl){
		try{
			JSONArray dataToPost = createDataForUserPreference();
			ResponseBean response;
			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.post(GET_USER_URL_V1 + partyId + "/preferences", headers, dataToPost.toString());
			}
			else
			{
				response = HttpService.post(GET_USER_URL_V2 + user_uid + "/preferences", headers, dataToPost.toString());
			}
			checkSuccessCode(response.code);
			JSONArray respJson = new JSONArray(response.message);
			for(int i = 0; i<respJson.length(); i++){
				if((respJson.getJSONObject(i).getString("code").equals((dataToPost.getJSONObject(0).get("code").toString()))) && (respJson.getJSONObject(i).getString("value").equals((dataToPost.getJSONObject(0).get("value").toString())))){
					assertEquals(dataToPost.getJSONObject(0).getString("code"),respJson.getJSONObject(i).getString("code"),"Code Mismatch");
					assertEquals(dataToPost.getJSONObject(0).getString("value"),respJson.getJSONObject(i).getString("value"),"Value Mismatch");
				}
			}
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@Test(dataProviderClass=UserService_DP.class, dataProvider="getUserUrl")
	public void getUserPreferences(String getUserUrl){
		try{
			ResponseBean response;
			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.get(GET_USER_URL_V1 + partyId + "/preferences", headers);
			}
			else
			{
				response = HttpService.get(GET_USER_URL_V2 + user_uid + "/preferences", headers);
			}
			
			assertResponseCode(response.code, 404);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("User Preferences not found", respJson, "description");
			assertEquals("U007", respJson, "error_code");
		}
		catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

}
