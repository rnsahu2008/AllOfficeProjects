package com.hs18.TestDataCreation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hs18.DataUtils.BulkOrderCreationOrderCommand;
import com.hs18.DataUtils.DbConnection;
import com.hs18.DataUtils.ExcelReader;
import com.hs18.util.LocatorExcel;

@SuppressWarnings("deprecation")
public class BulkOrderCreation extends LocatorExcel {
	ExcelReader read = new ExcelReader();
	Properties prop;
	ArrayList<String> suborderlist;

	@Test(description = "bulkordercreation", dataProvider = "BulkDetail")
	public void createBulkOrders(String no, String email, String firstname, String lastname, String city,
			String pincode, String mobile, String itemcode, String orderid) throws Exception {
		BulkOrderCreationOrderCommand bulk = new BulkOrderCreationOrderCommand();
		suborderlist = new ArrayList<>();
		int num = Integer.parseInt(no);

		try {
			bulk.setCommandId("543e3e32-e645-4c89-9173-c54efcd40f78");
			bulk.setUserId("1");
			bulk.setUserLogin(email);
			bulk.setBuyerFirstName(firstname);
			bulk.setBuyerLastName(lastname);
			bulk.setBillingAddress1("Delhi new address for bulk suborder command");
			bulk.setBillingCity(city);
			bulk.setBillingZip(pincode);
			bulk.setBillingCountry("40108");
			bulk.setBillingMobile(mobile);
			bulk.setBillingEmail(email);
			bulk.setReceiverFirstName(firstname);
			bulk.setReceiverLastName(lastname);
			bulk.setShippingAddress1("Delhi new address for bulk suborder command");
			bulk.setReceivingCity(city);
			bulk.setReceivingCountry("40108");
			bulk.setReceivingzip(pincode);
			bulk.setReceivingMobile(mobile);
			bulk.setRemark("Bulk Order Creation");
			bulk.setItemCode(itemcode);
			bulk.setQuantity("1");
			bulk.setCatalogueRFNum("1");
			bulk.setCcRfNum("N");
			bulk.setIsCallCenter("Y");
			bulk.setCallerNumber(mobile);
			bulk.setRoute("I");
			bulk.setDthRfNum("7");
			bulk.setLeadSource("1");
			bulk.setSuborderStatus("2140");
			bulk.setPaymentMode("145");
			bulk.setOrderId(orderid);

	JAXBContext jaxbContext = JAXBContext.newInstance(BulkOrderCreationOrderCommand.class);
	Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	StringWriter writer = new StringWriter();
	jaxbMarshaller.marshal(bulk, System.out);
	jaxbMarshaller.marshal(bulk, writer);
	System.out.println(writer);
			for (int i = 0; i < num; i++) {
				postRequestOms(writer, i);
			}
			String propfile = DbConnection.propfile;
			FileInputStream ip = new FileInputStream(propfile + "\\Data\\config.properties");
			prop = new Properties();
			prop.load(ip);
			String path = propfile + prop.getProperty("BulkorderOutputFile");

			read.writeExcel(suborderlist, "Suborder", 0, path, "Suborder");

		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
	}

	public void postRequestOms(StringWriter writer, int i)  {
		@SuppressWarnings("resource")
		//HttpClient httpClient = HttpClientBuilder.create().build();

		DefaultHttpClient httpClient = new DefaultHttpClient();
		try { 
			
			HttpPost postrequest = new HttpPost("http://app04.preprod.hs18.lan:7004/api/1/oms/executeOmsCommand");
			postrequest.addHeader("content-type", "application/xml");
			postrequest.addHeader("accept", "application/json");
			StringEntity entity = new StringEntity(writer.getBuffer().toString());
			postrequest.setEntity(entity);

			HttpResponse response = httpClient.execute(postrequest);
			// verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			String output = handler.handleResponse(response);
			System.out.println(output);

			if (statusCode != 201 && statusCode != 204 && statusCode != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(output);
			JSONObject omsCommandResult = (JSONObject) json.get("omsCommandResult");
			JSONObject result = (JSONObject) omsCommandResult.get("result");
			JSONObject response2 = (JSONObject) result.get("response");
			Object suborder = response2.get("suborderId");
			String subordername=suborder.toString();
			System.out.println(subordername);
			suborderlist.add(suborder.toString());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// Important: Close the connect
			httpClient.getConnectionManager().shutdown();
		}
	}

	@DataProvider(name = "BulkDetail")
	public Object[][] dataProviderForY() throws IOException {
		read.testDataFile(file);
		Object[][] data = null;

		data = new Object[][] { { read.readFromColumn("BulkOrder", 1, 0), read.readFromColumn("BulkOrder", 1, 1),
				read.readFromColumn("BulkOrder", 1, 2), read.readFromColumn("BulkOrder", 1, 3),
				read.readFromColumn("BulkOrder", 1, 4), read.readFromColumn("BulkOrder", 1, 5),
				read.readFromColumn("BulkOrder", 1, 6), read.readFromColumn("BulkOrder", 1, 7),
				read.readFromColumn("BulkOrder", 1, 8) } };

		read.closeFile();
		return data;

	}

}
