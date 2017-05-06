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

public class ManifestScan {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
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
		driver.findElement(By.name("TextBox8")).sendKeys("3");
		driver.findElement(By.name("Command10")).click();
		driver.findElement(By.name("TextBox5")).sendKeys("1");
		driver.findElement(By.name("Command7")).click();
		driver.findElement(By.name("TextBox2")).sendKeys("MNIDAX60036");
		driver.findElement(By.name("Command4")).click();
		if(driver.findElements(By.name("Command5")).size()>0)
		{
		driver.findElement(By.name("Command5")).click();
		}
		FileInputStream fi = new FileInputStream("E:\\Test xls\\suborder.xls");	
		HSSFWorkbook w = new HSSFWorkbook(fi);	
		HSSFSheet s = w.getSheetAt(0);
		//ArrayList<String> data = new ArrayList<String>();
		for (int i=0 ; i<529;i++)
		{
		HSSFRow row=s.getRow(i);			
		String str = null;
		
		 if(row.getCell(0).getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
			   row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				 str = row.getCell(0).getStringCellValue();	
				str = str.trim();
				
			}
			else if(row.getCell(0).getCellType() == XSSFCell.CELL_TYPE_STRING)
			{
				str = row.getCell(0).getStringCellValue();	
				str = str.trim();				
			} 
		
		
		driver.findElement(By.name("TextBox4")).sendKeys(str);
		driver.findElement(By.name("Command8")).click();
		//Thread.sleep(500);
		if(driver.getPageSource().contains("MESSAGE:"))
		{
			driver.findElement(By.name("Command6")).click();
		}
		
		
	       
	    }

	}
}


