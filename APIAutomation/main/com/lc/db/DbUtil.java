package com.lc.db;

import static com.lc.constants.Contants_DBDetails.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.lc.utils.ReadProperties;

public class DbUtil {
	
	static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static Connection connection = null;
	static Statement stmt = null;
	static DbUtil instance = null;
	static String DB_URL = "";

	private DbUtil(String db) {
		initiateDbDriver(db);
	}

	public static DbUtil getInstance(String db) {
		if (instance == null) {
			instance = new DbUtil(db);
			return instance;
		} else {
			return instance;
		}
	}

	public void initiateDbDriver(String db) {

		DB_URL = "jdbc:sqlserver://" + SQL_HOST + ":" + SQL_PORT
				+ ";DatabaseName=" + db + ";user=" + SQL_USER + ";password="
				+ SQL_PWD;
		try {
			DriverManager
					.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getResultSet(String query) {

		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData metadata = rs.getMetaData();
			int columnCount = metadata.getColumnCount();
			ArrayList<String> columns = new ArrayList<String>();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metadata.getColumnName(i);
				columns.add(columnName);
			}
			HashMap<String, String> hm = new HashMap<>();
			while (rs.next()) {
				for (String columnName : columns) {
					String value = rs.getString(columnName);
					System.out.println(columnName + " = " + value);
					hm.put(columnName, value);
				}
				rs.close();
				return hm;
			}
		} catch (SQLException se) {
			se.printStackTrace();
			if (se.getMessage() != null) {
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				System.exit(1);
			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			instance = null;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getResultSetList(String query) {

		Map mMap = new HashMap();
		List<Map> list = new ArrayList();
		try {

			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData metadata = rs.getMetaData();
			int columnCount = metadata.getColumnCount();
			ArrayList<String> columns = new ArrayList<String>();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metadata.getColumnName(i);
				columns.add(columnName);
			}
			while (rs.next()) {
				mMap = new HashMap();
				for (String columnName : columns) {
					String value = rs.getString(columnName);
					mMap.put(columnName, value);
				}
				list.add(mMap);
			}
			rs.close();
			return list;
		} catch (SQLException se) {
			se.printStackTrace();
			if (se.getMessage() != null) {
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				System.exit(1);
			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			instance = null;
		}
		return null;
	}

	public static HashMap<String, String> getUserDetails(String partyId) {
		HashMap<String, String> hMap = new HashMap<>();
		String db = "Livecareer";
		String query = "select * from [documents].[dbo].[document] where documentID = "
				+ partyId;
		// hMap = getResultSet(db,query);
		return hMap;
	}

	@Test
	public static HashMap<String, String> executeDbQuery(String docId) {
		HashMap<String, String> hMap = new HashMap<>();
		String db = "documents";
		/*
		 * //int planId1 = 60; //String query =
		 * "select * from "+db+".dbo.[Plan] where planID = 60";
		 */
		String query = "select * from [documents].[dbo].[document] where documentID = "
				+ docId;
		// hMap = getResultSet(db,query);
		// System.out.println("Result :" + hMap);
		return hMap;

	}

	@Test
	public static HashMap<String, String> executeDbQuery(String userId,
			String portalId, String docTypeCD) {
		HashMap<String, String> hMap = new HashMap<>();
		String db = "documents";
		/*
		 * //int planId1 = 60; //String query =
		 * "select * from "+db+".dbo.[Plan] where planID = 60";
		 */
		String query = "Select count(*) as Count from [documents].[dbo].[document] where partyid = "
				+ userId
				+ " AND documentTypeCD = "
				+ "'"
				+ docTypeCD
				+ "'"
				+ " AND portalId = " + portalId;
		// hMap = getResultSet(db,query);
		// System.out.println("Result :" + hMap);
		return hMap;

	}

	@Test
	public static HashMap<String, String> executeDbQuery(String userId,
			String docTypeCD) {

		HashMap<String, String> hMap = new HashMap<>();
		String db = "documents";
		/*
		 * //int planId1 = 60; //String query =
		 * "select * from "+db+".dbo.[Plan] where planID = 60";
		 */
		String query = "Select count(*) as Count from [documents].[dbo].[document] where partyid = "
				+ userId + " AND documentTypeCD = " + "'" + docTypeCD + "'";
		// hMap = getResultSet(db,query);
		// System.out.println("Result :" + hMap);
		return hMap;

	}

	@Test
	public static HashMap<String, String> executeDbQueryWithResumeCheck(
			String portalId, String documentId) {

		HashMap<String, String> hMap = new HashMap<>();
		String db = "documents";
		/*
		 * //int planId1 = 60; //String query =
		 * "select * from "+db+".dbo.[Plan] where planID = 60";
		 */
		String query = "Select count(*) as Count from [documents].[dbo].[document] where portalid = "
				+ portalId + " AND documentID = " + documentId;
		// hMap = getResultSet(db,query);
		// System.out.println("Result :" + hMap);
		return hMap;

	}

	@Test
	public static HashMap<String, String> executeDbQueryToDocumentPreference(
			String documentId) {

		HashMap<String, String> hMap = new HashMap<>();
		String db = "documents";
		String query = "Select * from [Documents].[dbo].[DocPreference] where DocumentID = "
				+ documentId;
		// String query =
		// "SELECT  * FROM [Documents].[dbo].[DocPreference] where [DocumentID] = "
		// + documentId +
		// "AND DocPreferenceTypeCD IN ('PRIV','WHJS','JHOP','HLSK')";
		// hMap = getResultSet(db,query);
		// System.out.println("Result :" + hMap);
		return hMap;
	}

	@Test
	public static HashMap<String, String> executeDbQueryToVerifyDocumentFileCreated(
			String documentId) {

		HashMap<String, String> hMap = new HashMap<>();
		String db = "documents";
		String query = "Select * from [Documents].[dbo].[DocFile] where DocumentID = "
				+ documentId;
		// hMap = getResultSet(db,query);
		// System.out.println("Result :" + hMap);
		return hMap;
	}

	@Test
	public static HashMap<String, String> executeDbQueryToFetchDocumentFiles(
			String documentId) {

		HashMap<String, String> hMap = new HashMap<>();
		String db = "documents";
		String query = "Select * from [Documents].[dbo].[File] where DocumentID = "
				+ documentId;
		// hMap = getResultSet(db,query);
		// System.out.println("Result :" + hMap);
		return hMap;
	}

	@Test
	public static HashMap<String, String> executedbQueryToFetchPortalId(
			String partyId) {

		HashMap<String, String> hMap = new HashMap<>();
		String db = "Livecareer";
		String query = "Select * from [Livecareer].[dbo].[Person] where PartyID = "
				+ partyId;
		// hMap = getResultSet(db,query);
		// System.out.println("Result :" + hMap);
		return hMap;
	}

	@Test
	public static HashMap<String, String> executeDbQueryToVerifyDocumentPostData(
			String partyId) {

		HashMap<String, String> hMap = new HashMap<>();
		String db = "Documents";
		String query = "Select * from [Documents].[dbo].[document] where PartyID = "
				+ partyId;
		// hMap = getResultSet(db,query);
		// System.out.println("Result :" + hMap);
		return hMap;
	}

	/**
	 * DB utility method for update, delete query
	 * @param query
	 *            query to be hit on DB
	 */
	public void executeNonQuery(String query) {

		try {
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			stmt.executeUpdate(query);
		} catch (SQLException se) {
			se.printStackTrace();
			if (se.getMessage() != null) {
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				System.exit(1);
			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		instance = null;
	}

	public void executeStoredProcedure(String storedProcedureName) {
		CallableStatement callableStatement = null;
		try {
			callableStatement = connection.prepareCall(storedProcedureName);
			callableStatement.execute();
		} catch (SQLException se) {
			se.printStackTrace();
			if (se.getMessage() != null) {
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				System.exit(1);
			}
		} finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
			} catch (SQLException se2) {
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		instance = null;
	}

}
