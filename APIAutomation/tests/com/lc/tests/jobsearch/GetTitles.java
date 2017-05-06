package com.lc.tests.jobsearch;

import java.net.URLEncoder;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;

import static com.lc.constants.Constants_JobSearch.*;

import com.lc.common.Common;
import com.lc.components.JobSearchCoreComponents;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;

public class GetTitles extends JobSearchCoreComponents{

	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test
	public void getTitlesWithDefaultCount(){
		try {
			String searchText = "dev";
			String url = BASE_URL + JOB_SEARCH + "/titles?prefix=" + searchText;
			ResponseBean response = HttpService.get(url, null);
			JSONArray respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			if(respJson.length()==10){
				assertTrue(true, "Not getting default (10) number of locations");
				for (int i=0 ; i<respJson.length() ; i++) {
					assertTrue(respJson.getString(i).toLowerCase().contains(searchText), "Value '" + respJson.getString(i) + "' does not contain prefix '" + searchText + "' : ");
				}
				}else if(respJson.length()<10){
					for (int i=0 ; i<respJson.length() ; i++) {
						assertTrue(respJson.getString(i).toLowerCase().contains(searchText), "Value '" + respJson.getString(i) + "' does not contain prefix '" + searchText + "' : ");
					}
				}
				else if(respJson.length()>10){
					assertTrue(false, "Not getting default (10) number of locations");
				}
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getTitlesWithRandomCount(){
		try {
			String searchText = "dev";
			String url = BASE_URL + JOB_SEARCH + "/titles?prefix=" + searchText;
			ResponseBean response = HttpService.get(url, null);
			JSONArray respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			int records = respJson.length();
			Random rnd = new Random();
			int countNum = rnd.nextInt(records) + 1;
			url = BASE_URL + JOB_SEARCH + "/titles?prefix="+searchText+"&count="+countNum;
			response = HttpService.get(url, null);
			respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			assertTrue(respJson.length()==countNum, "Not getting specified ("+countNum+") number of locations");
			for (int i=0 ; i<respJson.length() ; i++) {
				assertTrue(respJson.getString(i).toLowerCase().contains(searchText), "Value '" + respJson.getString(i) + "' does not contain prefix '" + searchText + "' : ");
			}
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	/*@Test
	public void getTitlesWithSpecificCount_15(){
		try {
			String searchText = "dev";
			String url = BASE_URL + JOB_SEARCH + "/titles?prefix=" + searchText + "&count=15";
			ResponseBean response = HttpService.get(url, null);
			JSONArray respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			assertTrue(respJson.length()==15, "Not getting specified (15) number of titles");
			for (int i=0 ; i<respJson.length() ; i++) {
				assertTrue(respJson.getString(i).toLowerCase().contains(searchText), "Value '" + respJson.getString(i) + "' does not contain prefix 'manager' : ");
			}
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}*/
	
	@Test
	public void getTitlesWithInvalidText(){
		try {
			String searchText = "qwerty123";
			String url = BASE_URL + JOB_SEARCH + "/titles?prefix=" + searchText;
			ResponseBean response = HttpService.get(url, null);
			JSONArray respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			assertTrue(respJson.length()==0, "Showing incorrect titles for incorrect text");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getTitlesWithTextHavingSpace(){
		try {
			String searchText = "software dev";
			String url = BASE_URL + JOB_SEARCH + "/titles?prefix=" + URLEncoder.encode(searchText, "UTF-8");
			ResponseBean response = HttpService.get(url, null);
			JSONArray respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			assertTrue(respJson.length()!=0, "Not getting any title for given search text");
			for (int i=0 ; i<respJson.length() ; i++) {
				assertTrue(respJson.getString(i).toLowerCase().contains(searchText), "Value '" + respJson.getString(i) + "' does not contain prefix 'software dev' : ");
			}
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getTitlesWithInvalidCount(){
		try {
			String searchText = "software dev";
			String url = BASE_URL + JOB_SEARCH + "/titles?prefix="+URLEncoder.encode(searchText, "UTF-8")+"&count=0";
			ResponseBean response = HttpService.get(url, null);
			JSONObject respJson = new JSONObject(response.message);
			assertResponseCode(response.code, 400);
			JSONObject expectedJSON = new JSONObject("{\"Message\": \"count should be greater than 0.\"}");
			assertJsonObjectEquality(expectedJSON, respJson);
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
}
