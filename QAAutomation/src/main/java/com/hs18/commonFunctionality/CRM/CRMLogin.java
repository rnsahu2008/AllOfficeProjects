package com.hs18.commonFunctionality.CRM;

import com.hs18.util.LocatorExcel;

public class CRMLogin extends LocatorExcel {
	
	public void logIn(String username , String password) 
	{
		waitForElementVisible(locator("CRM_username"));
		type(locator("CRM_username"), username);
		type(locator("CRM_password"), password);
		click(locator("CRM_logInButton"));		
	}


}
