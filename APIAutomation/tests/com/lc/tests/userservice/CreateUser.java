package com.lc.tests.userservice;
import static com.lc.constants.Constants_UserService.createUserUrl;

import java.util.Date;
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
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.HeaderManager;

public class CreateUser extends UserServiceCoreComponents{
	
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

	@Test
	@SuppressWarnings("rawtypes")
	public void createGuestUser(){

		try{
			JSONObject respJson = createUser(postDataGuestUser());
			String query_person = "Select PartyID, RegisteredOn, LastLoginDatetime, RoleID, CountryCD from Person where partyID = " + respJson.getInt("party_id");
			String query_user = "Select UserUID, UserStageID from [user] where partyID = " + respJson.getInt("party_id");
			String query_portalUser = "Select Count(*) as Count from dbo.PortalUser where partyID = " + respJson.getInt("party_id");
			List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_person);
			List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);
			List<Map> list_portalUser = DbUtil.getInstance("Livecareer").getResultSetList(query_portalUser);	
			assertEquals(list_person.get(0).get("PartyID").toString(), respJson.getString("party_id"), "partyID Mismatch");
			assertEquals(list_person.get(0).get("RoleID").toString(), "0", "RoleID Mismatch");
			assertNull(list_person.get(0).get("LastLoginDatetime"), "LastLoginDatetime is not null");
			assertNull(list_person.get(0).get("RegisteredOn"), "RegisteredOn is not null");
			assertEquals("US", list_person.get(0).get("CountryCD").toString(), "CountryCD Mismatch in Person table");
			assertEquals(list_user.get(0).get("UserUID").toString(), respJson.getString("user_uid"), "UserUID Mismatch");
			assertEquals("0", list_user.get(0).get("UserStageID").toString(), "UserStageID Mismatch");
			assertEquals("0", list_portalUser.get(0).get("Count").toString(), "Count Mismatch");
			
			//Verify that an entry is made to TestUser table 
			String testUser_query = "select Count(*) as Count from TestUser where partyid = " + respJson.getInt("party_id");
			Map testUser_map = DbUtil.getInstance("Livecareer").getResultSet(testUser_query);
			assertEquals("1", testUser_map.get("Count").toString(), "Count Mismatch");
			
			//Verify CountryCD in PersonGeoLocation
			String query_personGL = "select CountryCD from persongeolocation where partyid = " + respJson.getInt("party_id");
			Map map_personGL = DbUtil.getInstance("Livecareer").getResultSet(query_personGL);
			assertEquals("IN", map_personGL.get("CountryCD").toString().trim(), "CountryCD Mismatch in PersonGeoLocation table");
			
		}catch (Exception e) {
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void createGuestUserWithMandatoryParameters(){
		try{
			JSONObject requestObject = new JSONObject();
			requestObject.put("type", "guest");
			requestObject.put("ip_address", "14.141.80.210");
			requestObject.put("is_active", true);
			JSONObject respJson = createUser(requestObject);
			String query_person = "Select PartyID, RoleID from Person where partyID = " + respJson.getInt("party_id");
			String query_user = "Select UserUID, UserStageID from [user] where partyID = " + respJson.getInt("party_id");
			List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_person);
			List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);
			assertEquals(list_person.get(0).get("PartyID").toString(), respJson.getString("party_id"), "partyID Mismatch");
			assertEquals(list_person.get(0).get("RoleID").toString(), "0", "RoleID Mismatch");
			assertEquals(list_user.get(0).get("UserUID").toString(), respJson.getString("user_uid"), "UserUID Mismatch");
			assertEquals("0", list_user.get(0).get("UserStageID").toString(), "UserStageID Mismatch");
		}catch(Exception e){
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}	
	
	@SuppressWarnings("rawtypes")
	@Test
	public void createRegisteredUserWithMandatoryParameters(){
		try{
			JSONObject requestObject = new JSONObject();
			requestObject.put("type", "registered");
			requestObject.put("email_address", "rakshit.jain" + getDateTime(new Date(), format, 0) + ++counter + "@lc.com");
			requestObject.put("password", "123456");
			requestObject.put("ip_address", "14.141.80.210");
			requestObject.put("is_active", true);
			JSONObject respJson = createUser(requestObject);
			String query_person = "Select PartyID from Person where partyID = " + respJson.getInt("party_id");
			String query_user = "Select UserUID, UserStageID from [user] where partyID = " + respJson.getInt("party_id");
			List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_person);
			List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);
			assertEquals(list_person.get(0).get("PartyID").toString(), respJson.getString("party_id"), "partyID Mismatch");
			assertEquals(list_user.get(0).get("UserUID").toString(), respJson.getString("user_uid"), "UserUID Mismatch");
			assertEquals("1", list_user.get(0).get("UserStageID").toString(), "UserStageID Mismatch");
		}catch(Exception e){
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void createRegisteredUser(){

		try{
			JSONObject dataToPost = postDataRegisteredUser();
			JSONObject respJson = createUser(dataToPost);
			String query_person = "Select PartyID, RegisteredOn, LastLoginDatetime,WorkPhone, CountryCD from Person where partyID = " + respJson.getInt("party_id");
			String query_user = "Select UserUID, UserStageID from [user] where partyID = " + respJson.getInt("party_id");
			String query_portalUser = "Select Count(*) as Count from dbo.PortalUser where partyID = " + respJson.getInt("party_id");
			List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_person);
			List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);	
			List<Map> list_portalUser = DbUtil.getInstance("Livecareer").getResultSetList(query_portalUser);	
			assertEquals(list_person.get(0).get("PartyID").toString(), respJson.getString("party_id"), "partyID Mismatch");
			assertNull(list_person.get(0).get("LastLoginDatetime"), "LastLoginDatetime is not null");
			assertNotNull(list_person.get(0).get("RegisteredOn").toString(), "RegisteredOn Mismatch");
			assertEquals("US", list_person.get(0).get("CountryCD").toString(), "CountryCD Mismatch in Person table");
			assertEquals(list_user.get(0).get("UserUID").toString(), respJson.getString("user_uid"), "UserUID Mismatch");
			assertEquals("0", list_portalUser.get(0).get("Count").toString(), "Count Mismatch");
			assertEquals(list_person.get(0).get("WorkPhone").toString(), dataToPost.getString("work_phone"), "WorkPhone phone number Mismatch");
			assertEquals("1", list_user.get(0).get("UserStageID").toString(), "UserStageID Mismatch");
			
			//Verify that an entry is made to TestUser table 
			String testUser_query = "select Count(*) as Count from TestUser where partyid = " + respJson.getInt("party_id");
			Map testUser_map = DbUtil.getInstance("Livecareer").getResultSet(testUser_query);
			assertEquals("1", testUser_map.get("Count").toString(), "Count Mismatch");
			
			//Verify CountryCD in PersonGeoLocation
			String query_personGL = "select CountryCD from persongeolocation where partyid = " + respJson.getInt("party_id");
			Map map_personGL = DbUtil.getInstance("Livecareer").getResultSet(query_personGL);
			assertEquals("IN", map_personGL.get("CountryCD").toString().trim(), "CountryCD Mismatch in PersonGeoLocation table");
			
		}catch (Exception e) {
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	public void createUserWithExistingEmailAddress(){
		try{
			JSONObject dataToPost = postDataRegisteredUser();
			createUser(dataToPost);
			ResponseBean response = HttpService.post(createUserUrl, headers, dataToPost.toString());
			assertResponseCode(response.code, 400);
			JSONObject respJson = new JSONObject(response.message);
			String expectedValueDescription = "The Email is already existing. Can't create another user with the same email address";
			String expectedValueError_Code = "U013";
			assertEquals(expectedValueDescription, respJson, "description");
			assertEquals(expectedValueError_Code, respJson, "error_code");
		}catch(Exception e){
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void createGuestUserWithInvalidIP(){

		try{
			JSONObject dataCreateUser = new JSONObject();
			dataCreateUser.put("type", "guest");
			dataCreateUser.put("visit_id", "127854");
			dataCreateUser.put("ip_address", "127.0.0.1");
			dataCreateUser.put("is_active", true);
			JSONObject respJson = createUser(dataCreateUser);
			int party_id = respJson.getInt("party_id");
			System.out.println(party_id);
			String query_person = "Select PartyID, RoleID from Person where partyID = " + party_id;
			String query_user = "Select UserUID from [user] where partyID = " + party_id;
			List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_person);
			List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);
			assertEquals(list_person.get(0).get("PartyID").toString(), respJson.getString("party_id"), "partyID Mismatch");
			assertEquals(list_person.get(0).get("RoleID").toString(), "0", "RoleID Mismatch");
			assertEquals(list_user.get(0).get("UserUID").toString(), respJson.getString("user_uid"), "UserUID Mismatch");
		}catch (Exception e) {
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void createRegisteredUserWithInvalidIP(){

		try{
			JSONObject requestObject = new JSONObject();
			requestObject.put("type", "registered");
			requestObject.put("email_address", "rakshit.jain" + getDateTime(new Date(), format, 0) + ++counter + "@lc.com");
			requestObject.put("password", "123456");
			requestObject.put("ip_address", "2.3.4.5");
			requestObject.put("is_active", true);
			JSONObject respJson = createUser(requestObject);
			int party_id = respJson.getInt("party_id");
			System.out.println(party_id);
			String query_person = "Select PartyID from Person where partyID = " + party_id;
			String query_user = "Select UserUID from [user] where partyID = " + party_id;
			List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_person);
			List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);	
			assertEquals(list_person.get(0).get("PartyID").toString(), respJson.getString("party_id"), "partyID Mismatch");
			assertEquals(list_user.get(0).get("UserUID").toString(), respJson.getString("user_uid"), "UserUID Mismatch");
		}catch (Exception e) {
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}
	
	/*@Test
	public void createUserWithExpiedAccessToken(){
		try{
			JSONObject dataToPost = postDataGuestUser();
			
			String OldAccessToken = "EY7NwmqgGOs9wPEizeC2BHvnmzgVMzff-kPWifDrIOioOrRVoRzb8bI7Q6fLR3R6aJuRZrAJyrWl8qvn8RTQKF0Tft0vauCKyJUTt8BA5oL1vFuFKA5EENFGrq5AwOo1jO4ADL14BC3uKtadUHO1TP4z4hQmobm33UiPdlsMyViGbPpOUBGkHsNcOpAi6vd7G5V3M7-GCCO-GzNZ2KvfXhv011h3wNL2K98pxU1U1t8";
			
			ResponseBean response = HttpService.post(createUserUrl,HeaderManager.getHeaders("LCAUS", OldAccessToken, "1234"), dataToPost.toString());
			
			assertResponseCode(response.code, 403);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("The token is expired.", respJson, "description");
			assertEquals("U021", respJson, "error_code");
			
		}catch (Exception e) {
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}*/
	
	@Test
	public void createUserWithoutAccessToken(){
		try{
			JSONObject dataToPost = postDataGuestUser();
		
			ResponseBean response = HttpService.post(createUserUrl,HeaderManager.getHeaders("LCAUS", "", "1234"), dataToPost.toString());
			
			assertResponseCode(response.code, 403);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("The Request does not have a bearer token in the headers.", respJson, "description");
			assertEquals("U023", respJson, "error_code");
			
		}catch (Exception e) {
			fail(e.getMessage());
		}
		s_assert.assertAll();
	}
	
}
