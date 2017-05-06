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

public class TestClass {
	
	WebDriver driver;
@BeforeSuite
public void setUp() throws MalformedURLException
{
	DesiredCapabilities cap = new DesiredCapabilities();
	cap.setCapability("device", "Android");
	cap.setCapability(CapabilityType.BROWSER_NAME, "");
    cap.setCapability(CapabilityType.VERSION, "4.4");
     cap.setCapability("platformName", "Android");
     cap.setCapability("deviceName","Emulator");
     cap.setCapability("appPackage", "com.android.calculator2"); // This is package name of your app (you can get it from apk info app)
     cap.setCapability("appActivity", "com.android.calculator2.Calculator"); // This is Launcher activity of your app (you can get it from apk info app)
    // cap.setCapability("javascriptEnabled", "true");
     driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
}


@Test
public void calTest()
{
	 WebElement two=driver.findElement(By.name("2"));
     two.click();
     WebElement plus=driver.findElement(By.name("+"));
     plus.click();
     WebElement four=driver.findElement(By.name("4"));
     four.click();
     WebElement equalTo=driver.findElement(By.name("="));
     equalTo.click();
	
}

@AfterSuite
public void tearDown()
{
	driver.quit();
}
}
