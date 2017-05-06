package com.sel.casepanelTests;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.hs18.lib.ReadExcel;

public class Test1 {

	@Test
	public void testdataprovider() throws Exception
	{
	
		ReadExcel re = new ReadExcel("src/test/resources/Testdata.xls");

		 HashMap[][] hmdouble=	 re.getTableToHashMapDoubleArray();
		 
		 for (HashMap[] hashMaps : hmdouble) {
		
			 System.out.println("");
		}
	}
}
