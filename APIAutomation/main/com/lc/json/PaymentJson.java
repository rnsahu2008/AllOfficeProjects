package com.lc.json;

import org.json.JSONObject;

import com.lc.utils.JsonValidator;

public class PaymentJson {
	
	public static JSONObject paymentData(String sku_id, int id, double price, double amount, int quantity , String descriptor , Object status , String item_type_cd){
		try {
			JSONObject json = new JSONObject();
			json.put("id", JsonValidator.getValue(id));
			json.put("sku_id", JsonValidator.getValue(sku_id));
			json.put("price", JsonValidator.getValue(price));
			json.put("amount", JsonValidator.getValue(amount));
			json.put("descriptor", JsonValidator.getValue(descriptor));
			json.put("quantity", JsonValidator.getValue(quantity));
			json.put("status", JsonValidator.getValue(status));
			json.put("item_type_cd", JsonValidator.getValue(item_type_cd));
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
