package src;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.testng.annotations.Test;

public class EbizSkuUpdateTest extends Base {
	
  @Test(description="Add New PO")
  public void addPo() throws InterruptedException, IOException, AWTException 
  {
	  //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	  driver.get("http://10.50.33.12/ebiznet/Default.aspx");
	  Thread.sleep(2000);
	  browserStop();
	  type(By.id("txtAccNo"),"1");
	  type(By.id("txtUser"),testData(2,1, 1));
	  type(By.id("txtPwd"),testData(2,1, 2));
	  click(By.id("btnLogin"));
	  waitForText(By.xpath("//span[text()='OK']"), "OK");
	  click(By.xpath("//span[text()='OK']"));
	  click(By.id("ctl00_ddSite_Input"));
	  click(By.xpath("//li[text()='DRH - DHARUHERA']"));
	  Thread.sleep(3000);
	  mouseoverWithoutClick(By.xpath("//span[text()='Purchasing']"));
	  Thread.sleep(1000);
	  mouseoverWithoutClick(By.xpath("//span[text()='Purchase Order Request']"));
	  Thread.sleep(1000);
	  click(By.xpath("(//span[text()='New'])[2]"));
	  waitForElementVisible(By.name("ctl00$ContentPlaceHolder1$txtPONumber"));
	  //writeExcel(0, 1, 3);
	  type(By.name("ctl00$ContentPlaceHolder1$txtPONumber"), testData(0, 1, 3));
	  type(By.name("ctl00$ContentPlaceHolder1$ddlRcvType"), "sales");
	  click(By.xpath("//div[@id='ctl00_ContentPlaceHolder1_ddlRcvType_DropDown']/div/ul/li[1]"));
	  type(By.name("ctl00$ContentPlaceHolder1$ddlSupplier"), testData(0, 1, 4));
	  click(By.xpath("//div[@id='ctl00_ContentPlaceHolder1_ddlSupplier_DropDown']/div/ul/li"));
	  click(By.name("ctl00$ContentPlaceHolder1$btnSave"));	  
	  
  }
  
  @Test(description="Add tax")
  public void addTax() throws InterruptedException
  {
	  try
	  { 
		  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		  type(By.id("txtAccNo"),"1");
		  type(By.id("txtUser"),testData(0,1, 1));
		  type(By.id("txtPwd"),testData(0,1, 2));
		  click(By.id("btnLogin"));
		  waitForText(By.xpath("//span[text()='OK']"), "OK");
		  click(By.xpath("//span[text()='OK']"));
		  click(By.id("ctl00_ddSite_Input"));
		  click(By.xpath("//li[text()='DRH - DHARUHERA']"));
		  Thread.sleep(3000);
	  mouseoverWithoutClick(By.xpath("//span[text()='Purchasing']"));
	  Thread.sleep(1000);
	  mouseoverWithoutClick(By.xpath("//span[text()='Purchase Order Request']"));
	  Thread.sleep(1000);
	  click(By.xpath("(//span[text()='Search'])[3]"));
	  waitforAlert();
	  alertAccept();
	  waitForElementVisible(By.name("ctl00$ContentPlaceHolder1$ddlPONumber"));
	  type(By.name("ctl00$ContentPlaceHolder1$ddlPONumber"), testData(0, 1, 3));
	  click(By.xpath("//div[@id='ctl00_ContentPlaceHolder1_ddlPONumber_DropDown']/div/ul/li[1]"));
	  click(By.name("ctl00$ContentPlaceHolder1$btnDisplay"));
	  
	  }
	  catch(NoAlertPresentException | IOException e)
	  {
		  e.printStackTrace();
	  }
  }
}
