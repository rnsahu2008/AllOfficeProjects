package src;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Base extends Browser {
	FileInputStream fi;
	FileOutputStream fo;

	HSSFSheet s;
	HSSFWorkbook w;

	public static String parent, child;

	// public void setAuthorInfoForReports() {
	// try
	// {
	// ATUReports.setAuthorInfo("Abhishek Bisht", Utils.getCurrentTime(),
	// "1.0");
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	//
	//
	//
	// public void setIndexPageDescription() {
	// ATUReports.indexPageDescription = "CEB Survey";
	// }
	public void type(By locator, String data) // To type data on the specified
												// locator
												// Created by - Abhishek Bisht
	{
try
{
		waitForElementVisible(locator);
		element(locator).clear();
		element(locator).sendKeys(data);
}

catch(Exception e)
{
	e.getMessage();
}
	}

	public String title() {
		String title = config.get(str).getTitle();
		return title;
	}

	public void click(By locator) // To click on a locator
	{
		waitForElementVisible(locator);
		try
		{
		element(locator).click(); // Created by - Abhishek Bisht
		}
		catch(Exception e)
		{
			e.getMessage();
		}

	}

	public void timeout() // Wait for page to load till the time specified
	{
		try // Created by - Abhishek Bisht
		{
			config.get(str).manage().timeouts()
					.pageLoadTimeout(35, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			e.printStackTrace();

		}
	}

	public void setClipboardData(String string) {
		StringSelection stringSelection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(stringSelection, null);
	}

	public void mouseOver(By locator) // For mouse hover functionality which use
										// to click or to navigate to sub menu

	{
		if (!(str.toLowerCase().equals("safari"))) {
			// Created by - Abhishek Bisht
			Actions actions = new Actions(config.get(str));
			actions.moveToElement(element(locator)).build().perform();
			actions.click();
		} else {

			// try
			// {
			// String mouseOverScript =
			// "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			// JavascriptExecutor js = (JavascriptExecutor) config.get(str);
			// js.executeScript(mouseOverScript, locator);
			//
			// }
			// catch (Exception e)
			// {
			//
			// }
		}
	}

	public void mouseoverWithoutClick(By locator) {
		// config.get(str).findElement(locator).
		Actions actions = new Actions(config.get(str));
		actions.moveToElement(element(locator)).build().perform();
	}

	public void dropdown(By locator, String text) // To select an option from
													// drop down
	{
		try {
			waitForElementVisible(locator);
			Select drop = new Select(element(locator));
			drop.selectByVisibleText(text);
		} catch (NoSuchElementException e) {
			throw e;
		}
	}

	public void dropdown(By locator, int i) {
		waitForElementVisible(locator);
		Select drop = new Select(element(locator));
		drop.selectByIndex(i);
	}

	public WebElement dropdownVerify(By locator, String option) {
		WebElement element = null;
		Select drop = new Select(element(locator));
		List<WebElement> options = drop.getOptions();
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).getText().equals(option)) {
				element = options.get(i);
			}
		}

		return element;

	}

	public void waitForTitle(String title) {
		WebDriverWait wait = new WebDriverWait(config.get(str), 50);
		wait.until(ExpectedConditions.titleIs(title));
	}

	public void waitForElementVisible(By locator) // Waits for an element to be
													// clickable
	{
		try {
			WebDriverWait wait = new WebDriverWait(config.get(str), 10);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(locator)));
		} catch (TimeoutException e) {
			throw new TimeoutException("Error message:  " + e.getMessage());
		}

	}

	public void waitforAlert() {
		WebDriverWait wait = new WebDriverWait(config.get(str), 30);
		wait.until(ExpectedConditions.alertIsPresent());
	}

	public By link(String linkText) // returns locator for link type
	{

		return By.linkText(linkText);
	}

	public void javaScript(String script) {
		JavascriptExecutor jse = (JavascriptExecutor) config.get(str);
		jse.executeScript(script);
	}

	public void loadjscssfile(String fileUrl) {
		String script = "var fileref=document.createElement(\"script\"); "
				+ "fileref.setAttribute(\"type\",\"text/javascript\"); "
				+ "fileref.setAttribute(\"src\"," + fileUrl + ");";
		javaScript(script);
	}

	public void switchWindow(int sec) throws InterruptedException // Switch to
																	// newly
																	// opened
																	// window
	{
		try {
			Thread.sleep(sec);
			parent = config.get(str).getWindowHandle();
			Set<String> str1 = config.get(str).getWindowHandles();
			Iterator<String> it = str1.iterator();
			while (it.hasNext()) {
				child = it.next();
				// System.out.println(child);
				config.get(str).switchTo().window(child);
			}
			if (!(str.toLowerCase().equals("safari"))) {
				config.get(str).manage().timeouts()
						.pageLoadTimeout(15, TimeUnit.SECONDS);
			}
		} catch (TimeoutException e) {
			e.printStackTrace();

		}

	}

	public void reloadPage() {
		String s = config.get(str).getCurrentUrl();
		config.get(str).get(s);

	}

	public void switchParent(int sec) throws InterruptedException // Switches
																	// back to
																	// original
																	// window
	{
		try {
			Thread.sleep(sec);
			config.get(str).switchTo().window(parent);
			if (!(str.toLowerCase().equals("safari"))) {
				config.get(str).manage().timeouts()
						.pageLoadTimeout(50, TimeUnit.SECONDS);
			}
		} catch (TimeoutException e) {
			e.printStackTrace();

		}

	}

	public String getUrl() {
		String url = config.get(str).getCurrentUrl();
		return url;
	}

	public void switchnestedFrame(String id) {
		List<WebElement> elements = new ArrayList<WebElement>();
		elements = config.get(str).findElements(By.tagName("iframe"));

		for (int i = 0; i < elements.size(); i++)

		{
			WebElement el = elements.get(i);

			if (el.getAttribute("id").equals(id)) {

				config.get(str).switchTo().frame(elements.get(i));

			} else if (el.getAttribute("name").equals(id)) {
				config.get(str).switchTo().frame(elements.get(i));
			}
		}

	}

	public void switchtoFrame() {
		WebElement iFrame = config.get(str).findElement(By.tagName("iframe"));
		config.get(str).switchTo().frame(iFrame);
	}

	public void switchtoInnerFrame(String s) {
		config.get(str).switchTo().frame(s);
	}

	public void switchfromFrame(String window) {
		config.get(str).switchTo().window(window);
	}

	public void alertAccept() // Accepts alert present on page
	{
		Alert alert = config.get(str).switchTo().alert();
		alert.accept();
	}

	public void alertReject() throws IOException // Rejects alert present on
													// page
	{
		Alert alert = config.get(str).switchTo().alert();
		alert.dismiss();
	}

	public String alerttext() {
		Alert alert = config.get(str).switchTo().alert();
		return alert.getText(); // Returns text present on alert
	}

	public void ImplicitWait(int i) // waits for a locator to be present
	{

		config.get(str).manage().timeouts()
				.pageLoadTimeout(i, TimeUnit.SECONDS);
	}

	public String errorMessage(By locator) throws IOException // Read and save
																// text present
																// on a locator
	{
		try {
			assertElementPresent(locator);
			String message = config.get(str).findElement(locator).getText();
			return message;
		} catch (NoSuchElementException e) {
			throw e;
		}
	}

	public boolean assertElementPresent(By locator) // verifies that locator is
													// present
	{
		Assert.assertNotNull(locator, "Element Is present");
		return true;
	}

	public void waitForText(By locator, String text) // Waits for a text to
														// present on the
														// specified locator
	{
		WebDriverWait wait = new WebDriverWait(config.get(str), 180);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(locator,
				text));
		// System.out.println(config.get(str).findElement(locator).getText());
	}

	public void testDataFile(String file) throws IOException {
		fi = new FileInputStream(file);

		w = new HSSFWorkbook(fi);
	}

	public String testData(int st, int r, int c) throws IOException // To read
																	// any
																	// specific
																	// String
																	// type
																	// column
																	// data from
																	// excel
																	// file.
	{
		testDataFile(File);
		s = w.getSheetAt(st);

		HSSFRow row = s.getRow(r);
		HSSFCell cell = row.getCell(c);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		String data = cell.getStringCellValue();
		// data=String.valueOf(Double.valueOf(data).longValue());
		System.out.println(data);
		closeFile();
		return data;
	}

	public double testDataInt(int st, int r, int c) // To read any specific int
													// type column data from
													// excel file.
	{
		s = w.getSheetAt(st);

		HSSFRow row = s.getRow(r);
		HSSFCell cell = row.getCell(c);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		double data = cell.getNumericCellValue();
		System.out.println(data);
		return data;
	}

	public boolean writeExcel(String data, String sheetname, int row, int column)
			throws IOException

	{
		try {

			HSSFSheet s = w.getSheet(sheetname);
			HSSFCell cell = s.getRow(row).getCell(column);
			cell.setCellValue(data);
			closeFile();
			fo = new FileOutputStream(File);
			w.write(fo);
			fo.close();
			return true;
		}

		catch (Exception e) {
			return false;
		}
	}

	

	public void closeFile() throws IOException // Closes the opened excel file.
	{
		fi.close();

	}

	public void ifElementPresent(By locator) // Waits till an element is visible
	{
		WebDriverWait wait = new WebDriverWait(config.get(str), 25);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

	}

	public void waitForElementtoInvisible(By locator) // Waits till an element
														// get invisible
	{
		WebDriverWait wait = new WebDriverWait(config.get(str), 50);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public void waitForTextToInvisible(By locator, String text) {
		WebDriverWait wait = new WebDriverWait(config.get(str), 90);
		wait.until(ExpectedConditions.invisibilityOfElementWithText(locator,
				text));
	}

	public void browserStop() throws AWTException {
		// JavascriptExecutor js = (JavascriptExecutor) config.get(str);
		// js.executeScript("return window.stop");

		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ESCAPE);
		robot.keyRelease(KeyEvent.VK_ESCAPE);

	}

	public WebElement element(By locator) // returns Webelement
	{
		WebElement element;
		waitForElementVisible(locator);
		element = config.get(str).findElement(locator);

		return element;
	}

	public List<WebElement> elements(By locator) // Returns list of webelements
													// for the specified locator
	{
		List<WebElement> elements = config.get(str).findElements(locator);

		return elements;
	}

	public WebElement elementInTable(By locator, String tag1, String tag2,
			String searchitem) // Returns Webelement from a table of elements
	{

		WebElement element = element(locator);
		WebElement elementfound = null;
		List<WebElement> list = element.findElements(By.tagName(tag1));
		Iterator<WebElement> it = list.iterator();
		while (it.hasNext()) {
			WebElement tr = it.next();
			List<WebElement> tds = tr.findElements(By.tagName(tag2));
			Iterator<WebElement> i = tds.iterator();
			while (i.hasNext()) {
				WebElement td = i.next();
				String LabelText = td.getText();

				if (LabelText.contains(searchitem)) {
					elementfound = td;
					break;
				}

			}
		}
		return elementfound;
	}

	public int rowCount(int st) {
		int totalrow = 0;
		s = w.getSheetAt(st);
		totalrow = s.getPhysicalNumberOfRows();
		return totalrow;
	}

	public ArrayList<String> dataProviderByRow(int st, int a, int b, int column)
			throws IOException // Reads column from test data excel sheet
	{
		String str = null;
		s = w.getSheetAt(st);
		ArrayList<String> data = new ArrayList<String>();
		int totalrow = rowCount(st);
		for (int i = 1; i < totalrow; i++) {
			HSSFRow row = s.getRow(i);

			try {

				if (row.getCell(column).getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
					row.getCell(column).setCellType(Cell.CELL_TYPE_STRING);
					str = row.getCell(column).getStringCellValue();
					str = str.trim();

					data.add(str);

				} else  {
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

	public void verifyList(ArrayList<String> list, ArrayList<String> array) {
		for (int i = 0; i < list.size(); i++) {
			Assert.assertNotNull(list.get(i));
			Assert.assertNotNull(array.get(i));
			Assert.assertEquals(list.get(i), array.get(i));

		}
	}

	public String dateToStringSql(String date) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat resultformatter = new SimpleDateFormat("yyyy-MM-dd");
		Date datenew = formatter.parse(date);
		// System.out.println(formatter.format(datenew));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(datenew);
		calendar.add(calendar.DATE, -1);
		Date reduceDate = calendar.getTime();
		String result = (resultformatter.format(reduceDate));
		return result;
	}

}
