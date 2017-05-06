package com.lc.dataprovider;
import static com.lc.constants.Constants_UserService.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;

import com.lc.db.DbUtil;
import com.lc.excel.ExcelReader;

public class UserService_DP {
		
	@DataProvider(name = "getUserOptins")
    public static Object[][] getUserOptins() {
	String[][] dataset = {{"NEWS"} , {"SPOF"} , {"SMVL"} , {"RESJ"} , {"JBAD"} , {"JBAW"} , {"POSM"} };
    return dataset;
	}
	
	@DataProvider(name = "getUserInvalidCodeOptins")
    public static Object[][] getUserInvalidCodeOptins() {
	String[][] dataset = {{RandomStringUtils.randomAlphabetic(4)} , {RandomStringUtils.randomNumeric(4)} , {RandomStringUtils.randomAscii(4)}};
    return dataset;
	}
	
	@DataProvider(name = "getRoleNamesInvalid")
    public static Object[][] getRoleNamesInvalid() {
		String[][] dataset = {{RandomStringUtils.randomAlphabetic(6)} , {RandomStringUtils.randomNumeric(8)} , {RandomStringUtils.randomAscii(10)}};
    return dataset;
	}
	
	@DataProvider(name = "getUserInvalidResponseOptins")
    public static Object[][] getUserInvalidResponseOptins() {
	String[][] dataset = {{"2"} ,{"-2"}, };
    return dataset;
	}
	
	@DataProvider(name = "getUserData")
    public static Object[][] getUserData() {
	String[][] dataset = {{"0"} , {"1234567"}};
    return dataset;
	}
	
	@DataProvider(name = "getUserCredentials")
    public static Object[][] getUserCredentials() {
		int index = 0;
		 List<HashMap<String, String>> data = ExcelReader.readExcel("res/userservicedata/authenticateuser_inputsheet.xlsx");
		 Object[][] dataSet = new Object[data.size()][];
	        for (HashMap<String, String> list : data) {
	            dataSet[index] = new Object[]{list};
	            index++;
	        }
	        return dataSet;
	}
	
	@DataProvider(name = "getPassword")
    public static Object[][] getPassword() {
	String[][] dataset = {{"!@#$%^&*()_+{}|:<>?~"},{"123456789012"},{"qwertyuioplkjhgfdsaz"},{"qwqeexcvbnm"},{"Aa@#bB12ASnb^&n234c"}};
    return dataset;
	}
	
	@DataProvider(name = "getUserUrl")
    public static Object[][] getUserUrl() {
	String[][] dataset = {{GET_USER_URL_V1},{GET_USER_URL_V2}};
    return dataset;
	}
	
	
	@DataProvider(name = "userType")
    public static Object[][] getUserType() 
    {
        return new Object[][] { { "Guest" }, { "Registered" } };
    }
	
	@DataProvider(name = "providerCD")
    public static Object[][] getproviderCD() 
    {
        return new Object[][] { { "FCBK" }, { "GGLE" } };
    }
	
	
	/*@DataProvider(name = "getRoles")
    public static List<Map> getRoleName() {
		 String role_query = "select Role from Role where RoleID IN (select RoleID from PortalRoles where PortalCD = (select PortalCD from client where ClientCD = 'LCAUS'))";
		    List<Map> list_Roles = DbUtil.getResultSetDb("Livecareer" , role_query);
	        return list_Roles;
	}*/
	
 }
