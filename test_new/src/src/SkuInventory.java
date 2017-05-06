package src;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SkuInventory extends Base{

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Base bas = new Base();
		System.setProperty("webdriver.ie.driver","E:\\Demo_Framework\\Driver\\IEDriverServer.exe");
		WebDriver driver = new InternetExplorerDriver();
		WebDriverWait wait = new WebDriverWait(driver, 50);
		driver.get("http://10.50.33.12/ebiznet/Default.aspx");
		bas.waitForElementVisible(By.id("txtAccNo"));
		driver.findElement(By.id("txtAccNo")).sendKeys("1");
		driver.findElement(By.id("txtUser")).sendKeys("ebizuser1");
		driver.findElement(By.id("txtPwd")).sendKeys("ebiznet");
		driver.findElement(By.id("btnLogin")).click();
		driver.findElement(By.xpath("//span[text()='OK']")).click();
		driver.findElement(By.id("ctl00_ddSite_Input")).click();
		driver.findElement(By.xpath("//li[text()='DRH - DHARUHERA']")).click();
		Thread.sleep(3000);
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath("//span[text()='Purchasing']")));
		Thread.sleep(1000);
		action.moveToElement(driver.findElement(By.xpath("//span[text()='Purchase Order Request']")));
		Thread.sleep(1000);
		action.moveToElement(driver.findElement(By.xpath("(//span[text()='New'])[2]"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("ctl00$ContentPlaceHolder1$txtPONumber")));
		

	}

}
