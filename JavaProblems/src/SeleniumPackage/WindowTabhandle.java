package SeleniumPackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
public class WindowTabhandle{
	
	WebDriver driver;
	@Test
	public void Test() throws InterruptedException,ElementNotFoundException,NoSuchElementException
	{
	System.setProperty("webdriver.chrome.driver", "C:\\jars\\Chormedriver\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://www.google.co.in/");	

		//driver.get("https://www.naukri.com/");	
	 Actions action = new Actions(driver);
	 WebElement image=driver.findElement(By.xpath("//*[@id='gbw']/div/div/div[1]/div[2]/a"));
	 action.keyDown(Keys.SHIFT).keyDown(Keys.CONTROL).click(image).keyUp(Keys.SHIFT).keyUp(Keys.CONTROL).build().perform();

	 ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
     driver.switchTo().window(tabs2.get(1));
    
     WebElement signin=driver.findElement(By.xpath("//*[@id='gb_70']"));
     signin.click();
     driver.close();;
     
     Thread.sleep(5000);
     driver.switchTo().window(tabs2.get(0));
     WebElement signin1=driver.findElement(By.xpath("//*[@id='gb_70']"));
     signin1.click();

	//Set<String> winSet =new HashSet<String>(driver.getWindowHandles());
	// List<String> winList = new ArrayList<String>(driver.getWindowHandles());
	 Set<String> winSet =driver.getWindowHandles();
	 
	 //Thread.sleep(500000);
	for (String str:winSet)
	{
      
		System.out.println("RAM"+str+driver.switchTo().window(str).getCurrentUrl());
		String title=driver.switchTo().window(str).getTitle();
		System.out.println("ID:" + str);
		if(title.equals("Google Images"))
		{
//			driver.switchTo().window(newTab); // switch to new tab
			 WebElement signin2=driver.findElement(By.xpath("//*[@id='gb_70']"));
				
			action.keyDown(Keys.SHIFT).keyDown(Keys.CONTROL).click(signin2).keyUp(Keys.SHIFT).keyUp(Keys.CONTROL).build().perform();
		    Thread.sleep(7000);

			//driver.close();
		}
		
	}
//	String newTab = winSet.get(winSet.size()-4);
//	driver.switchTo().window(newTab); // switch to new tab
	//action.keyDown(Keys.SHIFT).keyDown(Keys.CONTROL).click(image).keyUp(Keys.SHIFT).keyUp(Keys.CONTROL).build().perform();
    Thread.sleep(7000);
    //WebElement signin=driver.findElement(By.xpath("//*[@id='gb_70']"));
	//signin.click();
	driver.close();;
    Thread.sleep(7000);
   //  driver.switchTo().window(winSet.get(0));
    WebElement signin3=driver.findElement(By.xpath("//*[@id='gb_70']"));
	signin3.click();


	


}

}