package com.lc.tests.emailapi;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.lc.components.EComCoreComponents;
import com.lc.dataprovider.Email_DP;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.json.EmailJson;

import static com.lc.constants.Constants_Email.*;

public class TransactionalEmails extends EComCoreComponents{	
	String access_token = "";	
	@BeforeClass
	public void generateAccessToken(){
		try {
			JSONObject json = new JSONObject();
			json.append("grant_type", "client_credentials");
			json.append("client_id", "LCAUS");
			json.append("client_secret", "bGl2ZWNhcmVlciByb2NrcyE=");
			json.append("response_type", "token:");
			
			String dataToPost = "grant_type=client_credentials&client_id=LCAUS& client_secret=bGl2ZWNhcmVlciByb2NrcyE=&response_type=token:";

			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD", "LCAUS");
			
			ResponseBean response = HttpService.post(AUTH_TOKEN_URL, headers, dataToPost);
			checkSuccessCode(response.code);

			JSONObject respJson = new JSONObject(response.message);
			assertTrue(respJson.get("token_type").toString().equals("bearer"), "Issue with getting token_type");
			
			assertTrue(respJson.get("token_type").toString().equals("bearer"), "Issue with getting token_type");
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
	
	@Test (dataProviderClass=Email_DP.class, dataProvider="getHappyPath")
	public void transactionalEmailsHappyPath(HashMap<String, String> list){
		try {
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", access_token);
			headers.put("client_request_reference", "635004786");
			headers.put("ClientCD", "LCAUS");

			String dataToPost = EmailJson.eMailData(list);
			ResponseBean response = HttpService.post(TRANSACTIONAL_EMAIL_URL, headers, dataToPost);
			int actualCode = response.code;
			int expectedCode = Integer.parseInt(list.get("response_code").toString());
			assertTrue(actualCode==expectedCode , "Actual error code " + actualCode + " does not match expected error code " + expectedCode );

			JSONObject respJson = new JSONObject(response.message);
			assertJsonContainsKey(respJson, "client_requestid");
			assertJsonValueIsNotNull(respJson, "to_email");
			assertJsonValueIsNotNull(respJson, "cuid");
			
			assertJsonValueEquality(respJson, "StatusCD", "PEND");
			assertJsonValueEquality(respJson, "Message", "Message successfully recieved");
			assertJsonValueEquality(respJson, "client_cd", "LCAUS");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test (dataProviderClass=Email_DP.class, dataProvider="getNegativeScenarios")
	public void transactionalEmailsNegative(HashMap<String, String> list){
		try {
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", access_token);
			headers.put("client_request_reference", "635004786");
			headers.put("ClientCD", "LCAUS");

			String dataToPost = EmailJson.eMailData(list);
			ResponseBean response = HttpService.post(TRANSACTIONAL_EMAIL_URL, headers, dataToPost);
			int actualCode = response.code;
			int expectedCode = Integer.parseInt(list.get("response_code").toString());
			assertTrue(actualCode==expectedCode , "Actual error code " + actualCode + " does not match expected error code " + expectedCode );
			JSONObject respJson = new JSONObject(response.message);
			assertJsonContainsKey(respJson, "ErrorMessage");
			assertJsonValueEquality(respJson, "ErrorMessage", list.get("error_message").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public void consolidateAssertions(){
		try {
			s_assert.assertAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}



