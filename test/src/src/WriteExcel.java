package src;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;




public class WriteExcel extends Base{

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub	
		WriteExcel test = new WriteExcel();
		test.writeexcel(0);
	}

	public void writeexcel(int st) throws IOException
	{
		HSSFWorkbook workbook = new HSSFWorkbook(); 
	      //Create a blank sheet
	      HSSFSheet spreadsheet = workbook.createSheet( 
	      " productdata ");
	      
	      
		ArrayList<String> data = new ArrayList<String>();
		
		testDataFile("E:\\jmeter data\\jmeterproductjava.xls");
		data = dataProviderByRow(st, 0, 0, 1);
		HSSFSheet s =  w.getSheetAt(st);
		HSSFCell cell;
		HSSFRow row;
		int k=0;
		int a=0;
		for(int i=0;i<s.getPhysicalNumberOfRows()/5;i++)
		{
			row = spreadsheet.createRow(k);
			
			
			for(int j=0;j<5;j++)
			{
				String str = data.get(a);
				cell=row.createCell(j);
				cell.setCellValue(str);
				a=a+1;
				
			}
			k=k+1;
			
		}
		closeFile();
		fo = new FileOutputStream("E:\\jmeter data\\result.xls");
		workbook.write(fo);
		fo.close();
		
	}
}
