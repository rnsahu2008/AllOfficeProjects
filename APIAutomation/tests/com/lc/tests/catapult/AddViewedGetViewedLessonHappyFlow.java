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

public class AddViewedGetViewedLessonHappyFlow extends CatapultCoreComponent {
	
	HashMap<String, String> headers = null;
	public static String uid=null,lessonid=null,viewTime=null,bookmarkTime=null;
	
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}

	
	@SuppressWarnings("rawtypes")
	@Test(priority=0)
	public void addviewedlesson(){
		try {
			String query = "WITH mytable AS (SELECT (ROW_NUMBER() OVER (ORDER BY userlessons.CreatedOn)) as row,* FROM userlessons where viewtimestamp  is null and bookmarktimestamp is not null) SELECT useruid,lessonid FROM mytable where row="+randInt(1,75);
			List<Map> result = DbUtil.getInstance("Catapult").getResultSetList(query);			
			uid = result.get(0).get("useruid").toString().trim();
			lessonid=result.get(0).get("lessonid").toString().trim();			
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD",clientCd);
			headers.put("ClientSecret", clientSecret);
			headers.put("Content-type", "application/json");
			ResponseBean response=view_lesson(uid, lessonid, headers);
			checkSuccessCode(response.code);
			if(response.code==200)
			{
			String query1 = "select viewtimestamp,bookmarktimestamp from userlessons where useruid='"+uid+"' and lessonid='"+lessonid+"'";			
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
			
		}catch(Exception e)
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		
		s_assert.assertAll();		
}

	@SuppressWarnings("rawtypes")
	@Test(priority=1,dependsOnMethods = {"addviewedlesson"})
	public void getviewedlesson(){
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
		}catch (Exception e) 
		{
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
	}	
}
