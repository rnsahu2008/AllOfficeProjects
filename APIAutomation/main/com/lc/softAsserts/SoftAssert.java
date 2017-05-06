package com.lc.softAsserts;
import java.util.Map;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.collections.Maps;
import com.lc.common.Common;

public class SoftAssert extends Assertion {
	private Map<AssertionError, IAssert> m_errors = Maps.newLinkedHashMap();
		
	public void executeAssert(IAssert a) {
		try {
			a.doAssert();
		} catch (AssertionError ex) {
			this.onAssertFailure(a, ex);
			this.m_errors.put(ex, a);
		}
	}
	
	/**
	* This method is used for soft asserting all assertions of a test method and appends url, request, response code and response message in a string builder object for all api calls in a test in sequential order in which they are called 
	*/
	public void assertAll() 
	{
		if (!this.m_errors.isEmpty()) {
			StringBuilder sb = new StringBuilder("The following asserts failed:\n");
			boolean first = true;
			for (Map.Entry<AssertionError, IAssert> ae : this.m_errors.entrySet()) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(ae.getValue().getMessage());
			}

			StringBuilder strBuilder = new StringBuilder();
			for (int i = 0; i < Common.apiLogInfo.size(); i++)
			{
				String url = Common.apiLogInfo.get(i).getUrl();
				String[] str = url.split("#");
				strBuilder.append("<p><b><u>" + str[0] + "</u></b><br> <i><b>Url	:</b> " + str[1] + ",<br> <b>Request Body	:</b> " + Common.apiLogInfo.get(i).getRequest() + ",<br> <b>Response Code	:</b> " + Common.apiLogInfo.get(i).getResponseCode() +",<br><b> Response Message	:</b> " + Common.apiLogInfo.get(i).getResponseMessage() 	+ "</i></p><p></p>");
			}

			String[] asserts = sb.toString().split("The following asserts failed:");
			String assertLog = asserts[1].replace(",", "<br>");
			strBuilder.append("<p style=\"color:red;\"> The following asserts failed: <br>" + assertLog + "</p>");
			
			String className = new Exception().getStackTrace()[1].getClassName();
			String[] classNameArray = className.split("\\.");	
			Common.errorBuilder.append("<tr bgcolor = \"#87CEFA\"><td align=\"center\">" + Common.failedTestCount++ + "</td><td align=\"center\">" + classNameArray[4] + "</td><td align=\"center\">" + new Exception().getStackTrace()[1].getMethodName() + "</td><td align=\"left\">" + strBuilder.toString() + "</td></tr>");
			
			throw new AssertionError((Object) sb.toString());
		}
	}
	
}