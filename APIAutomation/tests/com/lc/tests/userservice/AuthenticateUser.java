package com.lc.tests.userservice;
import static com.lc.constants.Constants_UserService.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
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

public class AuthenticateUser extends UserServiceCoreComponents {

	HashMap<String, String> headers = null;
	String partyIdForNs = ""; // this partyId is for negative scenarios
	String guestUserUid = ""; //this guest user uid is for negative scenarios
	HashMap<String,String> dbValue = null;

	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@BeforeClass
	public void classInit()
	{
		headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
		partyIdForNs = setPartyID();
		JSONObject respJson = createUser(postDataGuestUser());
		try {
			guestUserUid = respJson.getString("user_uid");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//authenticate User
	@Test
	public void authenticateUser(){
		try {
			String partyId = setPartyID();
			if(partyId != null)
			{		
				//check for SessionId null
				String query_Person = "select SessionID,LastLoginDatetime,RegisteredOn from Person where PartyID = "+ partyId;
				List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_Person);	
				assertNull(list_person.get(0).get("SessionID"), "Session Id is not null");

				JSONObject dataToPost = authenticateUserPostData(partyId);
				String url = BASE_URL + AUTHENTICATE_USER_URL;

				ResponseBean response = HttpService.post(url, headers, dataToPost.toString());

				checkSuccessCode(response.code);
				JSONObject respJson = new JSONObject(response.message);
				String query_session = "select SessionGUID, SessionID, ProductID from [Session] where PartyID = "+ partyId;
				List<Map> list_session = DbUtil.getInstance("Livecareer").getResultSetList(query_session);
				String query_user = "select UserUID from [User] where PartyID = "+ partyId;	
				List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);
				list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_Person);	
				String query_portalUser = "Select Count(*) as Count from dbo.PortalUser where partyID = " + partyId;
				List<Map> list_portalUser = DbUtil.getInstance("Livecareer").getResultSetList(query_portalUser);

				assertEquals(partyId, respJson.getString("party_id"), "party_id Mismatch");
				assertEquals(list_user.get(0).get("UserUID").toString(), respJson.getString("user_uid"), "user_uid Mismatch");
				assertEquals(list_session.get(0).get("SessionGUID").toString(), respJson.getString("session_id"), "SessionID Mismatch");
				assertEquals(list_session.get(0).get("SessionID").toString(), respJson.getString("session_identity"), "session_identity Mismatch");
				assertEquals("[\"User\"]", respJson.getString("user_role"), "user_role Mismatch");
				assertEquals("10", list_session.get(0).get("ProductID").toString(), "ProductID Mismatch in Person table"); //For product_cd = RSM, ProductID = 10
				
				//verify an entry is made to sessionuseragenttype table
				String query = "select Count(*) as Count from sessionuseragenttype where sessionID = " + list_session.get(0).get("SessionID").toString();
				Map query_map = DbUtil.getInstance("Livecareer").getResultSet(query);
				assertEquals("1", query_map.get("Count").toString(), "No entry is made to sessionuseragenttype table.");
				
				//LastLoginDatetime field should not be null whereas RegisteredOn field null after authentication in Person table
				assertNotNull(list_person.get(0).get("LastLoginDatetime"), "LastLoginDatetime is null");
				assertNotNull(list_person.get(0).get("RegisteredOn"), "RegisteredOn is null");

				//There should be an entry in PortalUser table after authentication
				assertEquals("1", list_portalUser.get(0).get("Count").toString(), "Count Mismatch in PortalUser table");
				
				//LastLogin and ResgisteredOn field should not be null after authentication in PortalUser table
				query_portalUser = "Select LastLogin, RegisteredOn from dbo.PortalUser where partyID = " + partyId;
				list_portalUser = DbUtil.getInstance("Livecareer").getResultSetList(query_portalUser);			
				assertNotNull(list_portalUser.get(0).get("LastLogin"), "LastLogin is null");
				assertNotNull(list_portalUser.get(0).get("RegisteredOn"), "RegisteredOn is null");

				//check Session ID is updated on first user login 
				list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_Person);	
				assertEquals(list_person.get(0).get("SessionID").toString(), respJson.getString("session_identity"), "SessionID Mismatch in Person table");
				
			}
			else
			{
				fail("Party Id is " + partyId + " hence discontinuing execution of further test steps of this test case");
			}
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test
	public void authenticateUserWithInvalidProductCD(){
		try {
			JSONObject dataToPost = new JSONObject();
			String query = "Select EmailAddress, HitlogID from Person where partyID = " + partyIdForNs;
			List<Map> list = DbUtil.getInstance("Livecareer").getResultSetList(query);
			Map tmpData = list.get(0);
			dataToPost.put("username", tmpData.get("EmailAddress").toString());
			dataToPost.put("password", "123456");
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("product_cd", "hdsjdswq");
			dataToPost.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");

			String url = BASE_URL + AUTHENTICATE_USER_URL;

			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			JSONObject respJson = new JSONObject(response.message);

			assertResponseCode(response.code, 500);
			assertEquals("Oops, There was an internal Error", respJson, "description");
			assertEquals("API_ERROR", respJson, "error_code");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//authenticate User with invalid value of credentials
	@Test(dataProviderClass = UserService_DP.class , dataProvider = "getUserCredentials")
	public void authenticateUserInvalidCredentials(HashMap<String, String> list){
		try{
			JSONObject dataToPost = authenticateUserPostData(list);
			String url = BASE_URL + AUTHENTICATE_USER_URL;
			ResponseBean response;
			response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 404);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("Invalid Username / Password", respJson, "description");
			assertEquals("U011", respJson, "error_code");
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//authenticate Guest User By UserUID API (Create Session)
	@Test
	public void authenticateGuestUserByUserUID()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			dataToPost.put("hitlog_id", 127854);
			dataToPost.put("product_cd", "RSM");
			dataToPost.put("type", "guest");
			authenticateUserByUserUid(dataToPost);
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//authenticate user by UserUID : Valid Only for Guest User and administrator user
	@Test
	public void authenticateUserByUserUIDWithMandatoryParameters()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("product_cd", "RSM");
			dataToPost.put("type", "guest");
			dataToPost.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			authenticateUserByUserUid(dataToPost);
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//If we update value of product_cd in the new API, then there will be update in session table as well the productID*/
	@Test
	public void authenticateUserByUserUIDWithoutMandatoryParameter()
	{
		try
		{
			//authenticate user by UserUID : Valid Only for Guest User and administrator user
			JSONObject dataToPost = new JSONObject();
			String url = GET_USER_URL_V1 + guestUserUid + "/sessions";

			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());

			JSONObject respJson = new JSONObject(response.message);
			assertEquals("[\"The ip field is required.\"]", respJson.getJSONObject("ModelState").getString("loginRequest.ip"), "Message mismatch");	
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//Update Session API with All parameters for following fields :	1. PartyID, 2. AutoExtend
	@Test
	public void updateSessionWithAllParameters()
	{
		try
		{
			String newPartyId = "9" + RandomStringUtils.randomNumeric(7);
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("party_id", Integer.parseInt(newPartyId));
			dataToPost.put("auto_extend", false);

			updateSession(dataToPost, newPartyId, "0");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//Update Session API with Mandatory parameters 
	@Test
	public void updateSessionWithMandatoryParameters()
	{
		try
		{	
			String newPartyId = "9" + RandomStringUtils.randomNumeric(8);
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("party_id", Integer.parseInt(newPartyId));

			updateSession(dataToPost, newPartyId, "Null");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//Update Session API without Mandatory parameters 
	@Test
	public void updateSessionWithoutMandatoryParameters()
	{
		try
		{
			//get session guid by using "authenticate user by UserUID" api
			String session_id =  getSessionGuidByAuthenticatingUserByUserUID(guestUserUid);;

			//update session 
			session_id = session_id.replace("{", "%7B");
			session_id = session_id.replace("}", "%7D");
			String url = BASE_URL + "/v1/sessions/" + session_id;	

			JSONObject dataToPost = new JSONObject();

			ResponseBean response = HttpService.put(url, headers, dataToPost.toString());	
			JSONObject respJson = new JSONObject(response.message);

			JSONObject modelState = respJson.getJSONObject("ModelState");
			assertEquals("[\"The field party Id is mandatory for processing the request\"]", modelState.getString("SMObj.PartyId"), "Message mismatch");	
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//update Session With PartyID as null
	@Test
	public void updateSessionWithNullPartyID()
	{
		try
		{
			ResponseBean response = updateSessionWithInvalidValuesOfParameters(JSONObject.NULL, false);
			assertResponseCode(response.code, 400);
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//update Session With PartyID of String data type. data type of party_id is Integer
	@Test
	public void updateSessionWithPartyIDOfStringDatatype()
	{
		try
		{
			ResponseBean response = updateSessionWithInvalidValuesOfParameters(RandomStringUtils.randomAlphabetic(10), false);
			assertResponseCode(response.code, 400);
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//update Session With Invalid value for auto_extend parameter
	@Test
	public void updateSessionWithInvalidValueForAutoExtend()
	{
		try
		{
			String newPartyId = RandomStringUtils.randomNumeric(8);
			ResponseBean response = updateSessionWithInvalidValuesOfParameters(Integer.parseInt(newPartyId), "gfsdfu");
			assertResponseCode(response.code, 400);
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//Authenticate User By Session GUID Method got deprecated
	//authenticate User By SessionGUID 
	/*@Test
	public void authenticateUserBySessionGUID()
	{
		try
		{
			//create guest user
			JSONObject respJson = createUser(postDataRegisteredUser());
			String userUID = respJson.getString("user_uid");
			String party_Id = respJson.getString("party_id");

			//get session guid 
			respJson = getSessionGuidByLoginAsUser(party_Id);
			String session_id =  respJson.getString("session_id");
			session_id = session_id.replace("{", "%7B");
			session_id = session_id.replace("}", "%7D");

			//authenticate user by session GUID
			String url = BASE_URL + "/v1/sessions/" + session_id;
			ResponseBean response = HttpService.post(url, headers, "");
			respJson = new JSONObject(response.message);

			String query_session = "select SessionGUID, SessionID from [Session] where PartyID = " + party_Id;
			List<Map> list = DbUtil.getInstance("Livecareer").getResultSetList(query_session);
			Map tmpData_session = list.get(0);

			assertEquals(party_Id, respJson.getString("party_id"), "party_id Mismatch");
			assertEquals(userUID, respJson.getString("user_uid"), "user_uid Mismatch");
			assertEquals(tmpData_session.get("SessionGUID").toString(), respJson.getString("session_id"), "SessionID Mismatch");
			assertEquals(tmpData_session.get("SessionID").toString(), respJson.getString("session_identity"), "session_identity Mismatch");
			assertEquals("[\"User\"]", respJson.getString("user_role"), "user_role Mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}*/

	@SuppressWarnings("rawtypes")
	@Test
	/*
	 * New method used by deprecating authenticateUserBySessionGUID()
	 */
	public void getSessionDetails()
	{
		try
		{
			//create guest user
			JSONObject respJson = createUser(postDataRegisteredUser());
			String userUID = respJson.getString("user_uid");
			String party_Id = respJson.getString("party_id");

			//get session guid 
			respJson = getSessionGuidByLoginAsUser(party_Id);
			String session_id =  respJson.getString("session_id");
			session_id = session_id.replace("{", "%7B");
			session_id = session_id.replace("}", "%7D");

			//get Session Details
			String url = BASE_URL + "/v1/sessions/" + session_id;
			ResponseBean response = HttpService.get(url, headers);
			respJson = new JSONObject(response.message);

			String query_session = "select SessionGUID, SessionID, Sessionlastaccessed, Sessionexpiry, PortalID, ProductID from [Session] where PartyID = " + party_Id;
			List<Map> list = DbUtil.getInstance("Livecareer").getResultSetList(query_session);
			Map tmpData_session = list.get(0);
			String query_product = "Select ProductCD from Product where ProductID = " + tmpData_session.get("ProductID").toString();
			String query_portal = "Select PortalCD from Portal where PortalID = " + tmpData_session.get("PortalID").toString();
			dbValue = DbUtil.getInstance("Livecareer").getResultSet(query_product);
			String productCD = dbValue.get("ProductCD");
			dbValue = DbUtil.getInstance("Livecareer").getResultSet(query_portal);
			String portalCD = dbValue.get("PortalCD");
			assertEquals(tmpData_session.get("SessionID").toString(), respJson.getString("session_identity"), "session_identity Mismatch");
			assertEquals(tmpData_session.get("Sessionexpiry").toString().substring(0, 16), respJson.getString("session_expiry").substring(0, 16), "session_expiry Mismatch");
			assertEquals(tmpData_session.get("Sessionlastaccessed").toString().substring(0, 16), respJson.getString("session_lastaccessed").substring(0, 16), "session_lastaccessed Mismatch");
			assertEquals(portalCD, respJson.getString("portal_cd"), "PortalCD mismatch");
			assertEquals(productCD, respJson.getString("product_cd"), "ProductCD mismatch");
			assertEquals(userUID, respJson.getString("user_uid"), "user_uid Mismatch");
			assertEquals(party_Id, respJson.getString("party_id"), "party_id Mismatch");
			assertEquals("[\"User\"]", respJson.getString("user_role"), "user_role Mismatch");
			assertEquals(tmpData_session.get("SessionGUID").toString(), respJson.getString("session_id"), "SessionID Mismatch");		

		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}


	//Authenticate User By Session GUID Method got deprecated
	//authenticate User By SessionGUID with invalid session GUID
	/*@Test
	public void authenticateUserBySessionGUIDWithInvalidSessionGUID()
	{
		try
		{
			String url = BASE_URL + "/v1/sessions/hkggkg789ggbjhygrr76rg";
			ResponseBean response = HttpService.post(url, headers, "");
			JSONObject respJson = new JSONObject(response.message);

			assertResponseCode(500, response.code);
			assertEquals("Oops, There was an internal Error", respJson, "description");
			assertEquals("API_ERROR", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}*/

	@Test
	public void authenticateRegisteredUserByUserUID()
	{
		try
		{
			JSONObject respJson = createUser(postDataRegisteredUser());
			String partyId = respJson.getString("party_id");
			String userUID = respJson.getString("user_uid");

			//authenticate user by UserUID : Valid Only for Guest User and administrator user
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("type", "registered");
			dataToPost.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");

			ResponseBean response = HttpService.post(GET_USER_URL_V1 + userUID + "/sessions", headers, dataToPost.toString());
			checkSuccessCode(response.code);

			respJson = new JSONObject(response.message);
			assertEquals(partyId, respJson.getString("party_id"), "party_id Mismatch");
			assertEquals(userUID, respJson.getString("user_uid"), "user_uid Mismatch");
			assertEquals("[\"User\"]", respJson.getString("user_role"), "user_role Mismatch");

			String session_query = "Select SessionGUID, SessionID from session where PartyID =" + partyId; 
			List<Map> list_session = DbUtil.getInstance("Livecareer").getResultSetList(session_query);
			assertEquals(list_session.get(0).get("SessionGUID").toString(), respJson.getString("session_id"), "session_id Mismatch");
			assertEquals(list_session.get(0).get("SessionID").toString(), respJson.getString("session_identity"), "session_identity Mismatch");
			
			//verify an entry is made to sessionuseragenttype table
			String query = "select Count(*) as Count from sessionuseragenttype where sessionID = " + list_session.get(0).get("SessionID").toString();
			Map query_map = DbUtil.getInstance("Livecareer").getResultSet(query);
			assertEquals("1", query_map.get("Count").toString(), "No entry is made to sessionuseragenttype table.");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass = UserService_DP.class , dataProvider = "providerCD")
	public void guestUserFlow_LoginBySocialApiWithExisitngUser(String provider)
	{
		try
		{
			//call create guest user
			JSONObject respJson = createUser(postDataGuestUser());
			String userUID = respJson.getString("user_uid");
			String party_Id = respJson.getString("party_id");

			//call authenticate by user_uid so that session id will be created
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			dataToPost.put("hitlog_id", 127854);
			dataToPost.put("product_cd", "RSM");
			dataToPost.put("type", "guest");

			String url = GET_USER_URL_V1 + userUID + "/sessions";
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			
			String sessionId = respJson.getString("session_id");
			String session_id = sessionId;
			
			//Assert session is created
			String query_sessionCount = "Select Count(*) as Count from Session where PartyID = " + party_Id;
			List<Map> list_sessionCount = DbUtil.getInstance("Livecareer").getResultSetList(query_sessionCount);
			assertEquals("1", list_sessionCount.get(0).get("Count").toString(), "count mismatch in session table in authenticate by user uid api call");
			
			//assume an existing email is returned from facebook or google
			JSONObject requestObject = new JSONObject();
			requestObject.put("type", "registered");
			requestObject.put("email_address", "first.last" + getDateTime(new Date(), format, 0) + ++counter + "@lc.com");
			requestObject.put("password", "123456");
			requestObject.put("ip_address", "138.91.242.92");
			requestObject.put("is_active", true);
			respJson = createUser(requestObject);
			String existingUserUID = respJson.getString("user_uid");
			String existingUserPartyID = respJson.getString("party_id");
			
			//call update session API with party id of existing user in above step
			session_id = session_id.replace("{", "%7B");
			session_id = session_id.replace("}", "%7D");
			url = BASE_URL + "/v1/sessions/" + session_id;	

			dataToPost = new JSONObject();
			dataToPost.put("party_id", Integer.parseInt(existingUserPartyID));
			dataToPost.put("auto_extend", false);
			
			response = HttpService.put(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			
			//Guest User Party Id is replaced by Existing User Party Id as session is updated
			String query_session = "Select PartyID from Session where SessionGUID = '" + sessionId + "'";
			List<Map> list_session = DbUtil.getInstance("Livecareer").getResultSetList(query_session);
			assertEquals(existingUserPartyID, list_session.get(0).get("PartyID").toString(), "PartyID Mismatch in Session Table");
			
			list_sessionCount = DbUtil.getInstance("Livecareer").getResultSetList(query_sessionCount);
			assertEquals("0", list_sessionCount.get(0).get("Count").toString(), "count mismatch in session table for guest user party id in update session api call");
			
			String query_updatedSessionCount = "Select Count(*) as Count from Session where PartyID = " + existingUserPartyID;
			List<Map> list_updatedSessionCount = DbUtil.getInstance("Livecareer").getResultSetList(query_updatedSessionCount);
			assertEquals("1", list_updatedSessionCount.get(0).get("Count").toString(), "count mismatch in session table for existing user in update session api call");

			//call login by social API. Since session id is passed so new session will NOT be created.
			dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("provider_cd", provider);
			dataToPost.put("access_token", RandomStringUtils.randomAlphanumeric(20));
			dataToPost.put("puid", RandomStringUtils.randomNumeric(10));
			dataToPost.put("is_primary", false);
			dataToPost.put("SessionID", sessionId);
			dataToPost.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			dataToPost.put("visit_id", 127854);
			dataToPost.put("product_cd", "RSM");
			dataToPost.put("type", "social"); // GGLE is for google
			
			url = GET_USER_URL_V1 + existingUserUID + "/sessions";
			response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			
			//Check if there is entry in SharedAuth table
			String query_sharedAuth = "Select * from SharedAuth where PartyID = " + existingUserPartyID;
			List<Map> list_sharedAuth = DbUtil.getInstance("Livecareer").getResultSetList(query_sharedAuth);

			assertEquals(dataToPost.getString("access_token"), list_sharedAuth.get(0).get("AccessToken").toString(), "Mismatch of Access Token");
			assertEquals(dataToPost.getString("puid"), list_sharedAuth.get(0).get("PUID").toString(), "Mismatch of PUID");
			assertEquals((dataToPost.getString("is_primary")=="true")?"1" :"0", list_sharedAuth.get(0).get("IsPrimary").toString(), "Mismatch of isPrimary Field");
			assertEquals(dataToPost.getString("provider_cd"), list_sharedAuth.get(0).get("AuthProviderCD").toString(), "Mismatch of ProviderCode");
			assertEquals(existingUserPartyID, list_sharedAuth.get(0).get("PartyId").toString(), "Mismatch of PartyId");
			
			//assert new session is not created
			list_updatedSessionCount = DbUtil.getInstance("Livecareer").getResultSetList(query_updatedSessionCount);
			assertEquals("1", list_updatedSessionCount.get(0).get("Count").toString(), "count mismatch in session table for existing user in login by social api call");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass = UserService_DP.class , dataProvider = "providerCD")
	public void guestUserFlow_LoginBySocialApiWithNonExisitngUser(String provider)
	{
		try
		{
			//call create guest user
			JSONObject guestUserData = postDataGuestUser();
			JSONObject respJson = createUser(guestUserData);
			String userUID = respJson.getString("user_uid");
			String party_Id = respJson.getString("party_id");

			//call authenticate by user uid so that session id will be created
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			dataToPost.put("hitlog_id", 127854);
			dataToPost.put("product_cd", "RSM");
			dataToPost.put("type", "guest");

			String url = GET_USER_URL_V1 + userUID + "/sessions";
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);			
			String sessionId = respJson.getString("session_id");
			
			String query_updatedSessionCount = "Select Count(*) as Count from Session where PartyID = " + party_Id;
			List<Map> list_updatedSessionCount = DbUtil.getInstance("Livecareer").getResultSetList(query_updatedSessionCount);
			assertEquals("1", list_updatedSessionCount.get(0).get("Count").toString(), "count mismatch in session table for guest user through authenticate by user uid api call");
			
			//assume a non existing email is returned from facebook or google			
			//call update User with email returned from facebook or google and password 
			JSONObject dataToPut = createDataForGuestUserUpdate(guestUserData);	
			response = HttpService.put(GET_USER_URL_V2 + userUID, headers, dataToPut.toString());	
			
			//call insert user role API with role = User 
			List<JSONObject> inputRoleList = new ArrayList<JSONObject>();
			List<JSONObject> expectedRoleList = new ArrayList<JSONObject>();
			
			JSONObject userRole = new JSONObject();
			userRole.put("id", 1);
			userRole.put("role", "User");
			
			inputRoleList.add(userRole);
			expectedRoleList.add(userRole);
			JSONArray expectedJArray = getRoleArray(expectedRoleList);
		    
			url = GET_USER_URL_V1 + userUID + "/roles";
			response = HttpService.post(url, headers, getRoleArray(inputRoleList).toString());
			JSONArray actualJArray = new JSONArray(response.message);
			assertJsonArrayEquality(expectedJArray, actualJArray);
			
			//call login by social API. Since session id is passed so new session will NOT be created.
			dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("provider_cd", provider);
			dataToPost.put("access_token", RandomStringUtils.randomAlphanumeric(20));
			dataToPost.put("puid", RandomStringUtils.randomNumeric(10));
			dataToPost.put("is_primary", true);
			dataToPost.put("SessionID", sessionId);
			dataToPost.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			dataToPost.put("visit_id", 127854);
			dataToPost.put("product_cd", "RSM");
			dataToPost.put("type", "social"); // GGLE is for google
			
			url = GET_USER_URL_V1 + userUID + "/sessions";
			response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			
			//Check if there is entry in SharedAuth table
			String query_sharedAuth = "Select * from SharedAuth where PartyID = " + party_Id;
			List<Map> list_sharedAuth = DbUtil.getInstance("Livecareer").getResultSetList(query_sharedAuth);

			assertEquals(dataToPost.getString("access_token"), list_sharedAuth.get(0).get("AccessToken").toString(), "Mismatch of Access Token");
			assertEquals(dataToPost.getString("puid"), list_sharedAuth.get(0).get("PUID").toString(), "Mismatch of PUID");
			assertEquals((dataToPost.getString("is_primary")=="true")?"1" :"0", list_sharedAuth.get(0).get("IsPrimary").toString(), "Mismatch of isPrimary Field");
			assertEquals(dataToPost.getString("provider_cd"), list_sharedAuth.get(0).get("AuthProviderCD").toString(), "Mismatch of ProviderCode");
			assertEquals(party_Id, list_sharedAuth.get(0).get("PartyId").toString(), "Mismatch of PartyId");
						
			//assert new session is not created
			list_updatedSessionCount = DbUtil.getInstance("Livecareer").getResultSetList(query_updatedSessionCount);
			assertEquals("1", list_updatedSessionCount.get(0).get("Count").toString(), "count mismatch in session table for guest user through login by social api");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@Test(dataProviderClass = UserService_DP.class , dataProvider = "providerCD")
	public void registeredUserFlow_LoginBySocialApi(String provider)
	{
		try
		{
			//create registered user
			JSONObject requestObject = new JSONObject();
			requestObject.put("type", "registered");
			requestObject.put("email_address", "first.last" + getDateTime(new Date(), format, 0) + ++counter + "@lc.com");
			requestObject.put("password", "123456");
			requestObject.put("ip_address", "138.91.242.92");
			requestObject.put("is_active", true);
			JSONObject respJson = createUser(requestObject);
			String userUID = respJson.getString("user_uid");
			String partyID = respJson.getString("party_id");
			
			//check for SessionId null
			String query_Person = "select SessionID,LastLoginDatetime,RegisteredOn from Person where PartyID = "+ partyID;
			List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_Person);	
			assertNull(list_person.get(0).get("SessionID"), "Session Id is not null");
			
			//call login by social API. Here Session ID is NOT passed so new session will be created.
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("provider_cd", provider);
			dataToPost.put("access_token", RandomStringUtils.randomAlphanumeric(20));
			dataToPost.put("puid", RandomStringUtils.randomNumeric(10));
			dataToPost.put("is_primary", false);
			dataToPost.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			dataToPost.put("visit_id", 127854);
			dataToPost.put("product_cd", "RSM");
			dataToPost.put("type", "social"); 
			
			String url = GET_USER_URL_V1 + userUID + "/sessions";
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			
			respJson = new JSONObject(response.message);
			
			assertEquals("[\"User\"]", respJson.getString("user_role"), "user_role Mismatch");
			String query_session = "select SessionGUID, SessionID, ProductID from [Session] where PartyID = "+ partyID;
			List<Map> list_session = DbUtil.getInstance("Livecareer").getResultSetList(query_session);
			String query_user = "select UserUID from [User] where PartyID = "+ partyID;	
			List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);
			list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_Person);	
			String query_portalUser = "Select Count(*) as Count from dbo.PortalUser where partyID = " + partyID;
			List<Map> list_portalUser = DbUtil.getInstance("Livecareer").getResultSetList(query_portalUser);
			
			assertEquals(partyID, respJson.getString("party_id"), "party_id Mismatch");
			assertEquals(list_user.get(0).get("UserUID").toString(), respJson.getString("user_uid"), "user_uid Mismatch");
			assertEquals(list_session.get(0).get("SessionGUID").toString(), respJson.getString("session_id"), "SessionID Mismatch");
			assertEquals(list_session.get(0).get("SessionID").toString(), respJson.getString("session_identity"), "session_identity Mismatch");
			assertEquals("[\"User\"]", respJson.getString("user_role"), "user_role Mismatch");
			assertEquals("10", list_session.get(0).get("ProductID").toString(), "ProductID Mismatch in Person table"); //For product_cd = RSM, ProductID = 10

			//LastLoginDatetime field should not be null whereas RegisteredOn field null after authentication in Person table
			assertNotNull(list_person.get(0).get("LastLoginDatetime"), "LastLoginDatetime is null");

			//There should be an entry in PortalUser table after authentication
			assertEquals("1", list_portalUser.get(0).get("Count").toString(), "Count Mismatch");

			//LastLogin and ResgisteredOn field should not be null after authentication in PortalUser table
			query_portalUser = "Select LastLogin, RegisteredOn from dbo.PortalUser where partyID = " + partyID;
			list_portalUser = DbUtil.getInstance("Livecareer").getResultSetList(query_portalUser);			
			assertNotNull(list_portalUser.get(0).get("LastLogin"), "LastLogin is null");
			assertNotNull(list_portalUser.get(0).get("RegisteredOn"), "RegisteredOn is null");

			//Check for RegisteredOn Field in Person Table: Should Not be NULL for Registered User
			assertNotNull(list_person.get(0).get("RegisteredOn"), "RegisteredOn is null");

			//check Session ID is updated on first user login 
			list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_Person);	
			assertEquals(list_person.get(0).get("SessionID").toString(), respJson.getString("session_identity"), "SessionID Mismatch in Person table");
			
			//Check if there is entry in SharedAuth table
			String query_sharedAuth = "Select * from SharedAuth where PartyID = " + partyID;
			List<Map> list_sharedAuth = DbUtil.getInstance("Livecareer").getResultSetList(query_sharedAuth);

			assertEquals(dataToPost.getString("access_token"), list_sharedAuth.get(0).get("AccessToken").toString(), "Mismatch of Access Token");
			assertEquals(dataToPost.getString("puid"), list_sharedAuth.get(0).get("PUID").toString(), "Mismatch of PUID");
			assertEquals((dataToPost.getString("is_primary")=="true")?"1" :"0", list_sharedAuth.get(0).get("IsPrimary").toString(), "Mismatch of isPrimary Field");
			assertEquals(dataToPost.getString("provider_cd"), list_sharedAuth.get(0).get("AuthProviderCD").toString(), "Mismatch of ProviderCode");
			assertEquals(partyID, list_sharedAuth.get(0).get("PartyId").toString(), "Mismatch of PartyId");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void loginBySocialApiWithoutIpParameter()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("provider_cd", "FCBK");
			dataToPost.put("puid", RandomStringUtils.randomNumeric(10));
			dataToPost.put("is_primary", false);			
			dataToPost.put("type", "social"); 

			String url = GET_USER_URL_V1 + guestUserUid + "/sessions";
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 400);
			
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("[\"The ip field is required.\"]", respJson.getJSONObject("ModelState").getString("loginRequest.ip"), "Message mismatch");	
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	/*@Test
	public void loginBySocialApiWithoutProviderCDParameter()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("puid", RandomStringUtils.randomNumeric(10));
			dataToPost.put("is_primary", false);			
			dataToPost.put("type", "social"); 

			String url = GET_USER_URL_V1 + guestUserUid + "/sessions";
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 400);
			
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("[\"The provider_cd field is required.\"]", respJson.getJSONObject("ModelState").getString("loginRequest.provider_cd"), "Message mismatch");	
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}*/
	
	/*@Test
	public void loginBySocialApiWithoutPuidParameter()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("provider_cd", "FCBK");
			dataToPost.put("is_primary", false);			
			dataToPost.put("type", "social"); 

			String url = GET_USER_URL_V1 + guestUserUid + "/sessions";
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 400);
			
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("[\"The puid field is required.\"]", respJson.getJSONObject("ModelState").getString("loginRequest.puid"), "Message mismatch");	
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}*/
	
	/*@Test
	public void loginBySocialApiWithoutIsPrimaryParameter()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("provider_cd", "FCBK");
			dataToPost.put("puid", RandomStringUtils.randomNumeric(10));		
			dataToPost.put("type", "social"); 

			String url = GET_USER_URL_V1 + guestUserUid + "/sessions";
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 400);
			
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("[\"The is_primary field is required.\"]", respJson.getJSONObject("ModelState").getString("loginRequest.is_primary"), "Message mismatch");	
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}*/
	
	/*@Test
	public void loginBySocialApiWithoutTypeParameter()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("provider_cd", "FCBK");
			dataToPost.put("is_primary", false);			
			dataToPost.put("puid", RandomStringUtils.randomNumeric(10));		

			String url = GET_USER_URL_V1 + guestUserUid + "/sessions";
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 400);
			
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("[\"The type field is required.\"]", respJson.getJSONObject("ModelState").getString("loginRequest.type"), "Message mismatch");	
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	*/
	
	//authenticateUserByUserUid is also called as "Login by party id"
	private void authenticateUserByUserUid(JSONObject dataToPost)
	{
		try
		{
			//create guest user
			JSONObject respJson = createUser(postDataGuestUser());
			String userUID = respJson.getString("user_uid");
			String party_Id = respJson.getString("party_id");

			//check for SessionId null
			String query_Person = "select SessionID,LastLoginDatetime,RegisteredOn from Person where PartyID = "+ party_Id;
			List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_Person);	
			assertNull(list_person.get(0).get("SessionID"), "Session Id is not null");

			//authenticate user by UserUID : Valid Only for Guest User and administrator user
			String url = GET_USER_URL_V1 + userUID + "/sessions";
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);		
			assertEquals(party_Id, respJson.getString("party_id"), "party_id Mismatch");
			assertEquals(userUID, respJson.getString("user_uid"), "user_uid Mismatch");
			assertEquals("[\"Guest\"]", respJson.getString("user_role"), "user_role Mismatch");

			String session_query = "Select SessionGUID, SessionID, ProductID from session where PartyID =" + party_Id; 
			List<Map> list_session = DbUtil.getInstance("Livecareer").getResultSetList(session_query);
			assertEquals(list_session.get(0).get("SessionGUID").toString(), respJson.getString("session_id"), "session_id Mismatch");
			assertEquals(list_session.get(0).get("SessionID").toString(), respJson.getString("session_identity"), "session_identity Mismatch");

			assertEquals("10", list_session.get(0).get("ProductID").toString(), "ProductID Mismatch in Person table"); //For product_cd = RSM, ProductID = 10
			
			//verify an entry is made to sessionuseragenttype table
			String query = "select Count(*) as Count from sessionuseragenttype where sessionID = " + list_session.get(0).get("SessionID").toString();
			Map query_map = DbUtil.getInstance("Livecareer").getResultSet(query);
			assertEquals("1", query_map.get("Count").toString(), "No entry is made to sessionuseragenttype table.");

			//LastLoginDatetime field should not be null whereas RegisteredOn field null after authentication in Person table
			list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_Person);	
			assertNotNull(list_person.get(0).get("LastLoginDatetime"), "LastLoginDatetime is null");
			assertNull(list_person.get(0).get("RegisteredOn"), "RegisteredOn is not null");

			//There should be an entry in PortalUser table after authentication
			String query_portalUser = "Select Count(*) as Count from dbo.PortalUser where partyID = " + party_Id;
			List<Map> list_portalUser = DbUtil.getInstance("Livecareer").getResultSetList(query_portalUser);		
			assertEquals("1", list_portalUser.get(0).get("Count").toString(), "Count Mismatch");

			//LastLogin and ResgisteredOn field should not be null after authentication in PortalUser table
			query_portalUser = "Select LastLogin, RegisteredOn from dbo.PortalUser where partyID = " + party_Id;
			list_portalUser = DbUtil.getInstance("Livecareer").getResultSetList(query_portalUser);			
			assertNotNull(list_portalUser.get(0).get("LastLogin"), "LastLogin is null");
			assertNotNull(list_portalUser.get(0).get("RegisteredOn"), "RegisteredOn is null");

			//check Session ID is updated on first user login 
			list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_Person);	
			assertEquals(list_person.get(0).get("SessionID").toString(), respJson.getString("session_identity"), "SessionID Mismatch in Person table");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	private ResponseBean updateSessionWithInvalidValuesOfParameters(Object partyID, Object autoExtend)
	{
		try
		{
			//get session guid by using "authenticate user by UserUID" api
			String session_id =  getSessionGuidByAuthenticatingUserByUserUID(guestUserUid);;

			//update session 
			session_id = session_id.replace("{", "%7B");
			session_id = session_id.replace("}", "%7D");
			String url = BASE_URL + "/v1/sessions/" + session_id;	
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("party_id", partyID);
			dataToPost.put("auto_extend", autoExtend);

			return HttpService.put(url, headers, dataToPost.toString());
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private void updateSession(JSONObject dataToPost, String partyID, String auto_extend)
	{
		try
		{
			//create guest user
			JSONObject respJson = createUser(postDataGuestUser());
			String userUID = respJson.getString("user_uid");
			System.out.println("User UID :" + userUID);

			//get session guid by using "authenticate user by UserUID" api
			String session_id =  getSessionGuidByAuthenticatingUserByUserUID(userUID);;
			String sessionGuid = session_id;

			//update session 
			session_id = session_id.replace("{", "%7B");
			session_id = session_id.replace("}", "%7D");
			String url = BASE_URL + "/v1/sessions/" + session_id;	

			ResponseBean response = HttpService.put(url, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			assertEquals("The Session has been updated", respJson.getString("message"), "Message mismatch");
			String session_query = "Select PartyID, AutoExtend from session where SessionGuid ='" + sessionGuid + "'"; 
			List<Map> list_session = DbUtil.getInstance("Livecareer").getResultSetList(session_query);	
			assertEquals(partyID, list_session.get(0).get("PartyID").toString(), "partyID Mismatch");
			if(auto_extend != "Null")
			{
				assertEquals(auto_extend, list_session.get(0).get("AutoExtend").toString(), "auto extend value Mismatch");
			}
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

}
