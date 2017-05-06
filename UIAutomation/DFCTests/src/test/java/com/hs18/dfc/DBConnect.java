package com.hs18.dfc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnect {
	public static final Logger logger = LoggerFactory.getLogger(DBConnect.class);
	public Connection conn=null;
	public Statement stmt=null;
	public ResultSet rs=null;
	String DBHost,schema, dbuserid, dbpwd,dbport;
	
	public DBConnect()
	{
		try 
		{
			getConnection();
		}
		catch (Exception e)
		{			
			e.printStackTrace();
		}
	}


	/*public DBConnect(String DBHost,String schema, String dbport,String dbuserid, String dbpwd) throws Exception
	{
		getConnection(DBHost, schema, dbport, dbuserid, dbpwd);
	}*/

	
	/*public DBConnect(String dbhost, String schema) throws Exception {
		// TODO Auto-generated constructor stub
		if(dbhost.contains("172.16.0.34") || dbhost.contains("hs18.net"))
		{
			if(schema.toLowerCase().contains("live"))
				getConnection("172.16.0.34","LIVE_hsn18db","hsn18","hs~Inno");
			else
				getConnection("172.16.0.34",schema,"hsn18","hs~Inno");
		}	
		else
		{
			if(schema.toLowerCase().contains("preprod"))
				getConnection("db.hs18.lan","hsn18db_preprod","root","hs~Inno");
			else
				getConnection("db.hs18.lan",schema,"root","hs~Inno");
			
		}	
	}*/

	public DBConnect(String DBHost,String schema, String dbuserid, String dbpwd) throws Exception
	{
			getConnection(DBHost, schema, dbuserid, dbpwd);
	}

	public void getConnection(String DBHost,String schema, String dbuserid, String dbpwd) throws Exception
	{
		getConnection(DBHost, schema, "3306", dbuserid, dbpwd);
	}
	
	public void getConnection(String DBHost,String schema, String dbport,String dbuserid, String dbpwd) throws Exception
	{
		this.DBHost=DBHost;
		this.schema=schema;
		this.dbuserid=dbuserid;
		this.dbport=dbport;
		this.dbpwd=dbpwd;
		String url = "jdbc:mysql://" + DBHost +":"+dbport+"/";
		  String dbName = schema;
		  String driver = "com.mysql.jdbc.Driver";
		  String userName = dbuserid; 
		  String password = dbpwd;

			  Class.forName(driver).newInstance();
			  conn = DriverManager.getConnection(url+dbName+"?zeroDateTimeBehavior=convertToNull",userName,password);
			  logger.debug("Connected to the database");
	}

	
	public void getConnection() throws Exception
	{
		getConnection("app06.preprod.hs18.lan","LIVE_testdb","hs18quality","DS865tNWMmo4mBkj");
	}
	
	public void closeConnection() 
	{
			DbUtils.closeQuietly(conn,stmt,rs);
			
		logger.debug("Disconnected from database");

	}
	
	public ResultSet execQuery(String query)
	{
		try {
			chkConnection();
			rs=stmt.executeQuery(query);
			rs.next();
			return rs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeConnection();
		}
		finally
		{
			closeConnection();
		}
		return rs;
	}

	public String execQueryWithSingleColumnResult(String query) 
	{
		try
		{
			chkConnection();
			rs=stmt.executeQuery(query);
			rs.next();
			return rs.getString(1);
		}
		catch(Exception e)
		{
			closeConnection();
			return null;
		}
		finally
		{
			closeConnection();
		}
	}
	
	public String[] execQueryWithSingleColumnMultipleResult(String query)
	{
		
		try
		{
			chkConnection();
			int i=0;
			rs=stmt.executeQuery(query);
			String[] result=new String[getRowCount(rs)];
			while(rs.next())
			{
				result[i++]=rs.getString(1);
			}
			return result;
		}
		catch(Exception e)
		{
			closeConnection();
			return null;
		}
		finally
		{
			closeConnection();
		}
	}

	
	
	public HashMap<String, String> execQueryWithSingleRowResult(String query)
	{
		HashMap<String, String> hm1=new HashMap<String, String>();
		try {
			
//			hm.clear();
			chkConnection();
			
			rs=stmt.executeQuery(query);
			if(rs.next()==false)
				return null;

			ResultSetMetaData md = rs.getMetaData();
			
			for (int i = 1; i <= md.getColumnCount(); i++)
				{
					  hm1.put(md.getColumnName(i),	rs.getString(md.getColumnName(i)) );
				}
			//return (HashMap<String, String>) hm.clone();
			return hm1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeConnection();
		}
		finally
		{
			closeConnection();
		}
//		return hm;
		return hm1;
	}
	

	
	public HashMap[] execQueryWithResultHM(String query)
	{
		try {
			HashMap<String, String>[] hmArr;
			
			chkConnection();
			
			rs=stmt.executeQuery(query);
			rs.next();
			hmArr= new HashMap[getRowCount(rs)];

			ResultSetMetaData md = rs.getMetaData();
			int j=0;
			while(rs.next())
			{
				hmArr[j]=new HashMap<String, String>();
				for (int i = 1; i <= md.getColumnCount(); i++)
					  hmArr[j].put(md.getColumnName(i),	rs.getString(md.getColumnName(i)) );
					
				j++;
			}
			return hmArr;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeConnection();
		}
		finally
		{
			closeConnection();
		}
		return null;
	}
	
	public int getRowCount(ResultSet rs) throws Exception
	{
		rs.last();
		int n=rs.getRow();
		rs.beforeFirst();
		return n;
	}

	public void chkConnection() throws SQLException, Exception
	{
		if (conn==null || conn.isClosed())
			getConnection(DBHost, schema,  dbport, dbuserid,  dbpwd);
		if(stmt ==null || stmt.isClosed() )
			stmt= conn.createStatement();
	}
}
