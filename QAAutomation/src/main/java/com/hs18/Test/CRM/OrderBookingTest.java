package com.hs18.Test.CRM;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.hs18.DataUtils.ExcelReader;
import com.hs18.commonFunctionality.CRM.OrderBooking;
public class OrderBookingTest extends OrderBooking {

	ExcelReader read = new ExcelReader();
	Logger log = Logger.getLogger(OrderBooking.class);

	@Test(description = "Caller no Test", dataProvider = "caller")
	public void callernoTest(String callerno) throws InterruptedException {
		log.info("Entering caller no");
		enterCallerNo(callerno);
		Thread.sleep(2000);
		Assert.assertEquals(callerno, element(locator("CRM_cartCallerDisplay")).getText());
		log.info("Verified caller no");
	}

	@Test(description = "Search Product Test", dataProvider = "search")
	public void searchProductTest(String productid) throws InterruptedException {
		log.info("Searching entered product");
		searchProduct(productid);
		Assert.assertTrue(element(locator("CRM_productOnPage")).getText().contains(productid));
		log.info("verified searched product");
	}

	@Test(description = "PinCode Test", dataProvider = "pin", dependsOnMethods = "searchProductTest")
	public void pincodeTest(String pincode) throws InterruptedException {
		log.info("Entering pincode no");
		editPinCode(pincode);
		Assert.assertTrue(element(locator("CRM_PDPPincodeMessage")).getText().contains(pincode));
		log.info("Verifying pin code no");
	}

	@Test(description = "Cart Test", dataProvider = "cart", dependsOnMethods ="pincodeTest")
	public void cartTest(String firstname, String lastName, String mobile, String mail, String address,
			String productid) throws Exception {
		log.info("Entering customer details");
		try {
			//addToCart(firstname, lastName, mobile, mail, address);
//			Assert.assertEquals(element(locator("CRM_cartFirstNameB")).getAttribute("value"), firstname);
//			Assert.assertEquals(element(locator("CRM_cartLastNameB")).getAttribute("value"), lastName);
//			Assert.assertEquals(element(locator("CRM_cartMobilefieldB")).getAttribute("value"), mobile);
//			Assert.assertEquals(element(locator("CRM_cartMailfieldB")).getAttribute("value"), mail);
//			Assert.assertEquals(element(locator("CRM_cartShippingAddress")).getAttribute("value"), address);
			//mouseoverWithoutClick(locator("CRM_cartConfirmButton"));
			//click(locator("CRM_cartConfirmButton"));
			Thread.sleep(3000);
			click(locator("CRM_PdpAddtoCartlink"));
			try {
//				waitforAlert();
//				alertAccept();
//				checkbox(locator("CRM_sameShippingcheckboxField"));
//				Thread.sleep(3000);
//				click(locator("CRM_cartConfirmButton"));
//				if (elements(locator("CRM_cartErrorMessage")).size() > 0) {
//					click(locator("CRM_cartErrorMessage"));
//					dropdown(locator("CRM_cartSource"), "Others");
//					type(locator("CRM_cartSourceCable"), "abc cable");
//					
//				}
				dropdown(locator("CRM_cartSource"), "DEN");
			} catch (Exception e) {

				//if (elements(locator("CRM_cartErrorMessage")).size() > 0) {
				{
					//click(locator("CRM_cartErrorMessage"));
					//dropdown(locator("CRM_cartSource"), "DEN");
					//type(locator("CRM_cartSourceCable"), "abc cable");
					e.getMessage();
				}
			}
			finally {
				click(locator("CRM_cartConfirmButton"));
			}
			
			waitForElementVisible(locator("CRM_orderSucessMessage"));
			Assert.assertTrue(element(locator("CRM_orderSucessMessage")).getText()
					.contains("Your order has been successfully placed"));
			log.info("Customer details verified");
		}

		catch (Exception e) {
			searchProduct(productid);
			throw e;

		}
	}
	@Test(description = "Lead Test", dataProvider = "cart")
	public void leadTest(String firstname, String lastName, String mobile, String mail, String address,
			String productid) throws Exception 
	{
		searchProduct(productid);
		click(locator("CRM_createLeadLink"));
		Assert.assertTrue(element(locator("CRM_leadAddress")).getText().contains(address));
		searchProduct(productid);
	}
	
	@DataProvider(name = "caller")
	public Object[][] dataProviderCaller() throws IOException {
		read.testDataFile(file);
		Object[][] data = new Object[][] { { read.readFromColumn("Base", 1, 3) } };
		read.closeFile();
		return data;
	}

	@DataProvider(name = "search")
	public Object[][] dataProviderSearch() throws IOException {
		read.testDataFile(file);
		Object[][] data = new Object[][] { { read.readFromColumn("Base", 1, 4) } };
		read.closeFile();
		return data;
	}

	@DataProvider(name = "pin")
	public Object[][] dataProviderpin() throws IOException {
		read.testDataFile(file);
		Object[][] data = new Object[][] { { read.readFromColumn("Base", 1, 5) } };
		read.closeFile();
		return data;

	}

	@DataProvider(name = "cart")	
	public Object[][] dataProviderCart() throws IOException {
		read.testDataFile(file);
		Object[][] data = new Object[][] { { read.readFromColumn("Base", 1, 6), read.readFromColumn("Base", 1, 7),
				read.readFromColumn("Base", 1, 8), read.readFromColumn("Base", 1, 9),
				read.readFromColumn("Base", 1, 10), read.readFromColumn("Base", 1, 4) } };
		read.closeFile();
		return data;

	}

}
