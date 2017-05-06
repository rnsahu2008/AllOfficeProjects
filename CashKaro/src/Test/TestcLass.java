package Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.server.handler.ImplicitlyWait;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.Wait;

import utils.ExcelReader;

public class TestcLass extends ActionClass {
	ExcelReader read = new ExcelReader();
	InputStream inputStream;
	Properties prop;
	String File1;
	
	@BeforeSuite
	public void setup() throws IOException
	{/*
		FluentWait wait = new FluentWait(driver)
		.withTimeout(20, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS).
		ignoring(Exception.class);
	*/	prop = new Properties();
		String propFileName = "data\\config.properties";
		FileInputStream ip = new FileInputStream(propFileName);

		// inputStream =
		// getClass().getClassLoader().getResourceAsStream(propFileName);
		prop.load(ip);
		File1 = prop.getProperty("TestFile");
		System.out.println(File1);

		
	}
	

	@BeforeMethod
	@Parameters({ "Browser", "url" })
	public void setDriver(String Browser, String Url) throws IOException {
		/*
		 * prop = new Properties(); FileInputStream ip = new
		 * FileInputStream(System.getProperty("data\\config.properties"));
		 * prop.load(ip); File1 = prop.getProperty("TestFile");
		 * System.out.println(File1);
		 */


		if (Browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		
			System.out.println("firefox driver selected");
			driver.get(Url);

		}
		if (Browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"libs\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			System.out.println("Chrome driver selected");
			driver.get(Url);

		}
		if (Browser.equalsIgnoreCase("iexplorer")) {
			System.setProperty("webdriver.ie.driver", "");
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver = new InternetExplorerDriver(caps);
			driver.manage().window().maximize();
			driver.get(Url);
		}

	}

	@Test(description = "login Test", dataProvider = "NormalLogin",enabled=false )
	public void SignInTestNormal(String firstName, String useremail,
			String usercon_email, String userpassWord)
			throws InterruptedException

	{
		System.out.println("1st start");
		click(Joinfree);
		type(firstname, firstName);
		type(email, useremail);
		type(con_email, usercon_email);
		click(passWord);
		type(password1, userpassWord);
		// click(Accept);
		click(join_free_submit);
		String txtd=driver.findElement(SigninVeriyText).getText();
		System.out.println(txtd);
		Assert.assertEquals(driver.findElement(SigninVeriyText).getText(),
				"Hello Ram NiwasSahu,");


	}

	@Test(description = "login Test by FB", dataProvider = "fbloginData")
	public void SigniNTestByFacebook(String userid, String pwd)
			throws InterruptedException {
		click(Joinfree);
		click(FbSingupButton);
		ArrayList<String> win2 = new ArrayList<String>(
				driver.getWindowHandles());
		driver.switchTo().window(win2.get(1));
		type(fbuserid, userid);
		type(fbpwd, pwd);
		click(fbLogin);
		driver.switchTo().window(win2.get(0));
		Thread.sleep(5000);
	String signintext=driver.findElement(SigninVeriyText).getText();
		Assert.assertEquals(signintext,
				"Hello Ram NiwasSahu,");


	
	}

	/*@Test(description = "login Test userid passowrod create by fb",dataProvider="fbloginData")
	public void SinginByCreatedFbAccount(String username,String password) throws InterruptedException {
		click(SignIn);
		type(usernameforSingin,username );
		type(userpwdforSingin, password);
		click(sign_in);
		ArrayList<String> win2 = new ArrayList<String>(
				driver.getWindowHandles());
		driver.switchTo().window(win2.get(1));
		// Thread.sleep(1000);
		Assert.assertEquals(driver.findElement(SigninVeriyText).getText(),
				"Hello Ram NiwasSahu,");


	}

*/	@Test(description = "login Test userid passowrod create by fb", dataProvider="email" )
	public void ForgotPassword(String username) throws InterruptedException {
		click(SignIn);
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='cboxLoadedContent']/iframe")));
		click(forgotPasswordLink);
		type(enterfromemail,username);
		click(enterfromemail);
		//click(submit_req);
		String forgotpasswordtext=driver.findElement(Message).getText();
		Assert.assertEquals(forgotpasswordtext,
				"An email has been sent with your new password. Please check your email now.");

		//click(closeButton);

	}

	@AfterMethod
	public void closedriver() {
		driver.close();
	}

	@DataProvider(name = "NormalLogin")
	public Object[][] dataProviderLogin() throws IOException {

		if (File1 != null) {
			read.testDataFile(File1);
			Object[][] data = new Object[][] { {
					read.readFromColumn("Base", 0, 0),
					read.readFromColumn("Base", 0, 1),
					read.readFromColumn("Base", 0, 2),
					read.readFromColumn("Base", 0, 3) } };
			read.closeFile();

			return data;
		} else {
			System.out.println("File not found");
			return null;
		}
	}

	@DataProvider(name = "fbloginData")
	public Object[][] dataProvideremail() throws IOException {

		read.testDataFile(File1);
		Object[][] data = new Object[][] { { read.readFromColumn("Base", 0, 4),
				read.readFromColumn("Base", 0, 5) } };

		read.closeFile();
		return data;
	}

	@DataProvider(name = "email")
	public Object[][] email() throws IOException {

		read.testDataFile(File1);
		Object[][] data = new Object[][] { { read.readFromColumn("Base", 0, 7) } };

		read.closeFile();
		return data;
	}
}