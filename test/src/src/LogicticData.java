package src;

import java.io.IOException;
import java.util.ArrayList;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LogicticData extends Base {

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
			if (desiredstate != 2109 || desiredstate != 2141 || desiredstate != 2169)
			{
			con.updateCourierInStl(courierid.get(i), couriername.get(i), suborder.get(i), "Order shipped");
			}
		}
	}

	@DataProvider(name = "logisticdata")
	public Object[][] dataProviderlogictic() throws IOException {
		testDataFile(File);
		Object[][] data = null;

		data = new Object[][] { { dataProviderByRow(2, 0, 10, 0), dataProviderByRow(2, 0, 10, 1),
				dataProviderByRow(2, 0, 10, 2), dataProviderByRow(2, 0, 10, 3) } };

		closeFile();
		return data;

	}
}
