package com.lc.tests.trackingservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.TrackingServiceCoreComponents;
import com.lc.dataprovider.TrackingService_DP;
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.ReadProperties;

import static com.lc.constants.Constants_TrackingService.*;

public class TrackingService extends TrackingServiceCoreComponents{

	List<Map> referrer = null;
	List<Map> defaultReferrer = null;
	HashMap<String, String> headers = null;

	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	@BeforeClass
	public void classInit()
	{
		headers = new HashMap<String, String>();
		headers.put("clientcd", "LCAUS");
		headers.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
		headers.put("Accept-Language","en-US,en;q=0.5");
		referrer = getReferrer("Rehan Reg");
		defaultReferrer = getReferrer("Livecareer Default");
	}

	//Test that when visit is created using referrer_id then utm_source, utm_camiagn, utm_content will be set based on referrer id
	@Test
	public void createVisitWithReferrerID()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("referrer_id", referrer.get(0).get("ReferrerID"));
			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals(referrer.get(0).get("ReferrerID").toString(), respJson.getString("ref_id"), "ref_id mismatch");

			//Visitor table verification
			String query_Visitor = "select * from dbo.Visitor where VisitorUID ='" + respJson.getString("visitor_uid") + "'";
			List<Map> list_Visitor = DbUtil.getInstance("Livecareer").getResultSetList(query_Visitor);
			assertEquals(list_Visitor.get(0).get("VisitorUID").toString(), respJson.getString("visitor_uid"), "visitor_uid mismatch");
			assertEquals(referrer.get(0).get("utm_source").toString(), list_Visitor.get(0).get("Source").toString(), "Source mismatch in Visitor table");
			assertEquals(referrer.get(0).get("utm_medium").toString(), list_Visitor.get(0).get("Medium").toString(), "Medium mismatch in Visitor table");
			assertEquals(referrer.get(0).get("utm_campaign").toString(), list_Visitor.get(0).get("Campaign").toString(), "Campaign mismatch in Visitor table");

			//HitLog table verification
			String query_HitLog = "select * from dbo.HitLog where VisitorUID ='" + respJson.getString("visitor_uid") + "'";
			List<Map> list_HitLog = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLog);
			assertEquals(list_HitLog.get(0).get("HitLogID").toString(), respJson.getString("visit_id"), "visit_id mismatch");
			assertEquals("N", list_HitLog.get(0).get("VisitType").toString(), "VisitType mismatch");
			assertEquals("3", list_HitLog.get(0).get("PortalID").toString(), "PortalID mismatch");

			//HitLogExtended table verification
			String query_HitLogEx = "select * from dbo.HitLogExtended where HitLogID =" + list_HitLog.get(0).get("HitLogID").toString();
			List<Map> list_HitLogEx = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLogEx);
			assertEquals("pv", list_HitLogEx.get(0).get("HitType").toString(), "HitType mismatch");

			//HitLogGA table verification
			String query_HitLogGA = "select * from dbo.HitLogGA where HitLogID =" + list_HitLog.get(0).get("HitLogID").toString();
			List<Map> list_HitLogGA = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLogGA);
			assertEquals(referrer.get(0).get("utm_source").toString(), list_HitLogGA.get(0).get("utm_source").toString(), "utm_source mismatch");
			assertEquals(referrer.get(0).get("utm_medium").toString(), list_HitLogGA.get(0).get("utm_medium").toString(), "utm_medium mismatch");
			assertEquals(referrer.get(0).get("utm_campaign").toString(), list_HitLogGA.get(0).get("utm_campaign").toString(), "utm_campaign mismatch");
			assertEquals(referrer.get(0).get("utm_content").toString(), list_HitLogGA.get(0).get("utm_content").toString(), "utm_content mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//create visit with referrer id so that targ_url is formed based on landing page CD
	@Test
	public void createVisitWithReferrerIDBasedOnLandingPageCD()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("referrer_id", 66);
			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("66", respJson.getString("ref_id"), "ref_id mismatch");
			//assertEquals("http://dev5.livecareer.com/home.aspx", respJson.getString("targ_url"), "targ_url mismatch");
			assertTrue(!respJson.getString("targ_url").equalsIgnoreCase(""), "targ_url is blank");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//create visit with referrer id so that targ_url is formed based on landing page CD
	@Test
	public void createVisitWithReferrerIDBasedOnDestinationID()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("referrer_id", 66);
			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			assertEquals("66", respJson.getString("ref_id"), "ref_id mismatch");
			//assertEquals("http://dev5.livecareer.com/home.aspx", respJson.getString("targ_url"), "targ_url mismatch");
			assertTrue(!respJson.getString("targ_url").equalsIgnoreCase(""), "targ_url is blank");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//TS-65
	//When insert visit by Post method API is called with invalid value for referrer_id then error message should be shown
	@Test
	public void createVisitWithInavlidReferrerID()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("referrer_id", 54321);
			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());
			//JSONObject respJson = new JSONObject(response.message);
			assertResponseCode(response.code, 500);
			/*assertEquals("Oops, There was an internal Error, Description:An error occurred while updating the entries. See the inner exception for details.", respJson, "description");
			assertEquals("API_ERROR", respJson, "error_code");*/
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by Post method API is called with string value for visit_id (integer data type) then status code 400 should be returned
	@Test
	public void createVisitWithVisitIDOfStringDataType()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visit_id", "jkjgsdfgkaks");

			createVisitWithInvalidDataTypeValue(dataToPost, "visitRequest.visit_id");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by Post method API is called with string value for referrer_id (integer data type) then status code 400 should be returned
	@Test
	public void createVisitWithReferrerOfStringDataType()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("referrer_id", "jkjgsdfgkaks");

			createVisitWithInvalidDataTypeValue(dataToPost, "visitRequest.referrer_id");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by Post method API is called with string value for source_id (integer data type) then status code 400 should be returned
	@Test
	public void createVisitWithSourceIDOfStringDataType()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("source_id", "jkjgsdfgkaks");

			createVisitWithInvalidDataTypeValue(dataToPost, "visitRequest.source_id");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by Post method API is called with string value for destination_id (integer data type) then status code 400 should be returned
	@Test
	public void createVisitWithDestinationIDOfStringDataType()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("destination_id", "jkjgsdfgkaks");

			createVisitWithInvalidDataTypeValue(dataToPost, "visitRequest.destination_id");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by Post method API is called with string value for party_id (integer data type) then status code 400 should be returned
	@Test
	public void createVisitWithPartyIDOfStringDataType()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("party_id", "jkjgsdfgkaks");

			createVisitWithInvalidDataTypeValue(dataToPost, "visitRequest.party_id");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by Post method API is called with string value for mailout_id (integer data type) then status code 400 should be returned
	@Test
	public void createVisitWithMailoutIDOfStringDataType()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("mailout_id", "jkjgsdfgkaks");

			createVisitWithInvalidDataTypeValue(dataToPost, "visitRequest.mailout_id");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method API is called with invalid visitor_uid then status code 400 should be returned
	@Test
	public void createVisitWithInvalidVistorUID()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visitor_uid", "hfdsgh98erhhgooeayof893hef");

			createVisitWithInvalidDataTypeValue(dataToPost, "visitRequest.visitor_uid");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method API is called with visit_id and visitor_uid but No hit type then no new visit id will be created and existing visit id and visitor uid will be returned in response
	@Test
	public void createVisitWithVisitIDAndVisitorUIDButNoHitType()
	{
		try
		{
			JSONObject sampleVisit = createSampleVisit();
			String visitorUID = sampleVisit.getString("visitor_uid");
			String visitID = sampleVisit.getString("visit_id");

			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visitor_uid", visitorUID);
			dataToPost.put("visit_id", visitID);

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			String query_HitLog_Count = "select count(*) as count from dbo.HitLog where VisitorUID ='" + visitorUID + "'";
			List<Map> list_HitLog_Count = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLog_Count);
			assertEquals(visitorUID, respJson.getString("visitor_uid"), "visitor_uid mismatch");
			assertEquals(visitID, respJson.getString("visit_id"), "visit_id mismatch");
			assertEquals("1", list_HitLog_Count.get(0).get("count").toString(), "Visit ID Row Count Mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method API is called with hit type but NO visit id then new visit id will be created and new visit id will be returned in response
	@Test
	public void createVisitWithHitTypeButNoVisitID()
	{
		try
		{
			JSONObject sampleVisit = createSampleVisit();
			String visitorUID = sampleVisit.getString("visitor_uid");

			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visitor_uid", visitorUID);
			dataToPost.put("ht", "eo");

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			verifyHitLogTablesForVisitIDAndHitTypeTests(visitorUID, respJson, "eo");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method API is called with hit type values in (eo, ec, uc) and visit id then new visit id will be created but in response old visit id will be returned
	@Test(dataProviderClass = TrackingService_DP.class, dataProvider = "getHitTypes1")
	public void createVisitWithHitTypeAndVisitID(String hitType)
	{
		try
		{
			JSONObject sampleVisit = createSampleVisit();
			String visitorUID = sampleVisit.getString("visitor_uid");
			String firstVisitID = sampleVisit.getString("visit_id");

			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visitor_uid", visitorUID);
			dataToPost.put("visit_id", firstVisitID);
			dataToPost.put("ht", hitType);

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			assertEquals(firstVisitID, respJson.getString("visit_id"), "response visit_id mismatch");

			verifyHitLogTablesForVisitIDAndHitTypeTests(visitorUID, respJson, hitType);
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method API is called with No hit type and no visit id then new visit id will be created 
	@Test
	public void createVisitWithNoHitTypeAndNoVisitID()
	{
		try
		{
			JSONObject sampleVisit = createSampleVisit();
			String visitorUID = sampleVisit.getString("visitor_uid");

			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visitor_uid", visitorUID);

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			verifyHitLogTablesForVisitIDAndHitTypeTests(visitorUID, respJson, "pv");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method API is called with hit type other than eo, ec, uc and valid value for visit_id parameter then NO NEW visit id will be created
	//This will happen for hit types = lp, pv, ac, ap, sc
	@Test(dataProviderClass = TrackingService_DP.class, dataProvider = "getHitTypes2")
	public void createVisitWithHitTypeOtherThanEoEcUcAndVisitID(String hitType)
	{
		try
		{
			JSONObject sampleVisit = createSampleVisit();
			String visitorUID = sampleVisit.getString("visitor_uid");
			String visitID = sampleVisit.getString("visit_id");

			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visitor_uid", visitorUID);
			dataToPost.put("visit_id", visitID);
			dataToPost.put("ht", hitType);

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			String query_HitLog_Count = "select count(*) as count from dbo.HitLog where VisitorUID ='" + visitorUID + "'";
			List<Map> list_HitLog_Count = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLog_Count);
			assertEquals(visitorUID, respJson.getString("visitor_uid"), "visitor_uid mismatch");
			assertEquals(visitID, respJson.getString("visit_id"), "visit_id mismatch");
			assertEquals("1", list_HitLog_Count.get(0).get("count").toString(), "Visit ID Row Count Mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method API is called with random GUID value for visitor_uid parameter then No_Data_Found custom message should be shown.
	@Test
	public void createVisitWithRandomGuidValueForVisitorUID()
	{
		try
		{
			UUID visitorUID = UUID.randomUUID();
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visitor_uid", visitorUID);

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());
			JSONObject respJson = new JSONObject(response.message);

			assertResponseCode(response.code, 500);
			assertEquals("Oops, There was an internal Error", respJson, "description");
			assertEquals("NO_DATA_FOUND", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by get method API is called with referrer (not encoded and encoded) then visit should be created successfully.
	@Test(dataProviderClass = TrackingService_DP.class, dataProvider = "getReferrer")
	public void insertVisitByGetMethod(String referrer)
	{
		try
		{
			String partyId =  "9" + RandomStringUtils.randomNumeric(7);

			ResponseBean response = HttpService.get(createVisitUrl  + "?clientcd=LCAUS&referrer_id=14&source_id=144&destination_id=156&party_id=" + partyId +"&mailout_id=113&keywords=HelloTS&ht=lp&visitor_uid=&data=&referrer=" + referrer + "&query_string=%7Butm_source:abcd&&utm_medium:efgh&&utm_campaign:ijkl&&utm_content:mnop&&utm_term:qrst&&SID:uvwx&&match_type:yzab&&ad:cdef&&placement:ghij&&network:klmn&&occ:opqr&&lpcd:stuv%7D&visit_id=", headers);

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			String visitorUID = respJson.getString("visitor_uid");
			String visitID = respJson.getString("visit_id");

			String query_HitLog = "select VisitorUID, HitLogID from dbo.HitLog where VisitorUID ='" + visitorUID + "'";
			List<Map> list_HitLog = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLog);
			assertEquals(list_HitLog.get(0).get("VisitorUID").toString(), visitorUID, "visitor_uid mismatch");
			assertEquals(list_HitLog.get(0).get("HitLogID").toString(), visitID, "Visit ID Mismatch");

			verifyHitLogTables(visitorUID, visitID, partyId, "stuv", "14", "qrst", 10, 3, 156, "lp", "144");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by get method API without query_string parameter then visit should be created successfully.
	@Test
	public void insertVisitByGetMethodWithoutQueryStringParameter()
	{
		try
		{
			ResponseBean response = HttpService.get(createVisitUrl, headers);

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			String visitorUID = respJson.getString("visitor_uid");

			String query_HitLog = "select VisitorUID, HitLogID from dbo.HitLog where VisitorUID ='" + visitorUID + "'";
			Map hitLog = DbUtil.getInstance("Livecareer").getResultSet(query_HitLog);
			assertEquals(hitLog.get("VisitorUID").toString(), respJson.getString("visitor_uid"), "visitor_uid mismatch");
			assertEquals(hitLog.get("HitLogID").toString(), respJson.getString("visit_id"), "Visit ID Mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When get portal API is called with valid portal then corresponding detail portal should be shown 
	@Test
	public void getPortal()
	{
		try
		{
			HashMap<String, String> header = new HashMap<String, String>();
			String baseUrl = ReadProperties.getbaseURL();
			if(baseUrl.contains("reg"))
			{
				header.put("ClientCD", "LCAUK");
				ResponseBean response = HttpService.get(getPortalUrl + "www.livecareer.co.uk" , header);
				checkSuccessCode(response.code);
				JSONObject respJson = new JSONObject(response.message);
				assertEquals("19", respJson.getString("PortalId"), "PortalId Mismatch");
			}
			else if(baseUrl.contains("dev"))
			{
				header.put("ClientCD", "LCAUS");
				ResponseBean response = HttpService.get(getPortalUrl + "qa.livecareer.com" , header);
				checkSuccessCode(response.code);
				JSONObject respJson = new JSONObject(response.message);
				assertEquals("3", respJson.getString("PortalId"), "PortalId Mismatch");
			}
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When get portal API is called with invalid portal then No_Data_Found custom message should be shown.
	@Test
	public void getPortalWithInvalidValue()
	{
		try
		{
			HashMap<String, String> header = new HashMap<String, String>();
			header.put("ClientCD", "LCAUS");
			ResponseBean response = HttpService.get(getPortalUrl + "qa3ew.lidfsgvecareer.correm" , header);

			JSONObject respJson = new JSONObject(response.message);
			assertResponseCode(response.code, 500);
			assertEquals("Oops, There was an internal Error", respJson, "description");
			assertEquals("NO_DATA_FOUND", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method is called with invalid value for ClientCD then user should not be authorized to access the resource.
	@Test
	public void createVisitByInvalidValueForClientCD()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();

			//post API
			ResponseBean response = HttpService.post(BASE_URL + "/v1/visits?clientcd=dsidou4ur", null, dataToPost.toString());

			JSONObject respJson = new JSONObject(response.message);

			assertResponseCode(response.code, 403);
			assertEquals("You are not authorized to access this resource.", respJson.getString("Message"), "message mismatch in post api");

			//get API
			response = HttpService.get(BASE_URL + "/v1/visits?clientcd=dsidou4ur", null);

			respJson = new JSONObject(response.message);

			assertResponseCode(response.code, 403);
			assertEquals("You are not authorized to access this resource.", respJson.getString("Message"), "message mismatch in get api");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//Since referrer_id = 14 does not have lpgid so lpgid will not be set in HitLogData table
	//Since hit type is not in ec, eo, uc and Accept Language is not set to empty so hit type will be set as per visit request.
	//Since domain set to Special.livecareer.com so portal id will be set based on domain
	//Since product_cd is set to RRW so product id will be set based on Product_cd
	@Test
	public void createVisitWithReferrerIDBasedOnLPGroupID()
	{
		try
		{
			String partyId =  "9" + RandomStringUtils.randomNumeric(7);

			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visitor_uid", "");
			dataToPost.put("referrer_id", 14);
			dataToPost.put("source_id", 144);
			dataToPost.put("party_id", partyId);
			dataToPost.put("mailout_id", 113);
			dataToPost.put("referrer", "http://dev2.livecareer.com:1024/home.aspx");
			dataToPost.put("keywords", "HelloTS");
			dataToPost.put("ht", "lp");
			dataToPost.put("query_string", "{utm_source:abcd&&utm_medium:efgh&&utm_campaign:ijkl&&utm_content:mnop&&utm_term:qrst&&SID:uvwx&&match_type:yzab&&ad:cdef&&placement:ghij&&network:klmn&&occ:opqr&&lpcd:stuv}");			

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			String visitorUID = respJson.getString("visitor_uid");
			String visitID = respJson.getString("visit_id");
			//assertEquals("http://careerplanning.about.com/od/careerchoicechan/a/", respJson.getString("targ_url"), "targ_url mismatch");
			assertTrue(!respJson.getString("targ_url").equalsIgnoreCase(""), "targ_url is blank");
			assertEquals("abcd", respJson.getString("source"), "targ_url mismatch");
			assertEquals("efgh", respJson.getString("medium"), "medium mismatch");
			assertEquals("mnop", respJson.getString("content"), "content mismatch");
			assertEquals("ijkl", respJson.getString("campaign"), "campaign mismatch");

			String query_referrer = "Select * from dbo.Referrer where ReferrerID = 14";
			Map list_referrer = DbUtil.getInstance("Livecareer").getResultSet(query_referrer);
			int desinationID = 0;
			if(list_referrer.get("DestinationID") != null)
				desinationID = Integer.parseInt(list_referrer.get("DestinationID").toString());

			verifyHitLogTables(visitorUID, visitID, partyId, "stuv", "14", "qrst", 10, 3, desinationID, "lp", "144");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//Here Data value will override lpcd value
	//Since referrer_id = 14 does not have lpgid so lpgid will not be set in HitLogData table
	//Since hit type is not in ec, eo, uc and Accept Language is not set to empty so hit type will be set as per visit request.
	//Since source_id is not a part of request object so source id will be set based on src parameter in query string. 
	//src parameter value will be inserted in page table and page id will be set as source id
	@Test
	public void createVisit()
	{
		try
		{
			String partyId =  "9" + RandomStringUtils.randomNumeric(7);

			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visitor_uid", "");
			dataToPost.put("referrer_id", 14);
			dataToPost.put("destination_id", 156);
			dataToPost.put("party_id", partyId);
			dataToPost.put("mailout_id", 113);
			dataToPost.put("data", "LCACDIHM00");
			dataToPost.put("referrer", "http://dev2.livecareer.com:1024/home.aspx");
			dataToPost.put("keywords", "HelloTS");
			dataToPost.put("ht", "lp");

			String file = "/" + RandomStringUtils.randomNumeric(7) + ".aspx";
			dataToPost.put("query_string", "{utm_source:abcd&&utm_medium:efgh&&utm_campaign:ijkl&&utm_content:mnop&&utm_term:best buy-owens cross roads, al&&SID:uvwx&&match_type:yzab&&ad:cdef&&placement:ghij&&network:klmn&&occ:opqr&&lpcd:stuv&&src:qa.livecareer.com/information" + file +"}");

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			String visitorUID = respJson.getString("visitor_uid");
			String visitID = respJson.getString("visit_id");
			assertTrue(!respJson.getString("targ_url").equalsIgnoreCase(""), "targ_url is blank");

			String query_referrer = "Select * from dbo.Referrer where ReferrerID = 14";
			Map list_referrer = DbUtil.getInstance("Livecareer").getResultSet(query_referrer);
			int productID = Integer.parseInt(list_referrer.get("ProductID").toString());
			int portalID = Integer.parseInt(list_referrer.get("PortalID").toString());

			//Get source ID
			String query_sourceId = "select PageID from page where [file] = '/information"+ file +"'";
			Map sourceID = DbUtil.getInstance("Livecareer").getResultSet(query_sourceId);

			verifyHitLogTables(visitorUID, visitID, partyId, "LCACDIHM00", "14", "best buy-owens cross roads, al", productID, portalID, 156, "lp", sourceID.get("PageID").toString());
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//Since data is not passed, lpcd value will be set in data field of HitLogExtended table
	//Since hit type is "lp" and referrer_id = 21 which has lpgid and destination id is not passed so lpgid will be set in HitLogData table. 
	//If hit type is not "lp" then lpgid will not be set
	//Since utm_term is not defined in query string so occ value will be set in utm_erm field
	@Test
	public void createVisitWithEncodedReferrer()
	{
		try
		{
			String partyId =  "9" + RandomStringUtils.randomNumeric(7);

			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visitor_uid", "");
			dataToPost.put("referrer_id", 21);
			dataToPost.put("source_id", 144);
			dataToPost.put("party_id", partyId);
			dataToPost.put("mailout_id", 113);
			dataToPost.put("referrer", "http%3A%2F%2Fdev2.livecareer.com%3A1024%2Fhome.aspx");
			dataToPost.put("keywords", "HelloTS");
			dataToPost.put("ht", "lp");
			dataToPost.put("query_string", "{utm_source:abcd&&utm_medium:efgh&&utm_campaign:ijkl&&utm_content:mnop&&SID:uvwx&&match_type:yzab&&ad:cdef&&placement:ghij&&network:klmn&&occ:opqr&&lpcd:stuv}");
			//dataToPost.put("domain", "special.livecareer.com");
			dataToPost.put("product_cd", "RRW");

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			String visitorUID = respJson.getString("visitor_uid");
			String visitID = respJson.getString("visit_id");

			String query_HitLogLpgID = "select Value from dbo.HitLogdata where HitLogID =" + visitID + " AND [Key] = 'LPGID'";
			Map hitLogLpgID = DbUtil.getInstance("Livecareer").getResultSet(query_HitLogLpgID);

			String query_lpgID = "select LPGroupID from Referrer where ReferrerID = 21";
			Map lpgID = DbUtil.getInstance("Livecareer").getResultSet(query_lpgID);
			assertEquals(lpgID.get("LPGroupID").toString(), hitLogLpgID.get("Value").toString(), "LPGID value mismatch");

			String query_referrer = "Select * from dbo.Referrer where ReferrerID = 21";
			Map referrer = DbUtil.getInstance("Livecareer").getResultSet(query_referrer);
			int desinationID = 0;
			if(referrer.get("DestinationID") != null)
				desinationID = Integer.parseInt(referrer.get("DestinationID").toString());

			/*String query_portal = "Select * from dbo.Portal where Url = 'special.livecareer.com'";
			Map list_portal = DbUtil.getInstance("Livecareer").getResultSet(query_portal);
			int portalID = Integer.parseInt(list_portal.get("PortalID").toString());
			 */
			String query_product = "Select * from dbo.Product where ProductCD = 'RRW'";
			Map product = DbUtil.getInstance("Livecareer").getResultSet(query_product);
			int productID = Integer.parseInt(product.get("ProductID").toString());

			String query_data = "select LandingpageCD from lpgrouppages where LpGroupID = (select LPGroupID from Referrer where ReferrerID = 21) and Weight = 100";
			Map data = DbUtil.getInstance("Livecareer").getResultSet(query_data);

			verifyHitLogTables(visitorUID, visitID, partyId, data.get("LandingpageCD").toString(), "21", "opqr", productID, 3, desinationID, "lp", "144");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visiyt by post method API is called with hit type other than LP, referrer_id = 21 which does not have lpgid, and destination id is not passed then visit will be created successfully 
	//BUT lpgid will not be set in HitLogExtended table.
	@Test(dataProviderClass = TrackingService_DP.class, dataProvider = "getHitTypes4")
	public void createVisitWhenHitTypeIsOtherThanLP(String hitType)
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("referrer_id", 21);
			if(hitType == null)
				dataToPost.put("ht", JSONObject.NULL);
			else
				dataToPost.put("ht", hitType);

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);
			String visitID = respJson.getString("visit_id");

			String query_HitLogLpgID = "select Count(*) as Count from dbo.HitLogdata where HitLogID =" + visitID + " AND [Key] = 'LPGID'";
			Map hitLogLpgID = DbUtil.getInstance("Livecareer").getResultSet(query_HitLogLpgID);

			assertEquals("0", hitLogLpgID.get("Count").toString(), "LpgId count mismatch in HitLogExtended Table");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method APi is called with hit type(ht) null and Accept Language NOT empty then visit will be created with hit type set to "pv".
	@Test
	public void createVisitWhenAcceptLangauageIsNotEmptyAndHitTypeIsNull()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("referrer_id", 21);
			dataToPost.put("ht", JSONObject.NULL);

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			String visitID = respJson.getString("visit_id");

			//HitLogExtended table verification
			String query_HitLogEx = "select * from dbo.HitLogExtended where HitLogID =" + visitID;
			Map HitLogEx = DbUtil.getInstance("Livecareer").getResultSet(query_HitLogEx);
			assertEquals("pv", HitLogEx.get("HitType").toString(), "HitType mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method API is called with accept Language Header set to empty and hit type(ht) is not amongst ec, oc, uc then visit will be created with hit type set to "sc".
	@Test(dataProviderClass = TrackingService_DP.class, dataProvider = "getHitTypes3")
	public void createVisitWithEmptyAcceptLanguageAndHitTypeValueAsOtherThanEcOcUc(String hitType)
	{
		try
		{
			//Set Accept Language Header to Empty
			HashMap<String, String> header = new HashMap<String, String>();
			header.put("clientcd", "LCAUS");
			header.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			header.put("Accept-Language","");

			JSONObject dataToPost = new JSONObject();
			if(hitType == null)
				dataToPost.put("ht", JSONObject.NULL);
			else
				dataToPost.put("ht", hitType);

			ResponseBean response = HttpService.post(createVisitUrl, header, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			String visitID = respJson.getString("visit_id");

			//HitLogExtended table verification
			String query_HitLogEx = "select * from dbo.HitLogExtended where HitLogID =" + visitID;
			Map HitLogEx = DbUtil.getInstance("Livecareer").getResultSet(query_HitLogEx);
			assertEquals("sc", HitLogEx.get("HitType").toString(), "HitType mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When get referrer API is called with valid referrer id then API returns result by combining data from two tables Referrer and Affiliate
	@Test
	public void getReferrer()
	{
		try
		{
			HashMap<String, String> header = new HashMap<String, String>();
			header.put("ClientCD", "LCAUS");
			ResponseBean response = HttpService.get(getReferrer + 64 , header);
			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			//referrer table verification
			assertEquals(64, respJson.getInt("ReferrerId"), "ReferrerId Mismatch");
			assertEquals(18, respJson.getInt("AffiliateId"), "AffiliateId Mismatch");
			assertEquals(1, respJson.getInt("PageId"), "PageId Mismatch");
			assertEquals(4, respJson.getInt("Status"), "Status Mismatch");
			assertEquals("pg", respJson.getString("Type"), "Type Mismatch");
			assertEquals("kip test", respJson.getString("Description"), "Description Mismatch");
			assertEquals(3, respJson.getInt("PortalId"), "PortalId Mismatch");
			assertEquals(1, respJson.getInt("ProductId"), "ProductId Mismatch");
			assertEquals(4, respJson.getInt("LpGroupId"), "LpGroupId Mismatch");
			assertEquals("kip test", respJson.getString("AffiliateDescription"), "AffiliateDescription Mismatch");
			assertEquals("tc", respJson.getString("CommissionMode"), "CommissionMode Mismatch");

			//affiliate table verification
			assertEquals("60", respJson.getString("CommissionLifeTime"), "CommissionLifeTime Mismatch");
			assertTrue(respJson.getString("Mode") == "null", "Mode not null");
			assertTrue(respJson.getString("CobrandCD") == "null", "CobrandCD not null");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When get referrer API is called with invalid referrer id  then error message should be shown.
	@Test
	public void getReferrerWithInvalidValue()
	{
		try
		{
			HashMap<String, String> header = new HashMap<String, String>();
			header.put("ClientCD", "LCAUS");

			ResponseBean response = HttpService.get(getReferrer + 54321 , header);

			//JSONObject respJson = new JSONObject(response.message);
			
			assertResponseCode(400, response.code);

			/*checkSuccessCode(response.code);

			/*assertEquals(respJson.getString("ReferrerPlacementTypeCd"), "null", "ReferrerPlacementTypeCd is Not Null");
			assertEquals(respJson.getString("Description"), "null", "Description is Not Null");
			assertEquals(respJson.getString("Commission"), "null", "Commission is Not Null");
			assertEquals(respJson.getString("UtmMedium"), "null", "UtmMedium is Not Null");
			assertEquals(respJson.getString("ProductId"), "null", "ProductId is Not Null");
			assertEquals(respJson.getString("CommissionLifeTime"), "null", "CommissionLifeTime is Not Null");
			assertEquals(respJson.getString("DestinationId"), "null", "DestinationId is Not Null");
			assertEquals(respJson.getString("PortalId"), "null", "PortalId is Not Null");
			assertEquals(respJson.getString("LandingPageCd"), "null", "LandingPageCd is Not Null");
			assertEquals(respJson.getString("StartDate"), "null", "StartDate is Not Null");
			assertEquals(respJson.getString("CobrandCD"), "null", "CobrandCD is Not Null");
			assertEquals(respJson.getString("IsDefault"), "null", "IsDefault is Not Null");
			assertEquals(respJson.getString("PageId"), "null", "PageId is Not Null");
			assertEquals(respJson.getString("Status"), "null", "Status is Not Null");
			assertEquals(respJson.getString("CommissionMode"), "null", "CommissionMode is Not Null");
			assertEquals(respJson.getString("AffiliateDescription"), "null", "AffiliateDescription is Not Null");
			assertEquals(respJson.getString("UtmCampaign"), "null", "UtmCampaign is Not Null");
			assertEquals(respJson.getString("AffiliateId"), "null", "AffiliateId is Not Null");
			assertEquals(respJson.getString("Mode"), "null", "Mode is Not Null");
			assertEquals(respJson.getString("RedirectType"), "null", "RedirectType is Not Null");
			assertEquals(respJson.getString("Type"), "null", "Type is Not Null");
			assertEquals(respJson.getString("UtmSource"), "null", "UtmSource is Not Null");
			assertEquals(respJson.getString("SortIndex"), "null", "SortIndex is Not Null");
			assertEquals(respJson.getString("LpGroupId"), "null", "LpGroupId is Not Null");
			assertEquals(respJson.getString("UtmContent"), "null", "UtmContent is Not Null");
			assertEquals("0", respJson.getString("ReferrerId"), "ReferrerId Mismatch");*/
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method API is called with visit_id but No visitor_uid parameter then existing Visitor_UID will be returned in create visit response
	@Test
	public void callCreateVisitAPIWithVisitIDButNoVisitorUID()
	{
		try
		{
			JSONObject sampleVisit = createSampleVisit();
			String visitorUID = sampleVisit.getString("visitor_uid");
			String visitID = sampleVisit.getString("visit_id");

			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visit_id", visitID);

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());
			JSONObject respJson = new JSONObject(response.message);

			assertEquals(visitID, respJson.getString("visit_id"), "visit_id mismatch");
			assertEquals(visitorUID, respJson.getString("visitor_uid"), "visitor_uid mismatch");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//In this case API_Error will be thrown 
	/*@Test
	public void callCreateVisitAPIWithInvalidVisitIDButNoVisitorUID()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visit_id", 999999999);

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());
			JSONObject respJson = new JSONObject(response.message);

			assertResponseCode(response.code, 500);
			assertEquals("Oops, There was an internal Error", respJson, "description");
			assertEquals("API_ERROR", respJson, "error_code");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}*/

	//When insert visit by post method API is called with existing visitor_uid in database but No visit_id parameter then new visit id will be created 
	@Test
	public void callCreateVisitAPIWithVisitorUIDButNoVisitId()
	{
		try
		{
			JSONObject sampleVisit = createSampleVisit();
			String visitorUID = sampleVisit.getString("visitor_uid");
			String firstVisitID = sampleVisit.getString("visit_id");

			JSONObject dataToPost = new JSONObject();
			dataToPost.put("visitor_uid", visitorUID);

			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			JSONObject respJson = new JSONObject(response.message);

			String query_HitLog_Count = "select count(*) as count from dbo.HitLog where VisitorUID ='" + visitorUID + "'";
			List<Map> list_HitLog_Count = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLog_Count);
			assertEquals("2", list_HitLog_Count.get(0).get("count").toString(), "Visit ID Row Count Mismatch");

			assertEquals(visitorUID, respJson.getString("visitor_uid"), "visitor_uid mismatch");

			String query_HitLog = "select HitlogID from dbo.HitLog where VisitorUID ='" + visitorUID + "'";
			List<Map> list_HitLog = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLog);
			assertEquals(firstVisitID, list_HitLog.get(0).get("HitlogID").toString(), "first visit_id mismatch");
			assertEquals(list_HitLog.get(1).get("HitlogID").toString(), respJson.getString("visit_id"), "second visit_id mismatch");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//When insert visit by post method API is called with invalid user agent header then custom error message will be shown
	@Test
	public void createVisitWithInvalidUserAgent()
	{
		try
		{
			HashMap<String, String> header = new HashMap<String, String>();
			header.put("clientcd", "LCAUS");
			header.put("User-Agent","Pingdom.com_bot_version_1.4");
			header.put("Accept-Language","en-US,en;q=0.5");

			JSONObject dataToPost = new JSONObject();
			ResponseBean response = HttpService.post(createVisitUrl, header, dataToPost.toString());

			JSONObject respJson = new JSONObject(response.message);

			assertResponseCode(response.code, 403);
			assertEquals("Invalid IP Address or UserAgent. UserAgent: Pingdom.com_bot_version_1.4", respJson, "description");
			assertEquals("U007", respJson, "error_code");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	//create sample visit 
	private JSONObject createSampleVisit()
	{
		try
		{
			JSONObject dataToPost = new JSONObject();
			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());

			checkSuccessCode(response.code);
			return new JSONObject(response.message);
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	//insert visit with invalid data type values 

	private void createVisitWithInvalidDataTypeValue(JSONObject dataToPost, String parameterName)
	{
		try
		{
			ResponseBean response = HttpService.post(createVisitUrl, headers, dataToPost.toString());
			assertResponseCode(response.code, 400);
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	//Database tables verification
	private void verifyHitLogTables(String visitorUID, String visitID, String partyId, String data, String referrerID, String utmTermValue, int productId, int portalId, int destinationID, String hitType, String sourceID)
	{
		try
		{
			//Visitor table verification
			String query_Visitor = "select * from dbo.Visitor where VisitorUID ='" + visitorUID + "'";
			List<Map> list_Visitor = DbUtil.getInstance("Livecareer").getResultSetList(query_Visitor);
			assertEquals("abcd", list_Visitor.get(0).get("Source").toString(), "Source mismatch in Visitor table");
			assertEquals("efgh", list_Visitor.get(0).get("Medium").toString(), "Medium mismatch in Visitor table");
			assertEquals("ijkl", list_Visitor.get(0).get("Campaign").toString(), "Campaign mismatch in Visitor table");
			assertEquals(referrerID, list_Visitor.get(0).get("ReferrerID").toString(), "ReferrerID mismatch in Visitor table");

			//HitLog table verification
			String query_HitLog = "select * from dbo.HitLog where VisitorUID ='" + visitorUID + "'";
			List<Map> list_HitLog = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLog);
			assertEquals(list_HitLog.get(0).get("HitLogID").toString(), visitID, "visit_id mismatch in HotLog table");
			assertEquals("N", list_HitLog.get(0).get("VisitType").toString(), "VisitType mismatch in HotLog table");
			assertEquals(portalId, Integer.parseInt(list_HitLog.get(0).get("PortalID").toString()), "PortalID mismatch in HotLog table");
			assertEquals(referrerID, list_HitLog.get(0).get("ReferrerID").toString(), "ReferrerID mismatch in HotLog table");
			assertEquals(sourceID, list_HitLog.get(0).get("SourceID").toString(), "SourceID mismatch in HotLog table");
			if(destinationID != 0)
			{
				assertEquals(destinationID, Integer.parseInt(list_HitLog.get(0).get("DestinationID").toString()), "DestinationID mismatch in HotLog table");
			}
			assertEquals(productId, Integer.parseInt(list_HitLog.get(0).get("ProductID").toString()), "ProductID mismatch in HotLog table");

			//HitLogSession table verification
			String query_HitLogSess = "select * from dbo.HitLogSession where HitLogID =" + visitID;
			List<Map> list_HitLogSess = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLogSess);
			assertEquals("http://dev2.livecareer.com:1024/home.aspx", list_HitLogSess.get(0).get("Referer").toString(), "Referer mismatch");

			//HitLogData table verification
			String query_HitLogSID = "select Value from dbo.HitLogdata where HitLogID =" + visitID + " AND [Key] = 'SID'";
			List<Map> list_HitLogSID = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLogSID);
			assertEquals("uvwx", list_HitLogSID.get(0).get("Value").toString(), "SID value mismatch");

			//HitLogExtended table verification
			String query_HitLogEx = "select * from dbo.HitLogExtended where HitLogID =" + visitID;
			List<Map> list_HitLogEx = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLogEx);
			assertEquals(hitType, list_HitLogEx.get(0).get("HitType").toString(), "HitType mismatch");
			assertEquals(data, list_HitLogEx.get(0).get("Data").toString(), "Data mismatch");
			assertEquals("113", list_HitLogEx.get(0).get("MailoutID").toString(), "MailoutID mismatch");
			assertEquals(partyId, list_HitLogEx.get(0).get("PartyID").toString(), "PartyID mismatch expected : " + partyId + " and actual : " + list_HitLogEx.get(0).get("PartyID").toString());

			//HitLogGA table verification
			String query_HitLogGA = "select * from dbo.HitLogGA where HitLogID =" + visitID;
			List<Map> list_HitLogGA = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLogGA);
			assertEquals("abcd", list_HitLogGA.get(0).get("utm_source").toString(), "utm_source mismatch");
			assertEquals("efgh", list_HitLogGA.get(0).get("utm_medium").toString(), "utm_medium mismatch");
			assertEquals("ijkl", list_HitLogGA.get(0).get("utm_campaign").toString(), "utm_campaign mismatch");
			assertEquals("mnop", list_HitLogGA.get(0).get("utm_content").toString(), "utm_content mismatch");
			assertEquals(utmTermValue, list_HitLogGA.get(0).get("utm_term").toString(), "utm_term mismatch");
			assertEquals("yzab", list_HitLogGA.get(0).get("match_type").toString(), "match_type mismatch");
			assertEquals("cdef", list_HitLogGA.get(0).get("ad").toString(), "ad mismatch");
			assertEquals("ghij", list_HitLogGA.get(0).get("Placement").toString(), "Placement mismatch");	
			assertEquals("klmn", list_HitLogGA.get(0).get("Network").toString(), "Network mismatch");

			//HitLogKeywords table verification
			String query_HitLogKey = "select * from dbo.HitLogKeywords where HitLogID =" + visitID;
			List<Map> list_HitLogKey = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLogKey);
			assertEquals("HelloTS", list_HitLogKey.get(0).get("Keywords").toString(), "keywords mismatch");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	//Database tables verification
	private void verifyHitLogTablesForVisitIDAndHitTypeTests(String visitorUID, JSONObject respJson, String hitType)
	{
		try
		{
			String query_HitLog_Count = "select count(*) as count from dbo.HitLog where VisitorUID ='" + visitorUID + "'";
			List<Map> list_HitLog_Count = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLog_Count);
			assertEquals(visitorUID, respJson.getString("visitor_uid"), "visitor_uid mismatch");
			assertEquals("2", list_HitLog_Count.get(0).get("count").toString(), "Visit ID Row Count Mismatch");

			//HitLog table verification
			String query_HitLog = "select top 1 * from dbo.HitLog where VisitorUID ='" + visitorUID + "' order by Timestamp desc";
			List<Map> list_HitLog = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLog);
			String secondVisitID = list_HitLog.get(0).get("HitLogID").toString();
			assertEquals("R", list_HitLog.get(0).get("VisitType").toString(), "VisitType mismatch");
			assertEquals("3", list_HitLog.get(0).get("PortalID").toString(), "PortalID mismatch");

			//HitLogExtended table verification
			String query_HitLogEx = "select * from dbo.HitLogExtended where HitLogID =" + secondVisitID;
			List<Map> list_HitLogEx = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLogEx);
			assertEquals(hitType, list_HitLogEx.get(0).get("HitType").toString(), "HitType mismatch");

			//HitLogGA table verification
			String query_HitLogGA = "select * from dbo.HitLogGA where HitLogID =" + secondVisitID;
			List<Map> list_HitLogGA = DbUtil.getInstance("Livecareer").getResultSetList(query_HitLogGA);
			assertEquals("LiveCareer", list_HitLogGA.get(0).get("utm_source").toString(), "utm_source mismatch in HitLogGA table");
			assertEquals("lcin", list_HitLogGA.get(0).get("utm_medium").toString(), "utm_medium mismatch in HitLogGA table");
			assertEquals(defaultReferrer.get(0).get("utm_campaign").toString(), list_HitLogGA.get(0).get("utm_campaign").toString(), "utm_campaign mismatch in HitLogGA table");
			assertTrue(list_HitLogGA.get(0).get("utm_content").toString().equalsIgnoreCase(""), "utm_content is not blank in HitLogGA table");
		}
		catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

}
