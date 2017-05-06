package com.lc.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONValueFinder {
	HashMap<Object, Object> hm = new HashMap<Object, Object>();
	
	public static String getJsonValue(String jsonString , String key){
		try {
			int keyIndex = jsonString.indexOf(key);
			String stringStartingFromKeyIndex = jsonString.substring(keyIndex, jsonString.length());
			
			String[] arraySplitWithColon = stringStartingFromKeyIndex.split(",");
			String keyValuePair = arraySplitWithColon[0].replaceAll("}", "").replaceAll("]", "").trim();
			int colonIndex = keyValuePair.indexOf(":");
			String finalKey = keyValuePair.substring(colonIndex +1, keyValuePair.length()).replace('"', ' ').trim();
			return finalKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static String getValue(JSONObject json , String key){
		try {
			Set<String> keySet = getKeys(json);
	        for (String k : keySet) {
	        	Object value = json.get(k);
	        	if(value instanceof JSONArray){
	        		JSONArray arr = json.getJSONArray(k);
	        		for(int i=0 ; i< arr.length() ; i++){
	        			JSONObject obj = arr.getJSONObject(i);
	        			getValue(obj, key);
	        		}
	        	}
	        	
	        	else if(value instanceof JSONObject){
        			getValue((JSONObject)value, key);
	        	}
	        	else{
	        		if(k.equals(key)){
	        			return value.toString();
	        		}
	        	}
	        }
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public void searchJSONObject(JSONArray arr , String key){
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void searchJSONArray(JSONObject json , String key){
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static Set<String> getKeys(JSONObject jsonObject) {
        Set<String> keys = new TreeSet<String>();
        Iterator<?> iter = jsonObject.keys();
        while(iter.hasNext()) {
            keys.add((String)iter.next());
        }
        return keys;
    }
    
	public static int JSONArrayLength(JSONObject json , String key){
		int length = 0;
		try {
				JSONArray jArray = json.getJSONArray(key);
				length = jArray.length();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return length;
	}

}
