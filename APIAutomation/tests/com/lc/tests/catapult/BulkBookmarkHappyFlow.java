package com.lc.tests.catapult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.lc.softAsserts.*;
import com.lc.common.Common;
import com.lc.components.CatapultCoreComponent;
import com.lc.db.DbUtil;
import com.lc.http.HttpService.ResponseBean;

public class BulkBookmarkHappyFlow  extends CatapultCoreComponent{
	
	HashMap<String, String> headers = null;
	public static String uid=null,lessonid=null,bookmarkTime=null;
	
	@BeforeMethod
	public void init(){
		s_assert = new SoftAssert();
		Common.apiLogInfo.clear();
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(priority=0)
	public void bulkbookmark(){
		try {		
			
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD",clientCd);
			headers.put("Content-Type","application/json");
			headers.put("ClientSecret", clientSecret);
			
			String userquery = "WITH mytable AS (SELECT (ROW_NUMBER() OVER (ORDER BY userlessons.CreatedOn)) as row,* FROM userlessons where bookmarktimestamp  is null) SELECT useruid FROM mytable where row="+randInt(1,100);
			List<Map> users = DbUtil.getInstance("Catapult").getResultSetList(userquery);
			uid = users.get(0).get("useruid").toString().trim();		
			
			List<String> data=new ArrayList();
			data=getLessons();
			String postrequest=data.get(0).toString();
			String dbrequest=data.get(1).toString();	
						
			ResponseBean response=bulk_bookmark(uid, postrequest, headers);
			checkSuccessCode(response.code);
			if(response.code==200)
			{			
					String query = "select count(*) AS bookmarkcount FROM userlessons where useruid='"+uid+"' and lessonid IN ("+dbrequest+") and bookmarktimestamp is null and clientCd='"+clientCd+"'";					
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
		}catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		s_assert.assertAll();
}
}
