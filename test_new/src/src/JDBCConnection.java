package src;

import java.sql.*;
import java.util.ArrayList;

public class JDBCConnection {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://app06.preprod.hs18.lan:3306/hsn18db";
	static final String DB_URLCMS = "jdbc:mysql://app06.preprod.hs18.lan:3306/hsn18cmsds";
	// Database credentials
	static final String USER = "hs18_dfcserver";
	static final String PASS = "6F($3Rv3R!";
	public Connection con = null;
	public Connection concms = null;
	public PreparedStatement stmt = null;
	public ResultSet rs;
	static final String USERCMS = "cmsdsUW";
	static final String PASSCMS = "Dv2s3QS31d";

	public JDBCConnection() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		con = DriverManager.getConnection(DB_URL, USER, PASS);
		concms = DriverManager.getConnection(DB_URLCMS, USERCMS, PASSCMS);
	}

	public void chkConnection() throws SQLException, Exception {
		if (con == null || con.isClosed())
			con = DriverManager.getConnection(DB_URL, USER, PASS);
		else if (concms == null || concms.isClosed()) {
			con = DriverManager.getConnection(DB_URLCMS, USERCMS, PASSCMS);
		}

	}

	public int checkSuborderState(String prodsuborder) {
		int state = 0;
		try {

			chkConnection();
			String sql = "select * FROM ISMPST where PSTRFNUM=? ";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, prodsuborder);
			rs = stmt.executeQuery();
			// rs=stmt.getResultSet();
			while (rs.next()) {
				state = rs.getInt("PSTSMTSTATUS");
			}
			stmt.close();
			con.close();
			return state;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return state;
	}

	public String checkShipmentMode(String suborder) {
		String mode = null;
		try {

			chkConnection();
			String sql = "select omsflag3 FROM ISMPST where PSTRFNUM=? ";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, suborder);
			rs = stmt.executeQuery();
			// rs=stmt.getResultSet();
			while (rs.next()) {
				mode = rs.getString("omsflag3");
			}
			stmt.close();
			con.close();
			return mode;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return mode;
	}

	public ArrayList<Integer> zonalCourierRate(String fpincode, String tpincode) {
		ArrayList<Integer> result = new ArrayList<>();
		try {
			chkConnection();
			String sql = "select fwd_rate,rto_rate from courier_pincode_rate_master where from_pincode=?"
					+ "and to_pincode=? ";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, fpincode);
			stmt.setString(2, tpincode);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result.add(rs.getInt("fwd_rate"));
				result.add(rs.getInt("rto_rate"));
			}
			stmt.close();
			con.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	public int grossWeight(String suborder) {
		int result = 0;
		try {
			chkConnection();
			
			String sql = "select weight from  product_costing_attributes where pilrfnum=("
					+ " select pstpilrfnum FROM ISMPST where PSTRFNUM=?)";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, suborder);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("fwd_rate");
			}
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	public int grossVolume(String suborder) {
		int result = 1;
		try {
			chkConnection();
			
			String sql = " select length,breadth,height from item_volumetric_dimensions  where item_id=("
					+ " select pstpilrfnum FROM ISMPST where PSTRFNUM=?)";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, suborder);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = result*rs.getInt("length");
				result = result*rs.getInt("breadth");
				result = result*rs.getInt("height");
			}			
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	public void updateSuborderInPstBeforeChange(String suborder, String prodsuborder) throws SQLException, Exception {
		try {
			chkConnection();

			String sql1 = "UPDATE ISMPST SET PSTRFNUM = ? WHERE PSTRFNUM=?";
			stmt = con.prepareStatement(sql1);
			stmt.setString(1, prodsuborder);
			stmt.setString(2, suborder);
			stmt.executeUpdate();
			// stmt.executeUpdate("UPDATE ISMPST SET PSTRFNUM
			// ="+prodsuborder+"WHERE PSTRFNUM="+suborder+";");
			// stmt.executeUpdate("UPDATE ISMPST SET CREATEDATE
			// ="+date+",MODIDATE="+date+",STLSTATUSDATE="+date+",PSTCOURIERNAME
			// ="+COURIERID+" ,PSTCOURIERNAMETEXT="+couriername+",set PSTAWBNUM
			// ="+awb+"WHERE PSTRFNUM="+prodsuborder);

			stmt.close();
			con.close();
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateSuborderInPstAfterChange(String date, String COURIERID, String couriername, String suborder,
			String prodsuborder, String awb) throws SQLException, Exception {
		try {
			chkConnection();

			String sql1 = "UPDATE ISMPST SET  PSTCOURIERNAME =?,PSTCOURIERNAMETEXT=?,PSTAWBNUM =?WHERE PSTRFNUM=?";
			stmt = con.prepareStatement(sql1);

			stmt.setString(1, COURIERID);
			stmt.setString(2, couriername);
			stmt.setString(3, awb);
			stmt.setString(4, prodsuborder);
			stmt.executeUpdate();

			stmt.close();
			con.close();
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateSuborderCourierInPst(String courierid, String couriername, String suborder)
			throws SQLException, Exception {
		try {
			chkConnection();

			String sql1 = "UPDATE ISMPST SET  PSTCOURIERNAME =?,PSTCOURIERNAMETEXT=? WHERE PSTRFNUM=?";
			stmt = con.prepareStatement(sql1);

			stmt.setString(1, courierid);
			stmt.setString(2, couriername);
			stmt.setString(3, suborder);
			stmt.executeUpdate();

			stmt.close();
			con.close();
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateCourierInStl(String courierid, String couriername, String suborder, String state) {
		try {
			chkConnection();

			String sql1 = "UPDATE ISMSTL SET STLCOURIERNAME=?,STLCOURIERNAMETEXT=?WHERE STLPSTRFNUM=? and STLSMTTSTATE=?";
			stmt = con.prepareStatement(sql1);
			stmt.setString(1, courierid);
			stmt.setString(2, couriername);
			stmt.setString(3, suborder);
			stmt.setString(4, state);
			stmt.executeUpdate();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateInStlFor2107(String suborder, String prodsuborder) {
		try {
			chkConnection();
			String sql = "UPDATE ISMSTL SET STLPSTRFNUM=? where STLPSTRFNUM=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, prodsuborder);
			stmt.setString(1, suborder);
			stmt.executeUpdate();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateSuborderInStl(String date, String COURIERID, String couriername, String suborder,
			String prodsuborder, String awb, String state) {

		try {
			chkConnection();

			String sql1 = "UPDATE ISMSTL SET  CREATEDATE =?,MODIDATE=?,STLSTATUSDATE =?,STLAWBNUM=?,STLCOURIERNAME=?,STLCOURIERNAMETEXT=?WHERE STLPSTRFNUM=? and STLSMTTSTATE=?";
			stmt = con.prepareStatement(sql1);
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
			con.close();
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateReverseInStl(String COURIERID, String couriername, String prodsuborder, String awb,
			String state) {
		try {
			chkConnection();

			String sql1 = "UPDATE ISMSTL SET STLAWBNUM=?,STLCOURIERNAME=?,STLCOURIERNAMETEXT=?WHERE STLPSTRFNUM=? and STLSMTTSTATE=?";
			stmt = con.prepareStatement(sql1);
			stmt.setString(1, awb);
			stmt.setString(2, COURIERID);
			stmt.setString(3, couriername);
			stmt.setString(4, prodsuborder);
			stmt.setString(5, state);
			stmt.executeUpdate();

			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateReverseInCmsStl(String COURIERID, String caseid, String awb) {
		try {
			chkConnection();

			String sql1 = "update hsn18_cms_returns_attributes_data set attribute_value = ?  where attribute_code = 'courierId' and task_pi_id='BPMN_ST_ReversePickupRequired' and returns_entity_id in   (Select id  from  hsn18_cms_returns where business_id=?)";
			stmt = concms.prepareStatement(sql1);
			stmt.setString(1, COURIERID);
			stmt.setString(2, caseid);
			stmt.executeUpdate();
			stmt.close();
			// concms.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateReverseInCmsStl2(String COURIERID, String caseid, String awb) {
		try {
			chkConnection();
			String sql2 = "update hsn18_cms_returns_attributes_data set attribute_value = ?  where attribute_code = 'awbNumber' and task_pi_id='BPMN_ST_ReversePickupRequired' and returns_entity_id in   (Select id  from  hsn18_cms_returns where business_id=?)";
			stmt = concms.prepareStatement(sql2);
			stmt.setString(1, awb);
			stmt.setString(2, caseid);
			stmt.executeUpdate();
			stmt.close();
			// concms.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateReverseInCmsStl3(String COURIERID, String caseid, String awb) {
		try {
			chkConnection();
			String sql3 = " update hsn18_cms_case_additional_data set attribute_value = ?  where attribute_code = 'courierId' and task_pi_id='BPMN_ST_ReversePickupRequired' and case_entity_id in   (Select id  from  hsn18_cms_case_file where business_id=?)";
			stmt = concms.prepareStatement(sql3);
			stmt.setString(1, COURIERID);
			stmt.setString(2, caseid);
			stmt.executeUpdate();

			stmt.close();
			// concms.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateReverseInCmsStl4(String COURIERID, String caseid, String awb) {
		try {
			chkConnection();
			String sql4 = " update hsn18_cms_case_additional_data set attribute_value = ?  where attribute_code = 'awbNumber' and task_pi_id='BPMN_ST_ReversePickupRequired' and case_entity_id in   (Select id  from  hsn18_cms_case_file where business_id=?)";
			stmt = concms.prepareStatement(sql4);
			stmt.setString(1, awb);
			stmt.setString(2, caseid);
			stmt.executeUpdate();
			stmt.close();
			// concms.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
