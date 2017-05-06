package com.lc.tests.catapult;

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

public class AddGetDeleteBookmarkHappyFlow extends CatapultCoreComponent {

	public static HashMap<String, String> headers = null;
	public static String uid = null, lessonid = null, bookmarkTime = null;
	

	@BeforeMethod
	public void init() {
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}	
	

	@SuppressWarnings("rawtypes")
	@Test(priority = 0)
	public void addbookmark() {
		try {
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD",clientCd);
			headers.put("ClientSecret", clientSecret);
			headers.put("Content-type", "application/json");				
			String query = "WITH mytable AS (SELECT (ROW_NUMBER() OVER (ORDER BY userlessons.CreatedOn)) as row,* FROM userlessons where bookmarktimestamp  is null) SELECT useruid,lessonid FROM mytable where row="
					+ randInt(1, 100);
			List<Map> result = DbUtil.getInstance("Catapult").getResultSetList(
					query);
			uid = result.get(0).get("useruid").toString().trim();
			lessonid = result.get(0).get("lessonid").toString().trim();

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
				
				
			}
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@SuppressWarnings("rawtypes")
	@Test(priority = 1, dependsOnMethods = { "addbookmark" })
	public void getbookmark() {
		try {

			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD", clientCd);
			headers.put("ClientSecret", clientSecret);
			headers.put("Content-type", "application/json");

			ResponseBean response = get_bookmark(uid, headers);
			checkSuccessCode(response.code);
			if (response.code == 200) {
				bookmarkTime = bookmarkTime.replace(" ", "T");
				JSONArray respJson = new JSONArray(response.message);

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
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}

	@SuppressWarnings("rawtypes")
	@Test(priority = 2, dependsOnMethods = { "addbookmark" })
	public void deletebookmark() {
		try {
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD", clientCd);
			headers.put("ClientSecret",clientSecret);
			headers.put("Content-type", "application/json");
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
								+ "' and bookmarktimestamp IS NULL and clientCd='"+clientCd+"'";
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

		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}
}
