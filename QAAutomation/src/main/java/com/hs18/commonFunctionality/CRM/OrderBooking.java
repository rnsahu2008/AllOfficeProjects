package com.hs18.commonFunctionality.CRM;

import com.hs18.util.LocatorExcel;

public class OrderBooking extends LocatorExcel {
	public void enterCallerNo(String callerno) {

		click(locator("CRM_callerlink"));
		type(locator("CRM_callerfield"), callerno);
		click(locator("CRM_callerSubmitbutton"));
	}

	public void searchProduct(String productid) {
		click(locator("CRM_bookingLink"));
		type(locator("CRM_productSearchfield"), productid);
		click(locator("CRM_productSearchbutton"));

	}

	public void editPinCode(String pincode) {
		click(locator("CRM_PdpPincodeLink"));
		type(locator("CRM_PdpPincodefield"), pincode);
		click(locator("CRM_PdpPincodesubmit"));

	}

	public void addToCart(String firstname, String lastName, String mobile,
			String mail, String address) throws InterruptedException {
		click(locator("CRM_PdpAddtoCartlink"));
		type(locator("CRM_cartFirstName"), firstname);
		type(locator("CRM_cartLastName"), lastName);
		type(locator("CRM_cartMobilefield"), mobile);
		type(locator("CRM_cartMailfield"), mail);
		type(locator("CRM_checkoutAddress"), address);
		
		//checkbox(crmLocator("CRM_sameShippingcheckboxField"));
		//mouseoverWithoutClick(locator("CRM_cartConfirmButton"));		

	}
}
