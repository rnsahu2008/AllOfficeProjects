package com.lc.components;

import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.lc.assertion.AssertionUtil;
import com.lc.db.DbUtil;
import com.lc.utils.ReadProperties;

public class TrackingServiceCoreComponents extends AssertionUtil{

	@Parameters("Environment")
	@BeforeTest
	public void setupEnvironment(String env)
	{
		ReadProperties.setupEnvironmentProperties(env, "res/trackingservicedata/config.json");
	}
	
	public List<Map> getReferrer(String description)
	{
		String referrer_query = "Select ReferrerID,utm_source,utm_medium,utm_campaign,utm_content from Referrer where Description ='" + description +  "'";
		return DbUtil.getInstance("Livecareer").getResultSetList(referrer_query);
	}

}
