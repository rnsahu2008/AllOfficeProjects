package com.hs18.dfc;

import java.util.List;

import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import static com.hs18.dfc.DfcTest.hMap;

import com.hs18.lib.SelBase;
import com.hs18.lib.UIMapParser;
import com.hs18.lib.Validator;
import com.hs18.lib.WaitEvent;
import com.hs18.lib.utils;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ReportUtil;
import util.TestUtil;

import com.hs18.lib.SelBase;
import com.hs18.lib.UIMapParser;
import com.hs18.lib.WaitEvent;
import com.hs18.lib.utils;

public class Components extends SelBase
{

    UIMapParser ui;
    DBConnect   db;

    public Components(WebDriver d) throws Exception
    {
        db = new DBConnect("app06.preprod.hs18.lan", "LIVE_testdb", "hs18quality", "DS865tNWMmo4mBkj");

        try
        {

            // utils.setConfiguration("src/test/resources/config.properties");
            driver = d;
            ui = new UIMapParser(utils.getConfig("ObjectRepository"));
        } catch (Exception e)
        {
            e.printStackTrace();
            Assert.fail("Failed to instantiate Components object");
        }
    }

    /**
     * returns webElement with the specified properties
     * 
     * @param pageName
     * @param uiElementName
     * @return
     * @throws Exception
     */
    public WebElement getWebElement(String pageName, String uiElementName) throws Exception
    {
        return driver.findElement(ui.getLocator(pageName, uiElementName));
    }

    public void LoginDfc(String userid, String pwd) throws Exception
    {
        setValue(ui.getLocator("login", "user"), userid, 15);
        setValue(ui.getLocator("login", "pwd"), pwd, 4);
        click(ui.getLocator("login", "login"), 10);
    }

    public void Loginbiz(String userid, String pwd) throws Exception
    {
        setValue(ui.getLocator("login", "user"), userid, 15);
        setValue(ui.getLocator("login", "pwd"), pwd, 4);
        click(ui.getLocator("login", "login"));
    }

    public String getProductID(String sku_id)
    {
        return db.execQueryWithSingleColumnResult("select PRODUCTID From PRODUCT_ITEM_SKU where skuid = " + sku_id + " ;");
    }

   

    public String getSiteId(String loginId)
    {
        String str = "SELECT SITEID FROM VENDOR_SITE WHERE VENDORID = (SELECT OUTSSIRFNUM FROM ISMOUT WHERE OUTLOGIN = '" + loginId + "')";
        return new DBConnect().execQueryWithSingleColumnResult(str);

    }

    public HashMap[] getsku_inventory(String sku_id)
    {
        return db.execQueryWithResultHM("SELECT * FROM sku_inventory WHERE sku_id= " + sku_id + " ;");
    }

    public HashMap[] getsupplier_inventory(String sku_id)
    {
        return db.execQueryWithResultHM("SELECT * FROM supplier_inventory WHERE sku_id= " + sku_id + " ;");
    }

    public String getAdvQty(String skuid, String siteid)
    {
        return new DBConnect().execQueryWithSingleColumnResult("select advanced_qty from sku_inventory where sku_id=" + skuid + " and site_id=" + siteid);
    }

    public String converttostring(Object obj)
    {
        try
        {
            obj.toString().trim();
        } catch (Exception e)
        {
            return "null";
        }

        return obj.toString().trim();
    }
    

    
    public void mouseMove(By locator)
    {
        Actions builder = new Actions(driver);
        Action move = builder.moveToElement(getWebElement(locator)).build();
        move.perform();

        /*
         * try { we.waitForElement(locator, 4); } catch (Exception e) { // TODO
         * Auto-generated catch block e.printStackTrace(); }
         */
        // Alternative code
        /*
         * String mouseOverScript =
         * "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}"
         * ; JavascriptExecutor js = (JavascriptExecutor) driver; WebElement
         * someElem = driver.findElement(locator);
         * js.executeScript(mouseOverScript, someElem);
         */
    }

    @Override
    public void setValue(By locator, String val, int durationSecs) throws Exception
    {
        try
        {
            logger.debug("Setting value " + val + " for the UI element " + locator);

            if (durationSecs > 0)
            {
                if (we == null)
                    we = new WaitEvent(driver);

                we.waitForElement(locator, durationSecs);
            }
            driver.findElement(locator).clear();
            driver.findElement(locator).sendKeys(val);
            logger.debug("Succesfully Set the value " + val + " for the UI element " + locator);
            ReportUtil.addKeyword("Enter value ", val, "Pass", "");

        } catch (Exception e)
        {
            Long time = TestUtil.randomnumber();
            TestUtil.takeScreenShot(driver, "src//test//resources//screenshots//" + time + ".jpg");
            ReportUtil.addKeyword("Enter value ", val, "Fail", "src//test//resources//screenshots//" + time + ".jpg");

        }

    }

    public WebElement findelement(By locator, int durationSecs) throws Exception
    {
        try
        {

            if (durationSecs > 0)
            {
                if (we == null)
                    we = new WaitEvent(driver);

                we.waitForElement(locator, durationSecs);
            }
            logger.debug("found the locator " + locator);
            ReportUtil.addKeyword("Found the element", "Found", "Pass", "");

        } catch (Exception e)
        {
            Long time = TestUtil.randomnumber();
            TestUtil.takeScreenShot(driver, "src//test//resources//screenshots//" + time + ".jpg");
            ReportUtil.addKeyword("Found the element ", "Found", "Fail", "src//test//resources//screenshots//" + time + ".jpg");

        }
        return driver.findElement(locator);

    }

    public List<WebElement> findelements(By locator, int durationSecs) throws Exception
    {
        try
        {

            if (durationSecs > 0)
            {
                if (we == null)
                    we = new WaitEvent(driver);

                we.waitForElement(locator, durationSecs);
            }
            logger.debug("found the locator " + locator);
            ReportUtil.addKeyword("Found the element", "Found", "Pass", "");

        } catch (Exception e)
        {
            Long time = TestUtil.randomnumber();
            TestUtil.takeScreenShot(driver, "src//test//resources//screenshots//" + time + ".jpg");
            ReportUtil.addKeyword("Found the element ", "Found", "Fail", "src//test//resources//screenshots//" + time + ".jpg");

        }
        return driver.findElements(locator);

    }

    @Override
    public void click(By locator, int durationSecs) throws Exception
    {
        try
        {
            logger.debug("Clicking the Web element " + locator);

            if (durationSecs > 0)
            {
                if (we == null)
                    we = new WaitEvent(driver);

                we.waitForElement(locator, durationSecs);
            }

            driver.findElement(locator).click();
            logger.debug("Succesfully Clicked the Web element " + locator);
            ReportUtil.addKeyword("Click Button ", " ", "Pass", "");

        } catch (Exception e)
        {
            Long time = TestUtil.randomnumber();
            TestUtil.takeScreenShot(driver, "src//test//resources//screenshots//" + time + ".jpg");
            ReportUtil.addKeyword("Click Button ", " ", "Fail", "src//test//resources//screenshots//" + time + ".jpg");

        }
    }

    public void selectcombobox(String id, String value) throws Exception

    {
        WebElement elem = findelement(By.id(id), 10);

        Select select = new Select(elem);
        List<WebElement> options = select.getOptions();

        for (WebElement we : options)
        {
            if (we.getText().equals(value))
            {
                we.click();
                break;
            }
        }
    }

    public void fnlogin(String username, String password) throws Exception
    {
        // driver.get("http://172.16.0.21:9080/callcenterIVR/faces/callCenter/crmLogin.jsp");
        setValue(ui.getLocator("Home", "Username"), username, 15);
        setValue(ui.getLocator("Home", "Password"), password, 15);
        click(ui.getLocator("Home", "Signin"));
        Wait<WebDriver> wait = new WebDriverWait(driver, 60).pollingEvery(1, TimeUnit.SECONDS);
        wait.until(new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver webDriver)
            {
                return webDriver.getTitle().equals("HomeShop18 Products Main Panel");
            }
        });

        verifyequals("HomeShop18 Products Main Panel", driver.getTitle());
    }

    private void verifyequals(String expected, String actual) throws IOException, AWTException
    {
        try
        {
            Assert.assertEquals(expected, actual);

        } catch (AssertionError e)
        {
            Long time = TestUtil.randomnumber();
            takeScreenshot(time);
            ReportUtil.addKeyword("Assertion error ", "Message " + e.toString(), "Fail", "src//test//resources//screenshots//" + time + ".jpg");
            ReportUtil.addTestCase(hMap.get("Scenario"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), "Fail");
            Assert.fail(e.toString());

        }
    }

    private void verifyequals(double expected, double actual) throws IOException, AWTException
    {
        try
        {
            Assert.assertEquals(expected, actual);

        } catch (AssertionError e)
        {
            Long time = TestUtil.randomnumber();
            takeScreenshot(time);
            ReportUtil.addKeyword("Assertion error ", "Message " + e.toString(), "Fail", "src//test//resources//screenshots//" + time + ".jpg");
            ReportUtil.addTestCase(hMap.get("Scenario"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), "Fail");
            Assert.fail(e.toString());

        }
    }

    private void verifyequals(double expected, float actual) throws IOException, AWTException
    {
        try
        {
            Assert.assertEquals(expected, actual);

        } catch (AssertionError e)
        {
            Long time = TestUtil.randomnumber();
            takeScreenshot(time);
            ReportUtil.addKeyword("Assertion error ", "Message " + e.toString(), "Fail", "src//test//resources//screenshots//" + time + ".jpg");
            ReportUtil.addTestCase(hMap.get("Scenario"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), "Fail");
            Assert.fail(e.toString());

        }
    }

    private void verifyequals(float expected, double actual, int i) throws IOException, AWTException
    {
        try
        {
            Assert.assertEquals(expected, actual, i);

        } catch (AssertionError e)
        {
            Long time = TestUtil.randomnumber();
            takeScreenshot(time);
            ReportUtil.addKeyword("Assertion error ", "Message " + e.toString(), "Fail", "src//test//resources//screenshots//" + time + ".jpg");
            ReportUtil.addTestCase(hMap.get("Scenario"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), "Fail");
            Assert.fail(e.toString());

        }
    }

    private void verifyequals(float expected, int actual, int i) throws IOException, AWTException
    {
        try
        {
            Assert.assertEquals(expected, actual, i);

        } catch (AssertionError e)
        {
            Long time = TestUtil.randomnumber();
            takeScreenshot(time);
            ReportUtil.addKeyword("Assertion error ", "Message " + e.toString(), "Fail", "src//test//resources//screenshots//" + time + ".jpg");
            ReportUtil.addTestCase(hMap.get("Scenario"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), "Fail");
            Assert.fail(e.toString());

        }
    }

    private void verifyequals(double expected, float actual, int i) throws IOException, AWTException
    {
        try
        {
            Assert.assertEquals(expected, actual, i);

        } catch (AssertionError e)
        {
            Long time = TestUtil.randomnumber();
            takeScreenshot(time);
            ReportUtil.addKeyword("Assertion error ", "Message " + e.toString(), "Fail", "src//test//resources//screenshots//" + time + ".jpg");
            ReportUtil.addTestCase(hMap.get("Scenario"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), "Fail");
            Assert.fail(e.toString());

        }
    }

    private void verifyequals(float expected, double actual) throws IOException, AWTException
    {
        try
        {
            Assert.assertEquals(expected, actual);

        } catch (AssertionError e)
        {
            Long time = TestUtil.randomnumber();
            takeScreenshot(time);
            ReportUtil.addKeyword("Assertion error ", "Message " + e.toString(), "Fail", "src//test//resources//screenshots//" + time + ".jpg");
            ReportUtil.addTestCase(hMap.get("Scenario"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), "Fail");
            Assert.fail(e.toString());

        }
    }

    public void takeScreenshot(Long time) throws IOException, AWTException
    {
        try
        {
            if (driver instanceof TakesScreenshot)
            {
                File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                try
                {
                    FileUtils.copyFile(tempFile, new File("src//test//resources//screenshots//" + time + ".jpg"));
                } catch (IOException e)
                {
                    // TODO handle exception
                }
            }
        } catch (Exception e)
        {
            // TODO: handle exception
            java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRectangle = new Rectangle(screenSize);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRectangle);
            ImageIO.write(image, "png", new File("src//test//resources//screenshots//" + time + ".jpg"));

        }

    }

}
