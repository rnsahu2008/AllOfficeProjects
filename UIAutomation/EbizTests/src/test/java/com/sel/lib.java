package com.sel;

import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class lib {


	/**=
	 *  generates dynamic user with  prefix as 'hsqa_' and domain as "testdomain.com"
	 */
	public static String generateDynamicUser()
	{
		return generateDynamicUser(13);
	}
	
	public static String generateDynamicAddress()
	{
		return RandomStringUtils.randomAlphabetic(8)+" "+RandomStringUtils.randomAlphabetic(8)+" "+RandomStringUtils.randomAlphabetic(8)+" "+RandomStringUtils.randomAlphabetic(8);
	}
	/**
	 *  generates dynamic user with prefix as 'hsqa_' and domain as "testdomain.com" with n chars 
	 */
	public static String generateDynamicUser(int n)

	
	{
		return "hsqa_" + RandomStringUtils.randomAlphabetic(n-5) +"@testdomain.com";
	}
	
	public static String generateDynamicPO()
	{
		return "PO-" + RandomStringUtils.randomAlphabetic(3) + "-" + RandomStringUtils.randomNumeric(3);
	}


 
}
