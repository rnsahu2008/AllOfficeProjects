package src;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GCFreebieTest extends Base {
	String newid=null;
	int gc100=0,inactive=0;
	int gc200=0,gc300=0,gc500=0;
  @Test(dataProvider="gc")
  public void gcFreebieCheck(ArrayList<String> productid , ArrayList<String> gcworth) 
  {
	  ArrayList<String> noresult = new ArrayList<String>();
	  for(int i=506;i<productid.size();i++)
		{
		
		  
		 // newid=productid.get(i).replace(".", "");
		  try
			 {
			  driver.manage().timeouts().pageLoadTimeout(6, java.util.concurrent.TimeUnit.SECONDS);
			  driver.get("homeshop18.com");
			  String  str = driver.findElement(By.xpath("//a[@class='view-more']")).getCssValue("font-weight");
			  System.out.println(str);
			  String st = driver.findElement(By.xpath("//a[@class='view-more']")).getCssValue("color");
			  System.out.println(st);
	//	  driver.get("http://www.homeshop18.com/la-casa-101-pc-dinner-set/home-kitchen/dining/product:"+productid.get(i)+"/cid:15819/?pos=1");
		  type(By.id("q"), productid.get(i));
		  click(By.id("searchgobutton"));
		 
		
		 }
		 
		 catch(Exception e)
		 {
			
		 }
		  try{
//		  Assert.assertEquals(gcworth.get(i), elementInTable(By.xpath("//table[@id='freebieTable']/tbody"), "tr", "td", gcworth.get(i)).getText());
		  
		  if(elementInTable(By.xpath("//table[@id='freebieTable']/tbody"), "tr", "td", gcworth.get(i)).getText().contains(gcworth.get(i)))
		  {
			 if( elementInTable(By.xpath("//table[@id='freebieTable']/tbody"), "tr", "td", gcworth.get(i)).getText().contains("100"))
			 {
				 gc100=gc100+1;
			 }
			 else if( elementInTable(By.xpath("//table[@id='freebieTable']/tbody"), "tr", "td", gcworth.get(i)).getText().contains("200"))
			 {
				 gc200=gc200+1;
			 }
			 else if( elementInTable(By.xpath("//table[@id='freebieTable']/tbody"), "tr", "td", gcworth.get(i)).getText().contains("300"))
			 {
				 gc300=gc300+1;
			 }
			 else if( elementInTable(By.xpath("//table[@id='freebieTable']/tbody"), "tr", "td", gcworth.get(i)).getText().contains("500"))
			 {
				 gc500=gc500+1;
			 }
		  }
		  
		  else
		  {
			  System.out.println("No result "+productid.get(i));
			  noresult.add(productid.get(i));
		  }
		}
		
		  catch(NoSuchElementException e)
		  {
			  inactive=inactive+1;
			  
		  }
		catch(Exception e)
		{
			System.out.println("No result "+productid.get(i));
			  noresult.add(productid.get(i));
		}
		}
	  System.out.println(noresult);
	  System.out.println(gc100 + "- total 100");
	  System.out.println(gc200 + "- total 200");
	  System.out.println(gc300 + "- total 300");
	  System.out.println(gc500 + "- total 500");
	  System.out.println(inactive + "- total inactive");
  }
  
  @DataProvider(name = "gc")                                   // Data provider for log in 
	public Object[][] dataProviderfreebie() throws IOException
	{
	  testDataFile(File);
		Object[][] data = null;
		
		 data= new Object[][]{{dataProviderByRow(2, 1,10, 0),dataProviderByRow(2, 1,10, 2)}};
		
		closeFile();
		return data;
	}
}
