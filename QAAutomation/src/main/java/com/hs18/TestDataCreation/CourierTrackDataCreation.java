package com.hs18.TestDataCreation;

import java.io.IOException;
import java.util.ArrayList;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.hs18.DataUtils.DBQuery;
import com.hs18.DataUtils.ExcelReader;
import com.hs18.util.LocatorExcel;

public class CourierTrackDataCreation extends LocatorExcel {

	DBQuery db = new DBQuery();
	ExcelReader read = new ExcelReader();
	@Test(description = "PstUpdate", dataProvider = "detail")
	public void forwardCourier(ArrayList<String> suborder, ArrayList<String> prodsuborder, ArrayList<String> isndr,
			ArrayList<String> courierid, ArrayList<String> couriername) throws Exception {

		int currentno = 0;
		try {
			for (int i = 0; i < suborder.size(); i++) {
				String date;
				if (isndr.get(i).equals("N")) {

					System.out.println(suborder.get(i));
					currentno = i;
					String awbno = null;
					int state = db.checkSuborderState(prodsuborder.get(i));
					if (state == 2154 || state == 2130 || state == 2169) {
						continue;
					}
					if (state == 2107) {
						db.updateInStlFor2107(suborder.get(i), prodsuborder.get(i));
					}
					openUrl("http://ops.homeshop18.com/");					
					type(By.id("j_id_k:searchorder"), prodsuborder.get(i));
					click(By.id("j_id_k:j_id_v"));
					click(By.linkText(("History")));
					Thread.sleep(2000);
					try {
						date = element(By.xpath("//tr[3][@class='ui-widget-content ui-datatable-even']/td[3]"))
								.getText();

					}

					catch (Exception e) {
						date = element(By.xpath("//tr[2][@class='ui-widget-content ui-datatable-odd']/td[3]"))
								.getText();

					}

					try {
						awbno = element(By.xpath("(//a[@class='blackboldSmall'])[2]/div/div[1]")).getText();
					} catch (Exception e) {
						awbno = element(By
								.xpath("html/body/table/tbody/tr/td/div/div/div[2]/div/form/table[1]/tbody/tr/td[2]/table/tbody/tr[2]/td[2]/span"))
										.getText();
					}
					String result = dateToStringSql(date);
					// awbno="12356849";
					db.updateSuborderInPstBeforeChange(suborder.get(i), prodsuborder.get(i));
					openUrl("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/" + prodsuborder.get(i)
							+ "/destination/2169");

					if (getPageSource().contains("Suborder already in destination state")) {
						continue;
					}
					// waitForText(By.xpath("//body"), "Suborder changed to
					// destination state");

					while (getPageSource().contains("504")
							|| !(getPageSource().contains("destination state"))) {
						openUrl("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/" + prodsuborder.get(i)
								+ "/destination/2169");
					}

					db.updateSuborderInPstAfterChange(result + "00:10", courierid.get(i), couriername.get(i),
							suborder.get(i), prodsuborder.get(i), awbno);
					db.updateSuborderInStl(result + " 00:10", courierid.get(i), couriername.get(i), suborder.get(i),
							prodsuborder.get(i), awbno, "Gate Passed");
					db.updateSuborderInStl(result + " 00:10", courierid.get(i), couriername.get(i), suborder.get(i),
							prodsuborder.get(i), awbno, "Cargo Ready");

				}
			}
		}

		catch (Exception e) {
			System.out.println("error :  " + e.getMessage() + " on line no -" + currentno);
		}

	}

	@Test(description = "NDR", dataProvider = "detail")
	public void NDR(ArrayList<String> suborder, ArrayList<String> prodsuborder, ArrayList<String> isndr,
			ArrayList<String> courierid, ArrayList<String> couriername) throws Exception {
		for (int i = 0; i < suborder.size(); i++) {
			if (isndr.get(i).equals("Y")) {
				System.out.println(suborder.get(i));				
				int state = db.checkSuborderState(prodsuborder.get(i));
				// if (state == 2154 || state == 2130 || state == 2169 || state
				// == 2181) {
				// continue;
				// }
				if (state == 2107 || state == 2109) {
					db.updateInStlFor2107(suborder.get(i), prodsuborder.get(i));
				}

				openUrl("http://ops.homeshop18.com/");
				type(By.id("j_id_k:searchorder"), prodsuborder.get(i));
				click(By.id("j_id_k:j_id_v"));
				click(By.linkText("History"));
				Thread.sleep(2000);
				String awbno;
				String date = element(By.xpath("//tr[4][@class='ui-widget-content ui-datatable-odd']/td[3]")).getText();
				try {
					awbno = element(By.xpath("(//a[@class='blackboldSmall'])[2]/div/div[1]")).getText();
				} catch (Exception e) {
					awbno = element(By.xpath(".//*[@id='j_id_j:1:awbNumb']")).getText();
				}
				//writeExcel(awbno, "Case", i + 1, 5);
				String result = dateToStringSql(date);
				if (!(state == 2130 || state == 2181 || state == 2146 || state == 2177)) {
					// JDBCConnection con = new JDBCConnection();
					db.updateSuborderInPstBeforeChange(suborder.get(i), prodsuborder.get(i));
					openUrl("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/" + prodsuborder.get(i)
							+ "/destination/2130");
					if (getPageSource().contains("Suborder already in destination state")) {
						continue;
					}
					// waitForText(By.xpath("//body"), "Suborder changed to
					// destination state");

					while (getPageSource().contains("504")
							|| !(getPageSource()).contains("destination state")) {
						openUrl("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/" + prodsuborder.get(i)
						+ "/destination/2130");
					}
				}
				db.updateSuborderInPstAfterChange(result + "00:10", courierid.get(i), couriername.get(i),
						suborder.get(i), prodsuborder.get(i), awbno);
				db.updateSuborderInStl(result + " 00:10", courierid.get(i), couriername.get(i), suborder.get(i),
						prodsuborder.get(i), awbno, "Cargo Ready");
				db.updateSuborderInStl(result + " 00:10", courierid.get(i), couriername.get(i), suborder.get(i),
						prodsuborder.get(i), awbno, "Order shipped");
			}
		}
	}

	

	

	@DataProvider(name = "detail")
	public Object[][] dataProviderForY() throws IOException {
		read.testDataFile(file);
		Object[][] data = null;

		data = new Object[][] { { read.dataProviderByRow("Courier", "Suborder"), read.dataProviderByRow("Courier", "Prod SuborderId"),
			read.dataProviderByRow("Courier", "IsNDR"), read.dataProviderByRow("Courier", "CourierId"), read.dataProviderByRow("Courier", "CourierName") } };

		read.closeFile();
		return data;

	}

}
