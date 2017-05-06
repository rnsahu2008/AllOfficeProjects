package com.lc.json;

import org.json.JSONObject;

public class CreditCardJson {
	
	public static JSONObject ccData(int id, int customerId, String cardHoler_name, String type, String number, int expire_year , int expire_month, int cvv, String bin, String created_on, String modified_on){
		try {
			JSONObject ccJson = new JSONObject();
			ccJson.put("id", id);
			ccJson.put("CustomerID", customerId);
			ccJson.put("cardholder_name", cardHoler_name);
			ccJson.put("type", type);
			ccJson.put("number", number);
			ccJson.put("expire_year", expire_year);
			ccJson.put("expire_month", expire_month);
			ccJson.put("cvv", cvv);
			ccJson.put("bin", bin);
			ccJson.put("created_on", created_on);
			ccJson.put("ModifiedOn", modified_on);
			return ccJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
