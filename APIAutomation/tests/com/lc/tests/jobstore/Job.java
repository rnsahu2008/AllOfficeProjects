package com.lc.tests.jobstore;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDateTime;
import org.json.JSONObject;

public class Job {
	public String PUID; 
	public String Title;
	public String Description;
	public String City;
	public String State;
	public String PostalCode;
	public String CountryCD;
	public String StatusCD;
	public String SourceCD;
	public String Company;
	public String PostedOn;
	public String EmploymentTypeCD;
	public String ApplyUrl;
	public String StringHash;
	
	public String ToString()
	{
		///ignoring comparing string hash and date time posted
		return "PUID="+ this.PUID + ";Title="+ this.Title + ";Description="+this.Description+ ";City="+ this.City+ ";State="+this.State+
		";PostalCode="+this.PostalCode + ";CountryCD="+ this.CountryCD + ";StatusCD="+ 
		this.StatusCD + ";SourceCD="+ this.SourceCD + ";Company="+ this.Company +
		//";PostedOn="+ this.PostedOn
		 ";EmploymentTypeCD="+this.EmploymentTypeCD + ";ApplyUrl="+this.ApplyUrl;
		//+ ";StringHash="+ this.StringHash		
		
		//return "PUID="+ this.PUID + ";Title="+ this.Title + ";Description="+this.Description+ ";City="+ this.City+ ";State="+this.State+
				//";PostalCode="+this.PostalCode + ";CountryCD="+ this.CountryCD + ";StatusCD="+ 
				//this.StatusCD + ";SourceCD="+ this.SourceCD + ";Company="+ this.Company + ";PostedOn="+ this.PostedOn
				//+ ";EmploymentTypeCD="+this.EmploymentTypeCD + ";ApplyUrl="+this.ApplyUrl + ";StringHash="+ this.StringHash;
	}
	public static Job getRandomGeneratedJob()
	{
		Job radomJob = new Job();
		radomJob.PUID = RandomStringUtils.randomNumeric(8);
		radomJob.Title=RandomStringUtils.randomAlphanumeric(8);
		radomJob.Description = RandomStringUtils.randomAlphanumeric(8);
		radomJob.City =  RandomStringUtils.randomAlphabetic(8);
		radomJob.State = RandomStringUtils.randomAlphabetic(8);
		radomJob.PostalCode =  RandomStringUtils.randomAlphanumeric(6);
		radomJob.CountryCD =  "US";
		radomJob.StatusCD =  "ACTV";
		radomJob.SourceCD =  "JTR";
		radomJob.Company =  RandomStringUtils.randomAlphabetic(8);
		radomJob.PostedOn =  LocalDateTime.now().toString();	
		radomJob.EmploymentTypeCD =  "1234test1234";
		radomJob.ApplyUrl = "http://www.jobtap.com/job?=" + RandomStringUtils.randomNumeric(4);
		radomJob.StringHash =  "";
		
		return radomJob;
	}
	
	///There must be some way to write generic code of serialization of JSON object to some specific class.
	public static JSONObject getJSONObject(Job job)
	{
		JSONObject json = new JSONObject();
		try {
		json.put("PUID", job.PUID);
		json.put("Title", job.Title);
		json.put("Description", job.Description);
		json.put("CountryCD", job.CountryCD);
		json.put("PostalCode", job.PostalCode);
		json.put("EmploymentTypeCD", job.EmploymentTypeCD);
		json.put("SourceCD", job.SourceCD);
		json.put("StatusCD", job.StatusCD);
		json.put("City", job.City);
		json.put("State", job.State);
		json.put("Company", job.Company);
		json.put("PostedOn", job.PostedOn);
		json.put("ApplyUrl", job.ApplyUrl);
		json.put("StringHash", job.StringHash);
		}
		catch (Exception e) {		
			e.printStackTrace();
		}
		return json;
	}
	
	///There must be some way to write generic code of de serialization of JSON object to some specific class.
	public static Job getJob(JSONObject jsonObject)
	{
		Job validjob = new Job();
		try{
		validjob.PUID = jsonObject.getString("PUID");
		validjob.Title=jsonObject.getString("Title");
		validjob.Description = jsonObject.getString("Description");
		validjob.City =  jsonObject.getString("City");
		validjob.State = jsonObject.getString("State");
		validjob.PostalCode =  jsonObject.getString("PostalCode");
		validjob.CountryCD = jsonObject.getString("CountryCD");
		validjob.StatusCD = jsonObject.getString("StatusCD");
		validjob.SourceCD =  jsonObject.getString("SourceCD");
		validjob.Company =  jsonObject.getString("Company");
		validjob.PostedOn =  jsonObject.getString("PostedOn");	
		validjob.EmploymentTypeCD =  jsonObject.getString("EmploymentTypeCD");
		validjob.ApplyUrl = jsonObject.getString("ApplyUrl");
		////validjob.StringHash = jsonObject.getString("StringHash");
		}
		catch (Exception e) {		
			e.printStackTrace();
		}
		return validjob;
	}
}
