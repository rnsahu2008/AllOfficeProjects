package com.hs18.dfc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import util.ReportUtil;
import util.TestUtil;

import com.hs18.lib.HS18TestsBase;
import com.hs18.lib.WaitEvent;

public class DfcTest extends HS18TestsBase {
	Logger logger = LoggerFactory.getLogger(DfcTest.class);
	public static String dfcUrl = "http://app04.preprod.hs18.lan:8090/dfc-web/";
	public static String omsUrl = "http://app04.preprod.hs18.lan:8080/oms/";

	Components comp;
	public static HashMap<String, String> hMap = null;
	public DBConnect dfcdb = null;
	public int qty = 1;
	public String skuid = "156606";

	@Parameters({ "Dfc_Url", "skuidForTest" })
	@BeforeClass
	public void initClass(@Optional String url, @Optional String sku)
			throws Exception {
		if (sku != null)
			skuid = sku;
		dfcdb = new DBConnect("app06.preprod.hs18.lan", "dfc", "hs18quality",
				"DS865tNWMmo4mBkj");

		if (url != null)
			dfcUrl = url;

	}

	@BeforeMethod(alwaysRun = true)
	public void init() throws Exception {
		Thread.sleep(1000);
		comp = new Components(driver);
		// driver.get(dfcUrl);
		// driver.manage().window().maximize();
	}

	@AfterMethod
	public void tearDown(Method method, ITestResult result) {
		driver.close();
		driver.quit();
		if (result.isSuccess())
			logger.info("*********************" + method.getName()
					+ "-PASSED**************************");
		else
			logger.info("*********************" + method.getName()
					+ "-FAILED**************************");
	}

	/**
	 * Given a sku it tests Commit Inventory functionality
	 * 
	 * @throws Exception
	 */
	// @Test(groups={"Sanity","Regression"})
	public void commitinventory() throws Exception {
		String userid = "fnp", pwd = "LfoSEfuD@U7F125Y84";

		driver.get(dfcUrl);
		comp.LoginDfc(userid, pwd);

		int commInv = 5;

		// get site id for login vendor
		String site = comp.getSiteId(userid);

		// get sku inventory for the site..
		int advQty = Integer.parseInt(comp.getAdvQty(skuid, site));

		click(By.linkText("Commit Inventory"), 15);
		setValue(getLocator("inventory", "filter"), skuid, 15);
		Thread.sleep(3000);

		setValue(getLocator("inventory", "invvalue"),
				Integer.toString(commInv), 15);
		click(getLocator("inventory", "submit"), 15);

		Thread.sleep(3000);
		int qtyAfterCommit = Integer.parseInt(comp.getAdvQty(skuid, site));

		logger.debug("Before Adv qty: " + advQty + ". After AdvQty: "
				+ qtyAfterCommit);
		// check sku inventory increased for the site of
		Assert.assertTrue(qtyAfterCommit == advQty + commInv,
				"Inventory did not updated correctly");
	}

	// @Test
	public void test() throws Exception {
		for (int i = 0; i < 5; i++) {
			placeorder("30523904");
		}
	}

	@Test(groups = { "Sanity", "Regression" })
	public void OrderFulfillment() throws Exception {
		String userid = "fnp", pwd = "LfoSEfuD@U7F125Y84";
		// commitinventory();
		// Get product and Item for the sku to get order placement in website
		// get site id for login vendor
		// String site = comp.getSiteId(userid);
		String productid = comp.getProductID(skuid);
		// Capture Inventory values : sku_inv and Supplier_inv
		HashMap[] hm_supplierinv_intial = comp.getsupplier_inventory(skuid);
		HashMap[] hm_inventory_intial = comp.getsku_inventory(skuid);
		// Place order in website for the same item By opening PDP directly.
		String ordernumber = placeorder(productid);

		HashMap[] hm_supplierinv_after_placing_order = comp
				.getsupplier_inventory(skuid);
		HashMap[] hm_inventory_intial_after_placing_order = comp
				.getsku_inventory(skuid);
		// oms inventory reduction
		Assert.assertEquals(
				Integer.parseInt(hm_inventory_intial_after_placing_order[0]
						.get("oms_qty").toString()),
				Integer.parseInt(hm_inventory_intial[0].get("oms_qty")
						.toString()) - qty, "OMS qty failed to reduce");
		
		// Advance should not reduce
		Assert.assertEquals(Integer
				.parseInt(hm_inventory_intial_after_placing_order[0].get(
						"advanced_qty").toString()),
				Integer.parseInt(hm_inventory_intial[0].get("advanced_qty")
						.toString()), "Advance qty got reduced");
		// check order is not pushed to DFC before oms verification.

		String dfcorderentry = comp
				.converttostring(dfcdb
						.execQueryWithSingleColumnResult("SELECT  ORDERI_ID FROM DFC_SUB_ORDER` WHERE  ORDER_ID= "
								+ productid + " limit 1"));

		Assert.assertEquals(dfcorderentry, "null");
		// virtual qty check after placing order should not increase until order
		// status changed to verified
		Assert.assertEquals(Integer
				.parseInt(hm_supplierinv_after_placing_order[0].get(
						"virtual_qty").toString()), Integer
				.parseInt(hm_supplierinv_intial[0].get("virtual_qty")
						.toString()),
				"Virtual qty got increased before the order got placed");

		verifyoms(ordernumber);
		Thread.sleep(6000);
		HashMap[] hm_supplier_inv_after_verifying_order = comp
				.getsupplier_inventory(skuid);
		HashMap[] hm_inventory_after_verifying_order = comp
				.getsku_inventory(skuid);
		dfcorderentry = comp
				.converttostring(dfcdb
						.execQueryWithSingleColumnResult("SELECT PRODUCT_ID FROM DFC_SUB_ORDER WHERE ORDER_ID = "
								+ ordernumber + " limit 1;"));

		Assert.assertNotEquals(dfcorderentry, "null",
				"Order not pushed to DFC ");

		Assert.assertEquals(Integer
				.parseInt(hm_inventory_after_verifying_order[0].get(
						"advanced_qty").toString()), Integer
				.parseInt(hm_inventory_intial_after_placing_order[0].get(
						"advanced_qty").toString()),
				"Advanced qty should not reduce after verification");
		Assert.assertEquals(Integer
				.parseInt(hm_inventory_after_verifying_order[0].get("oms_qty")
						.toString()), Integer
				.parseInt(hm_inventory_intial_after_placing_order[0].get(
						"oms_qty").toString()),
				"oms qty should not reduce after verification");

		// Virtual qty should decrease
		Assert.assertEquals(
				Integer.parseInt(hm_supplier_inv_after_verifying_order[0].get(
						"virtual_qty").toString()),
				Integer.parseInt(hm_supplierinv_after_placing_order[0].get(
						"virtual_qty").toString())
						- qty,
				"Virtual qty failed to  reduce after order got verified");

		// promised qty remains the same
		Assert.assertEquals(
				Integer.parseInt(hm_supplier_inv_after_verifying_order[0].get(
						"promised_qty").toString()),
				Integer.parseInt(hm_supplierinv_after_placing_order[0].get(
						"promised_qty").toString()),
				"Promised qty should not reduce after order verificaation");

		// supplier inventory assign qty check
		// verify order in DFC
		// Assert.assertEquals(actual, expected);
		dfcfullfilmentplan();
		dfcpicklist();
		dfcgenerateorder();
		Thread.sleep(3000);
		int dfqty = Integer
				.parseInt(comp.converttostring(dfcdb
						.execQueryWithSingleColumnResult(" SELECT SUM(QUANTITY) FROM `DFC_SUB_ORDER` WHERE  sku_id = "
								+ skuid + "  AND SUB_STATE_ID=1006 ")));

		dfcgeneratemanifest();

		Thread.sleep(6000);
		HashMap[] hm_supplier_inv_after_manifest_order = comp
				.getsupplier_inventory(skuid);
		HashMap[] hm_inventory_after_manifest_order = comp
				.getsku_inventory(skuid);
		Assert.assertEquals(
				Integer.parseInt(hm_supplier_inv_after_manifest_order[0].get(
						"promised_qty").toString()),
				Integer.parseInt(hm_supplier_inv_after_verifying_order[0].get(
						"promised_qty").toString())
						- dfqty,
				"Promised qty should  reduce after order manifestation");
		Assert.assertEquals(
				Integer.parseInt(hm_supplier_inv_after_manifest_order[0].get(
						"virtual_qty").toString()),
				Integer.parseInt(hm_supplier_inv_after_verifying_order[0].get(
						"virtual_qty").toString())
						+ dfqty,
				"Virtual qty failed to  increase after order got manifested");
		Assert.assertEquals(
				Integer.parseInt(hm_inventory_after_verifying_order[0].get(
						"advanced_qty").toString())
						- dfqty,
				Integer.parseInt(hm_inventory_after_manifest_order[0].get(
						"advanced_qty").toString()),
				"Advanced qty failed to  reduce after order got manifested");
		Assert.assertEquals(
				Integer.parseInt(hm_inventory_after_verifying_order[0].get(
						"oms_qty").toString())
						+ dfqty,
				Integer.parseInt(hm_inventory_after_manifest_order[0].get(
						"oms_qty").toString()),
				"oms qty failed to  increase after order got manifested");

		// suborder status change - CR $ OMS increase & reduce Adv

	}

	@Test
	public String placeorder(String ProductID) throws Exception {

		driver.get("http://www.homeshop18.biz/shop/product:" + ProductID + "/");

		click(By.id("btnBuyNow"), 10);

		// click(By.xpath("//*[@class='btn pay']/span/span"), 10);
		WebElement webElement = driver.findElement(By.xpath("//a[contains(@href,'/cart/proceedToPay')]"));
		Capabilities cp = ((RemoteWebDriver) driver).getCapabilities();
		if (cp.getBrowserName().equals("chrome")) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
			webElement.click();
		} 
		else
			webElement.click();

		try {
			driver.findElement(By.partialLinkText("sign out")).click();
			driver.findElement(By.partialLinkText("sign in")).click();
		} catch (Exception e) {

			driver.findElement(By.partialLinkText("sign in")).click();
		}

		setValue(getLocator("biz", "loginusername"),
				"santhosh.baby@network18online.com", 10);
		driver.findElement(By.id("password")).sendKeys("test123");

		click(getLocator("biz", "loginclick"), 10);

		try {
			click(By.xpath("//a[contains(@href,'/cart/proceedToPay')]"), 10);
		} catch (Exception e) {

		}

		click(By.xpath("//a[contains(text(),'to this address')]"), 10);

		click(By.xpath("//a[contains(text(),'Cash on Delivery')]"), 10);

		click(By.xpath("//form[@id='cashOnDelivery']//a/span"), 10);


		// Phase 2
		/*
		 * driver.get("http://www.homeshop18.biz/shopping-cart");
		 * List<WebElement> deletelist = driver.findElements(By.id("del-item"));
		 * for (WebElement webElement : deletelist) { webElement.click(); }
		 */

		return comp
				.findelement(By.xpath("//p[@class='order-number_bold']"), 10)
				.getText();

	}

	public void verifyoms(String orderid) throws Exception {
		driver.get(omsUrl);
		setValue(By.id("_id0:_id2"), "SudhirK", 10);
		setValue(By.name("_id0:_id3"), "dedhask", 10);
		click(By.id("_id0:_id4"), 10);

		driver.get(omsUrl+ "faces/oms/changeStatusOrder.jsp?orderType=ORDER&orderNoParam="
				+ orderid
				+ "&action=OrderDetailActionBean.orderDetail_changeStatusOrderAction");

		comp.selectcombobox("changeStatusForm:stateListMenu", "Verified");
		click(By.id("changeStatusForm:changeStatusButton"), 10);
	}

	public void dfcfullfilmentplan() throws Exception {
		Thread.sleep(2000);
		driver.get(dfcUrl);
		comp.LoginDfc("fnp", "LfoSEfuD@U7F125Y84");

		driver.get(dfcUrl+ "jsp/fulfilment/fulfilment.xhtml");
		String val = comp
				.findelement(
						By.xpath("//div[text()='156606']//parent::td/following-sibling::td[3]"),
						10).getText();

		setValue(
				By.xpath("//div[text()='156606']//parent::td/following-sibling::td[4]//input"),
				val, 10);

		Thread.sleep(3000);
		click(getLocator("dfc", "fullfilmentsubmit"), 10);
		Thread.sleep(3000);
		System.out.println("lfjdjks");

	}

	public void dfcpicklist() throws Exception {

		driver.get(dfcUrl+ "jsp/fulfilment/viewPickList.xhtml");

		List<WebElement> ilispickid = comp
				.findelements(
						By.xpath("//table/tbody/tr/td[2]/span/div/form/div/table//span[contains(@class,'triangle')]"),
						10);

		for (int i = 0; i < ilispickid.size(); i++) {
			click(By.xpath("//tr/td[2]/span/div/form/div/table/tbody/tr[1]/td[1]/div/span"),
					10);
			click(By.xpath("//table/tbody/tr/td[2]/span/div/form/div/table//button[contains(@id,'confirmButton')]"),
					10);

		}

	}

	public void dfcgenerateorder() throws Exception {
		Thread.sleep(3000);
		driver.get(dfcUrl+ "jsp/forms/orderForm.xhtml");

		comp.selectcombobox("form:courierName", "Blue Dart");
		driver.findElement(By.id("form:confirmDialogButton")).click();

		click(By.id("form:confirm"), 10);
		System.out.println("test");
	}

	public void dfcgeneratemanifest() throws Exception {
		Thread.sleep(3000);
		driver.get(dfcUrl + "jsp/forms/manifest.xhtml");

		comp.selectcombobox("form:courierName", "Blue Dart");
		driver.findElement(By.id("form:basic")).click();

	}

	@Override
	public void setValue(By locator, String val, int durationSecs)
			throws Exception {
		try {
			logger.debug("Setting value " + val + " for the UI element "
					+ locator);

			if (durationSecs > 0) {
				if (we == null)
					we = new WaitEvent(driver);

				we.waitForElement(locator, durationSecs);
			}
			driver.findElement(locator).click();
			driver.findElement(locator).clear();
			driver.findElement(locator).sendKeys(val);
			logger.debug("Succesfully Set the value " + val
					+ " for the UI element " + locator);
			ReportUtil.addKeyword("Enter value ", val, "Pass", "");

		} catch (Exception e) {
			Long time = TestUtil.randomnumber();
			TestUtil.takeScreenShot(driver,
					"src//test//resources//screenshots//" + time + ".jpg");
			ReportUtil.addKeyword("Enter value ", val, "Fail",
					"src//test//resources//screenshots//" + time + ".jpg");

		}

	}

	@Override
	public void click(By locator, int durationSecs) throws Exception {
		try {
			logger.debug("Clicking the Web element " + locator);

			if (durationSecs > 0) {
				if (we == null)
					we = new WaitEvent(driver);

				we.waitForElement(locator, durationSecs);
			}

			driver.findElement(locator).click();
			logger.debug("Succesfully Clicked the Web element " + locator);
			ReportUtil.addKeyword("Click Button ", " ", "Pass", "");

		} catch (Exception e) {
			e.printStackTrace();

			Long time = TestUtil.randomnumber();
			TestUtil.takeScreenShot(driver,
					"src//test//resources//screenshots//" + time + ".jpg");
			ReportUtil.addKeyword("Click Button ", " ", "Fail",
					"src//test//resources//screenshots//" + time + ".jpg");

		}
	}

}
