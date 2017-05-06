package com.lc.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

public class EmailJson {
	@SuppressWarnings("rawtypes")
	public static String eMailData(HashMap<String, String> list){
		try {
			JSONObject json = new JSONObject();
			Iterator it = list.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        if(!(pair.getKey().toString().equals("response_code") || pair.getKey().toString().equals("error_message"))){
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
