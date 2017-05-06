package src;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class LinkedInProfile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebDriver driver = new HtmlUnitDriver();
		driver.get("https://www.linkedin.com/");
		driver.findElement(By.id("login-email")).sendKeys("abhishek.annibisht@gmail.com");
		driver.findElement(By.id("login-password")).sendKeys("Confused123@");
		driver.findElement(By.name("submit")).click();
		driver.get("https://in.linkedin.com/in/tanuj-batra-8a222510");
		System.out.println(driver.getPageSource());
		
	}

}
