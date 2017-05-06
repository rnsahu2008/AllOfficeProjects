package com.hs18.extentreports.listener;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
 
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
 


import com.hs18.extentreports.utility.ScreenShotsUtility;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
 
public class ExtentReporterNG implements IReporter {
    private ExtentReports extent;
    
	ExtentTest logger;
	WebDriver driver;
 
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        extent = new ExtentReports(outputDirectory + File.separator + "Extent.html", true);
 
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
 
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
 
                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
            }
        }
 
        extent.flush();
        extent.close();
       // driver.get("D:\\Report\\VerifyTitle.html");
    }
 
    private void buildTestNodes(IResultMap tests, LogStatus status) {
        ExtentTest test;
 
        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                test = extent.startTest(result.getMethod().getMethodName());
 
                test.setStartedTime(getTime(result.getStartMillis()));
                test.setEndedTime(getTime(result.getEndMillis()));
 
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);
 
                if (result.getThrowable() != null) {
                    test.log(status, result.getThrowable());
                }
                else {
                    test.log(status, "Test " + status.toString().toLowerCase() + "ed");
                }
 
                extent.endTest(test);
            }
        }
    }
 
    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();        
    }
    
	//Capture ScreenShots Method
	public static String captureScreenshot(WebDriver driver,String screenshotName) 
	{

	try 
	{
	TakesScreenshot ts=(TakesScreenshot)driver;

	File source=ts.getScreenshotAs(OutputType.FILE);

	String dest="D:\\Report\\Screenshots\\"+screenshotName+".png";

	File destination=new File(dest);

	FileUtils.copyFile(source,destination);

	//FileUtils.copyFile(source, new File("./Screenshots/"+screenshotName+".png"));

	System.out.println("Screenshot taken");

	return dest;

	} 
	catch (Exception e)
	{

	System.out.println("Exception while taking screenshot "+e.getMessage());

	return e.getMessage(); 
	}
	}
	//Capturing Screenshots in Case of Failed Scenario
	
	public void FailedScreenshots(ITestResult result) {
		if(result.getStatus()==ITestResult.FAILURE){
			
		//tring screenshot_path = ScreenShotsUtility.captureScreenshot(driver,result.getName());
		
		
		String screenshot_path=ScreenShotsUtility.captureScreenshot(driver, result.getName());
		String image= logger.addScreenCapture(screenshot_path);
		logger.log(LogStatus.FAIL, "Title verification", image);
		 
		 
		}
}}