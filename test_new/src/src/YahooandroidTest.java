package src;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class YahooandroidTest {
	
	WebDriver driver;
	@BeforeSuite
	public void setUp() throws MalformedURLException
	{
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(CapabilityType.BROWSER_NAME, "Android");
	    cap.setCapability(CapabilityType.VERSION, "4.4");
	     cap.setCapability("platformName", "Android");
	     cap.setCapability("deviceName","U4QWKFZDUW49D64P");
	     cap.setCapability("appPackage", "com.google.android.gm"); // This is package name of your app (you can get it from apk info app)
	     cap.setCapability("appActivity", "com.google.android.gm.ComposeActivityGmail"); // This is Launcher activity of your app (you can get it from apk info app)
	     cap.setCapability("javascriptEnabled", "true");
	     driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
	}


	@Test
	public void sendMail() throws InterruptedException
	{
//		 WebElement two=driver.findElement(By.id("compose"));
//	     two.click();
	     Thread.sleep(3000);
	     WebElement plus=driver.findElement(By.name("To"));
	     plus.sendKeys("abhishek.annibisht@gmail.com");
	     //Thread.sleep(3000);
	     WebElement sub=driver.findElement(By.name("Subject"));
	     sub.click();
	     sub.sendKeys("android test mail");
	     //Thread.sleep(3000);
	     WebElement compose=driver.findElement(By.name("Compose email"));
	     compose.sendKeys("android test mail");	    
	     WebElement send=driver.findElement(By.name("Send"));
	     send.click();
	     Thread.sleep(3000);
	     WebElement confirm=driver.findElement(By.name("OK"));
	     confirm.click();
	     
	    
		
	}

	@AfterSuite
	public void tearDown()
	{
		driver.quit();
	}

}
