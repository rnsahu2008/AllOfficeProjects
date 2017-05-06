package com.lc.tests.userservice;

import static com.lc.constants.Constants_UserService.AUTHENTICATE_USER_URL;
import static com.lc.constants.Constants_UserService.BASE_URL;
import static com.lc.constants.Constants_UserService.GET_USER_URL_V1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
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

public class End2EndScenarios extends UserServiceCoreComponents {
	
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
	}
	
	/*
	 *  Scenario 1 - Create a registered user + Get User + Add Optin + Get Optin + Add Preference + Get Preference
	 */
	
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@Test
	public void scenarioOne(){
		try{
			//create registered user
			JSONObject dataToPost = postDataRegisteredUser();
			String url = GET_USER_URL_V1;
			ResponseBean response = HttpService.post(url,headers, dataToPost.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			String query_person = "Select PartyID from Person where partyID = " + respJson.getInt("party_id");
			String query_user = "Select UserUID from [user] where partyID = " + respJson.getInt("party_id");
			List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_person);
			Map tmpData_Person = list_person.get(0);
			List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);
			Map tmpData_User = list_user.get(0);	
			assertEquals(tmpData_Person.get("PartyID").toString(), respJson.getString("party_id"), "partyID Mismatch");
			assertEquals(tmpData_User.get("UserUID").toString(), respJson.getString("user_uid"), "UserUID Mismatch");
			String partyId = respJson.getString("party_id");

			//GetUser
			url = GET_USER_URL_V1 + partyId;
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			assertEquals(partyId, respJson.getString("party_id"),"PartyId mismatch in response");

			// Add User Optin
			url = GET_USER_URL_V1 + partyId + "/optins";
			String optin = "NEWS";
			JSONArray dataToPostOptin = new JSONArray();
			JSONObject parentJson = new JSONObject();
			parentJson.put("response","1");
			parentJson.put("code", optin);
			dataToPostOptin.put(parentJson);
			//dataToPostOptin.createDataForUserOptin(optin);
			response = HttpService.post(url, headers, dataToPostOptin.toString());
			checkSuccessCode(response.code);
			JSONArray respJsonArrayPost = new JSONArray(response.message);
			for(int i = 0;i<respJsonArrayPost.length();i++){
				JSONObject jObj = respJsonArrayPost.getJSONObject(i);
				if(jObj.get("code").equals(optin)){
					assertTrue(1, jObj.getInt("response"), "Mismatch of response value");
				}
			}

			//Get user optins
			url = GET_USER_URL_V1 + partyId + "/optins";
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			JSONArray respJsonArray = new JSONArray(response.message);
			assertTrue(1, respJsonArray.length(), "Mismatch of length");
			for(int i = 0;i<respJsonArray.length();i++){
				assertTrue(1, Integer.parseInt(respJsonArray.getJSONObject(i).get("response").toString()), "Value of Optin response is incorrect");
			}

			//Add Preference
			JSONArray dataToPostPref = createDataForUserPreference();
			url = GET_USER_URL_V1 + partyId + "/preferences";
			response = HttpService.post(url, headers, dataToPostPref.toString());
			checkSuccessCode(response.code);
			JSONArray respJsonPref = new JSONArray(response.message);
			for(int i = 0; i<respJsonPref.length(); i++){
				if((respJsonPref.getJSONObject(i).getString("code").equals((dataToPostPref.getJSONObject(0).get("code").toString()))) && (respJsonPref.getJSONObject(i).getString("value").equals((dataToPostPref.getJSONObject(0).get("value").toString())))){
					assertEquals(dataToPostPref.getJSONObject(0).getString("code"),respJsonPref.getJSONObject(i).getString("code"),"Code Mismatch");
					assertEquals(dataToPostPref.getJSONObject(0).getString("value"),respJsonPref.getJSONObject(i).getString("value"),"Value Mismatch");
				}
			}

			// Get Preferences
			url = GET_USER_URL_V1 + partyId + "/preferences";
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			JSONArray respJsonPrefGet = new JSONArray(response.message);
			int size = respJsonPrefGet.length();
			String query = "select PreferenceCD, Value from UserPreference where PartyId = " + partyId;
			List<Map> list = DbUtil.getInstance("Livecareer").getResultSetList(query);
			for(int i=0;i<size ;i++){
				Map tmpData = list.get(i);
				Set<String> key = tmpData.keySet();
				assertEquals(tmpData.get("PreferenceCD").toString(), respJsonPrefGet.getJSONObject(i).getString("code"), "Code Mismatch");
				assertEquals(tmpData.get("Value").toString(), respJsonPrefGet.getJSONObject(i).getString("value"), "Value Mismatch");
			}

		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	/*
	 *  Scenario 2 - Create a registered user + Deactivate the user + Authenticate the user
	 */
	
	@Test
	public void scenarioTwo(){
		try{
			// Create Registered Active User
			JSONObject dataToPost = postDataRegisteredUser();
			JSONObject respJson = createUser(dataToPost);
			
			// Deactivate the user
			int partyId = respJson.getInt("party_id");
			JSONObject dataToPut = createDataForRegisteredUserUpdate(dataToPost);
			dataToPut.put("email_address", dataToPost.get("email_address"));
			dataToPut.put("password", dataToPost.get("password"));
			
			String url = GET_USER_URL_V1 + partyId;
			ResponseBean response = HttpService.put(url,headers, dataToPut.toString());
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);

			// Authenticate the user
			dataToPost = authenticateUserPostData(String.valueOf(partyId));
			url = BASE_URL + AUTHENTICATE_USER_URL;
			response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 404);
			respJson = new JSONObject(response.message);
			JSONObject expectedJSON =  new JSONObject("{\"error_code\":\"U012\",\"description\":\"Your Account has been Deactivated.\"}");
			assertJsonObjectEquality(expectedJSON, respJson);

		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	/*
	 * Scenario 3 - Generate Access Token with LCAUS and create user with ClientCD other than LCAUS
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void scenarioThree(){
		try{
			JSONObject dataToPost = postDataRegisteredUser();
			String url = GET_USER_URL_V1;
			ResponseBean response = HttpService.post(url, HeaderManager.getHeaders("JEMPR", access_token, "1234"), dataToPost.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			String query_person = "Select PartyID, PortalID from Person where partyID = " + respJson.getInt("party_id");
			String query_user = "Select UserUID from [user] where partyID = " + respJson.getInt("party_id");
			List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_person);
			Map tmpData_Person = list_person.get(0);
			List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);
			Map tmpData_User = list_user.get(0);	
			assertEquals(tmpData_Person.get("PartyID").toString(), respJson.getString("party_id"), "partyID Mismatch");
			assertEquals(tmpData_User.get("UserUID").toString(), respJson.getString("user_uid"), "UserUID Mismatch");
			assertEquals("18", tmpData_Person.get("PortalID").toString(),"PortalID is incorrect corresponding to ClientCD");
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void scenarioFour_USRS_37(){
		try{
			JSONObject dataToPost = new JSONObject();
			String newPassword = RandomStringUtils.randomNumeric(8);
			String partyId = "9945003";
			dataToPost.put("password", newPassword);
			dataToPost.put("reset", 1);
			String url = GET_USER_URL_V1 + partyId+ "/password";
			ResponseBean response;
			response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertTrue(Boolean.parseBoolean(respJson.get("reset_password").toString()), "Mismatch in response");
			String email = respJson.getString("email");
			String user_uid = respJson.getString("user_uid");
			String partyid = respJson.getString("party_id");
			dataToPost = authenticateUserPostData_USRS37(email,newPassword);
			url = BASE_URL + AUTHENTICATE_USER_URL;
			response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			assertEquals(user_uid, respJson, "user_uid");
			assertEquals(partyid, respJson, "party_id");
			dataToPost = authenticateUserPostData_USRS37(email,newPassword+"-QA");
			url = BASE_URL + AUTHENTICATE_USER_URL;
			response = HttpService.post(url, headers, dataToPost.toString());
			respJson = new JSONObject(response.message);
			assertResponseCode(response.code, 404);
			assertEquals("Invalid Username / Password", respJson, "description");
			assertEquals("U011", respJson, "error_code");
			JSONObject dataToPostUpdate = new JSONObject();
			dataToPostUpdate.put("password", newPassword+"-QA");
			dataToPostUpdate.put("reset", 1);
			partyId = "9945004";
			url = GET_USER_URL_V1 + partyId + "/password";
			response = HttpService.post(url, headers, dataToPostUpdate.toString());
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			user_uid = respJson.getString("user_uid");
			url = BASE_URL + AUTHENTICATE_USER_URL;
			response = HttpService.post(url, headers, dataToPost.toString());
			respJson = new JSONObject(response.message);
			checkSuccessCode(response.code);
			assertEquals(user_uid, respJson, "user_uid");
			assertEquals(partyId, respJson, "party_id");
			
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	//USRS - 136. 1. create guest user 2. call update user API with email and password 3. call insert user role API with user role. Verify user role should get inserted successfully.
	@Test
	public void createGuestUser_UpdateUserWithEmailAndPassword_InsertUserRole(){
		try{
			JSONObject dataToPost = postDataGuestUser();
			JSONObject respJson = createUser(dataToPost);
			
			//Update user with email and password
			JSONObject dataToPut = createDataForGuestUserUpdate(dataToPost);
			String party_Id = respJson.getString("party_id");
			String user_uid = respJson.getString("user_uid");
			
			String url = GET_USER_URL_V1 + party_Id;
			
			ResponseBean response = HttpService.put(url, headers, dataToPut.toString());
			checkSuccessCode(response.code);
			
			//Add user role
			List<JSONObject> inputRoleList = new ArrayList<JSONObject>();
			List<JSONObject> expectedRoleList = new ArrayList<JSONObject>();
			
			JSONObject userRole = new JSONObject();
			userRole.put("id", 1);
			userRole.put("role", "User");		
			inputRoleList.add(userRole);
			
			expectedRoleList.add(userRole);		
			
			JSONArray expectedJArray = getRoleArray(expectedRoleList);
			
			response = HttpService.post(GET_USER_URL_V1 + user_uid  + "/roles", headers, getRoleArray(inputRoleList).toString());
			checkSuccessCode(response.code);
			JSONArray actualJArray = new JSONArray(response.message);
			assertJsonArrayEquality(expectedJArray, actualJArray);

		}catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	

}
