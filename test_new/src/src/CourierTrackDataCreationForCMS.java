package src;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.webdriven.commands.Click;

public class CourierTrackDataCreationForCMS extends Base {

	@Test(description = "PstUpdate", dataProvider = "suborderlist")
	public void forwardCourier(ArrayList<String> suborder,
			ArrayList<String> prodsuborder, ArrayList<String> awb,
			ArrayList<String> isndr, ArrayList<String> courierid,
			ArrayList<String> couriername) throws Exception {
		int currentno = 0;
		try {
			for (int i = 0; i < suborder.size(); i++) {
				String date;
				if (isndr.get(i).equals("N")) {

					System.out.println(suborder.get(i));
					currentno = i;

					JDBCConnection con = new JDBCConnection();
					int state = con.checkSuborderState(prodsuborder.get(i));
					if (state == 2154 || state == 2130 || state == 2169) {
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
								By.xpath("//tr[3][@class='ui-widget-content ui-datatable-odd']/td[3]"))
								.getText();

					}

					catch (NoSuchElementException e) {
						date = element(
								By.xpath("//tr[2][@class='ui-widget-content ui-datatable-odd']/td[3]"))
								.getText();
					}
					String result = dateToStringSql(date);

					con.updateSuborderInPstBeforeChange(suborder.get(i),
							prodsuborder.get(i));
					driver.get("http://app02.preprod.hs18.lan:8899/api/1/oms/suborder/"
							+ prodsuborder.get(i) + "/destination/2169");

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
								+ prodsuborder.get(i) + "/destination/2169");
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
				if (state == 2154 || state == 2130 || state == 2169
						|| state == 2181) {
					continue;
				}
				if (state == 2107) {
					con.updateInStlFor2107(suborder.get(i), prodsuborder.get(i));
				}
				driver.get("http://ops.homeshop18.com/");
				type(By.id("j_id_k:searchorder"), prodsuborder.get(i));
				click(By.id("j_id_k:j_id_v"));
				click(link("History"));
				Thread.sleep(2000);
				String date = element(
						By.xpath("//tr[4][@class='ui-widget-content ui-datatable-odd']/td[3]"))
						.getText();
				String awbno = element(
						By.xpath(".//*[@id='j_id_j:1:j_id_4m']/table[1]/tbody/tr/td[2]/table/tbody/tr[2]/td[2]/a/div/div[1]"))
						.getText();
				writeExcel(awbno, "Case", i + 1, 5);
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
						prodsuborder.get(i), awbno);
				con.updateSuborderInStl(result + " 00:10", courierid.get(i),
						couriername.get(i), suborder.get(i),
						prodsuborder.get(i), awbno, "Cargo Ready");
				con.updateSuborderInStl(result + " 00:10", courierid.get(i),
						couriername.get(i), suborder.get(i),
						prodsuborder.get(i), awbno, "Order shipped");
			}
		}
	}

	@Test(description = "Reverse", dataProvider = "suborderlist")
	public void Reverse(ArrayList<String> suborder,
			ArrayList<String> prodsuborder, ArrayList<String> awb,
			ArrayList<String> isndr, ArrayList<String> courierid,
			ArrayList<String> couriername,ArrayList<String> caseid2) throws Exception {
		for (int i = 0; i < suborder.size(); i++) {
			if (isndr.get(i).equals("R")) {
				System.out.println(prodsuborder.get(i));
				JDBCConnection con = new JDBCConnection();
				int state = con.checkSuborderState(prodsuborder.get(i));
				int state2 = con.checkSuborderState(suborder.get(i));
				if (state2 == 2107) {
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
				driver.get("http://app07.preprod.hs18.lan:8082/faces/callCenter/crmLogin.xhtml");
				
				type(By.id("form1:login"), "sandy");
				type(By.id("form1:password"), "hsnsandy");
				click(By.id("form1:j_id_o"));
				click(By.id("findCustid:j_id_1c"));
				type(By.id("findCustid:callerNumber"), "4141414245");
				click(By.xpath(".//button[@id='findCustid:j_id_1t']/span"));
				click(link("Support"));
				Thread.sleep(12000);
				switchtoFrame();
				type(By.xpath("//input[@class='search']"), prodsuborder.get(i));
				waitForText(By.xpath("//span[@class='suborderId']"), prodsuborder.get(i));
				click(By.xpath("//span[@class='suborderId']"));
//				click(link("Identify"));
//				// click(By.className("form-control"));
//				dropdown(By.xpath("(//select[@class='form-control'])[2]"),
//						"Suborder");
//
//				// click(By.xpath("//option[text()='Suborder']"));
//				type(By.xpath("(//input[@class='form-control'])[2]"),
//						prodsuborder.get(i));
//				Thread.sleep(5000);
//				click(By.xpath("//button[text()='Search']"));
				Thread.sleep(12000);
				click(By.xpath("//a[text()='Product']"));
				click(By.id("DEFECTIVE"));
				Thread.sleep(3000);
				type(By.xpath(".//*[@id='productTabContent']/div[1]/div[4]/textarea"),
						"Refund case");
				Thread.sleep(4000);
				click(By.xpath(".//*[@id='productTabContent']/div[1]/div[4]/button"));
				Thread.sleep(4000);
				try
				{
				if(element(By.xpath("//p[@class='message']")).getText().contains("Failed"))
				{
					click(By.xpath("//button[text()='Ok']"));
				}
				}
				
				catch(Exception e)
				{
					e.getMessage();
				}
				if(elements(By.xpath("//button[text()='Ok']")).size()>0)
				{
				click(By.xpath("//button[text()='Ok']"));
				}
				

				Thread.sleep(3000);
				String caseid = element(By.xpath("//a[@class='collapsed']"))
						.getText();
				
				driver.get("http://app03.preprod.hs18.lan:8080/CaseManagementSystem/assets/ui/index.jsp");
				if(i==0)
				{
				type(By.id("username"), "ts_anu");
				type(By.id("password"), "demo");
				click(By.id("kc-login"));
				}
				
				Thread.sleep(4000);
				driver.get("http://app03.preprod.hs18.lan:8080/CaseManagementSystem/assets/ui/index.jsp#caseDetails/"
						+ caseid + "/4141414245");
				click(link("Product Related Troubleshoot Pending"));
				Thread.sleep(2000);
				click(By.id("defective"));
				Thread.sleep(5000);
				click(By.xpath(".//*[@id='collapseProblemType']/div/div[2]/div/button"));
				Thread.sleep(9000);
				click(By.xpath("(//button[text()='Submit'])[1]"));
				Thread.sleep(2000);				
				con.updateReverseInCmsStl(courierid.get(i), caseid, awb.get(i));
				con.updateReverseInCmsStl2(courierid.get(i), caseid, awb.get(i));
				con.updateReverseInCmsStl3(courierid.get(i), caseid, awb.get(i));
				con.updateReverseInCmsStl4(courierid.get(i), caseid, awb.get(i));
				
				
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
				dataProviderByRow(1, 0, 10, 3),dataProviderByRow(1, 0, 10, 6) } };

		closeFile();
		return data;

	}
}
