package com.hs18.DataUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hs18.util.AppGroupMapping;

public class DBQuery {

	//// public DBQuery() throws SQLException, ClassNotFoundException,
	//// IOException {
	// super(DB_URL);
	// // TODO Auto-generated constructor stub
	// }
	static DbConnection db = null;
	public PreparedStatement stmt = null;
	public ResultSet rs;
	private DBQuery dbQuery;

	public DBQuery getInstance() throws Exception {
		if (dbQuery == null) {
			dbQuery = new DBQuery();
		}
		return dbQuery;
	}

	public LinkedHashMap<String, ArrayList<String>> classes(String Application, String groupName)
			throws SQLException, ClassNotFoundException, IOException {
		ArrayList<String> testCase = new ArrayList<String>();
		LinkedHashMap<String, ArrayList<String>> mapping = new LinkedHashMap<String, ArrayList<String>>();
		 db = new DbConnection("automation");
		try {
			db.chkConnection("automation");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			String sql = "select tc.Class_Name, tc.TestCase_Name  from TestCase tc join Execution e"
					+ " on e.TestCase_id= tc.TestCase_id " + " join Application_Group ag"
					+ " on ag.Application_Group_id=e.Application_Group_id" + " join Application a"
					+ " on a.Application_id=ag.Application_id" + " join Groups gr" + " on gr.Group_id = ag.Group_id"
					+ " where a.application_name=?" + "  and gr.Group_name=?;";
			stmt = db.con.prepareStatement(sql);
			stmt.setString(1, Application);
			stmt.setString(2, groupName);
			rs = stmt.executeQuery();
			while (rs.next()) {

				if (mapping.containsKey(rs.getString(1))) {
					testCase = mapping.get(rs.getString(1));
					testCase.add(rs.getString(2));
					mapping.put(rs.getString(1), testCase);
				} else {
					testCase = new ArrayList<>();
					testCase.add(rs.getString(2));
					mapping.put(rs.getString(1), testCase);
				}
			}

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			stmt.close();
			db.con.close();
		}
		return mapping;
	}

	public ArrayList<String> applicationsName(String groupname) {
		ArrayList<String> totalapps = new ArrayList<String>();
		return totalapps;
	}

	public ArrayList<AppGroupMapping> getGroupNames() throws SQLException {
		ArrayList<AppGroupMapping> appnameList = new ArrayList<AppGroupMapping>();
		
		try {
			if(db==null)
			{
			db = new DbConnection("automation");
			}
			db.chkConnection("automation");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			String sql = "  select app.application_name ,gr.Group_name FROM Application app join Application_Group ag "
					+ "on ag.Application_id=app.Application_id" + " join Groups gr on ag.Group_id=gr.Group_id; ";
			stmt = db.con.prepareStatement(sql);
			rs = stmt.executeQuery();
			List<String> groups = new ArrayList<String>();
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			while (rs.next()) {
				if (map.containsKey(rs.getString(1))) {
					groups = map.get(rs.getString(1));
					groups.add(rs.getString(2));
					map.put(rs.getString(1), groups);
				} else {
					groups = new ArrayList<String>();
					groups.add(rs.getString(2));
					map.put(rs.getString(1), groups);
				}
			}

			Set<String> keys = map.keySet();

			for (String appname : keys) {
				AppGroupMapping appGroupMapping = new AppGroupMapping();
				appGroupMapping.setAppName(appname);
				appGroupMapping.setGroup(map.get(appname));
				appnameList.add(appGroupMapping);
			}

		}

		catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
			if (!(stmt == null)) {
				stmt.close();
			}
			db.con.close();
		}
		return appnameList;
	}

	public int checkSuborderState(String prodsuborder) {
		int state = 0;
		
		try {
			if(db==null)
			{
			db = new DbConnection("preprod");
			}
			db.chkConnection("preprod");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String sql = "select * FROM ISMPST where PSTRFNUM=? ";
			stmt = db.con.prepareStatement(sql);
			stmt.setString(1, prodsuborder);
			rs = stmt.executeQuery();
			// rs=stmt.getResultSet();
			while (rs.next()) {
				state = rs.getInt("PSTSMTSTATUS");
			}
			stmt.close();
			//db.con.close();
			return state;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return state;
	}

	public void updateInStlFor2107(String suborder, String prodsuborder) {
		DbConnection db = null;
		try {
			if(db==null)
			{
			db = new DbConnection("preprod");
			}
			db.chkConnection("preprod");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			String sql = "UPDATE ISMSTL SET STLPSTRFNUM=? where STLPSTRFNUM=?";
			stmt = db.con.prepareStatement(sql);
			stmt.setString(1, prodsuborder);
			stmt.setString(1, suborder);
			stmt.executeUpdate();
			stmt.close();
			//db.con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateSuborderInPstBeforeChange(String suborder, String prodsuborder) throws SQLException, Exception {
		DbConnection db = null;
		try {
			if(db==null)
			{
			db = new DbConnection("preprod");
			}
			db.chkConnection("preprod");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			String sql1 = "UPDATE ISMPST SET PSTRFNUM = ? WHERE PSTRFNUM=?";
			stmt = db.con.prepareStatement(sql1);
			stmt.setString(1, prodsuborder);
			stmt.setString(2, suborder);
			stmt.executeUpdate();
			stmt.close();
			//db.con.close();
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateSuborderInPstAfterChange(String date, String COURIERID, String couriername, String suborder,
			String prodsuborder, String awb) throws SQLException, Exception {
		DbConnection db = null;
		try {
			if(db==null)
			{
			db = new DbConnection("preprod");
			}
			db.chkConnection("preprod");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			String sql1 = "UPDATE ISMPST SET  PSTCOURIERNAME =?,PSTCOURIERNAMETEXT=?,PSTAWBNUM =?WHERE PSTRFNUM=?";
			stmt = db.con.prepareStatement(sql1);
			stmt.setString(1, COURIERID);
			stmt.setString(2, couriername);
			stmt.setString(3, awb);
			stmt.setString(4, prodsuborder);
			stmt.executeUpdate();
			stmt.close();
			//db.con.close();
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateSuborderInStl(String date, String COURIERID, String couriername, String suborder,
			String prodsuborder, String awb, String state) {
		DbConnection db = null;
		try {
			if(db==null)
			{
			db = new DbConnection("preprod");
			}
			db.chkConnection("preprod");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			String sql1 = "UPDATE ISMSTL SET  CREATEDATE =?,MODIDATE=?,STLSTATUSDATE =?,STLAWBNUM=?,STLCOURIERNAME=?,STLCOURIERNAMETEXT=?WHERE STLPSTRFNUM=? and STLSMTTSTATE=?";
			stmt = db.con.prepareStatement(sql1);
			stmt.setString(1, date);
			stmt.setString(2, date);
			stmt.setString(3, date);
			stmt.setString(4, awb);
			stmt.setString(5, COURIERID);
			stmt.setString(6, couriername);
			stmt.setString(7, prodsuborder);
			stmt.setString(8, state);
			stmt.executeUpdate();
			stmt.close();
			//db.con.close();
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
