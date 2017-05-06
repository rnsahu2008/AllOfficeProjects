package src;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class LoginClass {
	
	public static void main(String[] args) {
		
		
		WebDriver driver = new   HtmlUnitDriver();
		driver.get("https://www.linkedin.com/");
		
		driver.findElement(By.id("login-email")).sendKeys("batra.tanuj@gmail.com");
		driver.findElement(By.id("login-password")).sendKeys("tanuj@221688");
		driver.findElement(By.name("submit")).click();
		driver.get("https://www.linkedin.com/in/ram-niwas-sahu-85708b16");
		System.out.println(driver.getPageSource());
		
		
	}
	

}
