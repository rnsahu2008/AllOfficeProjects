package com.lc.dataprovider;

import org.testng.annotations.DataProvider;

public class JobSearch_DP {
	
	 @DataProvider(name = "getRelevancyScore")
	    public static Object[][] getRelevancyScore() {
		String[][] dataset = {{"0"},{"1"}};
     return dataset;
	 }

}
