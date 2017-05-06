package com.hs18.extentreports.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ErrorPage {
	
	private WebDriver driver;
	
	private final String TITLE = "CRM Application";
	
	public ErrorPage(WebDriver driver) {
		
		this.driver = driver;
	}
	
	public boolean isAt() {
		
		return this.driver.getTitle().equals(TITLE);
	}
	
	public String getErrorText() {
        
        return driver.findElement(By.className("ui-messages-error-summary")).getText();
    }
}