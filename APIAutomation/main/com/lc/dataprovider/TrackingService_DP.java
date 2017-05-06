package com.lc.dataprovider;

import org.testng.annotations.DataProvider;

public class TrackingService_DP {

	@DataProvider(name = "getHitTypes1")
    public static Object[][] getHitTypes1() {
	String[][] dataset = {{"eo"} , {"ec"} , {"uc"}};
    return dataset;
	}
	
	@DataProvider(name = "getHitTypes2")
    public static Object[][] getHitTypes2() {
	String[][] dataset = {{"lp"} , {"pv"} , {"ac"}, {"ap"} , {"sc"}};
    return dataset;
	}
	
	@DataProvider(name = "getHitTypes3")
    public static Object[][] getHitTypes3() {
	String[][] dataset = {{"ac"} , {null}, {""}};
    return dataset;
	}
	
	@DataProvider(name = "getHitTypes4")
    public static Object[][] getHitTypes4() {
	String[][] dataset = {{"pv"} , {"ec"} , {"eo"}, {"uc"} , {"ac"}, {"ap"} , {"sc"}, {null}};
    return dataset;
	}
	
	@DataProvider(name = "getReferrer")
    public static Object[][] getReferrer() {
	String[][] dataset = {{"http://dev2.livecareer.com:1024/home.aspx"} , {"http%3A%2F%2Fdev2.livecareer.com%3A1024%2Fhome.aspx"}};
    return dataset;
	}
}


