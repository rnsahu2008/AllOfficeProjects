package com.hs18.commonFunctionality.Android;

import com.hs18.util.LocatorExcel;

public class AndroidCODBooking extends LocatorExcel{
	
	public void bookCODOrder(String username,String password, String pincode, String productid) throws InterruptedException
	{
		click(locator("product_search"));
		typeEnter(locator("product_searchText"), productid);
		click(locator("product_image"));
		click(locator("buy_button"));
		typeEnter(locator("pincode_check"), pincode);
		click(locator("pincode_checkButton"));
		click(locator("buynow_button"));
		click(locator("proceed_checkoutButton"));
		click(locator("username"));
		type(locator("username"), username);		
		type(locator("password"), password);		
		click(locator("signIn"));		
		element(locator("GCField"));		
		scrollTo("PayUMoney");		
		click(locator("cod_option"));
		click(locator("order_confirmationButton"));
		waitForElementVisible(locator("continue_shoppingButton"));		
	}

}
