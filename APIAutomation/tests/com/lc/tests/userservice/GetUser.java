package com.lc.tests.userservice;
import static com.lc.constants.Constants_UserService.*;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.UserServiceCoreComponents;
import com.lc.dataprovider.UserService_DP;
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.HeaderManager;

public class GetUser extends UserServiceCoreComponents{

	public String partyIdForNs = ""; //party id for negative scenarios
	String partyIdForPs = "";  //party id for positive scenarios
	String emailAddress = "";
	HashMap<String, String> headers = null;
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@BeforeClass
	public void classInit()
	{
		partyIdForNs = setPartyID();
		headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
		JSONObject respJson =  createUser();
		try {
			partyIdForPs = respJson.getString("party_id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Test(dataProviderClass=UserService_DP.class, dataProvider="getUserUrl")
	public void getUser(String getUserUrl){
		try {
			String party_Id = setPartyID();
			String user_uid = getUserUIDByPartyID(party_Id);
			ResponseBean response;
			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.get(GET_USER_URL_V1 + party_Id, headers);
			}
			else
			{
				response = HttpService.get(GET_USER_URL_V2 + user_uid, headers);
			}
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals(party_Id, respJson.getString("party_id"),"PartyId mismatch in response");
			String query_person = "Select FirstName, LastName, EmailAddress, ReferrerID, CountryCD, Phone, MobilePhone, WorkPhone from Person where partyID = " + party_Id;
			String query_user = "Select PartyID, UserUID, isactive  from [user] where partyID = " + party_Id;
			Map person = DbUtil.getInstance("Livecareer").getResultSet(query_person);
			Map user = DbUtil.getInstance("Livecareer").getResultSet(query_user);
			assertEquals(person.get("FirstName").toString(), respJson.getString("first_name"), "First Name Mismatch");
			assertEquals(person.get("LastName").toString(), respJson.getString("last_name"), "Last Name Mismatch");
			assertEquals(person.get("EmailAddress").toString(), respJson.getString("email"), "Email Address Mismatch");
			assertEquals(person.get("ReferrerID").toString(), respJson.getString("referrer_id"), "ReferrerID Mismatch");
			assertEquals(person.get("CountryCD").toString(), respJson.getString("country_cd"), "CountryCD Mismatch");
			assertEquals(person.get("Phone").toString(), respJson.getString("phone_no"), "phone number Mismatch");
			assertEquals(person.get("MobilePhone").toString(), respJson.getString("mobile_no"), "mobile phone number Mismatch");
			assertEquals(person.get("WorkPhone").toString(), respJson.getString("work_phone"), "WorkPhone phone number Mismatch");
			assertEquals(user.get("PartyID").toString(), respJson.getString("party_id"), "partyID Mismatch");
			assertEquals(user.get("UserUID").toString(), respJson.getString("user_uid"), "UserUID Mismatch");
			assertEquals(user.get("isactive").toString(), (respJson.getString("is_active").equals("true"))? "1" : "0", "isActive Flag Mismatch");
			
		}catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass = UserService_DP.class , dataProvider = "getUserData")
	public void getUserNegativeScenarios(String partyId){
		try{
			String url = GET_USER_URL_V1 + partyId;
			ResponseBean response;
			response = HttpService.get(url, headers);
			
			assertResponseCode(response.code, 404);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("There is no such User.", respJson, "description");
			assertEquals("U001", respJson, "error_code");
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test
	public void callAPIWithIncorrectTokenValue(){
		try{
			String access_token_invalid = "Bearer "+ RandomStringUtils.randomAscii(20); 
			HashMap<String, String> headers = HeaderManager.getHeaders("LCAUS", access_token_invalid, "1234");
			String url = GET_USER_URL_V1 + partyIdForNs;
			ResponseBean response;
			response = HttpService.get(url, headers);
			assertResponseCode(response.code, 403);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("The request could not authenticated. Please verify request headers.", respJson, "description");
			assertEquals("U024", respJson, "error_code");
			
		}catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test
	public void callAPIWithIncorrectClientCD(){
		try{
			HashMap<String, String> headers = HeaderManager.getHeaders(RandomStringUtils.randomAlphabetic(5).toUpperCase(), access_token, "1234");
			String url = GET_USER_URL_V1 + partyIdForNs;
			ResponseBean response;
			response = HttpService.get(url, headers);
			assertResponseCode(response.code, 403);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("Request cannot be served for this client.", respJson, "description");
			assertEquals("U022", respJson, "error_code");
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test
	public void getUserByEmailID()
	{
		try
		{	
			String url = BASE_URL + "/v1/emails?email=" + URLEncoder.encode(emailAddress, "UTF-8");
			//String url = GET_USER_URL_V1 + "?email=" + URLEncoder.encode(emailAddress, "UTF-8");
			ResponseBean response = HttpService.get(url, headers); 
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals(partyIdForPs, respJson.getString("party_id"), "partyID Mismatch");
			assertEquals(getUserUIDByPartyID(partyIdForPs), respJson.getString("user_uid"), "UserUID Mismatch");
			
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getUserByEmailIDWithNonExistingEmailInDBTable()
	{
		try
		{
			String url = BASE_URL + "/v1/emails?email=" + URLEncoder.encode("raj" + RandomStringUtils.randomAlphanumeric(5) + "@lc.com", "UTF-8");
			//String url = GET_USER_URL_V1 + "?email=" + URLEncoder.encode("raj" + RandomStringUtils.randomAlphanumeric(5) + "@lc.com", "UTF-8");
			ResponseBean response = HttpService.get(url, headers); 
			JSONObject respJson = new JSONObject(response.message);
			assertResponseCode(response.code, 404);
			assertEquals("We cannot find an account with the email address you've entered. Please check the email address or try another email address.", respJson, "description");
			assertEquals("U019", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test
	public void checkforExistingUserWithMandatoryParameters()
	{
		try
		{
			String url = BASE_URL + "/v1/emails?email=" + URLEncoder.encode(emailAddress, "UTF-8");
			//String url = GET_USER_URL_V1 + "?email=" + URLEncoder.encode(emailAddress, "UTF-8");
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("password", "123456");
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString()); 
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals(partyIdForPs, respJson.getString("party_id"), "partyID Mismatch");
			assertEquals(getUserUIDByPartyID(partyIdForPs), respJson.getString("user_uid"), "UserUID Mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void checkforExistingUserWithoutMandatoryParameters()
	{
		try
		{
			String url = BASE_URL + "/v1/emails?email=" + URLEncoder.encode(emailAddress, "UTF-8");
			//String url = GET_USER_URL_V1 + "?email=" + URLEncoder.encode(emailAddress, "UTF-8");
			JSONObject dataToPost = new JSONObject();
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString()); 
			JSONObject respJson = new JSONObject(response.message);
			assertResponseCode(response.code, 500);
			assertEquals("Oops, There was an internal Error", respJson, "description");
			assertEquals("API_ERROR", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void checkforExistingUserAPIWithNonExistingEmailInDBTable()
	{
		try
		{
			String url = BASE_URL + "/v1/emails?email=" + URLEncoder.encode("raj" + RandomStringUtils.randomAlphanumeric(5) + "@lcc.com", "UTF-8");
			//String url = GET_USER_URL_V1 + "?email=" + URLEncoder.encode("raj" + RandomStringUtils.randomAlphanumeric(5) + "@lcc.com", "UTF-8");
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("password", "123456");
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString()); 
			JSONObject respJson = new JSONObject(response.message);	
			assertResponseCode(response.code, 404);
			assertEquals("Invalid Username / Password", respJson, "description");
			assertEquals("U011", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test
	public void getSessionDetailsByInvalidSessionGuid()
	{
		try
		{
			//get session details by session guid
			String url = BASE_URL + "/v1/sessions/hsdfhowefdlflsdf9430ej";
			ResponseBean response = HttpService.get(url,headers);
			JSONObject respJson = new JSONObject(response.message);
			
			assertResponseCode(response.code, 500);
			assertEquals("Oops, There was an internal Error", respJson, "description");
			assertEquals("API_ERROR", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(description = "")
	public void getUserProduct()
	{
		try
		{
			JSONObject createUserObject = createUser(postDataRegisteredUser());
			String userUid = createUserObject.getString("user_uid");
			String partyId = createUserObject.getString("party_id");
			
			String query_StageID = "select top 3 * from ProductUserStage where ProductID = (Select ProductID from Product where ProductCD = 'RSM') and StrategyID = 14 order by 3";
			List<Map> list_StageID = DbUtil.getInstance("Livecareer").getResultSetList(query_StageID);
			
			//call update user stage API for user stage id
			JSONObject respJson = updateUserStage(list_StageID.get(1).get("StageID").toString(), userUid);
			assertEquals("The user stage has been saved", respJson.getString("message"), "Message mismatch - In Updating User Stage");
			
			String url = GET_USER_URL_V1 + userUid + "/products";
			ResponseBean response = HttpService.get(url, headers);
			
			checkSuccessCode(response.code);
			
			String productUser_query = "select ProductID, UserLevelID, RegisteredOn from productuser where partyid = " + partyId;
			Map productUser = DbUtil.getInstance("Livecareer").getResultSet(productUser_query);
			
			JSONArray respJSON = new JSONArray(response.message);
			assertEquals(productUser.get("ProductID").toString(), respJSON.getJSONObject(0).getString("product_id"), "product id mismatch");
			assertEquals(productUser.get("UserLevelID").toString(), respJSON.getJSONObject(0).getBoolean("is_premium") == false ? "0" : "1", "is_premium mismatch");
			assertEquals(productUser.get("RegisteredOn").toString().substring(0, 15), respJSON.getJSONObject(0).getString("registered_on").substring(0, 15), "product id mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void callGetUserProductApiWithUserWhichIsNotAssociatedWithAnyProduct()
	{
		try
		{
			String userUid = getUserUIDByPartyID(partyIdForPs);
			String url = GET_USER_URL_V1 + userUid + "/products";
			
			ResponseBean response = HttpService.get(url, headers);
			assertResponseCode(response.code, 404);
			
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("The user has not used any product.", respJson, "description");
			assertEquals("U025", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	private JSONObject createUser()
	{
		try
		{
			emailAddress = "first.last" + getDateTime(new Date(), format, 0) + ++counter + "@lc.com";
			JSONObject dataCreateUser = new JSONObject();		
			dataCreateUser.put("type", "registered");
			dataCreateUser.put("email_address", emailAddress);
			dataCreateUser.put("password", "123456");
			dataCreateUser.put("reset_pwd", true);
			dataCreateUser.put("visit_id", "127854");		
			dataCreateUser.put("ip_address", "138.91.242.92");
			dataCreateUser.put("is_active", true);
			String url = GET_USER_URL_V1;
			ResponseBean response = HttpService.post(url,HeaderManager.getHeaders("LCAUS", access_token, "1234"), dataCreateUser.toString());
			checkSuccessCode(response.code);
			return new JSONObject(response.message);
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
