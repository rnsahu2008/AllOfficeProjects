package SeleniumPackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Parameters;

import com.beust.jcommander.Parameter;


public class Excelhandle {


	@Parameters({ "filepath" })
public  void fetchCelldatafromexcel(String sheetname, int rownum, int colnum) throws IOException
	
	{		
		String s ="C:/Users/Ram.Sahu//Desktop/Book1.xlsx";
		File fl = new File(s);
		FileInputStream fi = new FileInputStream(fl);	
		XSSFWorkbook wb = new XSSFWorkbook(fi);
		XSSFSheet sh = wb.getSheet(sheetname);
        XSSFRow row = sh.getRow(rownum);
        XSSFCell cell = row.getCell(colnum);

			switch(cell.getCellType())
			{
			case Cell.CELL_TYPE_NUMERIC:
			{
				System.out.println(cell.getNumericCellValue());
				break;
			}
			case Cell.CELL_TYPE_STRING:
			{
				System.out.println(cell.getStringCellValue());
				break;
			}
			case Cell.CELL_TYPE_BOOLEAN:
			{
				System.out.println(cell.getBooleanCellValue());
				break;
			}
			}
				
			}
			
		
	public static void main(String[] args) throws IOException {
		Excelhandle ehan = new Excelhandle();
		ehan.fetchCelldatafromexcel("Sheet1", 0, 1);
		ehan.fetchCelldatafromexcel("Sheet1", 1, 2);
		
	
}	
	
	public void testDataFile(String file) throws IOException {
		FileInputStream fi = new FileInputStream(file);
	}

	
			
}
			
		

