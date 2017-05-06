package com.sel;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.internal.Locatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hs18.lib.HS18TestsBase;
import com.hs18.lib.Validator;
import com.sel.Components;

//import com.hs18.lib.HS18TestsBase;

public class InboundTests extends HS18TestsBase{
	public static final Logger logger = LoggerFactory.getLogger(InboundTests.class);
	public String sUserIncomp;
	Components comp=null;
	String sUserPwd="testing";
	public static String WebHost="10.50.27.17";
	
	
	 @BeforeMethod(alwaysRun=true)
	 public void init() throws InterruptedException
	 {
		 Thread.sleep(2000);
		 comp=new Components(driver);
		 driver.manage().window().maximize();
		 driver.get("http://" +WebHost +"/ebiznet/");
	 }
	
	 @Test(groups = { "Sanity","Regression","t1",""})
	 public void Do_login_intiliaze_site_company() throws Exception
	 {
        //Sample Code 
         comp.login("1", "hsnadmin", "adminhsn");
         comp.setcompanyvendor("PTD - Pataudi", "NITC - North India Top Company (P) Ltd");
         comp.ebiznavigation( "Warehouse","Fullfillment","Ship Labels");
        
	 }


    @Test
    public void Do_Return_Order_RTO()throws Exception 
    {
    	// RTO is not working .. and there are changes in RTO checkin only the Skelton is been formualated 
        comp.rflogin("1", "hsnadmin", "adminhsn", "1", "PTD", "NITC");

        setValue(ui.getLocator("RF", "Selection"), "1");
        click(ui.getLocator("RF", "Enter"));
        setValue(ui.getLocator("RF", "Selection"), "3");
        click(ui.getLocator("RF", "Enter"));
        setValue(ui.getLocator("RF", "ReturnsScanPO"), "POOCT3");
        setValue(ui.getLocator("RF", "ReturnsReturnType"), "RTO");
        click(ui.getLocator("RF", "Enter"));
        setValue(ui.getLocator("RF", "Ordernumber"), "978972");
        setValue(ui.getLocator("RF", "ReasonCode"), "978972");
        click(ui.getLocator("RF", "Enter"));
        click(ui.getLocator("RF", "Close"));

        // login to Ebiz Application
        // Reports->Reports->Receving->Purchase Orser Status Report
        comp.login("", "", "");

        comp.ebiznavigation("Reports", "Receving", "Purchase Order", "Status Report");
        // code for enter purchase order
        // code for click display button
        // code for getting necessary data from this UI

        comp.rflogin("1", "hsnadmin", "adminhsn", "1", "PTD", "NITC");
        // Receving
        setValue(ui.getLocator("RF", "Selection"), "1");
        click(ui.getLocator("RF", "Enter"));
        // Checkin
        setValue(ui.getLocator("RF", "Selection"), "1");
        click(ui.getLocator("RF", "Enter"));
        // Receipt number
        setValue(ui.getLocator("RF", "Selection"), "1");
        click(ui.getLocator("RF", "Enter"));

        // serial number
        setValue(ui.getLocator("RF", "Selection"), "1");
        click(ui.getLocator("RF", "Enter"));

        // Status GD
        setValue(ui.getLocator("RF", "Selection"), "1");
        click(ui.getLocator("RF", "Enter"));

        // Navigate back to Put awy step
        // LP number
        setValue(ui.getLocator("RF", "Selection"), "1");
        click(ui.getLocator("RF", "Enter"));
        // /Location mumber
        setValue(ui.getLocator("RF", "Selection"), "1");
        click(ui.getLocator("RF", "Enter"));


    }

    @Test
    public void PO()throws Exception
    {
    	String tax="2";
    	String PONum="NITC/PTD/2012-13/208";
    	String PORef="PO-LRR-252";
    	String qty="2";
    	String skuStatus="Good";
    	String sku="I-13041238";
    	String supplier="1541";
    	String Trailer="TRL-I-24";
    	
    	comp.login("1", "hsnadmin", "adminhsn");
        comp.setcompanyvendor("PTD - Pataudi", "NITC - North India Top Company (P) Ltd");
        PORef=comp.POCreation(supplier,sku,qty);
        //comp.ExistingPO(PORef,sku,qty);//---existing po
        comp.Taxation(PORef, tax);
        comp.Approval(PORef);
        PONum=comp.GetApprovedPO(PORef);
        String ReceiptNo=comp.POReceipt(PONum,Trailer);
        String[] SerialNo=comp.GenerateLabels(PONum,qty);
        for(int i=0;i<SerialNo.length;i++)
        	logger.info(SerialNo[i]);
        driver.get("http://" +WebHost +"/ebizrf/");
        comp.rflogin("1","hsnadmin","adminhsn","1","ptd","nitc");
        String[] LP=comp.GenerateLP(qty);
        comp.RFCheckin(PONum, qty, SerialNo, LP);
        driver.get("http://" +WebHost +"/ebizrf/");
        comp.rflogin("1","hsnadmin","adminhsn","1","ptd","nitc");
        comp.RFPutaway(qty, LP);
        driver.get("http://" +WebHost +"/ebiznet/");
        comp.login("1", "hsnadmin", "adminhsn");
        comp.setcompanyvendor("PTD - Pataudi", "NITC - North India Top Company (P) Ltd");
        comp.ClosePO(PONum);
    }
}
    

