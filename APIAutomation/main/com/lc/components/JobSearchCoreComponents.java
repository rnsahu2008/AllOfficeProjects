package com.lc.components;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.lc.assertion.AssertionUtil;
import com.lc.utils.ReadProperties;

public class JobSearchCoreComponents extends AssertionUtil{	
	
	@Parameters("Environment")
	@BeforeTest
	public void setupEnvironment(String env){
		ReadProperties.setupEnvironmentProperties(env,"res/jobsearchdata/config.json");
	}
	
	public JSONObject createJobLogRequest(String id) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("SearchGuid" , id);
		json.put("UserID" , RandomStringUtils.randomAlphabetic(5));
		json.put("JobID" , RandomStringUtils.randomAlphabetic(5));
		json.put("JobTitle" , RandomStringUtils.randomAlphabetic(5));
		json.put("JobUrl" , RandomStringUtils.randomAlphabetic(5));
		json.put("Query" , RandomStringUtils.randomAlphabetic(5));
		json.put("Location" , RandomStringUtils.randomAlphabetic(5));
		json.put("IP" , "");
		json.put("Position" , 0);
		json.put("ClientName" , "");
		json.put("PageNumber" , 0);
		json.put("PageSize" , 0);
		json.put("UserLocation" , "");
		return json;
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

}
