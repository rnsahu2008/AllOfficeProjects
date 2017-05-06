package com.hs18.commonFunctionality.DFC;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.hs18.DataUtils.ExcelReader;
import com.hs18.util.LocatorExcel;

public class DFCLogin extends LocatorExcel  {
	
	ExcelReader read = new ExcelReader();
	
	public void statechangetoVerified(String suborder)
	{
	openUrl("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"+suborder+"/destination/2109");
	while(getDriver().getPageSource().contains("504")||!(getDriver().getPageSource()).contains("destination state"))
	{
		openUrl("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"+suborder+"/destination/2109");
	
	}
	}
	
	
	public void DflogIn(String username , String password) throws InterruptedException 
	{
		waitForElementVisible(locator("Dfc_userName"));
		type(locator("Dfc_userName"), username);	
		type(locator("Dfc_password"), password);
		
		click(locator("Dfc_loginbtn"));		
	
	}


}
