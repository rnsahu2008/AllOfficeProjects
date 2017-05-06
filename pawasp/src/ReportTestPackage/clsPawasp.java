package ReportTestPackage;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class clsPawasp 

{
	WebDriver driver;                                                                                                                   
	public static String downloadPath = "D:\\Selen";
	public static String inputfile = "D:\\test.xls";

                                                                                                                              
	@Parameters({ "url","username","password"})                                                                                                                                   
    @Test   
    public void TestLogin(@Optional String url,@Optional String username,@Optional String password) throws Exception                                                                      
    {

        InputStream inp = new FileInputStream(inputfile);
        HSSFWorkbook wb = new HSSFWorkbook(inp);
        HSSFSheet sheet = wb.getSheetAt(0);
        int lastrow = sheet.getLastRowNum();
	
   for(int i=0;i<=lastrow;i++)
   {

	   
	System.setProperty("webdriver.chrome.driver", "C:\\jars\\chromedriver.exe");
	   HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
	   chromePrefs.put("profile.default_content_settings.popups", 0);
	   chromePrefs.put("download.default_directory", downloadPath);
	   ChromeOptions options = new ChromeOptions();
	   HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
	   options.setExperimentalOption("prefs", chromePrefs);
	   options.addArguments("--test-type");
	   DesiredCapabilities cap = DesiredCapabilities.chrome();
	   cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
	   cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	   cap.setCapability(ChromeOptions.CAPABILITY, options);
	   WebDriver driver = new ChromeDriver(cap);
	   Wait wait = new FluentWait(driver).withTimeout(30, TimeUnit.SECONDS).
			   pollingEvery(3,TimeUnit.SECONDS).ignoring(NoSuchElementException.class);


	   

	   //driver = new ChromeDriver();
	   
	 	   driver.manage().window().maximize();                                                                                            
	     driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);   
    driver.manage().window().maximize();                                                                                            
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);   
	  /* HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver();
	   DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
       capabilities = DesiredCapabilities.htmlUnit();
       capabilities.setBrowserName("htmlunit");
       htmlUnitDriver.manage().window().maximize();                                                                                            
       htmlUnitDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);   
*/
       

	   
	   
   Row row0 = sheet.getRow(i);
   	//Cell cell4 = row0.getCell(3);
   	//String url= cell4.getStringCellValue();
   	driver.get(url);
   
   	 WebElement userNameElement = driver.findElement(By.name("username"));
   //	 Cell cell5 = row0.getCell(4);
   	 //String username= cell5.getStringCellValue();
   	 userNameElement.sendKeys(username);
    
   	 WebElement userpwdElement = driver.findElement(By.name("password"));
  // 	 Cell cell6 = row0.getCell(5);
   	// String password= cell6.getStringCellValue();
   	 userpwdElement.sendKeys(password);
  	  	
     WebElement btnLogin = driver.findElement(By.xpath("/html/body/div/div[1]/div/div/form/button"));
     btnLogin.click();    	
     Cell cell0 = row0.getCell(0);
    String firstcell= cell0.getStringCellValue();

    
    
    System.out.println(firstcell);
    WebElement defualtlink= driver.findElement(By.xpath("//*[starts-with(text(),'" + firstcell + "')]")); 
    defualtlink.click();

   Cell cell1 = row0.getCell(1);
   String Secondcell= cell1.getStringCellValue();
   System.out.println(Secondcell);

   WebElement SecondLink= driver.findElement(By.xpath("//*[starts-with(text(),'" + Secondcell + "')]")); 
   SecondLink.click();

   Thread.sleep(1000);
   Cell cell3 = row0.getCell(2);
   String Thirdcell= cell3.getStringCellValue();   
   WebElement ThirdLink= driver.findElement(By.xpath("//*[starts-with(text(),'" + Thirdcell + "')]")); 
   ThirdLink.click(); 

   Thread.sleep(10000);
  driver.switchTo().frame(0);

//Cell cell7 = row0.getCell(6);
//String getKpi= cell7.getStringCellValue();

//System.out.println(getKpi);
//WebElement DashBoardClikColumn = driver.findElement(By.xpath(getKpi));

//WebElement DashBoardClikColumn = driver.findElement(By.xpath("/html/body/div[2]/div[4]/div[5]/div/div/div[1]/div[5]/div[1]/div[11]/div[1]/div[2]/canvas[2]"));


WebElement DashBoardClikColumn = driver.findElement(By.xpath("//*[starts-with(@id,'view')]/div[1]/div[2]/canvas[2]"));
Actions act = new Actions(driver);

Cell cell8 = row0.getCell(3);
int getxoffset= (int) cell8.getNumericCellValue();
Cell cell9 = row0.getCell(4);
int getyoffset= (int) cell9.getNumericCellValue();

act.moveToElement(DashBoardClikColumn,0,0).moveByOffset(getxoffset,getyoffset).click().build().perform(); 

Thread.sleep(3000); ///html/body/div[6]/div/div[5]/div[2]
//act.moveToElement(DashBoardClikColumn,0,0).moveByOffset((720/16)*3,1).click().build().perform(); 
//act.moveToElement(DashBoardClikColumn,0,0).moveByOffset((394*50)/100,1).click().build().perform(); 


WebElement DownLoadLink= driver.findElement(By.xpath("//*[@id='top_toolbar']/div/span[2]/a[1]/span")); 
DownLoadLink.click();
//Thread.sleep(5000);
    
//WebElement crossTabClik= driver.findElement(By.xpath("html/body/div[6]/div/div[5]/div[2]")); 
//crossTabClik.click();
  // 
Cell cell10 = row0.getCell(5);
String FileType= cell10.getStringCellValue();   
System.out.println(FileType);
//WebElement FileTypeLink= driver.findElement(By.xpath("//*[contains(text(),'" + FileType + "')]")); 
//WebElement FileTypeLink = driver.findElement(By.className("tabMenuContent"));
WebElement FileTypeLink = driver.findElement(By.xpath("/html/body/div[6]"));

Thread.sleep(2000);
WebElement FileTypeLink1= FileTypeLink.findElement(By.xpath("//*[starts-with(text(),'" +FileType+ "')]")); 
System.out.println(FileType);

FileTypeLink1.click(); 

//*[@id="tableau_base_widget_Dialog_0"]/div[2]/div[2]/a/span[2]

Thread.sleep(4000);
WebElement AlertDownLoad= driver.findElement(By.xpath("//*[starts-with(@id,'tableau_base_widget_Dialog')]/div[2]/div[2]/a/span[2]")); 
AlertDownLoad.click();

Thread.sleep(2000);
driver.quit();
//WebElement backtoDefault= driver.findElement(By.xpath(".//*[@id='ng-app']/div[1]/div/div[1]/div/div[1]/div/span[3]/a")); 
//backtoDefault.click();


    }
	
  }
	/*
	public static FirefoxProfile FirefoxDriverProfile() throws Exception
	{

		 
		
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.download.dir", downloadPath);
		profile.setPreference("browser.helperApps.neverAsk.openFile",
				"text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
"text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
		profile.setPreference("browser.download.manager.focusWhenStarting", false);
		profile.setPreference("browser.download.manager.useWindow", false);
		profile.setPreference("browser.download.manager.showAlertOnComplete", false);
		profile.setPreference("browser.download.manager.closeWhenDone", false);
		return profile;
	}

	
	*/

}
