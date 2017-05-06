package com.lc.utils;

import org.json.JSONObject;

public class JsonValidator {
	public static Object getValue(Object obj){
		if(obj==null){
			return JSONObject.NULL;
		}
		return obj;
	}

}
