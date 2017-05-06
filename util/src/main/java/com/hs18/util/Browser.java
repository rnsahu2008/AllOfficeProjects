package com.hs18.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.hs18.DataUtils.DBQuery;
import com.hs18.DataUtils.DbConnection;
import com.hs18.DataUtils.ExcelReader;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class Browser {

	static ThreadLocal<RemoteWebDriver> driverThreadLocal = new ThreadLocal<RemoteWebDriver>();
	static ThreadLocal<Map<String, String>> data = new ThreadLocal<Map<String, String>>();
	@SuppressWarnings("rawtypes")
	static ThreadLocal<AppiumDriver> appiumThreadLocal = new ThreadLocal<AppiumDriver>();
	public static String screenshotPath, file1;
	public String file;
	public static HashMap<String, ArrayList<String>> CRM = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> CMS = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> OMS = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> WEB = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> DFC = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> android = new HashMap<String, ArrayList<String>>();
	public HashMap<String, String> userdata = new HashMap<String, String>();
	static String appiumServerConfigurations = "--no-reset --local-timezone";
	public String chromedriver, iedriver, locator, apkpath, log, home;
	public static String appiumNodeFilePath;
	public static String appiumJavaScriptServerFile;
	Properties prop;
	ArrayList<String> integration = new ArrayList<>();
	ExcelReader reader = null;
	public String appName, GroupName;


	
	/*
	 * setDriver() reads the browser name from test data file and activate the
	 * browser mentioned on test data sheet . File , chromedriver.exe ,locator
	 * location are mentioned on config.properties file.
	 */

	@SuppressWarnings("rawtypes")
	@BeforeSuite
	@Parameters({ "Application", "Environment", "Group", "Browser", "filePath", "fileOutput", "mailto", "HomeDir" })
	public void setDriver(String Application, String Environment, String Group, String Browser, String filePath,
			String fileOutput, String mailto, String propfile)
			throws IOException, InterruptedException, ClassNotFoundException, SQLException {
		RemoteWebDriver driver = null;
		reader = new ExcelReader();
		AppiumDriver appium = null;
		prop = new Properties();
		if (DbConnection.propfile == null) {
			DbConnection.propfile = propfile;
		}
		FileInputStream ip = new FileInputStream(DbConnection.propfile + "\\Data\\config.properties");
		prop.load(ip);
		// file = prop.getProperty("File");
		GroupName = "GroupName";
		appName = "appName";
		userdata.put(appName, Application);
		userdata.put(GroupName, Group);
		userdata.put("file", filePath);
		userdata.put("Browser", Browser);
		userdata.put("fileOutput", fileOutput);
		userdata.put("mailto", mailto);
		userdata.put("HomeDir", propfile);
		home = prop.getProperty("home");
		apkpath = home + prop.getProperty("ApkPath");
		chromedriver = home + prop.getProperty("chromedriver");
		iedriver = home + prop.getProperty("iedriver");
		locator = home + prop.getProperty("LocatorFile");
		screenshotPath = home + prop.getProperty("screenshotPath");
		log = home + prop.getProperty("logs");
		appiumNodeFilePath = prop.getProperty("AppiumNodeFilePath");
		appiumJavaScriptServerFile = prop.getProperty("AppiumJavaScriptServerFile");
		String downloadFilepath = home + prop.getProperty("uploadDirectory");
		reader.testDataFile(userdata.get("file"));
		if (Browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			System.out.println("firefox driver selected");
			// driver.get(reader.readFromColumn("Base", 1, 0));

		}
		if (Browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", chromedriver);
			ChromeOptions options = new ChromeOptions();
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", downloadFilepath);
			HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
			options.setExperimentalOption("prefs", chromePrefs);
			options.addArguments("--test-type");
			options.addArguments("--start-maximized");
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(cap);

			driver.manage().window().maximize();
			System.out.println("Chrome driver selected");
			// driver.get("http://app01.preprod.hs18.lan:8091/faces/callCenter/crmLogin.xhtml");

		}
		if (Browser.equalsIgnoreCase("iexplorer")) {
			System.setProperty("webdriver.ie.driver", iedriver);
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			driver = new InternetExplorerDriver(caps);
			driver.manage().window().maximize();
			// driver.get(reader.readFromColumn("Base", 1, 0));

		}
		if (Browser.equalsIgnoreCase("safari")) {
			driver = new SafariDriver();
			System.out.println("safari driver selected");
			driver.manage().window().maximize();
			driver.get(reader.readFromColumn("Base", 1, 0));
			System.out.println("safari");
		}

		if (Browser.equalsIgnoreCase("grid")) {
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setBrowserName("firefox");
			cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
		}

		if (Application.equalsIgnoreCase("mobile")) {
			executeCommand();
			DesiredCapabilities capabilities = new DesiredCapabilities();
			File app = new File(apkpath);
			capabilities.setCapability("deviceName", reader.readFromColumn("Android", 1, 4));
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("app", app.getAbsolutePath());
			Thread.sleep(5000);
			appium = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		}

		reader.testDataFile(locator);

		if (!(Application.equalsIgnoreCase("Integration"))) {
			ArrayList<String> keyList = reader.dataProviderByRow(Application, "LocatorKey");
			if (Application.equals("CRM")) {
				for (int i = 0; i < keyList.size(); i++) {
					CRM.put(keyList.get(i).replaceAll("\\s", ""), reader.data(i+1, Application));
				}
			} else if (Application.equals("CMS")) {
				for (int i = 0; i < keyList.size(); i++) {
					CMS.put(keyList.get(i).replaceAll("\\s", ""), reader.data(i+1, Application));
				}
			}
			else if (Application.equals("DFC")) {
				for (int i = 0; i < keyList.size(); i++) {
					DFC.put(keyList.get(i).replaceAll("\\s", ""), reader.data(i+1, Application));
				}
			}

			else if (Application.equals("Mobile")) {
				for (int i = 0; i < keyList.size(); i++) {
					android.put(keyList.get(i).replaceAll("\\s", ""), reader.data(i+1, Application));
				}
			}

			// System.out.println(getIntegration());

		} else if (Application.equalsIgnoreCase("Integration")) {
			DBQuery connection = new DBQuery();
			integration = connection.applicationsName(Group);
			// integrationData.set(integration);
			if (integration.contains("CRM")) {
				ArrayList<String> keyList = reader.dataProviderByRow("CRM", "LocatorKey");
				for (int i = 0; i < keyList.size(); i++) {
					CRM.put(keyList.get(i).replaceAll("\\s", ""), reader.data(i+1, "CRM"));
				}
			}
			if (integration.contains("CMS")) {
				ArrayList<String> keyList = reader.dataProviderByRow("CMS", "LocatorKey");
				for (int i = 0; i < keyList.size(); i++) {
					CMS.put(keyList.get(i).replaceAll("\\s", ""), reader.data(i+1, "CMS"));
				}

			}
			if (integration.contains("DFC")) {
				ArrayList<String> keyList = reader.dataProviderByRow("DFC", "LocatorKey");
				for (int i = 0; i < keyList.size(); i++) {
					DFC.put(keyList.get(i).replaceAll("\\s", ""), reader.data(i+1, "DFC"));
				}

			}
			if (integration.contains("OMS")) {
				ArrayList<String> keyList = reader.dataProviderByRow("OMS", "LocatorKey");
				for (int i = 0; i < keyList.size(); i++) {
					OMS.put(keyList.get(i).replaceAll("\\s", ""), reader.data(i+1, "OMS"));
				}

			}
			if (integration.contains("WEB")) {
				ArrayList<String> keyList = reader.dataProviderByRow("WEB", "LocatorKey");
				for (int i = 0; i < keyList.size(); i++) {
					WEB.put(keyList.get(i).replaceAll("\\s", ""), reader.data(i+1, "WEB"));
				}

			}
		}
		reader.closeFile();
		driverThreadLocal.set(driver);
		appiumThreadLocal.set(appium);
		data.set(userdata);
	}

	public WebDriver getDriver() {
		return driverThreadLocal.get();
	}

	@SuppressWarnings("rawtypes")
	public AppiumDriver getappium() {
		return appiumThreadLocal.get();
	}

	public String getUserData(String userdata) {
		return data.get().get(userdata);
	}

	@BeforeClass
	@Parameters({ "filePath" })
	public void beforeFielSet() {

		this.file = data.get().get("file");
	}

	private void executeCommand() throws InterruptedException {

		try {
			CommandLine command = new CommandLine("cmd");
			File file1 = new File(appiumNodeFilePath);
			File file2 = new File(appiumJavaScriptServerFile);
			command.addArgument("/c");
			command.addArgument("'" + file1 + "'");
			command.addArgument("'" + file2 + "'");
			command.addArgument("--address", false);
			command.addArgument("127.0.0.1");
			command.addArgument("--port", false);
			command.addArgument("4723");
			command.addArgument("--full-reset", false);
			command.addArgument("--log");
			command.addArgument(log + "\\appiumLogs.txt");
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			DefaultExecutor executor = new DefaultExecutor();
			executor.setExitValue(1);
			executor.execute(command, resultHandler);
			Thread.sleep(5000);
			System.out.println("Appium server started");

		} catch (IOException e) {
			System.out.println("exception happened: ");
			e.printStackTrace();
		}
	}

	@AfterSuite
	public void driverClose() throws IOException {

		{
			CommandLine command = new CommandLine("cmd");
			command.addArgument("/c");
			command.addArgument("taskkill");
			command.addArgument("/F");
			command.addArgument("/IM");
			command.addArgument("node.exe");
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			DefaultExecutor executor = new DefaultExecutor();
			executor.setExitValue(1);
			executor.execute(command, resultHandler);
			System.out.println("Closing node.exe");
			try {
				getDriver().quit();
			} catch (WebDriverException e) {
				System.out.println(e.getMessage() + "No driver instance exist");
			}
		}
	}

}
