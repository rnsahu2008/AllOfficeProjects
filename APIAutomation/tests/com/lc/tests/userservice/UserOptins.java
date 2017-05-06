package com.lc.tests.userservice;

import static com.lc.constants.Constants_UserService.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class UserOptins extends UserServiceCoreComponents{
	
	private String partyId = "";
	private String userUID = "";
	HashMap<String, String> headers = null;
	
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
		userUID = getUserUIDByPartyID(partyId);
	}
	
	@Test(dataProviderClass=UserService_DP.class, dataProvider="getUserUrl")
	@SuppressWarnings("rawtypes")
	public void getUserOptins(String getUserUrl){
		try{
			ResponseBean response;
			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.get(GET_USER_URL_V1 + partyId + "/optins", headers);
			}
			else
			{
				response = HttpService.get(GET_USER_URL_V2 + userUID + "/optins", headers);
			}
			
			checkSuccessCode(response.code);
			JSONArray respJson = new JSONArray(response.message);
			String query_optins = "Select OptinID, Response from Useroptins where partyID = " + partyId;
			List<Map> list_optins = DbUtil.getInstance("Livecareer").getResultSetList(query_optins);
			assertTrue(list_optins.size(), respJson.length(), "Mismatch of Optins Count for a user");
			HashMap<Integer, String> optinCD = new HashMap<Integer, String>();
			optinCD = userOptinsMapping();
			for(int i = 0;i< list_optins.size();i++){
				String optinId = list_optins.get(i).get("OptinID").toString();
				String optinCode = optinCD.get(Integer.parseInt(optinId));					
				assertEquals(optinCode ,respJson.getJSONObject(i).getString("code") , "Incorrect optin code");
				assertTrue(Integer.parseInt(list_optins.get(i).get("Response").toString()), Integer.parseInt(respJson.getJSONObject(i).get("response").toString()), "Value of Optin response is incorrect");
			}	
		}
		catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass = UserService_DP.class , dataProvider = "getUserOptins")
	public void editUserOptins(String optin){
		try{
			JSONArray dataToPost = createDataForUserOptin(optin, partyId);
			String url = GET_USER_URL_V1 + partyId + "/optins";
			ResponseBean response;
			response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			JSONArray respJson = new JSONArray(response.message);
			for(int i = 0;i<respJson.length();i++){

				JSONObject jObj = respJson.getJSONObject(i);
				if(respJson.getJSONObject(i).get("code").toString().equals(optin)){
					assertTrue(dataToPost.getJSONObject(0).getInt("response"), jObj.getInt("response"), "Mismatch of response value");
				}
			}
		}
		catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass = UserService_DP.class , dataProvider = "getUserInvalidCodeOptins")
	public void editUserOptinsInvalidCode(String optin){
		try{
			JSONArray dataToPost = createDataForUserOptinInvalid(optin);
			String url = GET_USER_URL_V1 + partyId + "/optins";
			ResponseBean response;
			response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 500);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("API_ERROR",respJson.getString("error_code"),"Incorrect Error Code");
			assertEquals("Oops, There was an internal Error",respJson.getString("description"),"Incorrect Description");	
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass = UserService_DP.class , dataProvider = "getUserInvalidResponseOptins")
	public void editUserOptinsInvalidResponse(String resp){
		try{
			JSONArray dataToPost = createDataForUserOptinInvalidResponse(resp);
			String url = GET_USER_URL_V1 + partyId + "/optins";
			ResponseBean response;
			response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 400);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("The request is invalid.",respJson.getString("Message"), "Incorrect Error Code");	
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass = UserService_DP.class , dataProvider = "getUserOptins")
	public void saveUserOptins_V2(String optin){
		try{
			JSONArray dataToPost = createDataForUserOptin(optin, partyId);
			String url = GET_USER_URL_V2 + userUID + "/optins";
			ResponseBean response;
			response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			JSONArray respJson = new JSONArray(response.message);
			for(int i = 0;i<respJson.length();i++){

				JSONObject jObj = respJson.getJSONObject(i);
				if(respJson.getJSONObject(i).get("code").toString().equals(optin)){
					assertTrue(dataToPost.getJSONObject(0).getInt("response"), jObj.getInt("response"), "Mismatch of response value");
				}
			}
		}
		catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
		
}
