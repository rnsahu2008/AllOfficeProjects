package com.lc.components;

import static com.lc.constants.Constants_Catapult.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.lc.assertion.AssertionUtil;
import com.lc.db.DbUtil;
import com.lc.http.HttpService;
import com.lc.http.HttpService.ResponseBean;
import com.lc.utils.HeaderManager;
import com.lc.utils.ReadProperties;

public class CatapultCoreComponent extends AssertionUtil {

	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public static String clientCd = null, clientSecret = null,createUserUrl=null;
	public static String access_token = "";
	
	
	
	@BeforeTest
	@Parameters(value = {"Environment", "Application"})
	public void setupEnvironment(String env,String application)
	{
		
		//String application="LCI",env="DEV";		
		ReadProperties.setupEnvironmentProperties(env,"res/CatapultData/config.json");
		
		if(application.equalsIgnoreCase("LCI"))
		{
			clientCd="LCAUS";
			clientSecret=LCI_KEY;
			
		}else
			if(application.equalsIgnoreCase("MPI"))
			{
				clientCd="MPIUS";
				clientSecret=MPI_KEY;
			}
		
		if(env.equalsIgnoreCase("REG"))
		{
			createUserUrl="http://api-reg-user.livecareer.com/v1/users/";
		}else if(env.equalsIgnoreCase("DEV"))
		{
			createUserUrl="http://api-dev-user.livecareer.com/v1/users/";
		}
	}
	
	@SuppressWarnings("rawtypes")
	public String createuser() throws Exception
	{
		
		generateAccessToken();
		JSONObject requestObject = new JSONObject();
		requestObject.put("type", "registered");
		requestObject.put("email_address", "arpit.seth" + RandomStringUtils.randomNumeric(8) + "@bold.com");
		requestObject.put("password", "111111");
		requestObject.put("ip_address", "138.91.242.92");
		requestObject.put("is_active", true);
	
		ResponseBean response = HttpService.post(createUserUrl,HeaderManager.getHeaders("LCAUS", access_token, "1234"), requestObject.toString());
		JSONObject respJson=new JSONObject(response.message);
		String query_person = "Select PartyID from Person where partyID = " + respJson.getInt("party_id");
		String query_user = "Select UserUID from [user] where partyID = " + respJson.getInt("party_id");
		List<Map> list_person = DbUtil.getInstance("Livecareer").getResultSetList(query_person);
		List<Map> list_user = DbUtil.getInstance("Livecareer").getResultSetList(query_user);
		assertEquals(list_person.get(0).get("PartyID").toString(), respJson.getString("party_id"), "partyID Mismatch");
		assertEquals(list_user.get(0).get("UserUID").toString(), respJson.getString("user_uid"), "UserUID Mismatch");	
		return list_user.get(0).get("UserUID").toString();		
	}
	
		public void generateAccessToken(){		
		try {
			String dataToPost = "grant_type=client_credentials&client_id=LCAUS&client_secret=c2F2ZSB0aGUgdGlnZXI=&response_type=token:";
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("ClientCD", "LCAUS");
			ResponseBean response = HttpService.post(USERSERVICE_AUTH_URL, headers, dataToPost);
			checkSuccessCode(response.code);
			String respString = response.message;
			JSONObject respJson = new JSONObject(respString);
			assertEquals(respJson.get("token_type").toString() , "bearer", "Issue with getting token_type");
			assertNotNull(respJson.get("access_token").toString() , "Issue with getting access token");
			assertNotNull(respJson.get("refresh_token").toString(), "Issue with getting refresh_token");
			assertNotNull(respJson.get("expires_in").toString(), "Issue with getting expires_in");
			access_token = "Bearer "+respJson.get("access_token");
			if(StringUtils.isEmpty(access_token)){
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	// Add Bookmark
	public ResponseBean add_bookmark(String uid, String lessonid,
			HashMap<String, String> headers) throws IOException {

		String url = BASE_URL + USER_URL + uid + "/lesson/"
				+ lessonid + "/bookmarks";
		ResponseBean response = HttpService.post(url, headers, "{}");
		return response;

	}

	// Get Bookmark
	public ResponseBean get_bookmark(String uid, HashMap<String, String> headers)
			throws IOException {

		String url = BASE_URL + USER_URL + uid + "/bookmarks";
		ResponseBean response;
		response = HttpService.get(url, headers);
		return response;
	}

	// Delete Bookmark
	public ResponseBean delete_bookmark(String uid, String lessonid,
			HashMap<String, String> headers) {

		String url = BASE_URL + USER_URL + uid + "/lesson/"
				+ lessonid + "/bookmarks";
		ResponseBean response;
		response = HttpService.delete(url, headers);
		return response;

	}

	// Add Viewed Lesson
	public ResponseBean view_lesson(String uid, String lessonid,
			HashMap<String, String> headers) throws IOException {

		String url = BASE_URL + USER_URL + uid + "/lesson/"
				+ lessonid + "/views";
		ResponseBean response;
		response = HttpService.post(url, headers, "{}");
		return response;
	}

	// Get Viewed Lesson
	public ResponseBean get_viewed_lesson(String uid,
			HashMap<String, String> headers) throws IOException {

		String url = BASE_URL + USER_URL + uid + "/views";
		ResponseBean response;
		response = HttpService.get(url, headers);
		return response;
	}

	// Bulk Bookmark
	public ResponseBean bulk_bookmark(String uid, String postrequest,
			HashMap<String, String> headers) throws IOException {

		String url = BASE_URL + BULKBOOKMARK_URL + uid;
		ResponseBean response;
		response = HttpService.post(url, headers, postrequest);
		return response;

	}

	// This method will check the number of matches of key-value pairs of a JSON Object 
	//against a big JSON Array and return the position of matches in a tree set.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static TreeSet matchJsonSubset(JSONArray jsonArray, JSONObject jsonObject)
	{
		SoftAssert s_assert = new SoftAssert();
		try{
		int count = 0, i = 0;
		int lenArr = jsonArray.length();
		int lenObj = jsonObject.length();
		
		TreeSet tree = new TreeSet();
		JSONArray key = new JSONArray();
		key = jsonObject.names();
		for (i = 0; i < lenArr; i++) {
			if (count == lenObj) {
				count = 0;
				tree.add(i);
			} else {
				count = 0;
			}
			for (int j = 0; j < lenObj; j++) {
				if ((jsonArray.getJSONObject(i).get(key.getString(j)).toString())
						.equalsIgnoreCase(jsonObject.getString(key.getString(j)))) {
					count++;
				}
			}
		}
		if (count == lenObj) {
			tree.add(i);
			count = 0;
		} else {
			count = 0;
		}
		return tree;
		}catch(Exception e)
		{
			s_assert.fail("Key Not found");
			return null;
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getLessons()
	{
		String lessonid=null;
		String lessonquery = "select distinct lessonid FROM lesson";
		List<Map> lessons = DbUtil.getInstance("Catapult").getResultSetList(lessonquery);
		String postrequest="[",dbrequest="";
		int rand[]=new int[6];
		rand=RandomNumberList(6,50);			
		int i=0;
		for(i=0;i<5;i++)
		{				
		lessonid=lessons.get(rand[i]).get("lessonid").toString().trim();		
		postrequest=postrequest+"\""+lessonid+"\",";
		dbrequest=dbrequest+"'"+lessonid+"',";
		}			
		lessonid=lessons.get(rand[(i)]).get("lessonid").toString().trim();
		postrequest=postrequest+"\""+lessonid+"\"]";
		dbrequest=dbrequest+"'"+lessonid+"'";
		List<String> data=new ArrayList();
		data.add(postrequest);
		data.add(dbrequest);
		return data;
	}

	// This method is used to generate random integers between ranges - min to
	// max
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	// This method is used to generate m random integers between ranges - 1 to n
	public int[] RandomNumberList(int m, int n) {
		int a[] = new int[m];
		Random randomGenerator = new Random();
		for (int i = 0; i < m; ++i) {
			int randomInt = randomGenerator.nextInt(n);
			a[i] = randomInt;
		}
		return a;
	}

	// This method is used to fetch expiry date depending on the input provided
	// for example, PastDate, FutureDate and Today's Date
	public String getExpiryDate(String data) {
		String date = "";
		switch (data) {
		case "PastDate":
			date = addDays(new Date(), format, -30).toString();
			break;
		case "FutureDate":
			date = addDays(new Date(), format, 120).toString();
			break;
		case "Today'sDate":
			date = addDays(new Date(), format, 0).toString();
			break;
		default:
			break;
		}
		return date;
	}

	// This method is used to return Date in given format after adding days in
	// todays date
	public String addDays(Date date, SimpleDateFormat simpleDateFormat,Integer numberOfDays) 
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, numberOfDays);

		return simpleDateFormat.format(cal.getTime());
	}
}
