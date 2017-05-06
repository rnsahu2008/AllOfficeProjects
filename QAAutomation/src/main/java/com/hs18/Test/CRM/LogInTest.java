package com.hs18.Test.CRM;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hs18.DataUtils.ExcelReader;
import com.hs18.commonFunctionality.CRM.CRMLogin;

public class LogInTest extends CRMLogin {

	ExcelReader read = new ExcelReader();
	Logger log = Logger.getLogger(LogInTest.class);


	@Test(description = "login Test", dataProvider = "login")
	public void logInToCRM(String url, String username, String password, int i) throws InterruptedException {
		openUrl(url);
		logIn(username, password);
		log.info("Entered user credentials");
		if (i == 0) {

			waitForElementVisible(locator("CRM_loginerror"));
			log.info("Verifying error message");
			try {
				Assert.assertEquals(element(locator("CRM_loginerror")).getText(), "Invalid credential");
			}

			catch (AssertionError e) {
				Reporter.log("Text doesn't macthed -" + e.getMessage());
				throw e;
			}
		}

		else if (i == 2) {
			waitForTitle("CRM Application");
			log.info("User logined successfully");
			Assert.assertEquals(title(), "CRM Application");

		}
	}

	@DataProvider(name = "login")
	public Object[][] dataProviderLogin() throws IOException {

		read.testDataFile(file);
		Object[][] data = new Object[][] { { read.readFromColumn("Base", 1, 0), "sd", "sd", 0 },
				{ read.readFromColumn("Base", 1, 0), read.readFromColumn("Base", 1, 1),
						read.readFromColumn("Base", 1, 2), 2 } };
		read.closeFile();
		return data;

	}
}
