package com.hs18.Test.Android;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hs18.DataUtils.ExcelReader;
import com.hs18.commonFunctionality.Android.AndroidCODBooking;

public class AndroidCODTest extends AndroidCODBooking{
	
	ExcelReader read = new ExcelReader();
	
	
	
	@Test(description="COD Booking Test",dataProvider="cod")
	public void codBooking(String username , String password, String pincode, String productid) throws IOException, InterruptedException
	{
		bookCODOrder(username, password, pincode, productid);
		Assert.assertEquals(verifyText(locator("thankyou_message")), "Thank you for shopping with Homeshop18!");
	}
	
	@DataProvider(name = "cod")
	public Object[][] dataProviderLogin() throws IOException {

		read.testDataFile(file);
		Object[][] data = new Object[][] {{read.readFromColumn("Android", 1, 0),read.readFromColumn("Android", 1, 1),read.readFromColumn("Android", 1, 2),read.readFromColumn("Android", 1, 3)}};
		read.closeFile();
		return data;
}
}
