package src;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class StatusChange {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver","E:\\Demo_Framework\\Driver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		String suborder =null;
		FileInputStream fi = new FileInputStream("E:\\Test xls\\suborder.xls");	
		HSSFWorkbook w = new HSSFWorkbook(fi);	
		HSSFSheet s = w.getSheetAt(2);
		//ArrayList<String> data = new ArrayList<String>();
		for (int i=0 ; i<s.getPhysicalNumberOfRows();i++)
		{
		HSSFRow row=s.getRow(i);			
		
		 if(row.getCell(0).getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
			 row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				 suborder = row.getCell(0).getStringCellValue();	
				suborder = suborder.trim();
				System.out.println(suborder);
				//JDBCConnection con = new JDBCConnection();
//				int state = con.checkSuborderState(suborder);
//				if(state==2109||state==2130||state==2169||state==2140||state==2141||state==2107)
				{
					driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"+suborder+"/destination/2176");
					while(driver.getPageSource().contains("504")||!(driver.getPageSource()).contains("destination state"))
					{
						driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"+suborder+"/destination/2176");
					}
					//WebDriverWait wait = new WebDriverWait(driver, 50);
					//wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.tagName("//body")), "Suborder  changed to  destination state "));
				}
				
				
			}
		
	}
		
		driver.quit();

}}
