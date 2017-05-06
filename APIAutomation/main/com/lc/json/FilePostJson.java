package com.lc.json;
import org.json.JSONObject;

import com.lc.utils.JsonValidator;

public class FilePostJson {
	
	public static JSONObject filePostData(String ids, String format, boolean skip_cache){
		try {
			JSONObject json = new JSONObject();
			json.put("ids", JsonValidator.getValue(ids));
			json.put("format", JsonValidator.getValue(format));
			json.put("skipcache", JsonValidator.getValue(skip_cache));
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

