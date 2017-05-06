package Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.crypto.Data;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestcLass extends ActionClass {

	@BeforeSuite
	public void setDriver() {

		System.setProperty("webdriver.chrome.driver",
				"libs\\chromedriver_29.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		System.out.println("Chrome driver selected");
		driver.get("https://www.flipkart.com/");
	}

	@Test
	public void flipkartPrice() throws InterruptedException {
		type(searchbox, "i Phone 6");
		clickEnterButton(searchbox);
		Thread.sleep(5000);
		// clickEnterPageDown(searchbox);
		// click(mobilecategory);
		// clickEnterPageDown(searchbox);
		dropdown(mobilepricerange, "30000");
		// Thread.sleep(15000);

		clickEnterPageDown(searchbox);
		// clickEnterPageDown(searchbox);
		// Thread.sleep(30000);
		// dropdown(minpricedropdwon,"30000");
		// dropdown(maxpricedropdwon,"35000+");

		click(brandapple);
		Thread.sleep(5000);

		clickEnterPageDown(searchbox);
		clickEnterPageDown(searchbox);

		Thread.sleep(3000);
		click(Availbility);
		click(excludeoutofstcok);
		/*
		 * //WebElement tst= driver.findElement(By.xpath("")) //List<WebElement>
		 * elemts = driver.findElements(By.xpath(
		 * "//*[@id='container']/div/div[2]/div[2]/div/div[2]/div/div[3]/div/div"
		 * )); List<WebElement> elemts =
		 * driver.findElements(By.cssSelector("._1UoZlX"));
		 * 
		 * System.out.println("Product"+elemts.size());
		 */
		// click(mobile);

		Thread.sleep(10000);
		WebElement eleNopfProd = driver.findElement(noofProducts);
		String numberpfProd = eleNopfProd.getText();
		// chnage to Integer value
		int numbers = Integer.parseInt(numberpfProd);
		System.out.println("Total No of Products in List:" + numbers);
		// HashMap<Integer,Object> map = new HashMap<Integer, Object>();

		ArrayList<DataObject> list = new ArrayList<DataObject>();
		for (int i = 1; i <= numbers; i++) {

			String price1 = "//*[@id='container']/div/div[2]/div[2]/div/div[2]/div/div[3]/div/div/div["
					+ i + "]/a/div[2]/div[2]/div[1]/div/div";
			WebElement elePri = driver.findElement(By.xpath(price1));
			// System.out.println(elePri.getText().substring(1));
			String proName = "//*[@id='container']/div/div[2]/div[2]/div/div[2]/div/div[3]/div/div/div["
					+ i + "]/a/div[2]/div[1]/div[1]";
			WebElement elePro = driver.findElement(By.xpath(proName));

			String proLink = "//*[@id='container']/div/div[2]/div[2]/div/div[2]/div/div[3]/div/div/div["
					+ i + "]/a";
			WebElement eleLink = driver.findElement(By.xpath(proLink));

			int price2 = Integer.parseInt(elePri.getText().replaceAll(",","").substring(1));

			String product1 = elePro.getText();
			String link1 = eleLink.getAttribute("href");

			DataObject db = new DataObject(price2,product1,link1);
			int count = 0;
			db.setProductLink(link1);
			db.setProductName(product1);
			db.setProductPrice(price2);
			// map.put(count+1, db);
			list.add(db);
			Collections.sort(list, new Comperator1());

		 /*System.out.println(elePri.getText().substring(1)+" | "
			+elePro.getText()+" | " + eleLink.getAttribute("href"));

*/			if (i % 3 == 0) {
				clickEnterPageDown(searchbox);
			}

		}
		
		System.out.println(list);

	}

	@AfterMethod
	public void closedriver() {
		driver.close();
	}

}
