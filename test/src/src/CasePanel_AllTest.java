package src;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class CasePanel_AllTest extends Base {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void login() throws IOException
	{
		type(By.id("_id0:_id2"), testData(0,1, 1));
		type(By.name("_id0:_id3"), testData(0,1, 2));
		click(By.id("_id0:_id4"));
		mouseOver(By.xpath("(//table/tbody/tr/td[1]/a)[3]"));
		click(By.xpath("//font[text()='Search By Options']"));

	}
	
	@Test(dataProvider="case")
	public void casePanel(ArrayList<String> suborder) throws IOException, InterruptedException
	{
		for(int i=0;i<suborder.size();i++)
		{
		click(By.xpath("(//input[@value='SUBORDER'])[2]"));
		type(By.id("searchOrderForm:criteria"), suborder.get(i));
		click(By.id("searchOrderForm:submitButton"));
		click(By.xpath("//table[@id='myLocator:case']/tbody/tr/td[1]/a"));
		switchWindow(2);
		driver.manage().window().maximize();
		waitForElementVisible(By.id("myForm:edit"));
		driver.findElement(By.id("myForm:edit")).click();
	//	if(element(By.xpath("//table[@id='myLocator:case']/tbody/tr/td[1]/a")).getText().contains("New"))
			if(element(By.id("myForm:activityType")).getAttribute("value").contains("Default Digital Troubleshoot"))
		{				
		dropdown(By.id("myForm:nextActivityStatus"), i+1);
		//dropdown(By.id("myForm:nextActivityStatus"),"Refund");
		type(By.id("myForm:activityRemarks"), "test comment");
		if(element(By.id("myForm:nextActivityStatus")).getAttribute("value").equals("Refund")||element(By.id("myForm:nextActivityStatus")).getAttribute("value").equals("Replacement"))
		{
			
		dropdown(By.id("myForm:nextCaseStatus"), "Complete Defective Product");
		}
		click(By.id("myForm:updte"));
		}
		driver.close();
		switchParent(2);
		driver.get("http://app10.preprod.hs18.lan:8030/casehandling/faces/oms/searchOrder.jsp");
		}	
	}

	@DataProvider(name = "case")                                   // Data provider for log in 
	public Object[][] dataProviderLogin() throws IOException
	{
		testDataFile(File);
		Object[][] data = null;
		
		 data= new Object[][]{{dataProviderByRow(1, 0,10, 0)}};
		
		closeFile();
		return data;
		
	}
}
