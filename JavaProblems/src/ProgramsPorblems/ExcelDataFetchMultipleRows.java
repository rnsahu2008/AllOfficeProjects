package ProgramsPorblems;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelDataFetchMultipleRows {
	
	public static void main(String[] args) throws IOException {
		
		
		
		File fl = new File("C:/Users/Ram.Sahu/Desktop/Book1.xlsx");
		
		FileInputStream fi = new FileInputStream(fl);	
		//FileInputStream fl1 =new InputStream(fl);
		XSSFWorkbook wb = new XSSFWorkbook(fi);
		XSSFSheet sh = wb.getSheetAt(0);
		
		Iterator<Row> rowIterator = sh.rowIterator();
		//System.out.println(rowIterator.hasNext());
		while(rowIterator.hasNext())
			
		{	
			Row row =rowIterator.next();
			Iterator<Cell> cellitr =row.cellIterator();
			while(cellitr.hasNext())
				
			{
				Cell cell =cellitr.next();
				
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
			
			
			
		}
			
		}

		
		
		
	}


