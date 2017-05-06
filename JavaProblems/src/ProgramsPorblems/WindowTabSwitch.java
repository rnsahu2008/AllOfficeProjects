package ProgramsPorblems;
import java.util.HashMap;
import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;;
public class WindowTabSwitch{
	
	WebDriver driver;
	@Test
	public void Test()
	{
	System.setProperty("webdriver.chrome.driver", "C:\\jars\\Chormedriver\\chromedriver.exe");
		driver = new ChromeDriver();

	   //driver = new ChromeDriver(cap);
	driver.get("https://www.google.co.in/");	
	
	Actions action = new Actions(driver);
	WebElement ele=driver.findElement(By.xpath("//*[@id='gbw']/div/div/div[1]/div[2]/a"));

	action.keyDown(Keys.SHIFT).keyDown(Keys.CONTROL).click(ele).keyUp(Keys.SHIFT).keyUp(Keys.CONTROL).build().perform();
	

	


}

}