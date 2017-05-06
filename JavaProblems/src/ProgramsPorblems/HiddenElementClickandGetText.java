package ProgramsPorblems;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class HiddenElementClickandGetText {
	
	static WebDriver driver;
	public static void main(String[] args) throws InterruptedException {
		
		System.setProperty("webdriver.chrome.driver", "C:\\jars\\Chormedriver\\chromedriver.exe");
		driver = new ChromeDriver();

		driver.get("https://www.google.co.in");
//		WebElement ele=driver.findElement(By.id("q"));
		if(driver.findElements(By.name("q")).size()>0)
			
		{
		System.out.println("i am present");
			
			
		}
		else System.out.println("Not Present");
		
		 WebElement invisibleelement= driver.findElement(By.xpath("//*[@id='gbw']/div/div/div[1]/div[1]/a")); 
				 JavascriptExecutor executor = (JavascriptExecutor)driver;  
		 String content = (String) (executor.executeScript("return arguments[0].innerHTML", invisibleelement));
		 System.out.println(content);

		executor.executeScript("arguments[0].click()", invisibleelement);  
	
		//If id is given then can click like this on hidden element 
		 String Script = "javascript:document.getElementById('loginbutton').click();";
		 ((JavascriptExecutor) driver).executeScript(Script);
 
		
		 Thread.sleep(10000);
		 
		driver.quit();
		

		
		
		
		
	}
	

}
