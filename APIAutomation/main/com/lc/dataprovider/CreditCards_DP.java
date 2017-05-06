package com.lc.dataprovider;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;

public class CreditCards_DP {
	 
	 @DataProvider(name = "ccCard")
	    public static Object[][] getCardType() throws JSONException {
		 Object[][] dataSet = new Object[3][];
		 JSONObject amex = new JSONObject();
		 amex.put("id", 0);
		 amex.put("CustomerID", 0);
		 amex.put("cardholder_name", "katherinelivecareer");
		 amex.put("type", "AMEX");
		 amex.put("number", "370000000010085");
		 amex.put("expire_year", 2015);
		 amex.put("expire_month", 9);
		 amex.put("cvv", 1000);
		 amex.put("bin", "370000");
		 amex.put("created_on", "2014-12-04T03:49:15.2673085-05:00");
		 amex.put("ModifiedOn", "2014-12-04T03:49:15.2673085-05:00");
		 
		 JSONObject master = new JSONObject();
		 master.put("id", 0);
		 master.put("CustomerID", 0);
		 master.put("cardholder_name", "katherinelivecareer");
		 master.put("type", "MASTERCARD");
		 master.put("number", "5500000000100087");
		 master.put("expire_year", 2015);
		 master.put("expire_month", 9);
		 master.put("cvv", 100);
		 master.put("bin", "550000");
		 master.put("created_on", "2014-12-04T03:49:15.2673085-05:00");
		 master.put("ModifiedOn", "2014-12-04T03:49:15.2673085-05:00");
		 
		 JSONObject visa = new JSONObject();
		 visa.put("id", 0);
		 visa.put("CustomerID", 0);
		 visa.put("cardholder_name", "katherinelivecareer");
		 visa.put("type", "VISA");
		 visa.put("number", "4385000000104001");
		 visa.put("expire_year", 2015);
		 visa.put("expire_month", 9);
		 visa.put("cvv", 100);
		 visa.put("bin", "438587");
		 visa.put("created_on", "2014-12-04T03:49:15.2673085-05:00");
		 visa.put("ModifiedOn", "2014-12-04T03:49:15.2673085-05:00");
		 
		 dataSet[0] = new Object[]{amex};
		 dataSet[1] = new Object[]{master};
		 dataSet[2] = new Object[]{visa};
		 return dataSet;
	 }
	 	 
}
