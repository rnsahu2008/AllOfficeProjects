package com.lc.tests.userservice;

import static com.lc.constants.Constants_UserService.AUTHENTICATE_USER_URL;
import static com.lc.constants.Constants_UserService.BASE_URL;
import static com.lc.constants.Constants_UserService.GET_USER_URL_V1;
import static com.lc.constants.Constants_UserService.createUserUrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.lc.utils.ReadProperties;

public class UserRoles extends UserServiceCoreComponents {
	
	public String partyId = "";
	HashMap<String, String> headers = null;
	
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
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void addUserRolesForRegisteredUser(){
		try{
			String userId = getUserUIDByPartyID(partyId);
			
			//Verify User Role got inserted in UserPortalRoles table
			String query_upRoles = "Select RoleID from UserPortalRoles where UserUID = '" + userId + "'";
			List<Map> list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
			
			if(list_upRoles.isEmpty())
			{
				fail("No role is found in UserPortalRoles table");
			}
			assertEquals("1", list_upRoles.get(0).get("RoleID").toString(), "Incorrect Role is inserted in UserPortalRoles table");
			
		    String role_query = "select RoleID, Role from Role where RoleID IN (select RoleID from PortalRoles where PortalCD = (select PortalCD from client where ClientCD = 'LCAUS'))";
		    List<Map> list_Roles = DbUtil.getInstance("Livecareer").getResultSetList(role_query);
		    
		    for(int i = 0; i < list_Roles.size(); i++)
		    {
		    	JSONArray dataToPost = createDataForUserRoles(list_Roles.get(i).get("Role").toString());
				String url = GET_USER_URL_V1 + userId + "/roles";
				ResponseBean response;
				response = HttpService.post(url, headers, dataToPost.toString());
				JSONObject respJson;
				if(Integer.parseInt(list_Roles.get(i).get("RoleID").toString()) == 0)
				{
					assertResponseCode(response.code, 404);
					respJson = new JSONObject(response.message);
					
					assertEquals("User Roles could not be saved. Please verify if the roles requested are available on the Portal.", respJson, "description");
					assertEquals("U009", respJson, "error_code");
					
					query_upRoles = "Select Count(*) as Count from UserPortalRoles where UserUID = '" + userId + "' AND RoleID = " + list_Roles.get(i).get("RoleID");
					list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
					assertEquals(0, Integer.parseInt(list_upRoles.get(0).get("Count").toString()), "Guest Role should not get inserted in UserPortalRoles table");
				}
				else if (Integer.parseInt(list_Roles.get(i).get("RoleID").toString()) == 1)
				{
					query_upRoles = "Select Count(*) as Count from UserPortalRoles where UserUID = '" + userId + "' AND RoleID = " + list_Roles.get(i).get("RoleID");
					list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
					assertEquals(1, Integer.parseInt(list_upRoles.get(0).get("Count").toString()), "Guest Role should not get inserted in UserPortalRoles table");
				}
				else
				{
					checkSuccessCode(response.code);
					JSONArray respJSON = new JSONArray(response.message);
					for(int j = 1;j<respJSON.length();j++){
						if(respJSON.getJSONObject(j).getString("role").equals(list_Roles.get(i).get("Role").toString())){
							assertJsonValueEquality(respJSON.getJSONObject(j),"role",list_Roles.get(i).get("Role").toString());
						}
					}
					
					query_upRoles = "Select Count(*) as Count from UserPortalRoles where UserUID = '" + userId + "' AND RoleID = " + list_Roles.get(i).get("RoleID");
					list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
					assertEquals(1, Integer.parseInt(list_upRoles.get(0).get("Count").toString()), "Role " + list_Roles.get(i).get("Role").toString() + " is not inserted in UserPortalRoles table");
				}
		    }
		    
		    String role_queryCount = "select Count(*) as  Count from Role where RoleID IN (select RoleID from PortalRoles where PortalCD = (select PortalCD from client where ClientCD = 'LCAUS'))";
		    List<Map> list_RolesCount = DbUtil.getInstance("Livecareer").getResultSetList(role_queryCount);

		    //Here -1 for Guest User as once user is registered Guest User role won't be inserted in UserPortalRoles table
		    int expectedRolesCount = Integer.parseInt(list_RolesCount.get(0).get("Count").toString()) - 1;
		   
		    query_upRoles = "Select Count(*) as Count from UserPortalRoles where UserUID = '" + userId + "'";
		    list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
		    assertEquals(expectedRolesCount, Integer.parseInt(list_upRoles.get(0).get("Count").toString()), "Total Roles count in UserPortalRoles table mismatch");
		    
		    //Guest role should not exist in UserPortalRoles table as User role override the unassigned role
		    query_upRoles = "Select Count(*) as Count from UserPortalRoles where UserUID = '" + userId + "' AND RoleID = 0";
		    list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
		    assertEquals(0, Integer.parseInt(list_upRoles.get(0).get("Count").toString()), "Guest role should not exist in UserPortalRoles table");
		    
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(dependsOnMethods = {"addUserRolesForRegisteredUser"})
	public void getUserRoles(){
		try{
			String userId = getUserUIDByPartyID(partyId);
			String url = GET_USER_URL_V1 + userId + "/roles";
			ResponseBean response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			JSONArray respJson = new JSONArray(response.message);
			String query_getUserRoles = "Select RoleID as id,Role as role from Role where RoleID IN (Select RoleID from UserPortalRoles where UserUID =  '" + userId + "')";
			List<Map> list_userRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_getUserRoles);
			String roles = list_userRoles.toString();
			JSONArray jArray = new JSONArray(roles);
			assertJsonArrayEquality(jArray,respJson);
			JSONObject dataToPost = new JSONObject();
			String newPassword = RandomStringUtils.randomAscii(10);
			dataToPost.put("password", newPassword);
			dataToPost.put("reset", 1);
			url = GET_USER_URL_V1 + partyId + "/password";
			response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			dataToPost = authenticateUserPostDataWithNewPwd(partyId, newPassword);
			url = BASE_URL + AUTHENTICATE_USER_URL;
			response = HttpService.post(url, headers, dataToPost.toString());
			checkSuccessCode(response.code);
			JSONObject respJson_Auth = new JSONObject(response.message);
			String user_role_key_value = respJson_Auth.getString("user_role").replace("\"", "");
			String query_getUserRoles_Auth = "Select Role from Role where RoleID IN (Select RoleID from UserPortalRoles where UserUID =  '" + userId + "')";
			List<Map> list_userRoles_Auth = DbUtil.getInstance("Livecareer").getResultSetList(query_getUserRoles_Auth);
			String [] str = new String[list_userRoles_Auth.size()];
			Map tmpData = null;
			for(int i = 0;i<str.length;i++){
				tmpData = list_userRoles_Auth.get(i);
				str[i] = tmpData.get("Role").toString();
			}
			assertEquals(Arrays.toString(str).replace("\"", "").replace(", ", ","), user_role_key_value, "Incorect Role names displayed in response");

		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test(dependsOnMethods = {"getUserRoles"})
	public void deleteUserRolesForRegisteredUser()
	{
		try
		{
			String userId = getUserUIDByPartyID(partyId);
			String url = GET_USER_URL_V1 + userId + "/roles";
			
			 String role_query = "select RoleID, Role from Role where RoleID IN (select RoleID from PortalRoles where PortalCD = (select PortalCD from client where ClientCD = 'LCAUS'))";
			 List<Map> list_Roles = DbUtil.getInstance("Livecareer").getResultSetList(role_query);
			 
			 String query_upRoles = "";
			 List<Map> list_upRoles = null;
			 JSONObject respJson;
			
			 for(int i = 0; i < list_Roles.size(); i++)
			    {
			    	JSONArray dataToPost = createDataForUserRoles(list_Roles.get(i).get("Role").toString());
					ResponseBean response;
					response = HttpService.put(url, headers, dataToPost.toString());
					
					if(Integer.parseInt(list_Roles.get(i).get("RoleID").toString()) == 0)
					{
						assertResponseCode(response.code, 400);
						respJson = new JSONObject(response.message);
						
						assertEquals("User Roles could not be deleted. Kindly verify if the roles are available with the User.", respJson, "description");
						assertEquals("U020", respJson, "error_code");
					}
					else
					{
						checkSuccessCode(response.code);
						respJson = new JSONObject(response.message);
						assertEquals("User Roles have been removed.", respJson.getString("message"), "Message mismatch");
						query_upRoles = "Select Count(*) as Count from UserPortalRoles where UserUID = '" + userId + "' AND RoleID = " + list_Roles.get(i).get("RoleID") + " AND IsActive = 0";
						list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
						assertEquals(1, Integer.parseInt(list_upRoles.get(0).get("Count").toString()), "Role not deleted from UserPortalRoles table");
					}
			    }
			 
			 	//Get User Roles
			 	List<JSONObject> expectedRoleList = new ArrayList<JSONObject>();
				JSONObject userRole = new JSONObject();
				userRole.put("id", 1);
				userRole.put("role", "User");

				expectedRoleList.add(userRole);		
				JSONArray expectedJArray = getRoleArray(expectedRoleList);
				
			 	url = GET_USER_URL_V1 + userId + "/roles";
				ResponseBean response = HttpService.get(url, headers);
				checkSuccessCode(response.code);
				JSONArray actualJArray = new JSONArray(response.message);
				
				assertJsonArrayEquality(expectedJArray, actualJArray);
				
				//Add Agent role again after deletion through Insert/Edit User Roles API. Same role should get inserted again after deletion if Insert/Edit User Roles API is called
				//As role was already inserted but got deleted so IsActive flag was set to 0. Once role is added again IsActive flag will be set to 1.
				List<JSONObject> inputRoleList = new ArrayList<JSONObject>();
				
				JSONObject agentRole = new JSONObject();
				agentRole.put("id", 110);
				agentRole.put("role", "Agent");
				inputRoleList.add(agentRole);
				
				expectedRoleList.add(agentRole);
				expectedJArray = getRoleArray(expectedRoleList);
				
				response = HttpService.post(url, headers, getRoleArray(inputRoleList).toString());
				checkSuccessCode(response.code);
				actualJArray = new JSONArray(response.message);
				
				assertJsonArrayEquality(expectedJArray, actualJArray);
				query_upRoles = "Select Count(*) as Count from UserPortalRoles where UserUID = '" + userId + "' AND RoleID = 110 AND IsActive = 1";
				list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
				assertEquals(1, Integer.parseInt(list_upRoles.get(0).get("Count").toString()), "Role not inserted in UserPortalRoles table");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
		
	@Test
	public void deleteUserRolesWithRequestBodyEmpty()
	{
		try
		{
			String userId = getUserUIDByPartyID(partyId);
			String url = GET_USER_URL_V1 + userId + "/roles";
			
			JSONArray dataToPost = new JSONArray();
			ResponseBean response = HttpService.put(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 400);
			JSONObject respJson = new JSONObject(response.message);
			
			assertEquals("User Roles could not be deleted. Kindly verify if the roles are available with the User.", respJson, "description");
			
			assertEquals("U020", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void callDeleteUserRolesApiWithValidAndInvalidRoles()
	{
		try
		{
			String userUID = getUserUIDAfterAddingUserRoles();
			
			String url = GET_USER_URL_V1 + userUID + "/roles";
			
			List<JSONObject> inputRoleList = new ArrayList<JSONObject>();
			
			//Valid role for LCAUS
			JSONObject agentRole = new JSONObject();
			agentRole.put("id", 110);
			agentRole.put("role", "Agent");
			
			JSONObject CounselorRole = new JSONObject();
			CounselorRole.put("id", 2);
			CounselorRole.put("role", "Counselor");
			
			inputRoleList.add(agentRole);
			inputRoleList.add(CounselorRole);
			
			ResponseBean response = HttpService.put(url, headers, getRoleArray(inputRoleList).toString());
			assertResponseCode(response.code, 400);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("U020", respJson, "error_code");
			assertEquals("User Roles could not be deleted. Kindly verify if the roles are available with the User.", respJson, "description");
			
		}
		catch (Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void callDeleteUserRolesApiWithRoleThatIsAlreadyDeleted()
	{
		try
		{
			String userUID = getUserUIDAfterAddingUserRoles();
			
			String url = GET_USER_URL_V1 + userUID + "/roles";
			
			List<JSONObject> inputRoleList = new ArrayList<JSONObject>();
			
			//Valid role for LCAUS
			JSONObject agentRole = new JSONObject();
			agentRole.put("id", 110);
			agentRole.put("role", "Agent");
			
			inputRoleList.add(agentRole);
			
			ResponseBean response = HttpService.put(url, headers, getRoleArray(inputRoleList).toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("User Roles have been removed.", respJson.getString("message"), "Message mismatch");
			
			response = HttpService.put(url, headers, getRoleArray(inputRoleList).toString());
			assertResponseCode(response.code, 400);
			respJson = new JSONObject(response.message);
			assertEquals("User Roles could not be deleted. Kindly verify if the roles are available with the User.", respJson, "description");
			assertEquals("U020", respJson, "error_code");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void userRolesReturningApiShouldOnlyReturnApplicablePortalRoles()
	{
		try
		{
			String userUID = getUserUIDAfterAddingUserRoles();
			
			String url = GET_USER_URL_V1 + userUID + "/roles";
			
			//Get JEMPD Headers
			HashMap<String, String> jempdHeaders = HeaderManager.getHeaders("JEMPD", access_token, "1234");
			
			List<JSONObject> inputRoleList = new ArrayList<JSONObject>();
			List<JSONObject> expectedRoleList = new ArrayList<JSONObject>();
			
			//Valid role for JEMPD
			JSONObject employerRole = new JSONObject();
			employerRole.put("id", 301);
			employerRole.put("role", "Employer");		
			inputRoleList.add(employerRole);
			
			JSONObject userRole = new JSONObject();
			userRole.put("id", 1);
			userRole.put("role", "User");
			
			expectedRoleList.add(userRole);		
			expectedRoleList.add(employerRole);		
			
			JSONArray expectedJArray = getRoleArray(expectedRoleList);
			
			ResponseBean response = HttpService.post(url, jempdHeaders, getRoleArray(inputRoleList).toString());
			checkSuccessCode(response.code);
			JSONArray actualJArray = new JSONArray(response.message);
			assertJsonArrayEquality(expectedJArray, actualJArray);

			//Get User Roles for JEMPD
		 	url = GET_USER_URL_V1 + userUID + "/roles";
			response = HttpService.get(url, jempdHeaders);
			checkSuccessCode(response.code);
			actualJArray = new JSONArray(response.message);
			assertJsonArrayEquality(expectedJArray, actualJArray);
			
			//Get User Roles for LCAUS
			List<JSONObject> expectedRoleListLCAUS = new ArrayList<JSONObject>();
			
			JSONObject agentRole = new JSONObject();
			agentRole.put("id", 110);
			agentRole.put("role", "Agent");
			
			JSONObject supervisorRole = new JSONObject();
			supervisorRole.put("id", 112);
			supervisorRole.put("role", "Supervisor");

			expectedRoleListLCAUS.add(userRole);
			expectedRoleListLCAUS.add(agentRole);
			expectedRoleListLCAUS.add(supervisorRole);
			JSONArray expectedJArrayLCAUS = getRoleArray(expectedRoleListLCAUS);
			
		 	url = GET_USER_URL_V1 + userUID + "/roles";
			response = HttpService.get(url, headers);
			checkSuccessCode(response.code);
			actualJArray = new JSONArray(response.message);
			assertJsonArrayEquality(expectedJArrayLCAUS, actualJArray);
			
			//Authenticate user in LCAUS Portal
			String query_user = "Select PartyID from [user] where UserUID = '" + userUID + "'";
			List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);
			String party_id = list_user.get(0).get("PartyID").toString();
			JSONObject data = authenticateUserPostData(party_id);
			url = BASE_URL + AUTHENTICATE_USER_URL;
			
			response = HttpService.post(url, headers, data.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			String actualRoles = respJson.getString("user_role");
			String expectedRoles = "[\"User\",\"Agent\",\"Supervisor\"]";			
			assertEquals(expectedRoles, actualRoles, "Roles mismatch");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@SuppressWarnings("rawtypes")
	@Test(dataProviderClass = UserService_DP.class , dataProvider = "getRoleNamesInvalid")
	public void addUserRolesInvalidRoleNames(String roleNames){

		try{
			String query_userId = "select UserUID from [User] where partyID = " + partyId;
			List<Map> list_userId = DbUtil.getInstance("Livecareer").getResultSetList(query_userId);
			String userId= list_userId.get(0).get("UserUID").toString();
			JSONArray dataToPost = createDataForUserRoles(roleNames);
			String url = GET_USER_URL_V1 + userId + "/roles";
			ResponseBean response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code,500);
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void addUserRolesInvalidRoles(){

		try{
			//Get JEMPD Headers
			HashMap<String, String> jempdHeaders = HeaderManager.getHeaders("JEMPD", access_token, "1234");
			
			//Get JEMPD userUID
			JSONObject userData = postDataRegisteredUser();
			ResponseBean response = HttpService.post(createUserUrl, jempdHeaders, userData.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			String jempdPartyId = respJson.getString("party_id");
			String userId = getUserUIDByPartyID(jempdPartyId);
			
			List<JSONObject> inputRoleList = new ArrayList<JSONObject>();
			List<JSONObject> expectedRoleList = new ArrayList<JSONObject>();
			
			//Valid role for JEMPD
			JSONObject employerRole = new JSONObject();
			employerRole.put("id", 301);
			employerRole.put("role", "Employer");		
			
			JSONObject userRole = new JSONObject();
			userRole.put("id", 1);
			userRole.put("role", "User");		
			
			//Invalid role for JEMPD ClientCD
			JSONObject agentRole = new JSONObject();
			agentRole.put("id", 110);
			agentRole.put("role", "Agent");
			
			inputRoleList.add(employerRole);
			inputRoleList.add(agentRole);
			
			expectedRoleList.add(employerRole);
			expectedRoleList.add(userRole);
			JSONArray expectedJArray = getRoleArray(expectedRoleList);
			
			String url = GET_USER_URL_V1 + userId + "/roles";
			response = HttpService.post(url, jempdHeaders, getRoleArray(inputRoleList).toString());
			assertResponseCode(response.code,404);
			respJson = new JSONObject(response.message);
			assertEquals("User Roles could not be saved. Please verify if the roles requested are available on the Portal.", respJson, "description");
			assertEquals("U009", respJson, "error_code");
			
			//After removing Invalid Role
			inputRoleList.remove(agentRole);
			response = HttpService.post(url, jempdHeaders, getRoleArray(inputRoleList).toString());
			System.out.println(response);
			JSONArray actualJArray = new JSONArray(response.message);
			assertJsonArrayEquality(expectedJArray, actualJArray);

		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void addUserRolesForGuestUser(){
		try{	
			//create guest user
			JSONObject respJson = createUser(postDataGuestUser());
			String userUID = respJson.getString("user_uid");
			
			//Verify User Role got inserted in UserPortalRoles table
			String query_upRoles = "Select RoleID from UserPortalRoles where UserUID = '" + userUID + "'";
			List<Map> list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
			
		    if(list_upRoles.isEmpty())
			{
				fail("No role is found in UserPortalRoles table");
			}
			assertEquals(0, Integer.parseInt(list_upRoles.get(0).get("RoleID").toString()), "Incorrect Role is inserted in UserPortalRoles table");
			
			JSONArray dataToPost = createDataForUserRoles("Counselor");
			String url = GET_USER_URL_V1 + userUID + "/roles";
			ResponseBean response;
			response = HttpService.post(url, headers, dataToPost.toString());
			assertResponseCode(response.code, 401);
			respJson = new JSONObject(response.message);
			
			assertEquals("User Roles could not be saved. Kindly check if you have permission to update the Roles.", respJson, "description");
			assertEquals("U018", respJson, "error_code");
			
			query_upRoles = "Select Count(*) as Count from UserPortalRoles where UserUID = '" + userUID + "' AND RoleID = (Select RoleID from Role where Role = 'Counselor')";
			list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
			assertEquals(0, Integer.parseInt(list_upRoles.get(0).get("Count").toString()), "Role Counselor should not get inserted in UserPortalRoles table");
		    
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void addSameUserRoleMultipleTimes(){
		try{
			String party_Id = setPartyID();
			String userId = getUserUIDByPartyID(party_Id);
				
			List<JSONObject> inputRoleList = new ArrayList<JSONObject>();
			List<JSONObject> expectedRoleList = new ArrayList<JSONObject>();
			
			JSONObject agentRole = new JSONObject();
			agentRole.put("id", 110);
			agentRole.put("role", "Agent");
			
			inputRoleList.add(agentRole);
			
			JSONObject userRole = new JSONObject();
			userRole.put("id", 1);
			userRole.put("role", "User");
			
			expectedRoleList.add(userRole);
			expectedRoleList.add(agentRole);
			JSONArray expectedJArray = getRoleArray(expectedRoleList);
		    
			String url = GET_USER_URL_V1 + userId + "/roles";
			ResponseBean response = HttpService.post(url, headers, getRoleArray(inputRoleList).toString());
			JSONArray actualJArray = new JSONArray(response.message);
			assertJsonArrayEquality(expectedJArray, actualJArray);
			String query_upRoles = "Select Count(*) as Count from UserPortalRoles where UserUID = '" + userId + "' AND RoleID = (select RoleID from Role where Role = 'Agent')";
			List<Map> list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
			assertEquals(1, Integer.parseInt(list_upRoles.get(0).get("Count").toString()), "Agent Role is not inserted");
			
			//Adding Agent role 2 more times
			inputRoleList.add(agentRole);
			inputRoleList.add(agentRole);
			response = HttpService.post(url, headers, getRoleArray(inputRoleList).toString());
			
			actualJArray = new JSONArray(response.message);
			assertJsonArrayEquality(expectedJArray, actualJArray);
			list_upRoles = DbUtil.getInstance("Livecareer").getResultSetList(query_upRoles);
			assertEquals(1, Integer.parseInt(list_upRoles.get(0).get("Count").toString()), "Same Role should not get inserted multiple times in UserPortalRoles table");      
		}catch(Exception e){
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	private String getUserUIDAfterAddingUserRoles()
	{
		String userUID = "";
		try
		{
			String party_Id = setPartyID();
			userUID = getUserUIDByPartyID(party_Id);
			
			List<JSONObject> inputRoleList = new ArrayList<JSONObject>();
			List<JSONObject> expectedRoleList = new ArrayList<JSONObject>();
			
			//Valid role for LCAUS
			JSONObject agentRole = new JSONObject();
			agentRole.put("id", 110);
			agentRole.put("role", "Agent");
			
			JSONObject supervisorRole = new JSONObject();
			supervisorRole.put("id", 112);
			supervisorRole.put("role", "Supervisor");
			
			inputRoleList.add(agentRole);
			inputRoleList.add(supervisorRole);
			
			JSONObject userRole = new JSONObject();
			userRole.put("id", 1);
			userRole.put("role", "User");
			
			expectedRoleList.add(userRole);
			expectedRoleList.add(agentRole);
			expectedRoleList.add(supervisorRole);
			JSONArray expectedJArray = getRoleArray(expectedRoleList);
			
			String url = GET_USER_URL_V1 + userUID + "/roles";
			ResponseBean response = HttpService.post(url, headers, getRoleArray(inputRoleList).toString());
			checkSuccessCode(response.code);
			JSONArray actualJArray = new JSONArray(response.message);
			assertJsonArrayEquality(expectedJArray, actualJArray);	
		}
		catch (Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		return userUID;
	}
}
