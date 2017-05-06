package com.sel.casepanelTests;

import com.hs18.qe.DBConnect;

public class caseupdate {

	DBConnect db;
	 caseupdate() {
		 db = new DBConnect();
	}

	 String caseupdatexml(String caseid, String caseType,String activityStatus) {
		String caseactivityid = db.execQueryWithSingleColumnResult("select id from CASE_ACTIVITY where case_id= "
						+ caseid + " ;");
		String caseTypeid = db
				.execQueryWithSingleColumnResult("select CNDCODE from ISMCND  where   CNDGROUP = 'CaseType' and DELETED ='N' and CNDDESC =  "
						+ caseType + " ;");
		return "<updateCaseRequest><activityId>"
				+ caseactivityid
				+ "</activityId><activityRemarks>Automated Activity remarks</activityRemarks><activityStatus>"
				+ activityStatus
				+ "</activityStatus><callbackTime>2014-04-01T15:40:43.948+05:30</callbackTime><caseId>"
				+ caseid
				+ "</caseId><caseType>"
				+ caseTypeid
				+ "</caseType><clientType>CRM</clientType><internalCommunication>Automated Internal Communication update</internalCommunication><systemRemarks>true</systemRemarks><updatedBy>2700</updatedBy></updateCaseRequest>";
	}
}
