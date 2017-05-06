package com.lc.tests.jobstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.JobStoreCoreComponents;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

import static com.lc.constants.Constants_JobsStore.*;

public class UpdateJob_Negative extends JobStoreCoreComponents {
List<String> jobIds = new ArrayList<String>();
private String jobsUrl;
		
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
	    jobsUrl = BASE_URL_JOBS + "/v1/jobs";
	    Common.apiLogInfo.clear();
	}
	
	@Test(dataProvider = "getNullEmptyNonExistingstring")
	public void updateJobById_InValidPUID(String inputString){
		try {		
			Job randomJob = Job.getRandomGeneratedJob();
			JSONObject dataInPost = Job.getJSONObject(randomJob);
			ResponseBean response = HttpService.post(jobsUrl, new HashMap<String, String>() , dataInPost.toString());
			checkSuccessCode(response.code);
			String id = response.message.replace("\"", "");
			
			String url = BASE_URL_JOBS + "/v1/jobs/" + id;				
			
			JSONObject json = Job.getJSONObject(Job.getRandomGeneratedJob());
			json = this.getInvalidJSONObject(inputString,"PUID", json);
							
			response = HttpService.put(url, new HashMap<String, String>(), json.toString());					
			assertResponseCode(response.code,400);
			///assertTrue(response.message.contains("The request is invalid."), "Error message does not contains \"The request is invalid\" Now have full message body as " 
					///+ response.message);
			assertNotNull(response.message, "Getting NULL in response");	
			response = HttpService.get(url, null);
			Job actulJob = Job.getJob(new JSONObject(response.message));
			assertEquals(randomJob.ToString(), actulJob.ToString(), "Job found mis match..");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
		
	@Test(dataProvider = "getnullandemptystring")
	public void updateJobById_InValidAllPropertyOneByOne(String inputString){
		try {		
			Job randomJob = Job.getRandomGeneratedJob();
			JSONObject dataInPost = Job.getJSONObject(randomJob);
			ResponseBean response = HttpService.post(jobsUrl, new HashMap<String, String>() , dataInPost.toString());
			checkSuccessCode(response.code);
			String id = response.message.replace("\"", "");
			
			String url = BASE_URL_JOBS + "/v1/jobs/" + id;				
			
			//InValidTitle
			JSONObject json = Job.getJSONObject(Job.getRandomGeneratedJob());
			json = this.getInvalidJSONObject(inputString,"Title", json);
			json = this.getInvalidJSONObject(randomJob.PUID,"PUID", json);							
			response = HttpService.put(url, new HashMap<String, String>(), json.toString());					
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");	
			
			//InValidDescription
		    json = Job.getJSONObject(Job.getRandomGeneratedJob());
			json = this.getInvalidJSONObject(inputString,"Description", json);
			json = this.getInvalidJSONObject(randomJob.PUID,"PUID", json);							
			response = HttpService.put(url, new HashMap<String, String>(), json.toString());					
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
			
			//InValidCity
		    json = Job.getJSONObject(Job.getRandomGeneratedJob());
			json = this.getInvalidJSONObject(inputString,"City", json);
			json = this.getInvalidJSONObject(randomJob.PUID,"PUID", json);							
			response = HttpService.put(url, new HashMap<String, String>(), json.toString());					
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
			
			//InValidState
		    json = Job.getJSONObject(Job.getRandomGeneratedJob());
			json = this.getInvalidJSONObject(inputString,"State", json);
			json = this.getInvalidJSONObject(randomJob.PUID,"PUID", json);							
			response = HttpService.put(url, new HashMap<String, String>(), json.toString());					
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
			
			//InValidStatusCD Removing for now ,needs clarification 
		   /// json = Job.getJSONObject(Job.getRandomGeneratedJob());
			///json = this.getInvalidJSONObject(inputString,"StatusCD", json);
			///json = this.getInvalidJSONObject(randomJob.PUID,"PUID", json);							
			///response = HttpService.put(url, new HashMap<String, String>(), json.toString());					
			///assertResponseCode(response.code,400);
			///assertNotNull(response.message, "Getting NULL in response");
			
			//InValidSourceCD Removing for now ,needs clarification
		   /// json = Job.getJSONObject(Job.getRandomGeneratedJob());
			///json = this.getInvalidJSONObject(inputString,"SourceCD", json);
			///json = this.getInvalidJSONObject(randomJob.PUID,"PUID", json);							
			///response = HttpService.put(url, new HashMap<String, String>(), json.toString());					
			///assertResponseCode(response.code,400);
			///assertNotNull(response.message, "Getting NULL in response");
			
			//InValidCompany
		    json = Job.getJSONObject(Job.getRandomGeneratedJob());
			json = this.getInvalidJSONObject(inputString,"Company", json);
			json = this.getInvalidJSONObject(randomJob.PUID,"PUID", json);							
			response = HttpService.put(url, new HashMap<String, String>(), json.toString());					
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
			
			//InValidPostedOn
		    json = Job.getJSONObject(Job.getRandomGeneratedJob());
			json = this.getInvalidJSONObject(inputString,"PostedOn", json);
			json = this.getInvalidJSONObject(randomJob.PUID,"PUID", json);							
			response = HttpService.put(url, new HashMap<String, String>(), json.toString());					
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
			
			//InValidApplyUrl
		    json = Job.getJSONObject(Job.getRandomGeneratedJob());
			json = this.getInvalidJSONObject(inputString,"PostedOn", json);
			json = this.getInvalidJSONObject(randomJob.PUID,"PUID", json);							
			response = HttpService.put(url, new HashMap<String, String>(), json.toString());					
			assertResponseCode(response.code,400);
			assertNotNull(response.message, "Getting NULL in response");
			
			response = HttpService.get(url, null);
			Job actulJob = Job.getJob(new JSONObject(response.message));
			assertEquals(randomJob.ToString(), actulJob.ToString(), "Job found mis match..");						
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
		
	@DataProvider(name = "getnullandemptystring")
	public Object[][] getnullandemptystring()
	{
		return new Object[][] { { "" }, { "NULL" } , { "   " },{ null }};
	}
	
	@DataProvider(name = "getNullEmptyNonExistingstring")
	public Object[][] getNullEmptyNonExistingstring()
	{
		return new Object[][] { { "" }, { "NULL" } , { "   " },{ null },{ "NonExistingValuesToBeTested" }};
	}
	
	private JSONObject getInvalidJSONObject(String input,String properNameToInvalidate ,JSONObject dataInPost)
	{
		try {
			if(input=="NULL")
			{
			dataInPost.put(properNameToInvalidate, JSONObject.NULL);
			}
			else
			{
			dataInPost.put(properNameToInvalidate, input);	
			}
		}
		catch (Exception e) 
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		return dataInPost;
	}
}
