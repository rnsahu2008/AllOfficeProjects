package com.lc.tests.userservice;

import static com.lc.constants.Constants_UserService.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class UpdateUserInformation extends UserServiceCoreComponents {

	HashMap<String, String> headers = null;
	List<Map> list_StageID = null;
	String userUIDForNs = ""; // this userUid is for negative scenarios
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@BeforeClass
	public void classInit()
	{
		headers = HeaderManager.getHeaders("LCAUS", access_token, "1234");
		String query_StageID = "select top 3 * from ProductUserStage where ProductID = (Select ProductID from Product where ProductCD = 'RSM') and StrategyID = 14 order by 3";
		list_StageID = DbUtil.getInstance("Livecareer").getResultSetList(query_StageID);
		String partyIdForNs = setPartyID();
		userUIDForNs = getUserUIDByPartyID(partyIdForNs);
	}
	
	@Test(dataProviderClass=UserService_DP.class, dataProvider="getUserUrl")
	public void updateUserInfoForRegisteredUser(String getUserUrl){
		try{
			JSONObject dataToPost = postDataRegisteredUser();
			JSONObject respJson = createUser(dataToPost);
			JSONObject dataToPut = createDataForRegisteredUserUpdate(dataToPost);
			String party_Id = respJson.getString("party_id");
			String user_uid = respJson.getString("user_uid");
			ResponseBean response;
			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.put(GET_USER_URL_V1 + party_Id, headers, dataToPut.toString());
			}
			else
			{
				response = HttpService.put(GET_USER_URL_V2 + user_uid, headers, dataToPut.toString());
			}

			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			assertEquals(dataToPut.getString("first_name"), respJson.getString("first_name"), "First Name mismatch");
			assertEquals(dataToPut.getString("last_name"), respJson.getString("last_name"), "Last Name mismatch");
			assertEquals(dataToPut.getString("email_address"), respJson.getString("email"), "EmailAddress mismatch");
			assertEquals(dataToPut.getString("reset_pwd"), respJson.getString("reset_password"), "Reset Password mismatch");
			assertEquals(dataToPut.getString("is_active"), respJson.getString("is_active"), "isActive Flag mismatch");
			assertEquals(dataToPut.getString("phone_no"), respJson.getString("phone_no"), "phone number Mismatch");
			assertEquals(dataToPut.getString("mobile_no"), respJson.getString("mobile_no"), "mobile phone number Mismatch");
			assertEquals(dataToPut.getString("work_phone"), respJson.getString("work_phone"), "work phone number Mismatch");
			
			//database verification
			String query_person = "Select FirstName, LastName, EmailAddress, Phone, MobilePhone, WorkPhone from Person where partyID = " + party_Id;
			String query_user = "Select ResetPwd, isactive, HashedPwd from [user] where partyID = " + party_Id;
			Map person = DbUtil.getInstance("Livecareer").getResultSet(query_person);
			Map user = DbUtil.getInstance("Livecareer").getResultSet(query_user);
			assertEquals(dataToPut.getString("first_name"), person.get("FirstName").toString(), "First Name mismatch");
			assertEquals(dataToPut.getString("last_name"), person.get("LastName").toString(), "Last Name mismatch");
			assertEquals(dataToPut.getString("email_address"), person.get("EmailAddress").toString(), "EmailAddress mismatch");
			assertTrue(!Boolean.parseBoolean(user.get("ResetPwd").toString()), "Reset Password mismatch");
			assertTrue(!Boolean.parseBoolean(user.get("isactive").toString()), "isActive Flag mismatch");
			assertEquals(dataToPut.getString("phone_no"), person.get("Phone").toString(), "phone number Mismatch");
			assertEquals(dataToPut.getString("mobile_no"), person.get("MobilePhone").toString(), "mobile phone number Mismatch");
			assertEquals(dataToPut.getString("work_phone"), person.get("WorkPhone").toString(), "work phone number Mismatch");
			assertNotNull(user.get("HashedPwd"), "HashedPwd is NULL");
			
		}catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(dataProviderClass=UserService_DP.class, dataProvider="getUserUrl")
	public void updateUserInfoForGuestUser(String getUserUrl){
		try{
			JSONObject dataToPost = postDataGuestUser();
			JSONObject respJson = createUser(dataToPost);
			JSONObject dataToPut = createDataForGuestUserUpdate(dataToPost);
			String party_Id = respJson.getString("party_id");
			String user_uid = respJson.getString("user_uid");
			ResponseBean response;
			if(getUserUrl.equalsIgnoreCase(GET_USER_URL_V1))
			{
				response = HttpService.put(GET_USER_URL_V1 + party_Id, headers, dataToPut.toString());
			}
			else
			{
				response = HttpService.put(GET_USER_URL_V2 + user_uid, headers, dataToPut.toString());
			}

			checkSuccessCode(response.code);
			respJson = new JSONObject(response.message);
			assertEquals(dataToPut.getString("first_name"), respJson.getString("first_name"), "First Name mismatch");
			assertEquals(dataToPut.getString("last_name"), respJson.getString("last_name"), "Last Name mismatch");
			assertEquals(dataToPut.getString("email_address"), respJson.getString("email"), "EmailAddress mismatch");
			assertEquals(dataToPut.getString("reset_pwd"), respJson.getString("reset_password"), "Reset Password mismatch");
			assertEquals(dataToPut.getString("is_active"), respJson.getString("is_active"), "isActive Flag mismatch");
			assertEquals(dataToPut.getString("phone_no"), respJson.getString("phone_no"), "phone number Mismatch");
			assertEquals(dataToPut.getString("mobile_no"), respJson.getString("mobile_no"), "mobile phone number Mismatch");
			assertEquals(dataToPut.getString("work_phone"), respJson.getString("work_phone"), "work phone number Mismatch");
			
			//database verification
			String query_person = "Select FirstName, LastName, EmailAddress, Phone, MobilePhone, WorkPhone, RoleID from Person where partyID = " + party_Id;
			String query_user = "Select ResetPwd, isactive, HashedPwd from [user] where partyID = " + party_Id;
			Map person = DbUtil.getInstance("Livecareer").getResultSet(query_person);
			Map user = DbUtil.getInstance("Livecareer").getResultSet(query_user);
			assertEquals(dataToPut.getString("first_name"), person.get("FirstName").toString(), "First Name mismatch");
			assertEquals(dataToPut.getString("last_name"), person.get("LastName").toString(), "Last Name mismatch");
			assertEquals(dataToPut.getString("email_address"), person.get("EmailAddress").toString(), "EmailAddress mismatch");
			assertTrue(!Boolean.parseBoolean(user.get("ResetPwd").toString()), "Reset Password mismatch");
			assertTrue(!Boolean.parseBoolean(user.get("isactive").toString()), "isActive Flag mismatch");
			assertEquals(dataToPut.getString("phone_no"), person.get("Phone").toString(), "phone number Mismatch");
			assertEquals(dataToPut.getString("mobile_no"), person.get("MobilePhone").toString(), "mobile phone number Mismatch");
			assertEquals(dataToPut.getString("work_phone"), person.get("WorkPhone").toString(), "work phone number Mismatch");
			assertNotNull(user.get("HashedPwd"), "HashedPwd is NULL");
			assertEquals("0", person.get("RoleID").toString(), "RoleID mismatch");
			
		}catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserStages()
	{
		try
		{
			String party_Id = setPartyID();
			String userUID = getUserUIDByPartyID(party_Id);
			
			//call update user stage API for user stage id
			JSONObject respJson = updateUserStage(list_StageID.get(1).get("StageID").toString(), userUID);
			assertEquals("The user stage has been saved", respJson.getString("message"), "Message mismatch - In First Stage");
			String productUser_query = "Select UserStageID from ProductUser where PartyID =" + party_Id + " And ProductID = (Select ProductID from Product where ProductCD = 'RSM')";
			List<Map> list_userStage = DbUtil.getInstance("Livecareer").getResultSetList(productUser_query);
			assertEquals(list_StageID.get(1).get("StageID").toString(), list_userStage.get(0).get("UserStageID").toString(), "User Stage ID Mismatch - In First Stage, ProductUser table");
			
			//call update user stage API for user stage id 
			respJson = updateUserStage(list_StageID.get(2).get("StageID").toString(), userUID);
			assertEquals("The user stage has been saved", respJson.getString("message"), "Message mismatch - In Second Stage, ProductUser table");
			list_userStage = DbUtil.getInstance("Livecareer").getResultSetList(productUser_query);
			assertEquals(list_StageID.get(2).get("StageID").toString(), list_userStage.get(0).get("UserStageID").toString(), "User Stage ID Mismatch - In Second Stage, ProductUser table");

			String pdUserHistory_query = "Select UserStageID from ProductUserHistory where PartyID =" + party_Id + " And ProductID = (Select ProductID from Product where ProductCD = 'RSM')";
			List<Map> list_userStageHistory = DbUtil.getInstance("Livecareer").getResultSetList(pdUserHistory_query);
			assertEquals(list_StageID.get(1).get("StageID").toString(), list_userStageHistory.get(0).get("UserStageID").toString(), "First User Stage ID Mismatch - In Second Stage, ProductUserHistory table");
			assertEquals(list_StageID.get(2).get("StageID").toString(), list_userStageHistory.get(1).get("UserStageID").toString(), "Second User Stage ID Mismatch - In Second Stage, ProductUserHistory table");
			
			//call update user stage API for user stage id 
			respJson = updateUserStage(list_StageID.get(0).get("StageID").toString(), userUID);
			assertEquals("The user stage has been saved", respJson.getString("message"), "Message mismatch - In Third Stage, ProductUser table");
			list_userStage = DbUtil.getInstance("Livecareer").getResultSetList(productUser_query);
			assertEquals(list_StageID.get(2).get("StageID").toString(), list_userStage.get(0).get("UserStageID").toString(), "User Stage ID Mismatch - In Third Stage, ProductUser table");
			list_userStageHistory = DbUtil.getInstance("Livecareer").getResultSetList(pdUserHistory_query);
			assertEquals(list_StageID.get(1).get("StageID").toString(), list_userStageHistory.get(0).get("UserStageID").toString(), "First User Stage ID Mismatch - In Second Stage, ProductUserHistory table");
			assertEquals(list_StageID.get(2).get("StageID").toString(), list_userStageHistory.get(1).get("UserStageID").toString(), "Second User Stage ID Mismatch - In Second Stage, ProductUserHistory table");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserStagesWithInvalidDatatypeForUserStageID()
	{
		try
		{
			//call update user stage API for user stage id
			JSONObject userStage = new JSONObject();
			userStage.put("product_cd", "RSM");
			userStage.put("userstage_id", "fadsf");
			String url = BASE_URL + "/v1/users/" + userUIDForNs + "/stages"; 
			ResponseBean response = HttpService.post(url, headers, userStage.toString());
			assertResponseCode(response.code, 400);
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserStagesWithInvalidValueForProductCD()
	{
		try
		{
			//call update user stage API for user stage id
			JSONObject userStage = new JSONObject();
			userStage.put("product_cd", "XYZ");
			userStage.put("userstage_id", 17);
			String url = BASE_URL + "/v1/users/" + userUIDForNs + "/stages"; 
			
			ResponseBean response = HttpService.post(url, headers, userStage.toString());
			
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
	public void updateUserStagesWithInvalidDatatypeForCurrentPage()
	{
		try
		{
			//call update user stage API for user stage id
			JSONObject userStage = new JSONObject();
			userStage.put("product_cd", "RSM");
			userStage.put("userstage_id", 17);
			userStage.put("current_page", "errts4");
			String url = BASE_URL + "/v1/users/" + userUIDForNs + "/stages"; 
			
			ResponseBean response = HttpService.post(url, headers, userStage.toString());
			assertResponseCode(response.code, 400);

		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserStagesWithInvalidDatatypeForStrategyID()
	{
		try
		{
			//call update user stage API for user stage id
			JSONObject userStage = new JSONObject();
			userStage.put("product_cd", "RSM");
			userStage.put("userstage_id", 17);
			userStage.put("Strategy_id", "try");
			String url = BASE_URL + "/v1/users/" + userUIDForNs + "/stages"; 
			
			ResponseBean response = HttpService.post(url, headers, userStage.toString());
			assertResponseCode(response.code, 400);

		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserStagesWithoutProductCD()
	{
		try
		{
			//call update user stage API for user stage id
			JSONObject userStage = new JSONObject();
			userStage.put("userstage_id", 17);
			String url = BASE_URL + "/v1/users/" + userUIDForNs + "/stages"; 
			
			ResponseBean response = HttpService.post(url, headers, userStage.toString());
			
			JSONObject respJson = new JSONObject(response.message);
			assertResponseCode(response.code, 400);
			JSONObject modelState = respJson.getJSONObject("ModelState");
			assertEquals("[\"The field product_cd is mandatory for processing the request.\"]", modelState.getString("userStageRequest.ProductCD"), "Message mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserStagesWithoutUserStageID()
	{
		try
		{
			//call update user stage API for user stage id
			JSONObject userStage = new JSONObject();
			userStage.put("product_cd", "RSM");
			String url = BASE_URL + "/v1/users/" + userUIDForNs + "/stages"; 
			
			ResponseBean response = HttpService.post(url, headers, userStage.toString());
			
			JSONObject respJson = new JSONObject(response.message);
			assertResponseCode(response.code, 400);
			JSONObject modelState = respJson.getJSONObject("ModelState");
			assertEquals("[\"The field userstage_id is mandatory for processing the request.\"]", modelState.getString("userStageRequest.UserStageID"), "Message mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserLevelWithoutHavingAnyUserStageForRegisteredUser()
	{
		try
		{	
			String party_id = setPartyID();
			String userUID = getUserUIDByPartyID(party_id);
			JSONObject respJson = updateUserLevel(1, 404, userUID);
			assertEquals("User Level cannot be updated for this User.", respJson, "description");
			assertEquals("U015", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserLevelForGuestUser()
	{
		try
		{	
			JSONObject resp = createUser(postDataGuestUser());
			String userUID = resp.getString("user_uid");
			
			//Add User Stage
			updateUserStage(list_StageID.get(1).get("StageID").toString(), userUID);
			
			JSONObject respJson = updateUserLevel(1, 404, userUID);
			assertEquals("User Level cannot be updated for this User.", respJson, "description");
			assertEquals("U015", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserLevel()
	{
		try
		{		
			String party_Id = setPartyID();
			String userUID = getUserUIDByPartyID(party_Id);

			//Add User Stage
			updateUserStage(list_StageID.get(1).get("StageID").toString(), userUID);
			
			//User Level Upgrade to 2
			JSONObject respJson = updateUserLevel(2, 200, userUID);
			assertEquals("The user level has been saved", respJson.getString("message"), "Message mismatch - In First Stage");
			
			String productUser_query = "Select UserLevelID from ProductUser where PartyID =" + party_Id + "And ProductID = (Select ProductID from Product where ProductCD = 'RSM')";
			List<Map> list_userLevel = DbUtil.getInstance("Livecareer").getResultSetList(productUser_query);
			assertEquals("2", list_userLevel.get(0).get("UserLevelID").toString(), "User Level ID Mismatch - In First Stage, ProductUser table");
			
			//User Level degrade to 1
			respJson = updateUserLevel(1, 200, userUID);
			assertEquals("The user level has been saved", respJson.getString("message"), "Message mismatch - In Second Stage");
			list_userLevel = DbUtil.getInstance("Livecareer").getResultSetList(productUser_query);
			assertEquals("1", list_userLevel.get(0).get("UserLevelID").toString(), "User Level ID Mismatch - In Second Stage, ProductUser table");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserLevelWithoutProductCD()
	{
		try
		{
			//Add User Stage
			updateUserStage(list_StageID.get(1).get("StageID").toString(), userUIDForNs);
			
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("userlevel_id", 1);
			String url = BASE_URL + "/v1/users/" + userUIDForNs + "/levels";
			ResponseBean response = HttpService.put(url, headers, dataToPost.toString()); 
			assertResponseCode(response.code, 400);
			JSONObject respJson = new JSONObject(response.message);
			JSONObject modelState = respJson.getJSONObject("ModelState");
			assertEquals("[\"The field product_cd is mandatory for processing the request\"]", modelState.getString("SMObj.ProductCD"), "Message mismatch");	
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserLevelWithoutUserLevelID()
	{
		try
		{
			//Add User Stage
			updateUserStage(list_StageID.get(1).get("StageID").toString(), userUIDForNs);
			
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("product_cd", "RSM");
			String url = BASE_URL + "/v1/users/" + userUIDForNs + "/levels";
			ResponseBean response = HttpService.put(url, headers, dataToPost.toString()); 
			
			assertResponseCode(response.code, 400);
			JSONObject respJson = new JSONObject(response.message);
			JSONObject modelState = respJson.getJSONObject("ModelState");
			assertEquals("[\"The field userlevel_id is mandatory for processing the request\"]", modelState.getString("SMObj.UserLevelID"), "Message mismatch");	
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserLevelWithInvalidDatatypeForUserLevel()
	{
		try
		{
			//Add User Stage
			updateUserStage(list_StageID.get(1).get("StageID").toString(), userUIDForNs);
			
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("product_cd", "RSM");
			dataToPost.put("userlevel_id", "tr7ty");
			String url = BASE_URL + "/v1/users/" + userUIDForNs + "/levels";
			ResponseBean response = HttpService.put(url, headers, dataToPost.toString()); 
			assertResponseCode(response.code, 400);

		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void updateUserLevelWithInvalidValueOfProductCD()
	{
		try
		{
			//Add User Stage
			updateUserStage(list_StageID.get(1).get("StageID").toString(), userUIDForNs);
			
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("product_cd", "ABCD");
			dataToPost.put("userlevel_id", "2");
			String url = BASE_URL + "/v1/users/" + userUIDForNs + "/levels";
			ResponseBean response = HttpService.put(url, headers, dataToPost.toString()); 
			
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
		
}
