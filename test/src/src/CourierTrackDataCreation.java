package src;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CourierTrackDataCreation extends Base {

	@Test(description = "PstUpdate", dataProvider = "suborderlist")
	public void forwardCourier(ArrayList<String> suborder,
			ArrayList<String> prodsuborder, ArrayList<String> awb,
			ArrayList<String> isndr, ArrayList<String> courierid,
			ArrayList<String> couriername) throws Exception {
		int currentno = 0;
		try {
			for (int i = 0; i < suborder.size(); i++) {
				String date;
				String AwbNumber;
				if (isndr.get(i).equals("N")) {

					System.out.println(suborder.get(i));
					currentno = i;

					JDBCConnection con = new JDBCConnection();
					int state = con.checkSuborderState(prodsuborder.get(i));
					if (state == 2154 || state == 2130 || state == 2130) {
						continue;
					}
					if (state == 2107) {
						con.updateInStlFor2107(suborder.get(i),
								prodsuborder.get(i));
					}
					driver.get("http://ops.homeshop18.com/");
					type(By.id("j_id_k:searchorder"), prodsuborder.get(i));
					click(By.id("j_id_k:j_id_v"));
					click(link("History"));
					Thread.sleep(2000);
					try {
						date = element(
								By.xpath("//tr[3][@class='ui-widget-content ui-datatable-odd']/td[3]")).getText();
						//AwbNumber = element(By.xpath("//div[@class='ui-tabs-panel ui-widget-content ui-corner-bottom']/div/form/table[1]/tbody/tr/td[2]/table/tbody/tr[2]/td[2]/span")).getText();
						//writeExcel(1, i, 5, AwbNumber);

					}

					catch (NoSuchElementException e) {
						date = element(By.xpath("//tr[2][@class='ui-widget-content ui-datatable-odd']/td[3]")).getText();
						//AwbNumber = element(By.xpath("//div[@class='ui-tabs-panel ui-widget-content ui-corner-bottom']/div/form/table[1]/tbody/tr/td[2]/table/tbody/tr[2]/td[2]/span")).getText();
						//writeExcel(1, i+1, 5, AwbNumber);
					}
					String result = dateToStringSql(date);
					awb = dataProviderByRow(1, 0, 10, 5);
					con.updateSuborderInPstBeforeChange(suborder.get(i),
							prodsuborder.get(i));
					driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"
							+ prodsuborder.get(i) + "/destination/2130");

					if (driver.getPageSource().contains(
							"Suborder already in destination state")) {
						continue;
					}
					// waitForText(By.xpath("//body"),
					// "Suborder changed to destination state");

					while (driver.getPageSource().contains("504")
							|| !(driver.getPageSource())
									.contains("destination state")) {
						driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"
								+ prodsuborder.get(i) + "/destination/2130");
					}

					con.updateSuborderInPstAfterChange(result + "00:10",
							courierid.get(i), couriername.get(i),
							suborder.get(i), prodsuborder.get(i), awb.get(i));
					con.updateSuborderInStl(result + " 00:10",
							courierid.get(i), couriername.get(i),
							suborder.get(i), prodsuborder.get(i), awb.get(i),
							"Gate Passed");
					con.updateSuborderInStl(result + " 00:10",
							courierid.get(i), couriername.get(i),
							suborder.get(i), prodsuborder.get(i), awb.get(i),
							"Cargo Ready");

				}
			}
		}

		catch (Exception e) {
			System.out.println("error :  " + e.getMessage() + " on line no -"
					+ currentno);
		}

	}

	@Test(description = "NDR", dataProvider = "suborderlist")
	public void NDR(ArrayList<String> suborder, ArrayList<String> prodsuborder,
			ArrayList<String> awb, ArrayList<String> isndr,
			ArrayList<String> courierid, ArrayList<String> couriername)
			throws Exception {
		for (int i = 0; i < suborder.size(); i++) {
			if (isndr.get(i).equals("Y")) {
				System.out.println(suborder.get(i));
				JDBCConnection con = new JDBCConnection();
				int state = con.checkSuborderState(prodsuborder.get(i));
				if (state == 2154 || state == 2130 || state == 2130
						|| state == 2181 ) {
					continue;
				}
				if (state == 2107 ||state == 2109) {
					con.updateInStlFor2107(suborder.get(i),
							prodsuborder.get(i));
				}
			
				driver.get("http://ops.homeshop18.com/");
				type(By.id("j_id_k:searchorder"), prodsuborder.get(i));
				click(By.id("j_id_k:j_id_v"));
				click(link("History"));
				Thread.sleep(2000);
				String date = element(
						By.xpath("//tr[4][@class='ui-widget-content ui-datatable-odd']/td[3]"))
						.getText();
				String result = dateToStringSql(date);
				// JDBCConnection con = new JDBCConnection();
				con.updateSuborderInPstBeforeChange(suborder.get(i),
						prodsuborder.get(i));
				driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"
						+ prodsuborder.get(i) + "/destination/2130");
				if (driver.getPageSource().contains(
						"Suborder already in destination state")) {
					continue;
				}
				// waitForText(By.xpath("//body"),
				// "Suborder changed to destination state");

				while (driver.getPageSource().contains("504")
						|| !(driver.getPageSource())
								.contains("destination state")) {
					driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"
							+ prodsuborder.get(i) + "/destination/2130");
				}
				con.updateSuborderInPstAfterChange(result + "00:10",
						courierid.get(i), couriername.get(i), suborder.get(i),
						prodsuborder.get(i), awb.get(i));
				con.updateSuborderInStl(result + " 00:10", courierid.get(i),
						couriername.get(i), suborder.get(i),
						prodsuborder.get(i), awb.get(i), "Gate Passed");
				con.updateSuborderInStl(result + " 00:10", courierid.get(i),
						couriername.get(i), suborder.get(i),
						prodsuborder.get(i), awb.get(i), "Cargo Ready");
			}
		}
	}

	@Test(description = "Reverse", dataProvider = "suborderlist")
	public void Reverse(ArrayList<String> suborder,
			ArrayList<String> prodsuborder, ArrayList<String> awb,
			ArrayList<String> isndr, ArrayList<String> courierid,
			ArrayList<String> couriername) throws Exception {
		for (int i = 0; i < suborder.size(); i++) {
			if (isndr.get(i).equals("R")) {
				JDBCConnection con = new JDBCConnection();
				int state = con.checkSuborderState(prodsuborder.get(i));
				if (state == 2107) {
					con.updateInStlFor2107(suborder.get(i), prodsuborder.get(i));
				}
				if (!(state == 2154 || state == 2156)) {

					con.updateSuborderInPstBeforeChange(suborder.get(i),
							prodsuborder.get(i));
					driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"
							+ prodsuborder.get(i) + "/destination/2154");
					if (driver.getPageSource().contains(
							"Suborder already in destination state")) {
						continue;
					}
					// waitForText(By.xpath("//body"),
					// "Suborder changed to destination state");

					while (driver.getPageSource().contains("504")
							|| !(driver.getPageSource())
									.contains("destination state")) {
						driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"
								+ prodsuborder.get(i) + "/destination/2154");
					}
				}
				driver.get("http://app01.preprod.hs18.lan:8091/faces/callCenter/crmLogin.xhtml");
				type(By.id("form1:login"), "sandy");
				type(By.id("form1:password"), "hsnsandy");
				click(By.id("form1:j_id_p"));
				click(By.id("findCustid:j_id_1c"));
				type(By.id("findCustid:callerNumber"), "4141414141");
				click(By.xpath(".//button[@id='findCustid:j_id_1t']/span"));
				click(link("Support"));
				click(link("Find Customer"));
				click(By.id("findCustForm:selectChoice_label"));
				click(By.xpath("//li[text()='Suborder']"));
				type(By.id("findCustForm:custKwrd"), prodsuborder.get(i));
				click(By.id("findCustForm:fcCmdId"));
				Thread.sleep(1000);
				click(By.xpath("//tbody[@id='supportForm:orderList_data']/tr[1]/td[8]/button"));
				waitForElementVisible(By.id("supportForm:caseGroupTypeSelect"));
				dropdown(By.id("supportForm:caseGroupTypeSelect"),
						"Product Related");
				Thread.sleep(1000);
				try {

					if (element(By.xpath("//span[@class='ui-growl-title']"))
							.getText()
							.contains(
									"is open for this Suborder in the Product Related Group")) {
					}
				}

				catch (NoSuchElementException e) {
					dropdown(By.id("supportForm:caseTypeSelect"),
							"Complete Defective Product");
					Thread.sleep(500);
					dropdown(By.id("supportForm:disposTypeSelect"),
							"Request for Refund");
					Thread.sleep(500);
					// dropdown(By.id("supportForm:reasonSelect"),
					// "Product Damaged");
					// Thread.sleep(500);
					click(By.xpath("//span[text()='Escalate']"));
					waitForElementVisible(By
							.xpath("//span[@class='ui-growl-title']"));
					Thread.sleep(500);
				}
				driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"
						+ prodsuborder.get(i) + "/destination/2156");
				if (driver.getPageSource().contains(
						"Suborder already in destination state")) {
					continue;
				}
				// waitForText(By.xpath("//body"),
				// "Suborder changed to destination state");

				while (driver.getPageSource().contains("504")
						|| !(driver.getPageSource())
								.contains("destination state")) {
					driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"
							+ prodsuborder.get(i) + "/destination/2156");
				}
				con.updateReverseInStl(courierid.get(i), couriername.get(i),
						prodsuborder.get(i), awb.get(i),
						"Reverse Pickup required for Replacement");
			}
		}
	}

	@DataProvider(name = "suborderlist")
	public Object[][] dataProviderLogin() throws IOException {
		testDataFile(File);
		Object[][] data = null;

		data = new Object[][] { { dataProviderByRow(1, 0, 10, 0),
				dataProviderByRow(1, 0, 10, 4), dataProviderByRow(1, 0, 10, 5),
				dataProviderByRow(1, 0, 10, 1), dataProviderByRow(1, 0, 10, 2),
				dataProviderByRow(1, 0, 10, 3) } };

		closeFile();
		return data;

	}
}
