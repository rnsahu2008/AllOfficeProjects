package Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

public class ActionClass extends Locator {

	public static WebDriver driver;

	public void type(By locator, String data) {
		driver.findElement(locator).sendKeys(data);
	}

	public void click(By locator) // To click on a locatoraf
	{
		driver.findElement(locator).click();

	}

	public void clickEnterButton(By locator) // To click on a locatoraf
	{
		WebElement element = driver.findElement(locator);
		element.sendKeys(Keys.ENTER);

	}
	
	public void clickEnterPageDown(By locator) // To click on a locatoraf
	{
		WebElement element = driver.findElement(locator);
		element.sendKeys(Keys.PAGE_DOWN);

	}
	
	public void DragandDrop(By drag,By drop) // To click on a locatoraf
	{
		WebElement dragelem = driver.findElement(drag);
		WebElement dropelem = driver.findElement(drop);
		Actions builder = new Actions(driver);
		Action dragAndDrop = builder.clickAndHold(dragelem)
				.moveToElement(dropelem).release(dropelem).build();

		dragAndDrop.perform();	
		
	}

	public void dropdown(By locator, String value) // To select a text option
	// from drop down
	{
		WebElement element = driver.findElement(locator);
		Select drop = new Select(element);
		drop.selectByValue(value);
	}
}
