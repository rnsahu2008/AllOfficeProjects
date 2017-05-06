package com.lc.dataprovider;

import java.util.HashMap;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.lc.excel.ExcelReader;

public class Email_DP {
	 @DataProvider(name = "getHappyPath")
	    public static Object[][] getHappyPath() {
		 int index = 0;
		 List<HashMap<String, String>> data = ExcelReader.readExcel("res/emaildata/Transactional_Email_HappyPath.xlsx");
	        Object[][] dataSet = new Object[data.size()][];
	        for (HashMap<String, String> list : data) {
	            dataSet[index] = new Object[]{list};
	            index++;
	        }
	        return dataSet;
	 }
	 
	 @DataProvider(name = "getNegativeScenarios")
	    public static Object[][] getNegativeScenarios() {
		 int index = 0;
		 List<HashMap<String, String>> data = ExcelReader.readExcel("res/emaildata/Transactional_Email_NegativeScenarios.xlsx");
	        Object[][] dataSet = new Object[data.size()][];
	        for (HashMap<String, String> list : data) {
	            dataSet[index] = new Object[]{list};
	            index++;
	        }
	        return dataSet;
	 }
	 
	 @DataProvider(name = "getHappyPath_Template")
	    public static Object[][] getHappyPathWithTemplate() {
		 int index = 0;
		 List<HashMap<String, String>> data = ExcelReader.readExcel("res/emaildata/Transactional_Email_With _Template_HappyPath.xlsx");
	        Object[][] dataSet = new Object[data.size()][];
	        for (HashMap<String, String> list : data) {
	            dataSet[index] = new Object[]{list};
	            index++;
	        }
	        return dataSet;
	 }
	 
	 @DataProvider(name = "getNegativeScenarios_Template")
	    public static Object[][] getNegativeScenariosWithTemplate() {
		 int index = 0;
		 List<HashMap<String, String>> data = ExcelReader.readExcel("res/emaildata/Transactional_Email_With _Template_NegativeScenario.xlsx");
	        Object[][] dataSet = new Object[data.size()][];
	        for (HashMap<String, String> list : data) {
	            dataSet[index] = new Object[]{list};
	            index++;
	        }
	        return dataSet;
	 } 
}
