package com.hs18.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
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

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Action extends Browser {

	public static String parent, child;

	public void openUrl(String url) {
		getDriver().get(url);
	}

	public void type(By locator, String data) // To type data on the specified
												// locator

	{

		waitForElementVisible(locator);
		element(locator).clear();
		element(locator).sendKeys(data);

	}

	public void typeEnter(By locator, String data)

	{
		waitForElementVisible(locator);
		element(locator).clear();
		element(locator).sendKeys(data );
		getappium().tap(1, 993, 1690, 1);
	}

	public String title() {
		String title = getDriver().getTitle();
		return title;
	}

	public void click(By locator) // To click on a locatoraf
	{
		waitForElementVisible(locator);
		element(locator).click();

	}

	public void tab(By locator) {
		getappium().tap(1, element(locator), 1);
	}

	public void scrollTo(String element) {
		getappium().scrollTo(element);
	}

	public void timeout() // Wait for page to load till the time specified
	{
		try {
			getDriver().manage().timeouts()
					.pageLoadTimeout(35, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			e.printStackTrace();

		}
	}

	public void keyBoardEvent(By locator , Keys event)
	{
		type(locator, "");
		element(locator).sendKeys(event);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void keyboardAction( int keycode) throws AWTException
	{
		Robot robot = new Robot();
		robot.keyPress(keycode);
		robot.keyRelease(keycode);
	}
	
	public void setClipboardData(String string) {
		StringSelection stringSelection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(stringSelection, null);
	}

	public void mouseOverClick(By locator) // For mouse hover functionality
											// which can click or to navigate to
											// sub menu

	{
//		if (!(Formaction.Browser.toLowerCase().equals("safari"))) {
			// Created by - Abhishek Bisht
		{	Actions actions = new Actions(getDriver());
			actions.moveToElement(element(locator)).build().perform();
			actions.click();
		} 
	}

	public void moveToElement(By locator) {

		Actions actions = new Actions(getDriver());
		actions.moveToElement(element(locator)).perform();
	}

	public void dropdown(By locator, String text) // To select a text option
													// from drop down
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

	// Returns dropdown element which matches with the provided input
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
		WebDriverWait wait = new WebDriverWait(getDriver(), 50);
		wait.until(ExpectedConditions.titleIs(title));
	}

	public void waitForElementVisible(By locator) // Waits for an element to be
													// clickable
	{
		WebDriverWait wait;
		try {
			if (getUserData("appName").toLowerCase().equals("mobile")) {
				wait = new WebDriverWait(getappium(), 50);
			} else {
				wait = new WebDriverWait(getDriver(), 50);
			}

			wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (TimeoutException e) {
			throw new TimeoutException("Error message:  " + e.getMessage());
		}

	}
	
	public void doubleClick(By locator)
	{
		Actions actions = new Actions(getDriver());
		actions.doubleClick(element(locator));
	}

	public void waitforAlert() {
		WebDriverWait wait = new WebDriverWait(getDriver(), 15);
		wait.until(ExpectedConditions.alertIsPresent());
	}

	public void javaScript(String script) {
		JavascriptExecutor jse = (JavascriptExecutor) getDriver();
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
			parent = getDriver().getWindowHandle();
			Set<String> str1 = getDriver().getWindowHandles();
			Iterator<String> it = str1.iterator();
			while (it.hasNext()) {
				child = it.next();

				getDriver().switchTo().window(child);
			}
//			if (!(userdata.get(appName).toLowerCase().equals("safari"))) {
//				getDriver().manage().timeouts()
//						.pageLoadTimeout(15, TimeUnit.SECONDS);
//			}
		} catch (TimeoutException e) {
			e.printStackTrace();

		}

	}

	public void reloadPage() {
		String s = getDriver().getCurrentUrl();
		getDriver().get(s);

	}

	// Switch back to original window
	public void switchParent(int waitinsec) throws InterruptedException {
		try {
			Thread.sleep(waitinsec);
			getDriver().switchTo().window(parent);
			if (!(userdata.get("Browser").toLowerCase().equals("safari"))) {
				getDriver().manage().timeouts()
						.pageLoadTimeout(50, TimeUnit.SECONDS);
			}
		} catch (TimeoutException e) {
			e.printStackTrace();

		}

	}

	public String getUrl() {
		String url = getDriver().getCurrentUrl();
		return url;
	}

	public void switchtoFrame(By locator) {

		getDriver().switchTo().frame(element(locator));
	}

	public void switchtoInnerFrame(String s) {
		getDriver().switchTo().frame(s);
	}

	public void switchfromFrame() {
		getDriver().switchTo().defaultContent();
	}

	public void alertAccept() // Accepts alert present on page
	{
		Alert alert = getDriver().switchTo().alert();
		alert.accept();
	}

	public void alertReject() throws IOException {
		Alert alert = getDriver().switchTo().alert();
		alert.dismiss();
	}

	public String alerText() { // Returns text present on alert
		Alert alert = getDriver().switchTo().alert();
		return alert.getText();
	}

	public void ImplicitWait(int i) // waits for a locator to be present
	{

		getDriver().manage().timeouts().pageLoadTimeout(i, TimeUnit.SECONDS);
	}

	public String verifyText(By locator) throws IOException // Returns text
															// present on
															// selement
	{
		try {
			waitForElementVisible(locator);
			String message = element(locator).getText();
			return message;
		} catch (NoSuchElementException e) {
			throw e;
		}
	}

	public void checkbox(By locator) {

		if (element(locator).getAttribute("checked") == null) {
			click(locator);
		}

	}

	public boolean assertElementPresent(By locator) // verifies that element is
													// present

	{
		Assert.assertNotNull(locator, "Element Is present");
		return true;
	}

	public void waitForText(By locator, String text) // Wait for text
	{
		WebDriverWait wait = new WebDriverWait(getDriver(), 15);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(locator,
				text));
	}

	public void ifElementPresent(By locator) // Waits till an element is visible
	{
		WebDriverWait wait = new WebDriverWait(getDriver(), 25);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

	}

	public void waitForElementtoInvisible(By locator) // Waits till an element
														// gets invisible

	{
		WebDriverWait wait = new WebDriverWait(getDriver(), 50);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public void waitForTextToInvisible(By locator, String text) {
		WebDriverWait wait = new WebDriverWait(getDriver(), 90);
		wait.until(ExpectedConditions.invisibilityOfElementWithText(locator,
				text));
	}

	public void browserStop() throws AWTException {

		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ESCAPE);
		robot.keyRelease(KeyEvent.VK_ESCAPE);

	}

	public WebElement element(By locator) // returns Webelement
	{
		WebElement element;
		if (getUserData("appName").toLowerCase().equals("mobile")) {
			waitForElementVisible(locator);
			element = getappium().findElement(locator);
		} else {
			waitForElementVisible(locator);
			element = getDriver().findElement(locator);
		}

		return element;
	}

	public List<WebElement> elements(By locator) // Returns list of webelements

	{
		List<WebElement> elements = getDriver().findElements(locator);

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
				String labelText = td.getText();

				if (labelText.contains(searchitem)) {
					elementfound = td;
					break;
				}

			}
		}
		return elementfound;
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
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(datenew);
		calendar.add(calendar.DATE, -1);
		Date reduceDate = calendar.getTime();
		String result = (resultformatter.format(reduceDate));
		return result;
	}
	
	public String getPageSource()
	{
		String source=getDriver().getPageSource();
		return source;
	}
	

}
