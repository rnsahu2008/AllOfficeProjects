package com.lc.dataprovider;

import java.util.HashMap;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.lc.excel.ExcelReader;

public class JobStore_DP {
	 @DataProvider(name = "getJobData")
	    public static Object[][] getJobData() {
		 int index = 0;
		 System.out.println();
		 List<HashMap<String, String>> data = ExcelReader.readExcel("res/jobstoredata/GetJobs.xlsx");
		 Object[][] dataSet = new Object[data.size()][];
	        for (HashMap<String, String> list : data) {
	            dataSet[index] = new Object[]{list};
	            index++;
	        }
	        return dataSet;
	 }
	 
	 @DataProvider(name = "postJobData")
	    public static Object[][] postJobData() {
		 int index = 0;
		 List<HashMap<String, String>> data = ExcelReader.readExcel("res/jobstoredata/PostJobs.xlsx");
		 Object[][] dataSet = new Object[data.size()][];
	        for (HashMap<String, String> list : data) {
	            dataSet[index] = new Object[]{list};
	            index++;
	        }
	        return dataSet;
	 }
	 
	 @DataProvider(name = "getSourceCD")
	    public static Object[][] getSourceCD() {
		String[][] dataset = {{"JTR"} , {"JG8"} , {"J2C"} , {"ZRC"} , {"MJH"} , {"JHP"} , {"JDS"} , {"BYD"} , {"STW"} , {"L51"} , 
							  {"L52"} , {"L53"} , {"PJF"} , {"ADZ"} , {"CVL"} , {"LKP"} , {"EQT"} , {"JG2"} , {"JGU"} , {"JIJ"} , 
							  {"MNS"} , {"LCW"} , {"OUT"} , {"ZP1"} , {"ZP2"} , {"ZP3"} , {"ZP4"} , {"JD2"} , {"JC1"} , {"JC2"} , 
							  {"JC3"} , {"PJ2"} , {"LK1"} , {"LK2"}};
     return dataset;
	 }
	 	 
	 
	 @DataProvider(name = "getCountryCode")
	    public static Object[][] getCountryCode() {
		String[][] dataset = {{"US"} , {"UK"}};
		return dataset;
	 }

}
