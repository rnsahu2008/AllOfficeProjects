package com.lc.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import com.lc.common.Common;
import com.lc.common.Api;
import com.lc.utils.Util;

public class HttpService {
	static LinkedHashMap<String, String> requestResponseHashMap = null;
	public static ResponseBean get(String url , HashMap<String, String> headers) throws IOException {

		DefaultHttpClient client = getHttpClient(url);
		try{			
			HttpGet request = new HttpGet(url);
			if(headers!=null){
				Iterator it = headers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					request.addHeader(pair.getKey().toString(), pair.getValue().toString());
				}
			}
			request.addHeader("content-type", "application/json");
			request.addHeader("Accept","application/json");
			HttpResponse response = client.execute(request);
			try {

			} finally {
				//request.releaseConnection();			    
			}
			ResponseBean rb = new ResponseBean(response);
			Common.apiLogInfo.add(new Api("Get Api Call#" + url, "Empty Request", rb.code, rb.message));
			

			return rb;
		}
		catch(Exception e){

		}
		return null;
	}

	public static ResponseBean post(String URL , HashMap<String, String> headers , String dataToPost) throws IOException{
		DefaultHttpClient client = getHttpClient(URL);		
		try {
			HttpPost post = new HttpPost(URL);
			post.addHeader("content-type", "application/json");
			post.addHeader("Accept","application/json");
			if(headers!=null){
				Iterator it = headers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					post.addHeader(pair.getKey().toString(), pair.getValue().toString());
				}
			}
			StringEntity params = new StringEntity(dataToPost);
			post.setEntity(params);
			HttpResponse response = client.execute(post);
			try {

			} finally {
				//post.releaseConnection();
			}
			ResponseBean rb = new ResponseBean(response);
			Common.apiLogInfo.add(new Api("Post Api Call#" + URL, dataToPost, rb.code, rb.message));
			
			return rb;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ResponseBean put(String URL , HashMap<String, String> headers , String dataToPost) throws IOException{
		DefaultHttpClient client = getHttpClient(URL);
		try {
			HttpPut put = new HttpPut(URL);
			put.addHeader("content-type", "application/json");
			put.addHeader("Accept","application/json");
			if(headers!=null){
				Iterator it = headers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					put.addHeader(pair.getKey().toString(), pair.getValue().toString());
				}
			}

			if(!StringUtils.isEmpty(dataToPost)){
				StringEntity params = new StringEntity(dataToPost);
				put.setEntity(params);
			}

			HttpResponse response = client.execute(put);
			try {

			} finally {
				//post.releaseConnection();
			}
			ResponseBean rb = new ResponseBean(response);
			Common.apiLogInfo.add(new Api("Put Api Call#" + URL, dataToPost, rb.code, rb.message));
			
			return rb;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ResponseBean patch(String URL , HashMap<String, String> headers , String dataToPost) throws IOException{
		DefaultHttpClient client = getHttpClient(URL);
		try {
			HttpPatch patch = new HttpPatch(URL);
			patch.addHeader("content-type", "application/json");
			patch.addHeader("Accept","application/json");
			if(headers!=null){
				Iterator it = headers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					patch.addHeader(pair.getKey().toString(), pair.getValue().toString());
				}
			}
			if(StringUtils.isEmpty(dataToPost)){
				//do nothing
			}
			else{
				StringEntity params = new StringEntity(dataToPost);
				patch.setEntity(params);
			}
			HttpResponse response = client.execute(patch);
			try {

			} finally {
				//patch.releaseConnection();
			}
			ResponseBean rb = new ResponseBean(response);
			Common.apiLogInfo.add(new Api("Patch Api Call#" + URL, dataToPost, rb.code, rb.message));
			
			return rb;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ResponseBean delete(String url , HashMap<String, String> headers){
		DefaultHttpClient client = getHttpClient(url);
		try {
			HttpDelete delete = new HttpDelete(url);
			delete.addHeader("content-type", "application/json");
			delete.addHeader("Accept","application/json");
			if(headers!=null){
				Iterator it = headers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					delete.addHeader(pair.getKey().toString(), pair.getValue().toString());
				}
			}
			HttpResponse response = client.execute(delete);
			try {

			} finally {
				//patch.releaseConnection();
			}
			ResponseBean rb = new ResponseBean(response);
			Common.apiLogInfo.add(new Api("Delete Api Call#" + url, "Empty Request", rb.code, rb.message));
			
			return rb;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static DefaultHttpClient getHttpClient(String url){
		try {
			if(url.contains("https")){
				DefaultHttpClient sslClient = new DefaultHttpClient();
				HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
				SchemeRegistry registry = new SchemeRegistry();
				SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
				socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
				registry.register(new Scheme("https", socketFactory, 443));
				SingleClientConnManager mgr = new SingleClientConnManager(sslClient.getParams(), registry);
				DefaultHttpClient client = new DefaultHttpClient(mgr, sslClient.getParams());
				HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
				return client;
			}
			else{
				return new DefaultHttpClient();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static class ResponseBean{
		public int code;
		public String message;

		public ResponseBean(HttpResponse response) {
			this.message = Util.readResponse(response);
			this.code = response.getStatusLine().getStatusCode();
		}
	}

}
