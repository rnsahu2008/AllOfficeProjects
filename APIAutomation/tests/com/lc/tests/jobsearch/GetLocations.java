package com.lc.tests.jobsearch;

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

public class GetLocations extends JobSearchCoreComponents{

	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}
	
	@Test
	public void getLocationsWithDefaultCount(){
		try {
			String url = BASE_URL + JOB_SEARCH + "/locations?prefix=new";
			ResponseBean response = HttpService.get(url, null);
			JSONArray respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			if(respJson.length()==10){
			assertTrue(true, "Not getting default (10) number of locations");
			for (int i=0 ; i<respJson.length() ; i++) {
				assertTrue(respJson.getString(i).toLowerCase().contains("new"), "Value '" + respJson.getString(i) + "' does not contain prefix 'new' : ");
			}
			}else if(respJson.length()<10){
				for (int i=0 ; i<respJson.length() ; i++) {
					assertTrue(respJson.getString(i).toLowerCase().contains("new"), "Value '" + respJson.getString(i) + "' does not contain prefix 'new' : ");
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
	public void getLocationsWithRandomCount(){
		try {
			String url = BASE_URL + JOB_SEARCH + "/locations?prefix=new";
			ResponseBean response = HttpService.get(url, null);
			JSONArray respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			int records = respJson.length();
			Random rnd = new Random();
			int countNum = rnd.nextInt(records) + 1;
			url = BASE_URL + JOB_SEARCH + "/locations?prefix=new&count="+countNum;
			response = HttpService.get(url, null);
			respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			assertTrue(respJson.length()==countNum, "Not getting specified ("+countNum+") number of locations");
			for (int i=0 ; i<respJson.length() ; i++) {
				assertTrue(respJson.getString(i).toLowerCase().contains("new"), "Value '" + respJson.getString(i) + "' does not contain prefix 'new' : ");
			}
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	/*@Test
	public void getLocationsWithSpecificCount_15(){
		try {
			String url = BASE_URL + JOB_SEARCH + "/locations?prefix=new&count=15";
			ResponseBean response = HttpService.get(url, null);
			JSONArray respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			assertTrue(respJson.length()==15, "Not getting specified (15) number of locations");
			for (int i=0 ; i<respJson.length() ; i++) {
				assertTrue(respJson.getString(i).toLowerCase().contains("new"), "Value '" + respJson.getString(i) + "' does not contain prefix 'new' : ");
			}
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}*/
	
	@Test
	public void getLocationsWithTextHavingSpace(){
		try {
			String url = BASE_URL + JOB_SEARCH + "/locations?prefix=new%20yo";
			ResponseBean response = HttpService.get(url, null);
			JSONArray respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			if(respJson.length()<10){
				for (int i=0 ; i<respJson.length() ; i++) {
					assertTrue(respJson.getString(i).toLowerCase().contains("new"), "Value '" + respJson.getString(i) + "' does not contain prefix 'new' : ");
				}
			}
			else{
			assertTrue(respJson.length()==10, "Not getting default (10) number of locations");
			for (int i=0 ; i<respJson.length() ; i++) {
				assertTrue(respJson.getString(i).toLowerCase().contains("new"), "Value '" + respJson.getString(i) + "' does not contain prefix 'new' : ");
			}
			}
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getLocationsWithInvalidText(){
		try {
			String url = BASE_URL + JOB_SEARCH + "/locations?prefix=qwerty";
			ResponseBean response = HttpService.get(url, null);
			JSONArray respJson = new JSONArray(response.message);
			checkSuccessCode(response.code);
			assertTrue(respJson.length()==0, "Showing incorrect locations for incorrect text");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
	
	@Test
	public void getLocationsWithInvalidCount(){
		try {
			String url = BASE_URL + JOB_SEARCH + "/locations?prefix=NY&count=0";
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
