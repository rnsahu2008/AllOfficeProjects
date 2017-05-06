package src;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelDataIMport extends Base{

	static FileInputStream fi;
	static HSSFWorkbook w,w2 	;
	static HSSFSheet s;
	static HSSFRow row;
	public void fileopen() throws IOException
	{
		 fi = new FileInputStream("C:\\Users\\abhishek.bisht\\Desktop\\TestData.xls");	
		 w = new HSSFWorkbook(fi);	
		 s = w.getSheetAt(0);
		 row=s.getRow(0);
	}

//	public static void main(String[] args) throws IOException  {
//		// TODO Auto-generated method stub
//		ExcelDataIMport excel = new ExcelDataIMport();
//		excel.fileopen();
//		HashMap<String, Object[]> map = new HashMap<String, Object[]>();
//		w2 = new HSSFWorkbook();
//		HSSFSheet sheet = w2.createSheet("Sample sheet");
//		for(int i=0;i<=s.getPhysicalNumberOfRows();i++)
//		{
//			
//			for(int j=0;j<row.getLastCellNum();j++)
//			{
//				Object[] obj = new  
//				map.put(Integer.toString(j), row.getCell(j).getStringCellValue());
//				//array.add(row.getCell(j).getStringCellValue());
//				//fi.close();
//				 
//			}
//			Set<String> keyset = map.keySet();
//	        int rownum = 0;
//	        for (String key : keyset) 
//	        {
//	        HSSFRow row2 = sheet.createRow(rownum++);
//               // map.get(key);
//            int cellnum = 0;
//            //for (Object obj : objArr) {
//            HSSFCell cell = row.createCell(cellnum++);
//            cell.setCellValue(map.get(key));
//				//HSSFRow row2 = sheet.createRow(0);
////				HSSFCell cell=row2.createCell(j);
////				cell.setCellValue(row.getCell(j).getStringCellValue());
//				
//		
//	        }
//		}
//	    	
//			FileOutputStream fo = new FileOutputStream("C:\\Users\\abhishek.bisht\\Desktop\\test\\TestData2.xls");
//			
//			w2.write(fo);
//			fo.close();
//			excel.fileopen();

	}


