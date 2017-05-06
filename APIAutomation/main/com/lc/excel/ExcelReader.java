package com.lc.excel;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	
	public static List<HashMap<String, String>> readExcel(String path)
    {
		
		List<HashMap<String, String>> allData = new ArrayList<HashMap<String, String>>();
        try
        {
            FileInputStream file = new FileInputStream(new File(System.getProperty("user.dir") + File.separator + path));
            System.out.println(System.getProperty("user.dir") + File.separator + path);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            for (int testCase = 1; testCase <= sheet.getLastRowNum(); testCase++) {
            	HashMap<String, String> rowData = new HashMap<String, String>();
            	int c = 0;
            	Row headerRow = sheet.getRow(0);
                Row row = sheet.getRow(testCase);
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                Iterator<Cell> firstRowIterator = headerRow.cellIterator();
                while (firstRowIterator.hasNext())
                {
                    Cell cell = row.getCell(c);//cellIterator.next();

                    Cell cell1 = firstRowIterator.next();
                    if(cell==null){
                    	rowData.put(cell1.getStringCellValue() , "");
                    	c++;
                    	continue;
                    }
                    //Check the cell type and format accordingly
                    switch (cell.getCellType())
                    {
                        case Cell.CELL_TYPE_NUMERIC:
                        	 NumberFormat fmt = new DecimalFormat("##.##########");
                             String value = fmt.format(cell.getNumericCellValue()).toString();
                        	rowData.put(cell1.getStringCellValue().trim() , value.trim());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            //System.out.print(cell.getStringCellValue() + "t");
                            rowData.put(cell1.getStringCellValue().trim() , cell.getStringCellValue().trim());
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            //System.out.print(cell.getStringCellValue() + "t");
                            rowData.put(cell1.getStringCellValue().trim() , String.valueOf(cell.getBooleanCellValue()));
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            value = "";
                            break;
                        default:
                            value = cell.getStringCellValue();
                            rowData.put(cell1.getStringCellValue().trim() , cell.getStringCellValue().trim());
                            break;
                    }
                    c++;
                }
                allData.add(rowData);
                //System.out.println("");
            }
            //System.out.println(allData.size());
            file.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		return allData;
    }

}
