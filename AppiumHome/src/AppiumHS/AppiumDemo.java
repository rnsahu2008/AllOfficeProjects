package AppiumHS;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppiumDemo 
{

public AppiumDriver  driver;


	@BeforeTest
	public void InvokeApp() throws MalformedURLException, InterruptedException
	
	{
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
	    File app = new File("C:\\Jars\\HomeShop18_1.apk");
		capabilities.setCapability("deviceName", "ZY223K7SHW");

		capabilities.setCapability("platformName", "Android");
		
		capabilities.setCapability("app", app.getAbsolutePath());
		
		capabilities.setCapability("apppackage", "com.homeshop18.activity");
		
		capabilities.setCapability("appactivity", "com.homeshop18.ui.activiies.StartupActivity");
		
		driver= new AndroidDriver( new URL("http://127.0.0.1:4726/wd/hub"), capabilities) ;
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		Thread.sleep(15000);
	}
	

	@Test(description="End to End COD order")
	public void CODorder() throws MalformedURLException, InterruptedException
	
	{
		System.out.println("Test It now");
	
WebElement searchbox1 = driver.findElement(By.id("com.homeshop18.activity:id/search_view_home_pg"));
  searchbox1.click();
//driver.tap(1, driver.findElement(By.id("com.homeshop18.activity:id/search_view_home_pg")), 1);
WebElement searchbox = driver.findElement(By.id("com.homeshop18.activity:id/search_autocomplete"));
searchbox.clear();
searchbox.sendKeys("30860835");//31412643 31412531 ,31129191
driver.tap(1, 993, 1690, 1);


WebDriverWait searchwait = new WebDriverWait(driver,30);
searchwait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.homeshop18.activity:id/help_background_imageView")));



WebElement background = driver.findElement(By.id("com.homeshop18.activity:id/help_background_imageView"));
background.click();

WebElement buybutton = driver.findElement(By.id("com.homeshop18.activity:id/buy_notify_button_view"));
buybutton.click();
//System.out.println("Test3");
//Thread.sleep(5000);
WebDriverWait waitdropdown = new WebDriverWait(driver,30);
waitdropdown.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.homeshop18.activity:id/addtocart_check_delivery_editText")));

//WebElement dropdown = driver.findElement(By.id("com.homeshop18.activity:id/tv_credit_card_emi_bank_name"));
//dropdown.click();

//Select d1 = new Select(dropdown);
//d1.selectByVisibleText("1-2 Years");



WebElement enterPin = driver.findElement(By.id("com.homeshop18.activity:id/addtocart_check_delivery_editText"));
enterPin.sendKeys("122001");//31412643
Thread.sleep(5000);

WebElement checkpin = driver.findElement(By.id("com.homeshop18.activity:id/button_add_edit_pin_go"));
checkpin.click();
//Thread.sleep(5000);
WebElement buynowbtn = driver.findElement(By.id("com.homeshop18.activity:id/add_to_cart_buy_now_button"));
buynowbtn.click();

//Thread.sleep(5000);
//WebElement gotocart = driver.findElement(By.id("com.homeshop18.activity:id/widget_unread_count_icon"));
//gotocart.click();

//Thread.sleep(5000);
WebElement cart_checkout_button = driver.findElement(By.id("com.homeshop18.activity:id/cart_checkout_button"));
cart_checkout_button.click();

WebElement enterEmail = driver.findElement(By.id("com.homeshop18.activity:id/login_email_et"));
enterEmail.sendKeys("abhishekhomeshop@gmail.com");//31412643

WebElement enterpassword = driver.findElement(By.id("com.homeshop18.activity:id/login_password_et"));
enterpassword.sendKeys("homeshop");//31412643


WebElement loginbtn = driver.findElement(By.id("com.homeshop18.activity:id/sign_in_up_button"));
loginbtn.click();




//MobileElement element = (MobileElement) driver.findElement(By.id("com.homeshop18.activity:id/tv_payment_option_sub_heading"));
//element.click(); // OK 
//element.swipe(SwipeElementDirection.DOWN, 1000); // Test Swipe #1 : KO 
//driver.swipe(397,605,538,245,500); // Test Swipe #2 : KO 
//driver.swipe(397,605,538,245,1);

driver.scrollTo("PayUMoney");
//Thread.sleep(5000);

driver.findElement(By.name("Cash On Delivery")).click();
driver.findElement(By.id("com.homeshop18.activity:id/button_checkout_confirm_order")).click();

WebDriverWait wait = new WebDriverWait(driver,30);
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.homeshop18.activity:id/button_continue_shopping")));

String itemBook= driver.findElement(By.id("com.homeshop18.activity:id/tv_heading")).getText();
Assert.assertEquals(itemBook, "Thank you for shopping with Homeshop18!");

	}
	
	@AfterTest
	public void takeScreenShot() throws IOException { 

		String destDir = "D:\\SeleniumFIle1"; 
		FileUtils.cleanDirectory(new File(destDir));
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE); 
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
		new File(destDir).mkdirs();
		String destFile = dateFormat.format(new Date()) + ".png"; 
		try { 
		FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
		 } catch (IOException e) { e.printStackTrace(); 
		}
		 }
	
	
	@AfterSuite
public void quitApplication()

{
	driver.quit();
}
	
	
	
}

