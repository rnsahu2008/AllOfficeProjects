package com.hs18.commonFunctionality.DFC;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.hs18.util.LocatorExcel;

public class DFCCreateOrderForm extends LocatorExcel {
	
		
	public void searchProduct(String productid) throws InterruptedException {

		click(locator("Dfc_TabOrderProcessing"));
		click(locator("Dfc_searchProductidinput"));
		type(locator("Dfc_searchProductidinput"), productid);
		Thread.sleep(5000);

			
		}


	public void AddBalanceQty() throws InterruptedException 
	{
		String balanceOrder	=element(locator("Dfc_SerachPage_BalanceQunity")).getText();
		System.out.println(balanceOrder);
		click(locator("Dfc_SerachPage_enterquntity"));	
		type(locator("Dfc_SerachPage_enterquntity"), balanceOrder);
		Thread.sleep(2000);
		click(locator("Dfc_SerachPage__qty_Submit"));
		String picklistgenText=element(locator("Dfc_picklistid")).getText();
		
		 Assert.assertTrue(picklistgenText.contains("Generated a picklist with id:"));
		
 }

	public void GetPickList() throws InterruptedException  
	{
		String picklistidmsg	=element(locator("Dfc_picklistid")).getText();
		String[] picklist=picklistidmsg.split(":");
		String picklistId=picklist[1].trim();
		System.out.println(picklistId);
			
		 // String picklistId="2271";
		click(locator("Dfc_picklistLink"));

		WebElement table =  element(locator("Dfc_piclisttable"));
		  int numOfRow = table.findElements(By.tagName("tr")).size(); 
		  WebElement tablerow =  element(locator("Dfc_piclisttableNoofcolumn"));
		 int Noofcol	=tablerow.findElements(By.tagName("td")).size();
		  String first_part = "//*[@id='form1:pickListTable_data']/tr[";
		  String second_part = "]/td[";
		  String third_part = "]/div";
		  String fourth_part = "/span";
		  int j=2;
		  for (int i=1; i<=numOfRow; i++){
			  
			  boolean picklistconfirm=false;
			//*[@id="form1:pickListTable_data"]/tr[3]/td[2]/div
			  	String final_xpath = first_part+i+second_part+j+third_part;
			  	String final_xpathclick = first_part+i+second_part+(j-1)+third_part+fourth_part;
			      
		       String picklisttext	= element(By.xpath(final_xpath)).getText();
				
		       if(picklisttext.equals(picklistId))
		       {
		    	click(By.xpath(final_xpathclick));
		    	picklistconfirm=true;
		    	Thread.sleep(2000);
		    	}
		      
		       if(picklistconfirm)
		       {
		    	   break;
		       }

			  
			  
		  }

		  Thread.sleep(2000);
		 click(locator("Dfc_ConfirmPickList"));
				  
		  Thread.sleep(2000);
	
 }

	public void generateOrderForm(String orderformtype,String couriername) throws InterruptedException
	
	{
		
		click(locator("Dfc_GenerateOrderFormlink"));	
		dropdown(locator("Dfc_OrderFormType"), orderformtype);
		dropdown(locator("Dfc_CourierName"), couriername);
		click(locator("Dfc_ModeofPayment"));
		click(locator("Dfc_OrderFormSubmitbutton"));
		click(locator("Dfc_OrderFormAlerYes"));
		Thread.sleep(5000);
		
		
	}
	
/*public void generateMenifest(String orderformtype,String couriername,String suborder) throws InterruptedException
	
	{
		
		click(locator("Dfc_GenerateMenifestlink"));	
		dropdown(locator("Dfc_GenerateMenifestFormType"), orderformtype);
		dropdown(locator("Dfc_GenerateMenifestCourType"), couriername);
		click(locator("Dfc_GenerateMenifestbysuborderYes"));
		type(locator("Dfc_GenerateMenifestBysuborder"), suborder);
		
		click(locator("Dfc_OrderFormSubmitbutton"));
		click(locator("Dfc_OrderFormAlerYes"));
		Thread.sleep(5000);
		
		
	}
	
*/	
}
