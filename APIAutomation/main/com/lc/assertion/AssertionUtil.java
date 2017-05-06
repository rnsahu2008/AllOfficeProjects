package com.lc.assertion;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import org.testng.annotations.AfterTest;

import com.lc.common.Common;
import com.lc.jsonasserts.JSONCompare;
import com.lc.jsonasserts.JSONCompareMode;
import com.lc.softAsserts.*;

public class AssertionUtil {
	public SoftAssert s_assert = null;
	
	public AssertionUtil() {
		s_assert = new SoftAssert();
	}
	
	@AfterTest
	public void teardown()
	{
		Common.errorBuilder.append("</table>");
		Common.createLogFile(Common.logFileName, Common.errorBuilder.toString());
	}
	
	public void checkSuccessCode(int responseCode){
		try {
			s_assert.assertTrue(responseCode==200 || responseCode==201, "Expected error code : 200 or 201 but actual error code : " + responseCode);
		} catch (Exception e) {
			s_assert.fail(e.getMessage());
		}
	}
	
	public void assertResponseCode(int responseCode , int expectedCode){
		try {
			s_assert.assertTrue(responseCode==expectedCode , "actual server code: " + responseCode + " is not equal to expected code: " + expectedCode);
		} catch (Exception e) {
			s_assert.fail(e.getMessage());
		}
	}

	public void assertTrue(int expected , int actual, String message){
		try {
			s_assert.assertTrue(expected==actual , message + " | expected : " + expected + " but actual : " + actual);
		} catch (Exception e) {
			s_assert.fail(e.getMessage());
		}
	}
	
	public void assertEquals(String expected , String actual, String message){
		try {
			s_assert.assertTrue(expected.equalsIgnoreCase(actual) , message + " | expected : " + expected + " but actual : " + actual);
		} catch (Exception e) {
			s_assert.fail(e.getMessage());
		}
	}
	
	public void assertEquals(int expected , int actual, String message){
		try {
			s_assert.assertTrue(expected == actual , message + " | expected : " + expected + " but actual : " + actual);
		} catch (Exception e) {
			s_assert.fail(e.getMessage());
		}
	}
	
	public void assertEquals(String expected , JSONObject actual, String key){
		try {
			s_assert.assertTrue(expected.equals(actual.getString(key)) , "Expected value: " + expected + " is not equal to actual value " + actual.getString(key) + "::::Response is-->>" + actual.toString());
		} catch (Exception e) {
			s_assert.fail("Key " + key + " does not exist in json-->> " + actual.toString());
		}
	}
	
	public void assertTrue(boolean result, String message){
		try {
			s_assert.assertTrue(result , message);
		} catch (Exception e) {
			s_assert.fail(e.getMessage());
		}
	}
	public void assertNotNull(String object, String message){
		try {
			s_assert.assertTrue(!StringUtils.isEmpty(object) && !object.equalsIgnoreCase("null"), message);
			//s_assert.assertNotNull(object , message);
		} catch (Exception e) {
			s_assert.fail(e.getMessage());
		}
	}
	
	public void assertNotNull(Object object, String message){
		try {
			s_assert.assertNotNull(object , message);
		} catch (Exception e) {
			s_assert.fail(e.getMessage());
		}
	}
	
	public void assertNotNull(JSONObject response , String key){
		try {
			s_assert.assertTrue(!StringUtils.isEmpty(response.getString(key)) && !response.getString(key).equalsIgnoreCase("null") , key + " is NULL in response::::Response is-->>" + response.toString());
			//s_assert.assertNotNull(response.getString(key) , key + " is NULL in response::::Response is-->>" + response.toString());
		} catch (Exception e) {
			s_assert.fail(e.getMessage() + "::::Response is-->>" + response.toString());
		}
	}
	
	public void assertNull(String object, String message){
		try {
			s_assert.assertNull(object , message);
		} catch (Exception e) {
			s_assert.fail(e.getMessage());
		}
	}
	
	public void assertNull(Object object, String message){
		try {
			s_assert.assertNull(object , message);
		} catch (Exception e) {
			s_assert.fail(e.getMessage());
		}
	}
	
	public void assertJsonContainsKey(JSONObject json, String key){
		try {
			if(json.has(key)){
				s_assert.assertTrue(true);
			}
			else{
				s_assert.fail("Response does not contains key:: " + key);
			}
		} catch (Exception e) {
			s_assert.fail(e.getMessage() + "::::Response is -->> " + json.toString());
		}
	}
	
	public void assertJsonValueIsNotNull(JSONObject json, String key){
		try {
			if(json.has(key) && !StringUtils.isEmpty(json.get(key).toString())){
				s_assert.assertTrue(true);
			}
			else{
				s_assert.fail("Response does not contains key::" + key + " or value is null");
			}
		} catch (Exception e) {
			s_assert.fail(e.getMessage() + "::::Response is -->> " + json.toString());
		}
	}
	
	public void assertJsonValueEquality(JSONObject json, String key, String value){
		try {
			if(json.has(key) && json.get(key).toString().equals(value)){
				s_assert.assertTrue(true);
				}
			else{
				s_assert.fail("Response does not contains key::" + key + " or value is null or not equal to::" + value);
			}
		} catch (Exception e) {
			s_assert.fail(e.getMessage() + "::::Response is -->> " + json.toString());
		}
	}
	
	public void assertJsonArrayEquality(JSONArray expected , JSONArray actual){
        try {
            if(JSONCompare.compareJSON(expected, actual, JSONCompareMode.LENIENT).passed()){
                s_assert.assertTrue(true);
                }
            else{
                s_assert.fail("Expected response:  " + expected.toString() + "  does not match actual response " + actual.toString());
            }
        } catch (Exception e) {
        	s_assert.fail(e.getMessage() + "::::Response is -->> " + actual.toString());
        }
    }
	
	public void assertJsonObjectEquality(JSONObject expected , JSONObject actual){
        try {
            if(JSONCompare.compareJSON(expected, actual, JSONCompareMode.LENIENT).passed()){
                s_assert.assertTrue(true);
                }
            else{
                s_assert.fail("Expected response:  " + expected.toString() + "  does not match actual response " + actual.toString());
            }
        } catch (Exception e) {
        	s_assert.fail(e.getMessage() + "::::Response is -->> " + actual.toString());
        }
    }
	
	public void fail(String message){
			try {
				s_assert.fail(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public String trim(String exceptionMsg){
		try {
			String[]lines = exceptionMsg.split(System.getProperty("line.separator"));
			return lines[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exceptionMsg;
	}

}