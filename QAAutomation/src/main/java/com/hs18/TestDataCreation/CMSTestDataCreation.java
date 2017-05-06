package com.hs18.TestDataCreation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.hs18.DataUtils.DbConnection;
import com.hs18.DataUtils.ExcelReader;
import com.hs18.util.LocatorExcel;

public class CMSTestDataCreation extends LocatorExcel {
	Properties prop;
	ExcelReader read = new ExcelReader();
	ArrayList<String> caselist;

	@Test(description = "CMSData", dataProvider = "CMS")
	public void CMSDataCreation(ArrayList<String> suborderlist, String phoneno) throws IOException {
		caselist = new ArrayList<>();
		for (int i = 0; i < suborderlist.size(); i++) {			
			stateChange("2154", suborderlist.get(i));
			try{
			openUrl("http://app03.preprod.hs18.lan:8080/CaseManagementSystem/assets/ui/index.jsp");
			Thread.sleep(2000);
			if (!(title().equals("My Cases | HSN18 Case Management System"))) {
				type(locator("cmsUsername"), "ts_anu");
				type(locator("cmsPassword"), "demo");
				click(locator("cmsLoginButton"));
				waitForTitle("My Cases | HSN18 Case Management System");
			}
			openUrl("http://app03.preprod.hs18.lan:8080/CaseManagementSystem/assets/ui/index.jsp#caseDetails/PSIB/"
					+ phoneno);
			waitForTitle("CaseDetails | HSN18 Case Management System");
			fluentWaitForElement(By.xpath("//span[text()='" + phoneno + "']"));
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			type(locator("cmsHistoryTab"), suborderlist.get(i));
			fluentWaitForElement(By.xpath("(//span[text()='" + suborderlist.get(i) + "'])[1]"));
			click(By.xpath("(//span[text()='" + suborderlist.get(i) + "'])[1]"));
			fluentWaitForElement(By.xpath("(//span[text()='" + suborderlist.get(i) + "'])[2]"));
			click(locator("cmsProductTab"));
			fluentWaitForElement(locator("cmsDefective"));
			click(locator("cmsDefective"));
			fluentWaitForElement(locator("cmsTextArea"));
			type(locator("cmsTextArea"), "product");
			click(locator("cmsRegisterCase"));
			fluentWaitForElement(locator("cmsCaseIdLink"));
			caselist.add(verifyText(locator("cmsCaseIdLink")));
			fluentWaitForElement(locator("cmsProductRelatedLink"));
			// click(locator("cmsProductRelatedLink"));
			// dropdown(locator("cmsSelectCaseGroup"), "Service");
			// dropdown(locator("cmsSelectCaseFault"), "Damaged Product");
			// click(locator("cmsCaseProceedButton"));
			// fluentWaitForElement(locator("cmsCaseComment"));
			// type(locator("cmsCaseComment"), "RegisterCase");
			// moveToElement(locator("cmsCaseButton"));
			// click(locator("cmsCaseButton"));
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		String propfile = DbConnection.propfile;
		FileInputStream ip = new FileInputStream(propfile + "\\Data\\config.properties");
		prop = new Properties();
		prop.load(ip);
		String path = propfile + prop.getProperty("BulkorderOutputFile");

		read.writeExcel(caselist, "Suborder", 1, path, "CaseId");
	}

	public void stateChange(String state, String suborder) {
		openUrl("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/" + suborder + "/destination/" + state);

		while (getPageSource().contains("504") || !(getPageSource().contains("destination state"))) {
			openUrl("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/" + suborder + "/destination/" + state);
		}
	}

	@DataProvider(name = "CMS")
	public Object[][] dataProviderForY() throws IOException {
		read.testDataFile(file);
		Object[][] data = null;

		data = new Object[][] {
				{ read.dataProviderByRow("Suborder", "Suborder"), read.readFromColumn("BulkOrder", 1, 6) } };

		read.closeFile();
		return data;

	}
}
