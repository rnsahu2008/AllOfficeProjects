package com.lc.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import com.lc.utils.JsonValidator;

public class SubscriptionJson {
	
	public static JSONObject subscriptionData(String sku_id, int plan_id, String start_date, double amount, String descriptor, String cuid, String ip_address, String email, Object charge_now, JSONObject linked_subscription){
		try {
			JSONObject json = new JSONObject();
			json.put("sku_id", JsonValidator.getValue(sku_id));
			json.put("plan_id", JsonValidator.getValue(plan_id));
			json.put("start_date", JsonValidator.getValue(start_date));
			json.put("amount", JsonValidator.getValue(amount));
			json.put("descriptor", JsonValidator.getValue(descriptor));
			json.put("cuid", JsonValidator.getValue(cuid));
			json.put("ip_address", JsonValidator.getValue(ip_address));
			json.put("email", JsonValidator.getValue(email));
			json.put("charge_now", JsonValidator.getValue(charge_now));
			json.put("linked_subscription", JsonValidator.getValue(linked_subscription));
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String subscriptionData(HashMap<String, String> list){
		try {
			JSONObject json = new JSONObject();
			Iterator it = list.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        if((pair.getKey().toString().equals("Request") || pair.getKey().toString().equals("Response"))){
		        	json.put(pair.getKey().toString(), pair.getValue().toString());	
		        }		        
		    }
		    return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
