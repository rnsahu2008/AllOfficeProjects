package com.lc.components;

import static com.lc.constants.Constants_JobsStore.*;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.lc.assertion.AssertionUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.ReadProperties;

public class JobStoreCoreComponents extends AssertionUtil{
	
	@Parameters("Environment")
	@BeforeTest
	public void setupEnvironment(String env){
		ReadProperties.setupEnvironmentProperties(env,"res/jobstoredata/config.json");
	}
	
	public ResponseBean createRandomJob(){
		try {
			JSONObject dataInPost = new JSONObject();
			dataInPost.put("PUID", RandomStringUtils.randomNumeric(8));
			dataInPost.put("Title", RandomStringUtils.randomAlphanumeric(8));
			dataInPost.put("Description", RandomStringUtils.randomAlphanumeric(8));
			dataInPost.put("City", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("State", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("PostalCode", RandomStringUtils.randomAlphanumeric(6));
			dataInPost.put("CountryCD", "US");
			dataInPost.put("StatusCD", "ACTV");
			dataInPost.put("SourceCD", "JTR");
			dataInPost.put("Company", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("PostedOn", "2015-04-06 00:00:00");
			dataInPost.put("EmploymentTypeCD", "1234test1234");
			dataInPost.put("ApplyUrl", "http://www.jobtap.com/job?=" + RandomStringUtils.randomNumeric(4));
			dataInPost.put("StringHash", "");			
			
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResponseBean createRandomJob(String title){
		try {
			JSONObject dataInPost = new JSONObject();
			dataInPost.put("PUID", RandomStringUtils.randomNumeric(8));
			dataInPost.put("Title", title);
			dataInPost.put("Description", RandomStringUtils.randomAlphanumeric(8));
			dataInPost.put("City", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("State", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("PostalCode", RandomStringUtils.randomAlphanumeric(6));
			dataInPost.put("CountryCD", "US");
			dataInPost.put("StatusCD", "ACTV");
			dataInPost.put("SourceCD", "JTR");
			dataInPost.put("Company", RandomStringUtils.randomAlphabetic(8));
			dataInPost.put("PostedOn", "2015-04-06 00:00:00");
			dataInPost.put("EmploymentTypeCD", "1234test1234");
			dataInPost.put("ApplyUrl", "http://www.jobtap.com/job?=" + RandomStringUtils.randomNumeric(4));
			dataInPost.put("StringHash", "");			
			
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataInPost.toString());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String createUrl(HashMap<String, String> rowData){
		try {
			 StringBuffer sb = new StringBuffer();
			 Iterator it = rowData.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pair = (Map.Entry)it.next();
			        //System.out.println(pair.getKey() + " = " + pair.getValue());
			        if(!StringUtils.isEmpty(pair.getValue().toString())){
			        	sb.append(pair.getKey().toString() + "=" + URLEncoder.encode(pair.getValue().toString(), "UTF-8"));
			        	sb.append("&");
			        }			    }
			    String concatedUrl = sb.toString();
			    return concatedUrl.substring(0, concatedUrl.length()-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject createJobJson(HashMap<String, String> rowData){
		JSONObject json = new JSONObject();
		try {
			 Iterator it = rowData.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pair = (Map.Entry)it.next();
			        //System.out.println(pair.getKey() + " = " + pair.getValue());
			        if(!StringUtils.isEmpty(pair.getValue().toString())){
			        	json.put(pair.getKey().toString(), pair.getValue().toString());
			        }			
		        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public ResponseBean createRandomJob(String statusCD , String sourceCD){
		try {
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("PUID", RandomStringUtils.randomNumeric(8));
			dataToPost.put("Title", RandomStringUtils.randomAlphanumeric(8));
			dataToPost.put("Description", RandomStringUtils.randomAlphanumeric(8));
			dataToPost.put("City", RandomStringUtils.randomAlphabetic(8));
			dataToPost.put("State", RandomStringUtils.randomAlphabetic(8));
			dataToPost.put("PostalCode", RandomStringUtils.randomAlphanumeric(6));
			dataToPost.put("CountryCD", "US");
			dataToPost.put("StatusCD", statusCD);
			dataToPost.put("SourceCD", sourceCD);
			dataToPost.put("Company", RandomStringUtils.randomAlphabetic(8));
			dataToPost.put("PostedOn", "2015-04-06 00:00:00");
			dataToPost.put("EmploymentTypeCD", "1234test1234");
			dataToPost.put("ApplyUrl", "http://www.jobtap.com/job?=" + RandomStringUtils.randomNumeric(4));
			dataToPost.put("StringHash", "");			
			
			String url = BASE_URL_JOBS + "/v1/jobs";
			ResponseBean response = HttpService.post(url, new HashMap<String, String>() , dataToPost.toString());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
