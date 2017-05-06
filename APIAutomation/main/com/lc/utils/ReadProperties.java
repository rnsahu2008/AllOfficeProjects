package com.lc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReadProperties {

	private static FileInputStream fileInput = null;
	private static String authUrl;
	private static String baseUrl;
	private static String sqlHost;
	private static String sqlPort;
	private static String sqlUserName;
	private static String sqlPasswd;
	private static String sqlDBName;

	public static void setDbConfigProperties(String fileName) {
		try {
			File file = new File(fileName);
			fileInput = new FileInputStream(file);
			Properties prop = new Properties();
			prop.load(fileInput);
			setBaseURL(prop.getProperty("BASE_URL").trim());
			setSqlHost(prop.getProperty("SQL_HOST").trim());
			setSqlPort(prop.getProperty("SQL_PORT").trim());
			setSqlUserName(prop.getProperty("SQL_USER").trim());
			setSqlPasswd(prop.getProperty("SQL_PASSWORD").trim());
			setSqlDBName(prop.getProperty("SQL_DB_NAME").trim());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInput != null) {
				try {
					fileInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void setupEnvironmentProperties(String val,String filePath){
		try{
			FileReader fileReaderObj = new FileReader(filePath);
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(fileReaderObj);
			JSONObject jObj = (JSONObject) obj;
			JSONObject parentArray = new JSONObject();
			parentArray = (JSONObject) jObj.get(val);
			setAuthURL(parentArray.get("AUTH_URL").toString());
			setBaseURL(parentArray.get("BASE_URL").toString());
			setSqlHost(parentArray.get("SQL_HOST").toString());
			setSqlPort(parentArray.get("SQL_PORT").toString());
			setSqlUserName(parentArray.get("SQL_USER").toString());
			setSqlDBName(parentArray.get("SQL_DB_NAME").toString());
			setSqlPasswd(parentArray.get("SQL_PASSWORD").toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String getauthURL() {
		return authUrl;
	}

	public static void setAuthURL(String authurl) {
		ReadProperties.authUrl = authurl;
	}
	
	public static String getbaseURL() {
		return baseUrl;
	}

	public static void setBaseURL(String baseurl) {
		ReadProperties.baseUrl = baseurl;
	}
	public static String getSqlPort() {
		return sqlPort;
	}

	public static void setSqlPort(String sqlPort) {
		ReadProperties.sqlPort = sqlPort;
	}

	public static String getSqlUserName() {
		return sqlUserName;
	}

	public static void setSqlUserName(String sqlUserName) {
		ReadProperties.sqlUserName = sqlUserName;
	}

	public static String getSqlPasswd() {
		return sqlPasswd;
	}

	public static void setSqlPasswd(String sqlPasswd) {
		ReadProperties.sqlPasswd = sqlPasswd;
	}

	public static String getSqlDBName() {
		return sqlDBName;
	}

	public static void setSqlDBName(String sqlDBName) {
		ReadProperties.sqlDBName = sqlDBName;
	}

	public static String getSqlHost() {
		return sqlHost;
	}

	public static void setSqlHost(String sqlHost) {
		ReadProperties.sqlHost = sqlHost;
	}
}
