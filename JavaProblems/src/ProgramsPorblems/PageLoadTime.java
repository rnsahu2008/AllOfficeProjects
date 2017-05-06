package ProgramsPorblems;

import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageLoadTime {
    public static void main(String[] args) {
        WebDriver driver;
        driver = new FirefoxDriver();       
        StopWatch pageLoad = new StopWatch();
        pageLoad.start();
        //Open your web app (In my case, I opened facebook)
        driver.get("https://www.facebook.com/");
        // Wait for the required any element (I am waiting for Login button in fb)
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search_store")));
        pageLoad.stop();
        //Get the time
        long pageLoadTime_ms = pageLoad.getTime();
        long pageLoadTime_Seconds = pageLoadTime_ms / 1000;
        System.out.println("Total Page Load Time: " + pageLoadTime_ms + " milliseconds");
        System.out.println("Total Page Load Time: " + pageLoadTime_Seconds + " seconds");
        driver.close();
    }
}


