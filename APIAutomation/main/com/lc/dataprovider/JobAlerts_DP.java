package com.lc.dataprovider;

import org.testng.annotations.DataProvider;

import com.lc.constants.Constants_JobAlerts;

public class JobAlerts_DP {
	 @DataProvider(name = "getSourceCD")
	    public static Object[][] getSourceCD() {
		 String[][] dataset = null;
		 if(Constants_JobAlerts.environment.equalsIgnoreCase("test")){
			 System.out.println(Constants_JobAlerts.environment);
			 dataset = new String[][] {{"LCAUS" } , { "LCAUK" } , { "LCAFR" } , { "LCAIT" } , { "LCAES" } , { "LCIUS" } , { "MPRUS" } , { "MPRUK" } , { "MPRFR" } , { "MPRIT" } , { "MPRES" } , { "MPIUS" } /*, { "JTRSD" }*/ , { "JTRST" } /*, { "JTRSR" } , { "JTRSP" }*/ , { "RSMNA" } , { "CVRLN" } , { "RGWEB" } , { "RHWEB"} , { "QNTCR"}, {"JBFLG"} , {"RSBKT"}};
		 }
		 else{
			 System.out.println(Constants_JobAlerts.environment);
			 dataset = new String[][] {{"LCAUS" } , { "LCAUK" } , { "LCAFR" } , { "LCAIT" } , { "LCAES" } , { "LCIUS" } , { "MPRUS" } , { "MPRUK" } , { "MPRFR" } , { "MPRIT" } , { "MPRES" } , { "MPIUS" } /*, { "JTRSD" }*/ , { "JTRSR" } /*, { "JTRSR" } , { "JTRSP" }*/ , { "RSMNA" } , { "CVRLN" } , { "RGWEB" } , { "RHWEB"} , { "QNTCR"} , {"JBFLG"} , {"RSBKT"}};
		 }
		 return dataset;
	 }
	 
	 @DataProvider(name = "deleteData")
	    public static Object[][] deleteData() {
		String[] sourceCD = null;
		if(Constants_JobAlerts.environment.equalsIgnoreCase("test")){
			System.out.println("TEST........................");
			sourceCD = new String[] {"LCAUS"  ,  "LCAUK"  ,  "LCAFR"  ,  "LCAIT"  ,  "LCAES"  ,  "LCIUS"  ,  "MPRUS"  ,  "MPRUK"  ,  "MPRFR"  ,  "MPRIT"  ,  "MPRES"  ,  "MPIUS"  /*,  "JTRSD" */ ,  "JTRST"  /*,  "JTRSR"  ,  "JTRSP" */ ,  "RSMNA"  ,  "CVRLN"  ,  "RGWEB"  ,  "RHWEB" , "QNTCR", "JBFLG" , "RSBKT"};
		}
		else{
			System.out.println("SANDBOX........................");
			sourceCD = new String[] {"LCAUS"  ,  "LCAUK"  ,  "LCAFR"  ,  "LCAIT"  ,  "LCAES"  ,  "LCIUS"  ,  "MPRUS"  ,  "MPRUK"  ,  "MPRFR"  ,  "MPRIT"  ,  "MPRES"  ,  "MPIUS"  /*,  "JTRSD" */ ,  "JTRSR"  /*,  "JTRSR"  ,  "JTRSP" */ ,  "RSMNA"  ,  "CVRLN"  ,  "RGWEB"  ,  "RHWEB" , "QNTCR", "JBFLG" , "RSBKT"};
		}
		
		String[] type = {"HARD_BOUNCE"};
		String[][] dataset = new String[sourceCD.length * type.length][2];
		int index = 0;
		for(int i =0 ; i<sourceCD.length ; i++){
			for(int j=0 ; j<type.length ; j++){
				dataset[index][0] = sourceCD[i] ;
				dataset[index][1] = type[j] ;
				index++;
			}
		}
     return dataset;
	 }


}
