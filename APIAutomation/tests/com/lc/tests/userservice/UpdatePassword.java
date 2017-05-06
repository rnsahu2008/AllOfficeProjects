package com.lc.tests.userservice;

import static com.lc.constants.Constants_UserService.*;

import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
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

public class UpdatePassword extends UserServiceCoreComponents{

	public String partyId = "";
	HashMap<String, String> headers = null;
	private String userUID = "";

	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();	
		Common.apiLogInfo.clear();
	}

	@BeforeClass
	public void classInit()
	{
		partyId = setPartyID();
		headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
		userUID = getUserUIDByPartyID(partyId);
	}

	@Test(dataProviderClass=UserService_DP.class, dataProvider="getUserUrl")
	public void updateUserPassword(String getUserUrl){
		try{
			JSONObject dataToPost = new JSONObject();
			String newPassword = RandomStringUtils.randomAscii(10);
			dataToPost.put("password", newPassword);
			dataToPost.put("reset", 1);
			ResponseBean response;
			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.post(GET_USER_URL_V1 + partyId + "/password", headers, dataToPost.toString());
			}
			else
			{
				response = HttpService.put(GET_USER_URL_V2 + userUID + "/password", headers, dataToPost.toString());
			}
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertTrue(Boolean.parseBoolean(respJson.get("reset_password").toString()), "Mismatch in response");

		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass=UserService_DP.class, dataProvider="getUserUrl")
	public void updatePasswordAndAuthenticateUserNewPassword(String getUserUrl){

		try{
			JSONObject dataToPost = new JSONObject();
			String newPassword = RandomStringUtils.randomNumeric(6);
			dataToPost.put("password", newPassword);
			dataToPost.put("reset", 1);
			ResponseBean response;
			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.post(GET_USER_URL_V1 + partyId + "/password", headers, dataToPost.toString());
			}
			else
			{
				response = HttpService.put(GET_USER_URL_V2 + userUID + "/password", headers, dataToPost.toString());
			}
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			System.out.println(respJson);
			assertTrue(Boolean.parseBoolean(respJson.get("reset_password").toString()), "Mismatch in response");
			JSONObject dataToPostAuth = authenticateUserPostDataWithNewPwd(partyId,newPassword);
			String url_auth = BASE_URL + AUTHENTICATE_USER_URL;
			response = HttpService.post(url_auth, headers, dataToPostAuth.toString());
			checkSuccessCode(response.code);
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass=UserService_DP.class, dataProvider="getUserUrl")
	public void updatePasswordAndAuthenticateUserOldPassword(String getUserUrl){
		try{
			JSONObject dataToPost = new JSONObject();
			String newPassword = RandomStringUtils.randomNumeric(6);
			dataToPost.put("password", newPassword);
			dataToPost.put("reset", 1);
			ResponseBean response;
			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.post(GET_USER_URL_V1 + partyId + "/password", headers, dataToPost.toString());
			}
			else
			{
				response = HttpService.put(GET_USER_URL_V2 + userUID + "/password", headers, dataToPost.toString());
			}
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			System.out.println(respJson);
			assertTrue(Boolean.parseBoolean(respJson.get("reset_password").toString()), "Mismatch in response");
			JSONObject dataToPostAuth = authenticateUserPostData(partyId);
			String url_auth = BASE_URL + AUTHENTICATE_USER_URL;
			response = HttpService.post(url_auth, headers, dataToPostAuth.toString());
			assertResponseCode(response.code, 404);
			respJson = new JSONObject(response.message);
			assertEquals("Invalid Username / Password", respJson, "description");
			assertEquals("U011", respJson, "error_code");
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}	
		s_assert.assertAll();
	}

	@Test(dataProviderClass=UserService_DP.class, dataProvider="getUserUrl")
	public void updatePasswordCheckBoundaryValueOfPassword(String getUserUrl){
		try{
			JSONObject dataToPost = new JSONObject();
			String newPasswordMin = RandomStringUtils.randomAscii(5);
			dataToPost.put("password", newPasswordMin);
			dataToPost.put("reset", 1);
			ResponseBean response;
			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.post(GET_USER_URL_V1 + partyId + "/password", headers, dataToPost.toString());
			}
			else
			{
				response = HttpService.put(GET_USER_URL_V2 + userUID + "/password", headers, dataToPost.toString());
			}
			assertResponseCode(response.code, 400);
			JSONObject respJson = new JSONObject(response.message);
			JSONObject expectedJSON = new JSONObject("{\"ModelState\":{\"objPwd\":[\"The length must be between 6 characters and 20 characters\"]},\"Message\":\"The request is invalid.\"}");
			assertJsonObjectEquality(expectedJSON, respJson);
			String newPasswordMax = RandomStringUtils.randomAscii(21);
			dataToPost.put("password", newPasswordMax);
			dataToPost.put("reset", 1);

			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.post(GET_USER_URL_V1 + partyId + "/password", headers, dataToPost.toString());
			}
			else
			{
				response = HttpService.put(GET_USER_URL_V2 + userUID + "/password", headers, dataToPost.toString());
			}
			assertResponseCode(response.code, 400);
			respJson = new JSONObject(response.message);
			expectedJSON = new JSONObject("{\"ModelState\":{\"objPwd\":[\"The length must be between 6 characters and 20 characters\"]},\"Message\":\"The request is invalid.\"}");
			assertJsonObjectEquality(expectedJSON, respJson);
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass = UserService_DP.class,dataProvider = "getPassword")
	public void checkPasswordValidationValues(String password){
		try{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("password", password);
			dataToPost.put("reset", 1);
			String url = GET_USER_URL_V1 + partyId + "/password";
			ResponseBean response;
			response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertTrue(Boolean.parseBoolean(respJson.get("reset_password").toString()), "Incorrect Value of reset_password appear in response");
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass=UserService_DP.class, dataProvider="getUserUrl")
	public void updatePasswordWithBlankValue(String getUserUrl){
		try{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("password", "");
			dataToPost.put("reset", 1);
			ResponseBean response;
			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.post(GET_USER_URL_V1 + partyId + "/password", headers, dataToPost.toString());
			}
			else
			{
				response = HttpService.put(GET_USER_URL_V2 + userUID + "/password", headers, dataToPost.toString());
			}
			assertTrue(400, response.code, "Incorrect Response Code");
			JSONObject respJson = new JSONObject(response.message);
			String expectedModelState = "{\"objPwd.password\":[\"Password is required\"]}"; 
			String expectedMessage = "The request is invalid.";	
			assertEquals(expectedMessage, respJson,"Message");
			assertEquals(expectedModelState, respJson,"ModelState");
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//update password call should not get executed for guest user
	@Test
	public void userServiceTicketUSRS56()
	{
		try
		{
			JSONObject dataCreateUser = new JSONObject();
			dataCreateUser.put("type", "guest");
			dataCreateUser.put("visit_id", "127854");
			dataCreateUser.put("ip_address", "128.89.56.1");
			dataCreateUser.put("is_active", true);

			JSONObject respJson = createUser(dataCreateUser);
			String guestPartyId = respJson.getString("party_id");
			
			JSONObject dataToPost = new JSONObject();
			String newPassword = RandomStringUtils.randomAscii(10);
			dataToPost.put("password", newPassword);
			dataToPost.put("reset", 1);
			String url = GET_USER_URL_V1 + guestPartyId + "/password";
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			respJson = new JSONObject(response.message);
			assertEquals("The password cannot be updated for guest.", respJson, "description");
			assertEquals("U017", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
}
