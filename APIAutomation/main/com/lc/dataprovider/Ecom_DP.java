package com.lc.dataprovider;

import java.util.HashMap;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.lc.excel.ExcelReader;;

public class Ecom_DP 
{
	@DataProvider(name = "getSubscriptionData")
	public static Object[][] getSubscriptionData()
	{
		return getExcelDataRows("res/ecomdata/CreateSubscription.xlsx");
	}
	
	@DataProvider(name = "getSubscriptionWithoutCC")
	public static Object[][] getSubscriptionDataWithoutCC()
	{
		return getExcelDataRows("res/ecomdata/CreateSubscriptionWithoutCC.xlsx");
	}
	
	@DataProvider(name = "getCreditCardData")
	public static Object[][] getCreditCardData()
	{
		return getExcelDataRows("res/ecomdata/CreditCard.xlsx");
	}
	
	@DataProvider(name = "getCancelSubscriptionData")
	public static Object[][] getCancelSubscriptionData()
	{
		return getExcelDataRows("res/ecomdata/CancelSubscription.xlsx");
	}
	
	@DataProvider(name = "getDeactivateSubscriptionData")
	public static Object[][] getDeactivateSubscriptionData()
	{
		return getExcelDataRows("res/ecomdata/DeactivateSubscription.xlsx");
	}
	
	@DataProvider(name = "getOnePlanInvalidData")
	public static Object[][] getOnePlanInvalidData()
	{
		return getExcelDataRows("res/ecomdata/CreatePaymentOnePlan.xlsx");
	}
	
	@DataProvider(name = "getOnePlanInvalidCCData")
	public static Object[][] getOnePlanInvalidCCData()
	{
		return getExcelDataRows("res/ecomdata/CreditCardForPayment.xlsx");
	}
	
	@DataProvider(name = "getOnePlanOneSNGLInvalidData")
	public static Object[][] getOnePlanOneSNGLInvalidData()
	{
		return getExcelDataRows("res/ecomdata/CreatePaymentOnePlanOneSNGL.xlsx");
	}
	
	@DataProvider(name = "getSubscriptionWECDataWithoutCC")
	public static Object[][] getSubscriptionWECDataWithoutCC()
	{
		return getExcelDataRows("res/ecomdata/CSWECWithoutCC.xlsx");
	}
	
	@DataProvider(name = "csWithExistingCustomer")
	public static Object[][] csWithExistingCustomer()
	{
		return getExcelDataRows("res/ecomdata/CSWithExistingCustomer.xlsx");
	}
	
	@DataProvider(name = "lsChargeNowFalse")
	public static Object[][] linkedSubscriptionChargeNowFalse()
	{
		return getExcelDataRows("res/ecomdata/LSWithChargeNowFalse.xlsx");
	}
	
	@DataProvider(name = "lsChargeNowTrue")
	public static Object[][] linkedSubscriptionChargeNowTrue()
	{
		return getExcelDataRows("res/ecomdata/LSWithChargeNowTrue.xlsx");
	}
	
	@DataProvider(name = "refundSubscription")
	public static Object[][] refundSubscription()
	{
		return getExcelDataRows("res/ecomdata/RefundSubscription.xlsx");
	}
	
	@DataProvider(name = "cancelLinkedSubscription")
	public static Object[][] cancelLinkedSubscription()
	{
		return getExcelDataRows("res/ecomdata/CancelLinkedSubscription.xlsx");
	}
	
	@DataProvider(name = "creditCardLinkedSubscription")
	public static Object[][] creditCardLinkedSubscription()
	{
		return getExcelDataRows("res/ecomdata/CreditCardLinkedSubscription.xlsx");
	}
	
	@DataProvider(name = "linkedSubsWithoutCC")
	public static Object[][] linkedSubsWithoutCC()
	{
		return getExcelDataRows("res/ecomdata/LinkedSubsWithoutCC.xlsx");
	}
	
	/**
	* This method is used for creating array of excel data rows
	* @param excel sheet file name along with file path
	* @return array of excel data rows
	*/
	private static Object[][] getExcelDataRows(String fileNameWithPath)
	{
		int index = 0;
		List<HashMap<String, String>> data = ExcelReader.readExcel(fileNameWithPath);
		Object[][] dataSet = new Object[data.size()][];
		for(HashMap<String, String> list : data)
		{
			dataSet[index] = new Object[] {list};
			index++;
		}
		
		return dataSet;
	}
}
