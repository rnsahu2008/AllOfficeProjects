package src;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

public class RetryAnalyzer implements IRetryAnalyzer{
	public int count = 0;

	private int maxCount = 1;
	Base action = new Base();

	public RetryAnalyzer() {
		setCount(maxCount);
	}
	@Override
	public boolean retry(ITestResult result) {
		// TODO Auto-generated method stub
		if (!result.isSuccess()) {
			if (count < maxCount) {
				count++;
				Reporter.log(Thread.currentThread().getName() + "Error in "
						+ result.getName() + " with status "
						+ result.getStatus() + " Retrying " + count + " times");
				return true;
			}

		}
		return false;
	}

	public void setCount(int count) {
		maxCount = count;
	}
}
