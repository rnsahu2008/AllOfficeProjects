 package ProgramsPorblems;

 import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

 public class ImageComparison {

      public WebDriver driver;
      private String baseUrl;
     
      @BeforeSuite
      public void setUp() throws Exception {
       
    		System.setProperty("webdriver.chrome.driver", "C:\\jars\\Chormedriver\\chromedriver.exe");
    		driver = new ChromeDriver();
            baseUrl = "https://www.google.co.in/";

    	   //driver = new ChromeDriver(cap);
    	//driver.get("https://www.google.co.in/");	
    	

    	  
    	//  driver = new FirefoxDriver();
        // baseUrl = "https://www.google.co.in/";
         driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);      
      }
        
      @AfterSuite
      public void tearDown() throws Exception {
         driver.quit();    
      }
       
      @Test
      public void testImageComparison()
               throws IOException, InterruptedException
      {
    	  Wait wait = new FluentWait(driver).withTimeout(30, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
         driver.navigate().to(baseUrl);
         File screenshot = ((TakesScreenshot)driver).
getScreenshotAs(OutputType.FILE);
         Thread.sleep(3000);
         FileUtils.copyFile(screenshot, new File("D:\\GoogleOutput.jpg"));

         File fileInput = new File("D:\\GoogleOutput.jpg");
         File fileOutPut = new File("D:\\GoogleOutput.jpg");

         BufferedImage bufileInput = ImageIO.read(fileInput);
         DataBuffer dafileInput = bufileInput.getData().getDataBuffer();
         int sizefileInput = dafileInput.getSize();                     
         BufferedImage bufileOutPut = ImageIO.read(fileOutPut);
         DataBuffer dafileOutPut = bufileOutPut.getData().getDataBuffer();
         int sizefileOutPut = dafileOutPut.getSize();
         System.out.println(sizefileOutPut+"Size"+sizefileInput);
         Boolean matchFlag = true;
         if(sizefileInput == sizefileOutPut) {                         
            for(int j=0; j<sizefileInput; j++) {
                  if(dafileInput.getElem(j) != dafileOutPut.getElem(j)) {
                        matchFlag = false;
                        break;
                  }
             }
         }
         else                            
            matchFlag = false;
         Assert.assertTrue(matchFlag, "Images are not same");    
      }
 }

