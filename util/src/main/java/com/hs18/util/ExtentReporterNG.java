package com.hs18.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.hs18.DataUtils.DbConnection;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReporterNG implements IReporter {
	public ExtentReports extent;
	public ExtentTest test;
	Properties prop;
	String home;
	private String outputFileName;

	public ExtentReporterNG(String outputFile) {
		this.outputFileName = outputFile;
	}
	
//	public ExtentReporterNG() {
//		
//	}

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

		prop = new Properties();
		prop.setProperty("ws.home", "${basedir}");
		try {
			FileInputStream ip = new FileInputStream(DbConnection.propfile + "\\Data\\config.properties");
			prop.load(ip);
		} catch (IOException e) {
			e.getLocalizedMessage();
		}
		home = prop.getProperty("home");
		extent = new ExtentReports(home + File.separator + "/src/main/webapp/" + outputFileName, true);

		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();

			for (ISuiteResult r : result.values()) {
				ITestContext context = r.getTestContext();
				buildTestNodes(context.getPassedTests(), LogStatus.PASS);
				buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
				buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);

			}
		}

		for (String s : Reporter.getOutput()) {
			extent.setTestRunnerOutput(s);
		}

		extent.flush();
		extent.close();
	}

	@SuppressWarnings("static-access")
	private void buildTestNodes(IResultMap tests, LogStatus status) throws IllegalArgumentException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String destFile = dateFormat.format(new java.util.Date()) + ".png";
		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {
				test = extent.startTest(result.getMethod().getMethodName());
				StringBuffer details = new StringBuffer();
				test.getTest().setStartedTime(getTime(result.getStartMillis()));
				test.getTest().setEndedTime(getTime(result.getEndMillis()));
				Object[] instance = result.getParameters();
				for (int i = 0; i < instance.length; i++) {
					details.append(instance[i]);
					if (!(i == instance.length - 1)) {
						details.append(",");
					}
				}

				if ((result.FAILURE > 0)) {
					test.addScreenCapture(Browser.screenshotPath + "/" + destFile);
				}
			//	for (String group : result.getMethod().getGroups())
					test.assignCategory(Browser.data.get().get("GroupName"));

				if (result.getThrowable() != null) {
					test.log(status, "Test Data - (" + details.toString() + ")");
					test.log(status, result.getThrowable());
				} else {
					test.log(status, "Test Data - (" + details.toString() + ")");
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
}
