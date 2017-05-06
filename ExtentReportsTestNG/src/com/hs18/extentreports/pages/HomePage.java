package com.hs18.extentreports.pages;

import org.openqa.selenium.WebDriver;

public class HomePage {
	
	private WebDriver driver;
	
	private final String TITLE = "CRM Application";
	
	public HomePage(WebDriver driver) {
		
		this.driver = driver;
	}
	
	public boolean isAt() {
		
		return this.driver.getTitle().equals(TITLE);
	}
}