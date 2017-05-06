package com.lc.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

public class Util {
	
	public static String readResponse(HttpResponse response){
		try {
			InputStream ips  = response.getEntity().getContent();
	        BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));
//	        if(response.getStatusLine().getStatusCode()!=200 && response.getStatusLine().getStatusCode()!=201){
//	            throw new Exception(response.getStatusLine().getReasonPhrase());
//	        }
	        StringBuilder sb = new StringBuilder();
	        String s;
	        while(true ){
	            s = buf.readLine();
	            if(s==null || s.length()==0)
	                break;
	            sb.append(s);
	        }
	        return sb.toString();

		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject getRespJsonFromDB(String data){
		try {
			int index = data.indexOf("ResponseContent") + "ResponseContent".length();
			return new JSONObject(data.substring(index, data.length()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
