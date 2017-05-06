package src;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

public class LeadCreation {
	
	WebDriver driver;
	
	public static void main(String[] args) throws InterruptedException {
		
		
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ram.Sahu\\Downloads\\chromedriver.exe");
		   WebDriver driver = new ChromeDriver();
		   driver.get("http://app10.preprod.hs18.lan:8081/faces/callCenter/crmLogin.xhtml");
		   
		   driver.manage().window().maximize();                                                                                            
		    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);   
		   	 WebElement userName = driver.findElement(By.id("form1:login"));
		   	 userName.sendKeys("sandy");
		   	 
		   	 WebElement pwd = driver.findElement(By.id("form1:password"));
		   	 pwd.sendKeys("hsnsandy");


		   	 WebElement lgnbtn = driver.findElement(By.id("form1:j_id_o"));
		   	lgnbtn.click();

		   	 Thread.sleep(5000);
		   	WebElement  caller= driver.findElement(By.xpath("//a[text()='Enter']"));
		   	caller.click();
		   	

		   	WebElement  putcaller= driver.findElement(By.id("findCustid:callerNumber"));
		   	putcaller.sendKeys("9953918936");
		  //*[@id="findCustid:j_id_20"]/span

		   	 Thread.sleep(3000);
		   	WebElement  submitcaller= driver.findElement(By.xpath("//*[@id='findCustid:j_id_20']/span"));
		   	
		   //	WebElement  submitcaller= driver.findElement(By.id("(//span[text()='Submit'])[1]"));
		   	submitcaller.click();
		   	
		   	 WebElement  crlead= driver.findElement(By.xpath("//a[text()='Create Lead']"));
		   	 crlead.click();
		   	 
		   	 

		   	 Thread.sleep(3000);
		   	Select leadTypeSelect = new Select(driver.findElement(By.xpath("html/body/div[1]/div[2]/form[1]/div[2]/table/tbody/tr/td[3]/div/div/div[2]/div/div/table/tbody/tr[1]/td[2]/table/tbody/tr[3]/td[2]/select")));
		   //	leadTypeSelect.selectByVisibleText("Product Related");
		   //	leadTypeSelect.selectByVisibleText("Payment Related");
			leadTypeSelect.selectByValue("1024616");
			//leadTypeSelect.selectByIndex(2);
			Thread.sleep(5000);
		 	Select disposiTypeSelect = new Select(driver.findElement(By.xpath("html/body/div[1]/div[2]/form[1]/div[2]/table/tbody/tr/td[3]/div/div/div[2]/div/div/table/tbody/tr[1]/td[2]/table/tbody/tr[4]/td[2]/select")));
		 disposiTypeSelect.selectByValue("1024631");
		 
		 WebElement element = driver.findElement(By.xpath("html/body/div[1]/div[2]/form[1]/div[2]/table/tbody/tr/td[3]/div/div/div[2]/div/div/table/tbody/tr[4]/td/button"));
		 Actions actions = new Actions(driver);
		 actions.moveToElement(element);
		 // actions.click();
		 actions.perform();
		 Thread.sleep(6000);
		 WebElement element1 = driver.findElement(By.xpath("//*[@id='form:closure']/span"));
				 element1.click();
		 
		//*[@id="form:closure"]/span




		   	 
		   	 
		   	 
		   	 
		
	}

}
