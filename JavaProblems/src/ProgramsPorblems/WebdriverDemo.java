package ProgramsPorblems;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebdriverDemo {
	
	public static void main(String[] args) {
		
		WebDriver driver = new FirefoxDriver();
		driver.get("http://community.perfectomobile.com/posts/992642-remotewebdriver-vs-webdriver");
		
		
	}

}
