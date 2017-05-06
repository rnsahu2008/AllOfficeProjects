package com.hs18.dfc;

import org.apache.commons.lang.RandomStringUtils;

public class lib {


	/**
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

}
