package src;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PackingScan {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		int j = 1,l=7;
		
		System.setProperty("webdriver.chrome.driver","E:\\Demo_Framework\\Driver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("http://10.50.33.12/ebizrf/");
		driver.findElement(By.name("TextBox2")).sendKeys("1");
		driver.findElement(By.name("TextBox4")).sendKeys("ebizuser1");
		driver.findElement(By.name("TextBox6")).sendKeys("ebiznet");
		driver.findElement(By.name("TextBox8")).sendKeys("1");
		driver.findElement(By.name("TextBox10")).sendKeys("DRH");
		driver.findElement(By.name("TextBox12")).sendKeys("NITCDRH");
		driver.findElement(By.name("Command14")).click();
		driver.findElement(By.name("TextBox8")).sendKeys("5");
		driver.findElement(By.name("Command10")).click();
		FileInputStream fi = new FileInputStream("E:\\Test xls\\suborder.xls");	
		HSSFWorkbook w = new HSSFWorkbook(fi);	
		HSSFSheet s = w.getSheetAt(1);
		for (int i=1 ; i<s.getPhysicalNumberOfRows();i++)
		{
		HSSFRow rowOrder=s.getRow(i);	
		HSSFRow rowCluster=s.getRow(j);			
		String cluster = null;
		String orderno = null;
		String sku=null;
		if(driver.getPageSource().contains("CLUSTER NO"))
		{
		 if(rowCluster.getCell(0).getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
			 rowCluster.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				 cluster = rowCluster.getCell(0).getStringCellValue();	
				cluster = cluster.trim();
				
			}

		 driver.findElement(By.name("TextBox2")).sendKeys(cluster);
		 driver.findElement(By.name("Command4")).click();
		 j=j++;
		}
		 //for suborders
		 if(rowOrder.getCell(1).getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
			 rowOrder.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
			 orderno = rowOrder.getCell(1).getStringCellValue();	
			 orderno = orderno.trim();
				
			}
		 driver.findElement(By.name("TextBox2")).sendKeys(orderno);
		 driver.findElement(By.name("Command4")).click();
		// Thread.sleep(1000);
		 if(driver.getPageSource().contains("ALREADY PACK"))
		 {
			 driver.findElement(By.name("Command6")).click();
		 }
		 //for Sku
		 else
		 {
		 for(int k=1;k<=2;k++)
		 {
			 HSSFRow rowSku = s.getRow(k);
			 
				 rowSku.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
				 sku = rowSku.getCell(2).getStringCellValue();	
				 sku = sku.trim();
					
				
			 driver.findElement(By.name("TextBox2")).sendKeys(sku);
			 driver.findElement(By.name("Command4")).click();
			 if(driver.getPageSource().contains("SCAN IMEI#"))
			 {
				 l=l+1;
				 driver.findElement(By.name("TextBox2")).sendKeys("52641235622997"+l);
				 driver.findElement(By.name("Command4")).click();
			 }
		 }
		 Thread.sleep(1000);
		 if(!(driver.findElements(By.name("Command5")).size()==0))
		 {
			 driver.findElement(By.name("Command5")).click();
		 }
		 }
		 
	}
//Need to work on later
}
}