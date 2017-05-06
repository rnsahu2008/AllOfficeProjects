package src;
/
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
//import com.fasterxml.jackson.*;
//import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class suborderFreightCost extends Base {
	JSONObject response;
	ArrayList<Integer> rat      e = new ArrayList<>();

	@Test(description = "FreigthCharges", dataProvider = "freight")
	public void freightCost(ArrayList<String> suborderId, ArrayList<String> courierId, ArrayList<String> fromPincode,
			ArrayList<String> toPincode, ArrayList<String> isCOD, ArrayList<String> isRTO,
			ArrayList<String> shippingMode, ArrayList<String> payablePrice, ArrayList<String> volume,
			ArrayList<String> grossWt) {
		SuborderFreightCostCommandList root = new SuborderFreightCostCommandList();
		List<SuborderFreightCostCommand> suborderFreightCostCommandList = new ArrayList<>();
		for (int i = 0; i < courierId.size(); i++) {
			int courier = Integer.parseInt(courierId.get(i));
			Long fpin = Long.valueOf(fromPincode.get(i));
			Long tpin = Long.valueOf(toPincode.get(i));
			Long suborder = Long.valueOf(suborderId.get(i));
			BigDecimal vol = new BigDecimal(volume.get(i));
			BigDecimal wt = new BigDecimal(grossWt.get(i));
			Boolean rto, cod;
			if (isRTO.get(i).toLowerCase().equals("1")) {
				rto = true;
			} else {
				rto = false;
			}

			if (isCOD.get(i).toLowerCase().equals("1")) {
				cod = true;
			} else {
				cod = false;
			}

			SuborderFreightCostCommand cost = new SuborderFreightCostCommand();
			BigDecimal payable = new BigDecimal(payablePrice.get(i));
			cost.setCourierId(courier);
			cost.setFromPincode(fpin);
			cost.setToPincode(tpin);
			cost.setGrossWt(wt);
			cost.setIsCOD(cod);
			cost.setIsRTO(rto);
			cost.setPayablePrice(payable);
			cost.setShippingMode(shippingMode.get(i));
			cost.setVolume(vol);
			cost.setSuborderId(suborder);
			suborderFreightCostCommandList.add(cost);
		}
		root.setSuborderFreightCostCommandList(suborderFreightCostCommandList);
		String writer;
		try {

			ObjectMapper mapper = new ObjectMapper();
			AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
			mapper.setAnnotationIntrospector(introspector);
			// Printing JSON
			writer = mapper.writeValueAsString(root);

			// Parsing JSON
			// Recipe retr = mapper.readValue(result, Recipe.class);

			response = postRequestOms(writer, i);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean basicFreightCharges(JSONObject response, String suborder, String fromPincode, String toPincode,boolean rto)
			throws ClassNotFoundException, SQLException, JSONException {
		JDBCConnection con = new JDBCConnection();
        int chargeableWeight;
		int grossWeight = con.grossWeight(suborder);
		int volume = con.grossVolume(suborder);
		String mode = con.checkShipmentMode(suborder);
		if (mode.equals("N")) {
			volume = volume * 1000 / 366;
		} else {
			volume = volume * 1000 / 288;
		}
		chargeableWeight=Math.max(grossWeight, volume);
		rate = con.zonalCourierRate(fromPincode, toPincode);		
		int freightCharges;
		if(!(rto))
		{
			freightCharges=rate.get(0)*chargeableWeight;
		}
		else
		{
			freightCharges=rate.get(1)*chargeableWeight;
		}
		if(response.get("freightCharges").equals(freightCharges))
		{
			return true;
		}
		else
		{
			return false;
		}
		

	}

	public JSONObject postRequestOms(String writer, int i) throws Exception {
		@SuppressWarnings("resource")
		DefaultHttpClient httpClient = new DefaultHttpClient();
		JSONObject result = null;
		try {

			HttpPost postrequest = new HttpPost(
					"http://app21.preprod.hs18.lan:7112/logistics/api/1/suborderFreightCost");
			postrequest.addHeader("content-type", "application/json");
			postrequest.addHeader("accept", "application/json");
			StringEntity entity = new StringEntity(writer);
			postrequest.setEntity(entity);
			HttpResponse response = httpClient.execute(postrequest);
			// verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			ResponseHandler<String> handler = new BasicResponseHandler();
			String output = handler.handleResponse(response);

			if (statusCode != 201 && statusCode != 204 && statusCode != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}

			JSONObject json = new JSONObject(output);
			JSONArray apirespoinse = json.getJSONArray("commandResponseList");
			JSONObject response2 = apirespoinse.getJSONObject(0);
			result = (JSONObject) response2.get("SuborderFreightCostCommandResponse");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Important: Close the connect
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}

	@DataProvider(name = "freight")
	public Object[][] dataProviderForY() throws IOException {
		testDataFile(File);
		Object[][] data = null;

		data = new Object[][] { { dataProviderByRow(4, 0, 10, 0), dataProviderByRow(4, 0, 10, 1),
				dataProviderByRow(4, 0, 10, 2), dataProviderByRow(4, 0, 10, 3), dataProviderByRow(4, 0, 10, 4),
				dataProviderByRow(4, 0, 10, 5), dataProviderByRow(4, 0, 10, 6), dataProviderByRow(4, 0, 10, 7),
				dataProviderByRow(4, 0, 10, 8), dataProviderByRow(4, 0, 10, 9) } };

		closeFile();
		return data;

	}
}
