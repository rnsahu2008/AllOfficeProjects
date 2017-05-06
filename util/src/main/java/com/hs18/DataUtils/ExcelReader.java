package com.hs18.DataUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;

import com.hs18.util.Browser;
import com.hs18.util.Formaction;

public class ExcelReader {

	FileInputStream fi;
	FileOutputStream fo;

	HSSFSheet s;
	HSSFWorkbook w;
	HSSFWorkbook wo;

	public void testDataFile(String file) throws IOException {
		fi = new FileInputStream(file);

		w = new HSSFWorkbook(fi);
	}

	public String readFromColumn(String sheetname, int row, int column)
			throws IOException // To
	// read
	// It reads a particular column from a sheet
	{

		s = w.getSheet(sheetname);
		String data = null;
		HSSFRow r = s.getRow(row);
		if (r.getCell(column).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			r.getCell(column).setCellType(Cell.CELL_TYPE_STRING);
			data = r.getCell(column).getStringCellValue();
			data = data.trim();

		} else if (r.getCell(column).getCellType() == XSSFCell.CELL_TYPE_STRING) {
			data = r.getCell(column).getStringCellValue();
			data = data.trim();
			System.out.println(data);
		}

		return data;
	}

	// Write data in a particular column on the provided sheet no
	public boolean writeExcelFromWeb(ArrayList<String> data, String sheetname,
			int column,String file) throws IOException

	{
		try {
			HSSFSheet s = w.createSheet(sheetname);
			for(int i=0;i<data.size();i++)
			{
			
			HSSFRow r = s.createRow(i);
			Cell cell = r.createCell(column);
			cell.setCellValue(data.get(i));
			}
			fo = new FileOutputStream(file);
			w.write(fo);
			fo.close();
			return true;
		}

		catch (Exception e) {
			e.getMessage();
			return false;
		}
	}

	// Reads column from test data excel sheet
	public ArrayList<String> dataProviderByRow(String sheetname,
			String columnname) throws IOException {
		String str = null;
		int column = 0;
		s = w.getSheet(sheetname);
		ArrayList<String> data = new ArrayList<String>();
		HSSFRow r = s.getRow(0);
		for (int i = 0; i < r.getLastCellNum(); i++) {
			try {
				if (r.getCell(i).getStringCellValue().equals(columnname)) {
					column = i;
					break;
				}
			}

			catch (Exception e) {
				System.out.println(e.getMessage() + "   --NO such sheet exist");
			}
		}
		for (int i = 1; i < s.getPhysicalNumberOfRows(); i++) {
			HSSFRow row = s.getRow(i);

			try {

				if (row.getCell(column).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
					row.getCell(column).setCellType(Cell.CELL_TYPE_STRING);
					str = row.getCell(column).getStringCellValue();
					str = str.trim();
					data.add(str);

				} else if (row.getCell(column).getCellType() == XSSFCell.CELL_TYPE_STRING) {
					str = row.getCell(column).getStringCellValue();
					str = str.trim();
					data.add(str);
				}

			}

			catch (NullPointerException e) {

			}

		}
		return data;
	}

	public ArrayList<String> data(int j, String sheet) {
		s = w.getSheet(sheet);
		ArrayList<String> value = new ArrayList<String>();
		HSSFRow row = s.getRow(j);
		for (int i = 1; i < row.getLastCellNum(); i++) {
			try {

				String str = row.getCell(i).getStringCellValue();
				str = str.trim();
				if (!(str.equals(""))) {
					value.add(i + "%" + str);
				}

			} catch (NullPointerException e) {

			}

		}
		return value;
	}

	public String dateToStringSql(String date) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat resultformatter = new SimpleDateFormat("yyyy-MM-dd");
		Date datenew = formatter.parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(datenew);
		calendar.add(calendar.DATE, -1);
		Date reduceDate = calendar.getTime();
		String result = (resultformatter.format(reduceDate));
		return result;
	}

	public void closeFile() throws IOException // Closes the opened excel file.
	{
		fi.close();

	}
}
