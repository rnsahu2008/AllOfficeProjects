package utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	FileInputStream fi;
	FileOutputStream fo;
	XSSFSheet s;
	XSSFWorkbook w;
	XSSFWorkbook wo;
	Properties prop;
	
/**
 * Load file path 
 * @param file
 * @throws IOException
 */
	public void testDataFile(String file) throws IOException {
		//InputStream stream=this.getClass().getClassLoader().getResourceAsStream(file);
	fi = new FileInputStream(file);
		w = new XSSFWorkbook(fi);
	}

	/**
	 * Returns data present in specified sheet on teh basis of row no and column no
	 * @param sheetname
	 * @param row
	 * @param column
	 * @return
	 * @throws IOException
	 */
	public String readFromColumn(String sheetname, int row, int column)
			throws IOException // To
	
	{

		s = w.getSheet(sheetname);
		String data = null;
		XSSFRow r = s.getRow(row);
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



	/**
	 * Returns complete list of data from a column name
	 * @param sheetname
	 * @param columnname
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> dataProviderByRow(String sheetname,
			String columnname) throws IOException {
		String str = null;
		int column = 0;
		s = w.getSheet(sheetname);
		ArrayList<String> data = new ArrayList<String>();
		XSSFRow r = s.getRow(0);
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
			XSSFRow row = s.getRow(i);

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

	/**
	 * Returns list of element on a specific row no
	 * @param j
	 * @param sheet
	 * @return
	 */
	public ArrayList<String> data(int j, String sheet) {
		s = w.getSheet(sheet);
		ArrayList<String> value = new ArrayList<String>();
		XSSFRow row = s.getRow(j);
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

	
	public void closeFile() throws IOException // Closes the opened excel file.
	{
		fi.close();

	}
}
