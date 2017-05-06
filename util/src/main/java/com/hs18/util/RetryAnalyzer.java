package com.hs18.util;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

public class RetryAnalyzer implements IRetryAnalyzer {
	public int count = 0;
	private int maxCount = 2;
	public RetryAnalyzer() {
	setCount(maxCount);
	}
  
	@Override
	public boolean retry(ITestResult result) {

		if (!(result.isSuccess())) {
			if (count < maxCount) {
				count++;
				
			}
		
			else
				if(count==maxCount)
				{
					Reporter.log(Thread.currentThread().getName() + " Error in "
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
