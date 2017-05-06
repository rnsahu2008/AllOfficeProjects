package com.hs18.Test.DFC;

import java.awt.AWTException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hs18.DataUtils.ExcelReader;
import com.hs18.commonFunctionality.DFC.DFCCreateOrderForm;
import com.thoughtworks.selenium.webdriven.commands.KeyEvent;

public class DFCCreateOrderFormTest extends DFCCreateOrderForm {

	ExcelReader read = new ExcelReader();
	Logger log = Logger.getLogger(DFCCreateOrderFormTest.class);

	@Test(description = "product search", dataProvider = "DFCOrderForm")
	public void SearchProduct(String productid,String orderformtype,String couriername) throws InterruptedException, AWTException {
		
		
		log.info("Entering Product id");
	 searchProduct(productid);
	 AddBalanceQty(); 
	 GetPickList();
	generateOrderForm(orderformtype,couriername);
	keyboardAction(java.awt.event.KeyEvent.VK_ENTER);
	
		
	}

	
	


	@DataProvider(name = "DFCOrderForm")
	public Object[][] dataProviderCaller1() throws IOException {
		read.testDataFile(file);
		Object[][] data = new Object[][] { { read.readFromColumn("DFC", 1, 3) ,read.readFromColumn("DFC", 1, 5),
			read.readFromColumn("DFC", 1, 6)}};
		read.closeFile();
		return data;
	}

	}

	
	

