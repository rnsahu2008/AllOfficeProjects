package com.hs18.Test.DFC;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hs18.DataUtils.ExcelReader;
import com.hs18.Test.CRM.LogInTest;
import com.hs18.commonFunctionality.DFC.DFCLogin;


public class DfcLoginTest extends DFCLogin{
	
	ExcelReader read = new ExcelReader();
	Logger log = Logger.getLogger(DfcLoginTest.class);
	
	@Test(description = "change status", dataProvider = "suborderforchange")
	public void changeStatus(ArrayList<String> suborder) 
	{
		for(int i=0;i<suborder.size();i++)
		{
		statechangetoVerified(suborder.get(i));
		}
		
	}
	
	
	@Test(description = "login DFC Test", dataProvider = "loginDFC")
	public void logInToDFC(String url, String username, String password,int i)
			throws InterruptedException
	{
		openUrl(url);
		DflogIn(username, password);
		log.info("Entered user credentials");
		if (i == 0) {

			waitForElementVisible(locator("Dfc_logineError"));
			log.info("Verifying error message");
			try {
				String dfcloginError=element(locator("Dfc_logineError")).getText();
				Assert.assertEquals(dfcloginError, "Login not successful: invalid username or password!");
				}

			catch (AssertionError e) {
				Reporter.log("Text doesn't macthed -" + e.getMessage());
				throw e;
			}
		}

		if (i == 2) {
	
			try {
				Assert.assertEquals(element(locator("Dfc_NewFeedLognText")).getText(), "Login not successful: invalid username or password!");
			}

			catch (AssertionError e) {
				Reporter.log("Text doesn't macthed -" + e.getMessage());
				throw e;
			}

		}
	}

	@DataProvider(name = "loginDFC")
	public Object[][] dataProviderLogin() throws IOException {

		read.testDataFile(file);
		Object[][] data = new Object[][] {
				{ read.readFromColumn("DFC", 1, 0), "dfc", "dfc", 0 },{ read.readFromColumn("DFC", 1, 0), read.readFromColumn("DFC", 1, 1),read.readFromColumn("DFC", 1, 2), 2 } };
		
		read.closeFile();
		return data;

	}


@DataProvider(name = "suborderforchange")
public Object[][] dataProviderLogin1() throws IOException {

	read.testDataFile(file);
	Object[][] data = new Object[][] {{ read.dataProviderByRow("DFC", "suborder")	
		 } };
	
	read.closeFile();
	return data;

}
}




