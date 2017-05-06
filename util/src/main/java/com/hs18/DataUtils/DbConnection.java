package com.hs18.DataUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.hs18.util.Action;

public class DbConnection extends Action {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	 String DB_URL = "jdbc:mysql:";
	 String USER;
	 String PASS;
	public static Connection con = null;
	public PreparedStatement stmt = null;
	public ResultSet rs;
	ExcelReader reader = new ExcelReader();
	Properties prop;
	public static String propfile;

	public DbConnection(String type) throws SQLException, ClassNotFoundException, IOException {
		Class.forName(JDBC_DRIVER);
//		 propfile = "E:\\Projects\\HS18Automation\\UIAutomation\\QAAutomation";
		if(propfile==null)
		{
		 propfile = System.getProperty("project.home");
		}
		if (con == null) {
			getCon(type);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
		}
		
	}
	

	public void chkConnection(String type) throws SQLException, Exception {
		if (con == null || con.isClosed()) {
			getCon(type);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
		}

	}

	private void setDbParm(String dbName,String User,String Pass) throws IOException {
		prop = new Properties();
		FileInputStream ip = new FileInputStream(propfile + "\\Data\\dbDetails.properties");
		prop.load(ip);
		System.out.println("db props::"+prop);
		DB_URL = DB_URL + prop.getProperty(dbName);
		USER = prop.getProperty(User);
		PASS = prop.getProperty(Pass);

	}


	public Connection getCon(String type) throws IOException {
		if(type.equals("preprod"))
		{
			setDbParm("PreprodDbName", "PreprodDbUserName", "PreprodDbPassword");
		}
		else if(type.equals("automation"))
		{
			setDbParm("DbName", "DbUserName", "DbPassword");
		}
		return con;
	}


	public void setCon(Connection con) {
		this.con = con;
	}
	  
}
