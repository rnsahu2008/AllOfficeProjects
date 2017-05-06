package com.lc.tests.catapult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.CatapultCoreComponent;
import com.lc.db.DbUtil;
import com.lc.http.HttpService.ResponseBean;


public class CatapultEndtoEndTest extends CatapultCoreComponent {

	public static HashMap<String, String> headers = null;
	public static String uid = null, bookmarkTime = null,dbrequest=null,lessonid=null,viewTime;

	
	@BeforeMethod
	public void init() {
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}


	@Test(priority = 0)
	public void createUser() throws Exception 
	{		
			
		uid=createuser();			
	}	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(priority = 1, dependsOnMethods = { "createUser" })
	public void bulkBookmark() throws Exception {
		
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD",clientCd);
			headers.put("Content-Type","application/json");
			headers.put("ClientSecret", clientSecret);
			
			List<String> data=new ArrayList();
			data=getLessons();
			String postrequest=data.get(0).toString();
			dbrequest=data.get(1).toString();
			
			ResponseBean response=bulk_bookmark(uid, postrequest, headers);
			checkSuccessCode(response.code);
			if(response.code==200)
			{			
					String query = "select count(*) AS bookmarkcount FROM userlessons where useruid='"+uid+"' and lessonid IN ("+dbrequest+") and bookmarktimestamp is null and clientCD='"+clientCd+"'";					
					List<Map> bookmark = DbUtil.getInstance("Catapult").getResultSetList(query);
					String bulkCount = bookmark.get(0).get("bookmarkcount").toString().trim();
					if(bulkCount.equalsIgnoreCase("0"))
					{
						Assert.assertTrue(true);
					}else
					{
						assertEquals("true","false","Bookmarktimestamp not updated after bulkbookmark call");
					}
			}
			s_assert.assertAll();
		} 
		

	@SuppressWarnings("rawtypes")
	@Test(priority = 2,dependsOnMethods = { "bulkBookmark" })
	public void deleteBookmark() throws Exception {
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("ClientCD",clientCd);
		headers.put("Content-Type","application/json");
		headers.put("ClientSecret", clientSecret);
		
		
		String []lessons=dbrequest.split(",");
		lessonid=lessons[0].replaceAll("'"," ").trim();
		
		ResponseBean response = delete_bookmark(uid, lessonid, headers);
		checkSuccessCode(response.code);
		if (response.code == 200) {
			ResponseBean response1 = get_bookmark(uid, headers);
			checkSuccessCode(response1.code);
			if (response1.code == 200) {
				JSONArray respJson = new JSONArray(response1.message);					
				JSONObject reqObject = new JSONObject();
				reqObject.put("UserUID", uid);
				reqObject.put("LessonID", lessonid);
				reqObject.put("BookmarkTimestamp", bookmarkTime);

				TreeSet result = matchJsonSubset(respJson, reqObject);
				if (result.size() == 1) {
					assertEquals("true", "false",
							"Deleted lesson is returned in get bookmark API call. Hence failure.");

				} else if (result.size() == 0) {
					Assert.assertTrue(true);

					String query = "select count(*) AS ENTRY from userlessons where useruid='"
							+ uid
							+ "' and lessonid='"
							+ lessonid
							+ "' and bookmarktimestamp IS NULL";
					List<Map> result1 = DbUtil.getInstance("Catapult")
							.getResultSetList(query);
					String rowcount = result1.get(0).get("ENTRY")
							.toString();
					assertEquals(rowcount, "1",
							"BookmarkTimestamp not updated to null after delete bookmark API call");
				}
			} else {
				assertEquals("true", "false", "Get Bookmark call failed");
			}
		}
		s_assert.assertAll();
	}	

	@SuppressWarnings("rawtypes")
	@Test(priority = 3,dependsOnMethods = { "deleteBookmark" })
	public void addBookmark() throws Exception {

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("ClientCD",clientCd);
		headers.put("Content-Type","application/json");
		headers.put("ClientSecret", clientSecret);
		
		ResponseBean response = add_bookmark(uid, lessonid, headers);
		checkSuccessCode(response.code);

		if (response.code == 200) {
			String query1 = "select bookmarktimestamp from userlessons where useruid='"
					+ uid + "' and lessonid='" + lessonid + "' and clientCD='"+clientCd+"'";
			List<Map> bookmark = DbUtil.getInstance("Catapult")
					.getResultSetList(query1);
			bookmarkTime = bookmark.get(0).get("bookmarktimestamp")
					.toString();
			

			String actual[] = bookmarkTime.split(" ");
			
			if(actual[0].isEmpty())
			{
				assertEquals("true","false","BookmarkTimestamp not updated after addbookmark API call");
			}else
			{
				Assert.assertTrue(true);
			}
			
			ResponseBean response1 = get_bookmark(uid, headers);
			checkSuccessCode(response1.code);
			if (response1.code == 200) {
				bookmarkTime = bookmarkTime.replace(" ", "T");
				JSONArray respJson = new JSONArray(response1.message);

				JSONObject reqObject = new JSONObject();
				reqObject.put("UserUID", uid);
				reqObject.put("LessonID", lessonid);
				reqObject.put("BookmarkTimestamp", bookmarkTime);

				TreeSet result = matchJsonSubset(respJson, reqObject);
				if (result.size() == 1) {
					Assert.assertTrue(true);
				} else
					assertEquals("true", "false",
							"Added bookmark entry not found in Get Bookmark response");
			} else {
				assertEquals("true", "false", "Get Bookmark call failed");
			}		
	}
		s_assert.assertAll();
	}	
	
	@SuppressWarnings("rawtypes")
	@Test(priority = 4,dependsOnMethods = { "deleteBookmark" })
	public void viewLesson() throws Exception {
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("ClientCD",clientCd);
		headers.put("ClientSecret", clientSecret);
		headers.put("Content-type", "application/json");
		ResponseBean response=view_lesson(uid, lessonid, headers);
		checkSuccessCode(response.code);
		if(response.code==200)
		{
		String query1 = "select viewtimestamp,bookmarktimestamp from userlessons where useruid='"+uid+"' and lessonid='"+lessonid+"' and clientCD='"+clientCd+"'";			
		List<Map> view = DbUtil.getInstance("Catapult").getResultSetList(query1);
		viewTime=view.get(0).get("viewtimestamp").toString();			
		bookmarkTime=view.get(0).get("bookmarktimestamp").toString();		
		
		String actual[]=viewTime.split(" ");
		if(actual[0].isEmpty())
		{
			assertEquals("true","false","ViewTimestamp not updated after addviewedlesson API call");
		}else
		{
			Assert.assertTrue(true);
		}	
		}	
		s_assert.assertAll();
	}

	
	@SuppressWarnings("rawtypes")
	@Test(priority = 5,dependsOnMethods = { "viewLesson" })
	public void getViewedLesson() {
		try {
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD",clientCd);
			headers.put("ClientSecret",clientSecret);
			headers.put("Content-type", "application/json");
			ResponseBean response=get_viewed_lesson(uid,headers);			
			checkSuccessCode(response.code);
			if(response.code==200)
			{			
			viewTime=viewTime.replace(" ","T");		
			bookmarkTime=bookmarkTime.replace(" ","T");
			
			JSONArray respJson = new JSONArray(response.message);
			JSONObject reqObject = new JSONObject();
			reqObject.put("UserUID", uid);
			reqObject.put("LessonID", lessonid);
			reqObject.put("BookmarkTimestamp", bookmarkTime);
			reqObject.put("ViewTimestamp", viewTime);

			TreeSet result = matchJsonSubset(respJson, reqObject);
			if (result.size() == 1)		   
		    {
		    	Assert.assertTrue(true);
		    }else
		    {
		    	assertEquals("true","false","Added viewed lesson entry not found in Get Viewed Lesson response");
		    }
			}else
			{
				assertEquals("true","false","Get Viewed Lesson call failed");
			}
		} catch (Exception e) {

		}
		s_assert.assertAll();
	}
}
	
	

