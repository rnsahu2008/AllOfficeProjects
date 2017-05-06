package com.lc.assertion;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

public class CheckEquality {
	public static void assertTrue(String actual, String expected){
		try {
			if(actual.equalsIgnoreCase(expected)){
				Assert.assertTrue(true);
			}
			else{
				Assert.fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	public static void assertTrue(int actual, int expected){
		try {
			if(actual == expected){
				Assert.assertTrue(true);
			}
			else{
				Assert.fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	public static void notNull(String data){
		try {
			if(!StringUtils.isEmpty(data)){
				Assert.assertTrue(true);
			}
			else{
				Assert.fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}


}
