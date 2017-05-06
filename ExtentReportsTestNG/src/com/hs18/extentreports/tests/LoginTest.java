package com.hs18.extentreports.tests;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.hs18.extentreports.pages.HomePage;
import com.hs18.extentreports.pages.ErrorPage;
import com.hs18.extentreports.pages.LoginPage;

public class LoginTest {
	
	 WebDriver driver;
     
	    @BeforeSuite
	    public void setUp() {
	         
	        driver = new FirefoxDriver();
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    }
	     
	    @Parameters({"incorrectAgentID","incorrectpassword"})
	    @Test(description="Performs an unsuccessful login and checks the resulting error message (passes)")
	    public void testFailingLogin(String incorrectAgentID, String incorrectpassword) throws InterruptedException {
	         
	        LoginPage lp = new LoginPage(driver);
	        ErrorPage ep = lp.incorrectLogin(incorrectAgentID, incorrectpassword);
	        Assert.assertEquals(ep.getErrorText(), "Value is required.Enter Valid value.");
	    }
	    
	    @Parameters({"incorrectAgentID","incorrectpassword"})
	    @Test(description="Performs an unsuccessful login and checks the resulting error message (fails)")
	    public void failingTest(String incorrectAgentID, String incorrectpassword) throws InterruptedException {
	         
	        LoginPage lp = new LoginPage(driver);
	        ErrorPage ep = lp.incorrectLogin(incorrectAgentID, incorrectpassword);
	        Assert.assertEquals(ep.getErrorText(), "This is not the error message you're looking for.");
	    }
	    
	    @Parameters({"correctAgentID","correctpassword"})
	    @Test(description="Performs a successful login and checks Home page is opened")
	    public void testSuccessfulLogin(String correctAgentID, String correctpassword) throws InterruptedException {
	         
	        LoginPage lp = new LoginPage(driver);
	        HomePage aop = lp.correctLogin(correctAgentID, correctpassword);
	        Assert.assertEquals(aop.isAt(), true);
	    }
	     
	    @AfterSuite
	    public void tearDown() {
	         
	        driver.quit();
	    }
}