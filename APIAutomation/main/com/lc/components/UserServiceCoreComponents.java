package com.lc.components;

import static com.lc.constants.Constants_UserService.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.lc.assertion.AssertionUtil;
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.HeaderManager;
import com.lc.utils.ReadProperties;

public class UserServiceCoreComponents extends AssertionUtil{
	public static String access_token = "";
	public SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public int counter = 0;
	
	@Parameters("Environment")
	@BeforeTest
	public void setupEnvironment(String env){
		ReadProperties.setupEnvironmentProperties(env, "res/userservicedata/config.json");
		generateAccessToken();
	}
	
	public String setPartyID(){
		String partyId = null;
		try{
			JSONObject dataToPost = postDataRegisteredUser();
			ResponseBean response = HttpService.post(createUserUrl,HeaderManager.getHeaders("LCAUS", access_token, "1234"), dataToPost.toString());
			checkSuccessCode(response.code);
			assertNotNull(response.message, "PartyId does not appear in response");
			JSONObject respJson = new JSONObject(response.message);
			partyId = respJson.getString("party_id");
			String url = GET_USER_URL_V1 + partyId + "/optins";
			String optin = "NEWS";
			JSONArray dataToPostOptin = new JSONArray();
			JSONObject parentJson = new JSONObject();
			parentJson.put("response","1");
			parentJson.put("code", optin);
			dataToPostOptin.put(parentJson);
			response = HttpService.post(url, HeaderManager.getHeaders("LCAUS", access_token, "1234"), dataToPostOptin.toString());
			checkSuccessCode(response.code);	
		}catch(Exception e){
			e.printStackTrace();
		}
		return partyId;
	}

	public void generateAccessToken(){

		try {
			String dataToPost = "grant_type=client_credentials&client_id=BTEST&client_secret=UGFuZGl0UmF2aVNoYW5rYXIwNDA3MTkyMA==&response_type=token:";
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD", "LCAUS");
			ResponseBean response = HttpService.post(USERSERVICE_AUTH_URL, headers, dataToPost);
			checkSuccessCode(response.code);
			String respString = response.message;
			JSONObject respJson = new JSONObject(respString);
			assertEquals(respJson.get("token_type").toString() , "bearer", "Issue with getting token_type");
			assertNotNull(respJson.get("access_token").toString() , "Issue with getting access token");
			assertNotNull(respJson.get("refresh_token").toString(), "Issue with getting refresh_token");
			assertNotNull(respJson.get("expires_in").toString(), "Issue with getting expires_in");
			access_token = "Bearer "+respJson.get("access_token");
			if(StringUtils.isEmpty(access_token)){
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@SuppressWarnings("rawtypes")
	public JSONObject authenticateUserPostData(String partyId){

		try{
			JSONObject authUserPostData = new JSONObject();
			String query = "Select EmailAddress, HitlogID from Person where partyID = " + partyId;
			List<Map> list = DbUtil.getInstance("Livecareer").getResultSetList(query);
			Map tmpData = list.get(0);
			System.out.println(tmpData);
			authUserPostData.put("username", tmpData.get("EmailAddress").toString());
			authUserPostData.put("password", "123456");
			authUserPostData.put("ip", "14.141.80.210");
			authUserPostData.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			authUserPostData.put("visit_id", tmpData.get("HitlogID").toString());
			authUserPostData.put("portal_cd", "LCAUS");
			authUserPostData.put("product_cd", "RSM");
			return authUserPostData;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject authenticateUserPostData(HashMap<String, String> rowData){
		try{
			JSONObject authUserPostData = new JSONObject();
			authUserPostData.put("username", rowData.get("username").toString());
			authUserPostData.put("password", rowData.get("username").toString());
			authUserPostData.put("ip", "14.141.80.210");
			authUserPostData.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			authUserPostData.put("visit_id", "1224406");
			authUserPostData.put("portal_cd", "LCAUS");
			authUserPostData.put("product_cd", "RSM");
			return authUserPostData;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject authenticateUserPostData_USRS37(String emailAddress,String password){

		try{
			JSONObject authUserPostData = new JSONObject();
			authUserPostData.put("username", emailAddress);
			authUserPostData.put("password", password);
			authUserPostData.put("ip", "14.141.80.210");
			authUserPostData.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			authUserPostData.put("visit_id", "");
			authUserPostData.put("portal_cd", "LCAUS");
			authUserPostData.put("product_cd", "RSM");
			return authUserPostData;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public JSONObject authenticateUserPostDataWithNewPwd(String partyId,String password){

		try{
			JSONObject authUserPostData = new JSONObject();
			String query = "Select EmailAddress from Person where partyID = " + partyId;
			List<Map> list = DbUtil.getInstance("Livecareer").getResultSetList(query);
			Map tmpData = list.get(0);
			authUserPostData.put("username", tmpData.get("EmailAddress").toString());
			authUserPostData.put("password", password);
			authUserPostData.put("ip", "14.141.80.210");
			authUserPostData.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			authUserPostData.put("visit_id", RandomStringUtils.randomNumeric(5));
			authUserPostData.put("portal_cd", "LCAUS");
			authUserPostData.put("product_cd", "RSM");
			return authUserPostData;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public JSONArray createDataForUserOptin(String opt, String partyId){

		try{
			String response = "0";
			JSONArray dataToPost = new JSONArray();
			JSONObject parentJson = new JSONObject();
			String query_optins = "Select OptinID, Response from Useroptins where partyID = " + partyId;
			List<Map> list_optins = DbUtil.getInstance("Livecareer").getResultSetList(query_optins);
			HashMap<Integer, String> optinCD = new HashMap<Integer, String>();
			optinCD = userOptinsMapping();
			for(int i = 0;i< list_optins.size();i++){
				String optinId = list_optins.get(i).get("OptinID").toString();
				String optinCode = optinCD.get(Integer.parseInt(optinId));
				if(optinCode.equals(opt)){
					String resp = list_optins.get(i).get("Response").toString();
					if(resp.equals("0")){
						response="1";
					}
				}
			}
			parentJson.put("response",response);
			parentJson.put("code", opt);
			dataToPost.put(parentJson);
			return dataToPost;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public JSONArray createDataForUserOptinInvalid(String opt){

		try{
			JSONArray dataToPost = new JSONArray();
			JSONObject parentJson = new JSONObject();
			parentJson.put("response",0);
			parentJson.put("code", opt);
			dataToPost.put(parentJson);
			return dataToPost;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public JSONArray createDataForUserOptinInvalidResponse(String res){

		try{
			JSONArray dataToPost = new JSONArray();
			JSONObject parentJson = new JSONObject();
			parentJson.put("response",res);
			parentJson.put("code", "NEWS");
			dataToPost.put(parentJson);
			return dataToPost;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public JSONArray createDataForUserPreference(){
		try{
			JSONArray dataToPost = new JSONArray();
			JSONObject parentJson = new JSONObject();
			parentJson.put("code","QA"+RandomStringUtils.randomAlphabetic(2).toUpperCase());
			parentJson.put("value", "1");
			dataToPost.put(parentJson);
			return dataToPost;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, String> userOptinsMapping(){
		try{
			HashMap<Integer, String> OptinCD = new HashMap<Integer, String>();
			OptinCD.put(28, "POSM"); //Postal mails
			OptinCD.put(27, "JBAW"); //Job Alerts Weekly
			OptinCD.put(26, "JBAD"); //Job Alerts Daily
			OptinCD.put(25, "RESJ"); //Resume & Job Search Tips
			OptinCD.put(24, "SMVL"); //LiveCareer Smart Values
			OptinCD.put(20, "SPOF"); //Special offer by Emails
			OptinCD.put(19, "NEWS"); //Email newsletters
			return OptinCD;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public JSONArray createDataForUserRoles(HashMap<String, String> roleNamesData){
		try{
			JSONArray jArray = new JSONArray();
			JSONObject jSONObj = new JSONObject();
			jSONObj.put("role", roleNamesData.get("RoleName").toString());
			jArray.put(jSONObj);
			return jArray;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public JSONArray createDataForUserRoles(String roleNamesInvalidData)
	{
		try{
			JSONArray dataToPost = new JSONArray();
			JSONObject parentJson = new JSONObject();
			parentJson.put("role",roleNamesInvalidData);
			dataToPost.put(parentJson);
			return dataToPost;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject postDataGuestUser(){
		try{
			JSONObject dataCreateUser = new JSONObject();
			dataCreateUser.put("type", "guest");
			dataCreateUser.put("visit_id", "127854");
			dataCreateUser.put("visitor_id", "");
			dataCreateUser.put("referrer_id", "");
			dataCreateUser.put("ip_address", "14.141.80.210");
			dataCreateUser.put("is_active", true);
			return dataCreateUser;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject postDataRegisteredUser(){
		try{
			String emailAddress = "first.last" + getDateTime(new Date(), format, 0) + ++counter + "@livecareer.com";
			JSONObject dataCreateUser = new JSONObject();		
			dataCreateUser.put("type", "registered");
			dataCreateUser.put("first_name", "James-"+RandomStringUtils.randomAlphabetic(6));
			dataCreateUser.put("last_name", "Cobb-"+RandomStringUtils.randomAlphabetic(6));
			dataCreateUser.put("email_address", emailAddress);
			dataCreateUser.put("password", "123456");
			dataCreateUser.put("reset_pwd", true);
			dataCreateUser.put("visit_id", "127854");
			dataCreateUser.put("visitor_id", "");
			dataCreateUser.put("referrer_id", "65");
			dataCreateUser.put("country_cd", "US");
			dataCreateUser.put("product_cd", "MPR");
			dataCreateUser.put("ip_address", "14.141.80.210");
			dataCreateUser.put("is_active", true);
			dataCreateUser.put("phone_no", RandomStringUtils.randomNumeric(10));
			dataCreateUser.put("mobile_no", RandomStringUtils.randomNumeric(10));
			dataCreateUser.put("work_phone", RandomStringUtils.randomNumeric(10));
			return dataCreateUser;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject createDataForRegisteredUserUpdate(JSONObject obj){
		try{
			JSONObject dataUpdateUser = new JSONObject();
			dataUpdateUser.put("email_address" ,"QA-" +obj.getString("email_address"));
			dataUpdateUser.put("first_name" ,"QA-" +obj.getString("first_name"));
			dataUpdateUser.put("last_name" ,"QA-" +obj.getString("last_name"));
			dataUpdateUser.put("reset_pwd" ,obj.getString("reset_pwd").equals("true")? "false" :"true");
			dataUpdateUser.put("is_active" ,obj.getString("is_active").equals("true")? "false" :"true");
			dataUpdateUser.put("phone_no", RandomStringUtils.randomNumeric(10));
			dataUpdateUser.put("mobile_no", RandomStringUtils.randomNumeric(10));
			dataUpdateUser.put("work_phone", RandomStringUtils.randomNumeric(10));
			return dataUpdateUser;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONObject createDataForGuestUserUpdate(JSONObject obj){
		try{
			String emailAddress = "first.last" + getDateTime(new Date(), format, 0) + ++counter + "@lc.com";
			JSONObject dataUpdateUser = new JSONObject();
			dataUpdateUser.put("email_address" ,emailAddress);
			dataUpdateUser.put("password" ,RandomStringUtils.randomAlphanumeric(8));
			dataUpdateUser.put("first_name" ,"QA-" + RandomStringUtils.randomNumeric(4));
			dataUpdateUser.put("last_name" ,"SE-" + RandomStringUtils.randomNumeric(4));
			dataUpdateUser.put("reset_pwd" ,true);
			dataUpdateUser.put("is_active" ,obj.getString("is_active").equals("true")? "false" :"true");
			dataUpdateUser.put("phone_no", RandomStringUtils.randomNumeric(10));
			dataUpdateUser.put("mobile_no", RandomStringUtils.randomNumeric(10));
			dataUpdateUser.put("work_phone", RandomStringUtils.randomNumeric(10));
			return dataUpdateUser;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject createUser(JSONObject dataToPost)
	{
		try
		{
			ResponseBean response = HttpService.post(createUserUrl,HeaderManager.getHeaders("LCAUS", access_token, "1234"), dataToPost.toString());
			if(response != null)
			{
				checkSuccessCode(response.code);
				return new JSONObject(response.message);
			}
			else
			{
				fail("Create User Response is null");
			}
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject updateUserStage(String userStageId, String userUID)
	{
		try
		{
			JSONObject userStage = new JSONObject();
			userStage.put("product_cd", "RSM");
			userStage.put("userstage_id", userStageId);
			String url = BASE_URL + "/v1/users/" + userUID + "/stages"; 
			ResponseBean response = HttpService.post(url, HeaderManager.getHeaders("LCAUS", access_token, "1234"), userStage.toString()); 
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

	public JSONObject updateUserLevel(int userLevelID, int responseCode, String userUID)
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("product_cd", "RSM");
			dataToPost.put("userlevel_id", userLevelID);
			String url = BASE_URL + "/v1/users/" + userUID + "/levels";
			ResponseBean response = HttpService.put(url, HeaderManager.getHeaders("LCAUS", access_token, "1234"), dataToPost.toString()); 
			assertResponseCode(responseCode, response.code);
			return new JSONObject(response.message);
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public String getUserUIDByPartyID(String partyID)
	{
		if(partyID != null)
		{
			String query_user = "Select UserUID from [user] where partyID = " + partyID;
			List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);
			return list_user.get(0).get("UserUID").toString();
		}
		else
		{
			fail("Party Id is NULL");
			return null;
		}
	}

	public void compareTwoJsonObjects(JSONObject expected, JSONObject actual, String[] parameters)
	{
		try
		{
			for(int i=0;i<parameters.length;i++)
			{
				assertEquals(expected.getString(parameters[i]), actual.getString(parameters[i]), parameters[i] + " mismatch");
			}
		}
		catch(Exception e)
		{
			fail(e.getMessage());
		}
	}

	public String getSessionGuidByAuthenticatingUserByUserUID(String userUID)
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("ip", "138.91.242.92");
			dataToPost.put("type", "guest");
			dataToPost.put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			
			String url = GET_USER_URL_V1 + userUID + "/sessions";
			ResponseBean response = HttpService.post(url, HeaderManager.getHeaders("LCAUS", access_token, "1234"), dataToPost.toString());
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			return respJson.getString("session_id");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject getSessionGuidByLoginAsUser(String partyID)
	{
		try
		{
			JSONObject dataToPost = authenticateUserPostData(partyID);
			String url = BASE_URL + AUTHENTICATE_USER_URL;
			ResponseBean response = HttpService.post(url, HeaderManager.getHeaders("LCAUS", access_token, "1234"), dataToPost.toString());
			checkSuccessCode(response.code);
			return new JSONObject(response.message);
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public String getDateTime(Date date, SimpleDateFormat simpleDateFormat, Integer numberOfDays) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.DAY_OF_MONTH, numberOfDays);
	    return simpleDateFormat.format(cal.getTime());
	}
	
	public JSONArray getRoleArray(List<JSONObject> roleList)
	{
		try
		{
			JSONArray roleArr = new JSONArray();
			Iterator<JSONObject> iterator = roleList.iterator();

			while(iterator.hasNext())
			{
				roleArr.put(iterator.next());
			}
			
			return roleArr;
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
}
