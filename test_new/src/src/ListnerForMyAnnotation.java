package src;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class ListnerForMyAnnotation implements IInvokedMethodListener{
	WebDriver driver;


	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		MyAnnotation browser = method.getTestMethod().getMethod().getAnnotation(MyAnnotation.class);
		if(browser.browser().equalsIgnoreCase("firefox"))
		{
			 driver = new FirefoxDriver();
			driver.get("https://www.google.co.in");
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		driver.quit();
	}

}
