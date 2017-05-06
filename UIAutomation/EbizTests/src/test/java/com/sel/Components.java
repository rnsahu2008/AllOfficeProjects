package com.sel;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

// import com.hs18.lib.SelBase;
// import com.hs18.lib.UIMapParser;
// import com.hs18.lib.WaitEvent;
// import com.hs18.lib.utils;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.internal.PropertiesFile;

import com.hs18.lib.SelBase;
import com.hs18.lib.UIMapParser;
import com.hs18.lib.Validator;
import com.hs18.lib.WaitEvent;
import com.hs18.lib.utils;

public class Components extends SelBase
{

    UIMapParser ui;
    PropertiesConfiguration config;

    public Components(WebDriver d)
    {
        try
        {

            // utils.setConfiguration("src/test/resources/config.properties");
        	config = new PropertiesConfiguration("src\\test\\resources\\UIobj.properties");
            driver = d;
            we = new WaitEvent(driver);
            ui = new UIMapParser(utils.getConfig("ObjectRepository"));
        } catch (Exception e)
        {
            e.printStackTrace();
            Assert.fail("Failed to instantiate Components object");
        }
    }

    /**
     * returns webElement with the specified properties
     * 
     * @param pageName
     * @param uiElementName
     * @return
     * @throws Exception
     */
    public WebElement getWebElement(String pageName, String uiElementName) throws Exception
    {
        return driver.findElement(ui.getLocator(pageName, uiElementName));
    }

    public WebElement getWebElement(By locator)
    {
        return driver.findElement(locator);
    }

    public void mouseMove(String locator)
    {
        
   /*    Actions builder = new Actions(driver);
        builder.moveToElement().clickAndHold().build().perform();
      System.out.println("");*/
        mouseMove(By.xpath(locator));
/*        String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement someElem = driver.findElement(By.xpath(locator));
        js.executeScript(mouseOverScript, someElem);*/

        // /* try {
        // we.waitForElement(locator, 4);
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }*/
        // //Alternative code
        // /* String mouseOverScript =
        // "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
        // JavascriptExecutor js = (JavascriptExecutor) driver;
        // WebElement someElem = driver.findElement(locator);
        // js.executeScript(mouseOverScript, someElem);*/
    }

    public void login(String accountid, String userid, String password) throws Exception
    {
        setValue(ui.getLocator("LoginPage", "accountid"), accountid, 5);
        setValue(ui.getLocator("LoginPage", "userid"), userid, 5);
        setValue(ui.getLocator("LoginPage", "password"), password, 5);
        click(ui.getLocator("LoginPage", "login"));

    }
    
    public void rflogin(String accountid, String userid, String password,String termid, String siteid, String compid)throws Exception
    {
    	setValue(ui.getLocator("RFlogin", "accountid"), accountid, 5);
        setValue(ui.getLocator("RFlogin", "userid"), userid, 5);
        setValue(ui.getLocator("RFlogin", "pwd"), password, 5);
        setValue(ui.getLocator("RFlogin", "termid"), termid, 5);
        setValue(ui.getLocator("RFlogin", "siteid"), siteid, 5);
        setValue(ui.getLocator("RFlogin", "compid"), compid, 5);
        click(ui.getLocator("RFlogin", "login"));
    }

    public void ebiznavigation(String MainMenutext, int durationSecs) throws Exception
    {
        ebiznavigation(MainMenutext, "", "");
    }

    public void ebiznavigation( String MainMenutext, String Submenutext, int durationSecs)throws Exception
    {
        ebiznavigation( MainMenutext, Submenutext, "");
    }

    public void ebiznavigation(String MainMenutext, String Submenutext, String Subsubmenutext) throws Exception
    {
        int durationSecs=20;
        String xpath = ".//*[contains(@id,'eBizMenu')]/ul//a/";
        String mainmenu = xpath + "span[text()='" + MainMenutext + "']";
        String submenu = mainmenu + "/parent::a/parent::li/parent::ul//span[text()='" + Submenutext + "']";
        String subsubmenu = submenu + "/parent::a/parent::li//span[text()='" + Subsubmenutext + "']";
        if (durationSecs > 0)
        {
            if (we == null)
                we = new WaitEvent(driver);

            we.waitForElement(By.xpath(".//*[@id='ctl00_eBizMenu']"), durationSecs);
        }
        // List<WebElement> sLen =
        // driver.findElements(By.xpath(".//*[@id='ctl00_eBizMenu']/ul//a/span"));
        // for (WebElement webElement : sLen)
        // {
        // if (webElement.getText().equalsIgnoreCase(MainMenutext))
        // {
        // webElement.click();
        // mouseMove(webElement);
        // Thread.sleep(5000);
        // List<WebElement> sLen2 = webElement.findElements(By.tagName("span"));
        // for (WebElement webElement2 : sLen2)
        // {
        // if (webElement2.getText().equalsIgnoreCase(Submenutext))
        // {
        // mouseMove(webElement2);// webElement2.click();
        // List<WebElement> sLen3 =
        // webElement2.findElements(By.tagName("span"));
        // for (WebElement webElement3 : sLen3)
        // {
        // if (webElement3.getText().equalsIgnoreCase(Submenutext))
        // {
        // mouseMove(webElement3);
        // webElement3.click();
        // }
        // }
        // }
        // }
        // }
       

        mouseMove(mainmenu);
        Thread.sleep(3000);
        if (Submenutext != null)
        {
            mouseMove(submenu);
            driver.findElement(By.xpath(submenu)).click();
            // mouseMove(".//*[@id='ctl00_eBizMenu']/ul//a/span[text()='"+MainMenutext+"']/parent::a/parent::li/parent::ul//span[text()='Purchase Order Creation']");
            // driver.findElement(By.xpath(".//*[@id='ctl00_eBizMenu']/ul//a/span[text()='Purchasing']/parent::a/parent::li/parent::ul//span[text()='Purchase Order Creation']")).click();
        }
        if (Subsubmenutext != null)
        {
            Thread.sleep(3000);
            mouseMove(subsubmenu);
            
            // mouseMove(".//*[@id='ctl00_eBizMenu']/ul//a/span[text()='"+MainMenutext+"']/parent::a/parent::li/parent::ul//span[text()='Purchase Order Approval']/parent::a/parent::li/parent::ul//span[text()='Search']");
            // driver.findElement(By.xpath(".//*[@id='ctl00_eBizMenu']/ul//a/span[text()='"+MainMenutext+"']/parent::a/parent::li/parent::ul//span[text()='Purchase Order Approval']/parent::a/parent::li/parent::ul//span[text()='Search']")).click();
            driver.findElement(By.xpath(subsubmenu)).click();
        }
    }
    
    public void ebiznavigation(String MainMenutext, String Submenutext, String Subsubmenutext, String Lastmenutext ) throws Exception
    {
        int durationSecs=20;
        String xpath = ".//*[contains(@id,'eBizMenu')]/ul//a/";
        String mainmenu = xpath + "span[text()='" + MainMenutext + "']";
        String submenu = mainmenu + "/parent::a/parent::li/parent::ul//span[text()='" + Submenutext + "']";
        String subsubmenu = submenu + "/parent::a/parent::li//span[text()='" + Subsubmenutext + "']";
        String lastmenu= subsubmenu + "/parent::a/parent::li//span[text()='" + Lastmenutext + "']";
        if (durationSecs > 0)
        {
            if (we == null)
                we = new WaitEvent(driver);

            we.waitForElement(By.xpath(".//*[@id='ctl00_eBizMenu']"), durationSecs);
        }
        
        mouseMove(mainmenu);
        Thread.sleep(3000);
        if (Submenutext != null)
        {
            mouseMove(submenu);
            driver.findElement(By.xpath(submenu)).click();
        }
        if (Subsubmenutext != null)
        {
            Thread.sleep(3000);
            mouseMove(subsubmenu);
            driver.findElement(By.xpath(subsubmenu)).click();
        }
        if(Lastmenutext != null)
        {
        	Thread.sleep(3000);
        	mouseMove(lastmenu);
        	driver.findElement(By.xpath(lastmenu)).click();
        }
        
    }

    
    
    public void setcompanyvendor(String site, String company) throws Exception
    {

        PropertiesConfiguration config = new PropertiesConfiguration("src\\test\\resources\\UIobj.properties");
        we.waitForElement(ui.getLocator("Homepage", "sitearrow"), 40);

        click(ui.getLocator("Homepage", "sitearrow"), 40);

        ebizselect(config.getString("Homepage.site"), site, 40);

        click(ui.getLocator("Homepage", "companyarrow"), 40);

        ebizselect(config.getString("Homepage.company"), company, 40);

    }

    public void ebizselect(String locator, String text, int durationSecs) throws Exception
    {
        logger.debug("Selecting value " + text + "in the dropdown " + locator);
        if (durationSecs > 0)
        {
            if (we == null)
                we = new WaitEvent(driver);

            we.waitForElement(By.xpath(".//*[@id='" + locator + "']//li"), durationSecs);
        }
        List<WebElement> sLen = driver.findElements(By.xpath(".//*[@id='" + locator + "']//li"));
        for (WebElement webElement : sLen)
        {
            if (webElement.getText().equalsIgnoreCase(text))
            {
                webElement.click();
                break;
            }
        }

    }
    
    public String POCreation(String supplier,String sku,String qty)throws Exception
    {
    	String PORef=lib.generateDynamicPO();
    	ebiznavigation( "Purchasing","Purchase Order Request","New");
    	selectValueFromDropDown(ui.getLocator("Purchase","PORefNo"),PORef);
    	selectValueFromDropDown(ui.getLocator("Purchase","SupplierInput"),supplier);
    	selectValueFromDropDown(ui.getLocator("Purchase","ReceiptType"),"Sales");
        click(ui.getLocator("Purchase","Save"));
        logger.info(PORef);
        AddPOLine(sku,qty);
        return PORef;
    }
    
    public void ExistingPO(String SearchPORef,String sku,String qty)throws Exception
    {
    	ebiznavigation( "Purchasing","Purchase Order Request","Search");
    	selectValueFromDropDown(ui.getLocator("Purchase","PORefSearch"),SearchPORef);
        click(ui.getLocator("Purchase","SearchBtn"),5);
        click(ui.getLocator("Purchase","AddPOLine"),5);
        AddPOLine(sku,qty);
    }
    
    public void AddPOLine(String sku,String qty)throws Exception
    {
    	selectValueFromDropDown(ui.getLocator("Purchase","SkuInput"),sku);
        Thread.sleep(5000);
        selectValueFromDropDown(ui.getLocator("Purchase","SkuStatusInput"),"Good");
        Thread.sleep(5000);
        /*selectValueFromDropDown(ui.getLocator("Purchase", "PackCode"),"1");
        Thread.sleep(5000);
        setValue(ui.getLocator("Purchase", "UOM"),"EA");
        Thread.sleep(1000);
        driver.findElement(ui.getLocator("Purchase", "UOM")).sendKeys(org.openqa.selenium.Keys.ARROW_DOWN);
        driver.findElement(ui.getLocator("Purchase", "UOM")).sendKeys(org.openqa.selenium.Keys.ARROW_DOWN);
        driver.findElement(ui.getLocator("Purchase", "UOM")).sendKeys(org.openqa.selenium.Keys.ENTER);*/
        //selectValueFromDropDown(ui.getLocator("Purchase", "UOM"),"EA");
        //Thread.sleep(5000);
        setValue(ui.getLocator("Purchase","OrderQty"), qty);
        click(ui.getLocator("Purchase","SaveBtn"),5);
        Thread.sleep(1000);
        driver.switchTo().activeElement().click();
        driver.switchTo().activeElement();
        click(ui.getLocator("Purchase","SubmitPO"),5);
        driver.switchTo().activeElement().click();
        //Validator.verifyElementExistInPage(driver, getLocator("Purchase","CreationSuccess"));
        //Validator.verifyTextinPage(driver, "Purchase Request submitted for taxation successfully");
    }
    
    public void Taxation(String PORef,String tax)throws Exception
    {
    	ebiznavigation( "Purchasing","Purchase Order Creation","Search");
    	selectValueFromDropDown(ui.getLocator("Purchase","PORefSearch"),PORef);
    	Thread.sleep(3000);
        click(ui.getLocator("Purchase","SearchBtn"));
        Thread.sleep(5000);
        WebElement Tax=driver.findElement(ui.getLocator("Purchase","Tax"));
        if(Tax.isEnabled())
        	setValue(ui.getLocator("Purchase","Tax"), tax);
        click(By.className("rfdCheckboxUnchecked"));
        click(ui.getLocator("Purchase","UpdateTax"));
        driver.switchTo().activeElement().click();
        click(ui.getLocator("Purchase","SubmitPOApproval"));
        driver.switchTo().activeElement().click();
    }
    
    public void Approval(String PORef)throws Exception
    {
    	ebiznavigation( "Purchasing","Purchase Order Approval",null);
    	selectValueFromDropDown(ui.getLocator("Purchase","PORefInput"),PORef);
        click(ui.getLocator("Purchase","SearchBtn"));
        click(By.className("rfdCheckboxUnchecked"));
        selectValueFromDropDown(ui.getLocator("Purchase","Action"),"Approve");
        click(ui.getLocator("Purchase","SubmitApprove"));
        driver.switchTo().activeElement().click();
    }
    
    public String GetApprovedPO(String PORef)throws Exception

    {
    	ebiznavigation( "Purchasing","Purchase Order Approval",null);
    	click(ui.getLocator("Purchase","StatusArrow"));
    	 click(By.xpath("//*[@id='ctl00_ContentPlaceHolder1_ddlQbRequestStatus_DropDown']//li[text()='Approved']"));
    	//selectValueFromDropDown(ui.getLocator("Purchase","Status"),"Approved");
    	selectValueFromDropDown(ui.getLocator("Purchase","PORefInput"),PORef);
        click(ui.getLocator("Purchase","SearchBtn"));
        String PONum=getText(ui.getLocator("Purchase","PONumber"),10);
        logger.info(PONum);
        return PONum;
    }
    
    public String POReceipt(String PONum,String Trailer)throws Exception
    {
    	ebiznavigation( "Receiving","PO Receipt","New");
    	selectValueFromDropDown(ui.getLocator("Receiving","POInput"),PONum);
    	selectValueFromDropDown(ui.getLocator("Receiving","TrailerInput"),Trailer);
        click(ui.getLocator("Receiving","CreateReceipt"));
        click(By.className("rfdCheckboxUnchecked"),5);
        click(ui.getLocator("Receiving","Submit"));
        driver.switchTo().activeElement();
        String msg=driver.findElement(By.className("rwDialogText")).getText();
        String ReceiptNo=msg.split(": ")[1].trim();
        return ReceiptNo;
    }
    
    public String[] GenerateLabels(String PONum,String qty)throws Exception
    {
    	int Qty=Integer.parseInt(qty);
    	String[] Serial=new String[Qty];
    	ebiznavigation( "Reports","Warehouse","Receiving","Serial# Generate and Print Labels");
    	selectValueFromDropDown(ui.getLocator("Reports","POLabelInput"),PONum);
    	click(ui.getLocator("Reports", "Both"));
        click(ui.getLocator("Reports","Search"));
        click(By.linkText("Generate"));
        Thread.sleep(1000);
        driver.switchTo().activeElement().click();
        String SerialNo=driver.findElement(ui.getLocator("Reports","SerialBegin")).getText().split("/")[1].trim();
        long serial=Long.parseLong(SerialNo);
        for(int i=0;i<Qty;i++)
        	Serial[i]="NITC/0"+serial++;
        return Serial;
        		
    }
    
    public String[] GenerateLP(String qty)
    {
    	int Qty=Integer.parseInt(qty);
    	String[] LP=new String[Qty];
    	for(int i=0;i<Qty;i++)
    	{	LP[i]="LP-"+RandomStringUtils.randomAlphabetic(3)+i;
    		logger.info(LP[i]);
    	}
    	
    	return LP;
    	
    }
    
    public void RFCheckin(String PONum,String qty,String[] SerialNo,String[] LP)throws Exception
    {
    	String Receiving="1";
        String PutAway="2";
        String Checkin="1";
        String NewQty="4";
        int Qty=Integer.parseInt(qty);
        setValue(ui.getLocator("RF","Selection"), Receiving);
        click(ui.getLocator("RF","Enter"));
        setValue(ui.getLocator("RF","Selection"), Checkin);
        click(ui.getLocator("RF","Enter"));
        setValue(ui.getLocator("RF","Selection"), PONum+".1");
        click(ui.getLocator("RF","Enter"));
        setValue(ui.getLocator("RF","Selection"), "I-13041238");
        click(ui.getLocator("RF","Enter"));
        if(Validator.isTextPresent(driver, "UOM:EA"))
        {
        	setValue(ui.getLocator("Checkin","Height"), "10");
	        setValue(ui.getLocator("Checkin","Width"), "10");
	        setValue(ui.getLocator("Checkin","Length"), "10");
	        setValue(ui.getLocator("Checkin","Weight"), "10");
	        setValue(ui.getLocator("Checkin","Qty"), "1");
	        click(ui.getLocator("RF","Enter"));
	        setValue(ui.getLocator("Checkin","Layers"), "10");
	        setValue(ui.getLocator("Checkin","Cases"), "10");
	        setValue(ui.getLocator("Checkin","MaxHeight"), "10");
	        click(ui.getLocator("RF","Enter"));
        }
        for(int i=0;i<Qty;i++)
        {
        	setValue(ui.getLocator("RF","Selection"), SerialNo[i]);
	        click(ui.getLocator("RF","Enter"));
	        if(Validator.isTextPresent(driver, "Is Pallet Completed?"))
	        {
	        	if(i==Qty-1)
		        	setValue(ui.getLocator("RF","Selection"),"Y");
		        else
		        	setValue(ui.getLocator("RF","Selection"),"N");
		        click(ui.getLocator("RF","Enter"));
	        }
	        setValue(ui.getLocator("RF","Selection"),"GD");
	        click(ui.getLocator("RF","Enter"));
	        setValue(ui.getLocator("RF","Selection"),LP[i]);
	        click(ui.getLocator("RF","Enter"));
	        if(i==Qty-1)
	        	break;
	        setValue(ui.getLocator("RF","Selection"),NewQty);
	        click(ui.getLocator("RF","Enter"));
        }
    }
    
    public void RFPutaway(String qty,String[] LP)throws Exception
    {
    	
    	String PutAway="2";
    	String Receiving="1";
    	int Qty=Integer.parseInt(qty);
    	setValue(ui.getLocator("RF","Selection"), Receiving);
        click(ui.getLocator("RF","Enter"));
    	setValue(ui.getLocator("RF","Selection"), PutAway);
        click(ui.getLocator("RF","Enter"));
        for(int i=0; i<Qty;i++)
        {
        	setValue(ui.getLocator("RF","Selection"), LP[i]);
        	click(ui.getLocator("RF","Enter"));
        }
        click(ui.getLocator("RF","Enter"));
        for(int i=0; i<Qty;i++)
        {
        	click(By.xpath("//*[@id='Form1']/input[5]"));
	        String displayed= driver.findElement(By.id("Form1")).getText();
	        String location=displayed.substring(16,26);
	        logger.info(location);
	        setValue(ui.getLocator("RF","Selection"), location);
	    	click(ui.getLocator("RF","Enter"));
	    	if(Validator.isTextPresent(driver, "Do you want to Merge"+
	    			"this LP with existing"+
	    			"Product in the Location?"))
	    		click(By.xpath("//*[@id='Form1']/input[5]"));
        }
    }
    
    public void ClosePO(String PONum)throws Exception
    {
    	ebiznavigation( "Receiving","PO Receipt","Search");
    	selectValueFromDropDown(ui.getLocator("Receiving","ReceiptInput"),PONum+".1");
    	click(ui.getLocator("Receiving","Display"));
    	click(ui.getLocator("Receiving","SelectLine"));
    	driver.switchTo().activeElement().click();
    	
    }


    public void selectValueFromDropDown(By locator,String val) throws Exception
    {
    	  setValue(locator, val);
          Thread.sleep(1000);
          driver.findElement(locator).sendKeys(org.openqa.selenium.Keys.ARROW_DOWN);
          driver.findElement(locator).sendKeys(org.openqa.selenium.Keys.ENTER);
          
    }
    
    
    public String createPOReceipt(String PoNum,String trailer) throws Exception
    {
    	ebiznavigation( "Receiving","PO Receipt","New");

        selectValueFromDropDown(By.name("ctl00$ContentPlaceHolder1$ddlPO"),PoNum);
     
        Thread.sleep(1000);
        selectValueFromDropDown(org.openqa.selenium.By.name("ctl00$ContentPlaceHolder1$ddTrailer"), trailer);
        
        Thread.sleep(1000);

        try
        {
        	click(org.openqa.selenium.By.name("ctl00$ContentPlaceHolder1$btnNew"));
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        click(org.openqa.selenium.By.className("rfdCheckboxUnchecked"),5);
        click(ui.getLocator("Receiving","Submit"),5);
        driver.switchTo().activeElement();
        String msg=driver.findElement(org.openqa.selenium.By.className("rwDialogText")).getText();
        String ReceiptNo=msg.split(": ")[1].trim();
        logger.info(msg);
        return ReceiptNo;
    }
}