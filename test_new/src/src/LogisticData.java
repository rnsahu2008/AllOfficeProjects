package src;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.testng.TestNG;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.internal.ClassHelper;
import org.testng.internal.PackageUtils;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class LogisticData extends Base {

	@Test(description = "Data", dataProvider = "logisticdata")
	public void Logisticdata(ArrayList<String> suborder, ArrayList<String> courierid, ArrayList<String> couriername,
			ArrayList<String> nextstate) throws Exception {
		for (int i = 0; i < suborder.size(); i++) {

			System.out.println(suborder.get(i));
			JDBCConnection con = new JDBCConnection();
			int desiredstate = Integer.parseInt(nextstate.get(i));
			int state = con.checkSuborderState(suborder.get(i));
			if (state == desiredstate) {
				continue;
			}

			if (desiredstate == 2146 || desiredstate == 2170) {
				driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/" + suborder.get(i)
						+ "/destination/2181");
				if (driver.getPageSource().contains("Suborder already in destination state")) {
					continue;
				}

				while (driver.getPageSource().contains("504")
						|| !(driver.getPageSource()).contains("destination state")) {
					driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/" + suborder.get(i)
							+ "/destination/2181");
				}
			}
			driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/" + suborder.get(i) + "/destination/"
					+ nextstate.get(i));
			if (driver.getPageSource().contains("Suborder already in destination state")) {
				continue;
			}

			while (driver.getPageSource().contains("504") || !(driver.getPageSource()).contains("destination state")) {
				driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/" + suborder.get(i) + "/destination/"
						+ nextstate.get(i));
			}

			con.updateSuborderCourierInPst(courierid.get(i), couriername.get(i), suborder.get(i));
			if (!(desiredstate == 2109 || desiredstate == 2169 || desiredstate == 2141)) {
				con.updateCourierInStl(courierid.get(i), couriername.get(i), suborder.get(i), "Order shipped");
			}
		}
	}

	@DataProvider(name = "logisticdata")
	public Object[][] dataProviderlogictic() throws IOException {
		testDataFile(File);
		Object[][] data = null;

		data = new Object[][] { { dataProviderByRow(3, 0, 10, 0), dataProviderByRow(3, 0, 10, 1),
				dataProviderByRow(3, 0, 10, 2), dataProviderByRow(3, 0, 10, 3) } };

		closeFile();
		return data;

	}


}
