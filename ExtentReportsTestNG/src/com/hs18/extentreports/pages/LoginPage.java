package com.hs18.extentreports.pages;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
 
public class LoginPage {
     
    private WebDriver driver;
     
    public LoginPage(WebDriver driver) {
         
        this.driver = driver;
         
        if(!driver.getTitle().equals("CRM Application")) {
            driver.get("http://beta10.dev.hs18.lan:7170/faces/callCenter/crmLogin.xhtml");
        }       
    }
     
    public ErrorPage incorrectLogin(String AgentID, String password) throws InterruptedException {
    	
    	driver.findElement(By.id("form1:login")).clear(); 
        driver.findElement(By.id("form1:login")).sendKeys(AgentID);
        driver.findElement(By.id("form1:login")).clear();
        driver.findElement(By.id("form1:password")).sendKeys(password);
        driver.findElement(By.id("form1:j_id_o")).click();
        Thread.sleep(2000);
        return new ErrorPage(driver);
    }
     
    public HomePage correctLogin(String AgentID, String password) throws InterruptedException {
        
    	driver.findElement(By.id("form1:login")).clear();
        driver.findElement(By.id("form1:login")).sendKeys(AgentID);
        driver.findElement(By.id("form1:login")).clear();
        driver.findElement(By.id("form1:password")).sendKeys(password);
        driver.findElement(By.id("form1:j_id_o")).click();
        Thread.sleep(2000);
        return new HomePage (driver);
    }
}