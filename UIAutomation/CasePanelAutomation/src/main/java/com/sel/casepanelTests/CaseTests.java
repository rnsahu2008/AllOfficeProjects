package com.sel.casepanelTests;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import junit.framework.Assert;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.homeshop18.cs.api.constants.CaseDispositionType;
import com.homeshop18.cs.api.constants.CaseGroup;
import com.homeshop18.cs.api.constants.CaseType;
import com.homeshop18.cs.api.dto.CaseAdditionalKey;
import com.homeshop18.cs.api.dto.CaseDTO;
import com.homeshop18.cs.api.dto.CustomerDTO;
import com.hs18.commons.apiprovider.RestResource;
import com.hs18.lib.ReadExcel;
import com.hs18.qe.DBConnect;
import com.sel.casepanelTests.Graph;
import com.sel.casepanelTests.Node;
import com.sel.casepanelTests.oms_state_update;

@Service
public class CaseTests implements RestResource {

	public static final Logger logger = LoggerFactory
			.getLogger(CaseTests.class);
	public String caseupdateurl = "http://srvc04.preprod.hs18.lan:10090/api/cases/91435640/update";
	public String omsurl = "http://app04.preprod.hs18.lan:7004/api/1/oms/executeCommand";
	DBConnect db;
	Graph g;

	String strpath = "OUPwaitingGatewayresponse->PaymentRecived->Verfied->Cargoready->Shipped->Delivered \n OUP COD->Verfied->Cargoready->Shipped->Delivered OUPwaitingGatewayresponse->PaymentRecived->Verfied->Cargoready->Shipped->Delivered"
			+ "\n" + "OUP COD->Verfied->Cargoready->Shipped->Delivered ";
	final String CASE_TESTS = "/oms/";

	public CaseTests() {

		db = new DBConnect();

	}

	public void deltecasedetails(String casenumber) {

	}

	@DataProvider(name = "CRMTESTDATA")
	@SuppressWarnings("unchecked")
	public Object[][] testdata() throws Exception {

		ReadExcel re = new ReadExcel("src/test/resources/Testdata.xls");
		ArrayList<HashMap<String, String>> objData[][] = null;

		HashMap[] excelRows = re.getTableToHashMapArray();
		ArrayList<ArrayList<HashMap<String, String>>> testCases = null;
		ArrayList<HashMap<String, String>> testSTeps = null;
		testCases = new ArrayList();
		testSTeps = new ArrayList();

		for (HashMap<String, String> row : excelRows) {
			if (row.get("TestCase").toString().toLowerCase()
					.equalsIgnoreCase("start")) {
				continue;
			} else if (row.get("TestCase").toString().toLowerCase()
					.equalsIgnoreCase("")) {
				testSTeps.add(row);

			} else if (row.get("TestCase").toString().toLowerCase()
					.equalsIgnoreCase("end")) {

				testCases.add(testSTeps);

				testSTeps = new ArrayList();
				continue;
			}

		}
		objData = new ArrayList[testCases.size()][1];

		for (int i = 0; i < testCases.size(); i++) {
			objData[i][0] = new ArrayList<HashMap<String, String>>();
			objData[i][0] = testCases.get(i);
		}

		return objData;
	}

	/*
	 * @Test(dataProvider = "CRMTESTDATA") public void test1(ArrayList testCase)
	 * throws Exception {
	 * 
	 * db = new DBConnect(); oms_state_update oms = new oms_state_update(db);
	 * Method method = oms.getClass().getMethod("verfied", String.class); int
	 * val = hclient(omsurl, method.invoke(oms, "67823884").toString());
	 * System.out.println("test"); // oms.verfied("8");
	 * driver.get("www.google.com"); for (int i = 0; i < testCase.size(); i++) {
	 * HashMap<String, String> al = (HashMap) testCase.get(i); for
	 * (Map.Entry<String, String> entry : al.entrySet()) {
	 * System.setProperty("profile", "test"); OrderCreate oCreate = new
	 * OrderCreate(); Long lSuborder = oCreate.placeOrderForItem(32453753L, 1,
	 * "COD"); OmsCmds ooms = new OmsCmds(); VerifiedSuborderCommand ver = new
	 * VerifiedSuborderCommand( 67823379l);
	 * ooms.VerifiedSuborderCommand(67823379l, "PRSH", "Automation");
	 * CaseService cs = new CaseService(); CaseApiResponse csresponse = cs
	 * .createCase(fncasedto(67823379l)); System.out.println("");
	 * System.out.println(entry.getKey() + "/" + entry.getValue()); }
	 * 
	 * } }
	 */

	/*
	 * @Test public void testtest() throws Exception { statetransfer("67824843",
	 * "2154"); }
	 */

	@GET
	@Path(CASE_TESTS + "suborder/{suborderId}/destination/{destination}")
	public Object statetransfer(@PathParam("suborderId") String Suborder,
			@PathParam("destination") String destination)
	// public String statetransfer( String Suborder, String destination)
			throws Exception {
		String inStatus = db
				.execQueryWithSingleColumnResult("SELECT PSTSMTSTATUS from ISMPST where PSTRFNUM = "
						+ Suborder + " ;");
		oms_state_update oms = new oms_state_update(db);

		if (destination.contains("options")) {

			String states = "";
			HashMap[] execQueryWithResultHM3 = db
					.execQueryWithResultHM("select SMTRFNUM,SMTNAME from ISMSMT Order by SMTNAME");
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < execQueryWithResultHM3.length; i++) {

				str.append(execQueryWithResultHM3[i].get("SMTNAME").toString()
						+ "<->"
						+ execQueryWithResultHM3[i].get("SMTRFNUM").toString());
				str.append(" \n ");
				// states=execQueryWithResultHM3[i].get("SMTNAME")+ "<->" +
				// execQueryWithResultHM3[i].get("SMTRFNUM")+" \n "+
				// " ; "+states;
			}

			return str.toString();
		}

		String potrfnum=db.execQueryWithSingleColumnResult("select PSTPOTRFNUM  from ISMPST where PSTRFNUM = "+Suborder+ " ;"); 
		
		if(db.execQueryWithSingleColumnResult("select PSTSMTSTATUS  from ISMPST where PSTRFNUM = "+Suborder+ " ;").equalsIgnoreCase("2100"))
		{
			hclient(omsurl, oms.ChequeReceivedOrderCommand(Suborder));
		}
		
		
		
		if(db.execQueryWithSingleColumnResult("select POTPMTRFNUM  from ISMPOT where POTRFNUM = "+potrfnum+ " ;").equalsIgnoreCase("50019") &&  db.execQueryWithSingleColumnResult("select PSTSMTSTATUS  from ISMPST where PSTRFNUM = "+Suborder+ " ;").equalsIgnoreCase("2142") && destination.contains("2129"))
		{
			hclient(omsurl, oms.generateInvoiceOrder(Suborder));
			Thread.sleep(2000);
			hclient(omsurl, oms.invoicesentforcollection(Suborder));
			Thread.sleep(2000);
			
			//return "test";
			
		}
		
	
		
		if(db.execQueryWithSingleColumnResult("select POTPMTRFNUM  from ISMPOT where POTRFNUM = "+potrfnum+ " ;").equalsIgnoreCase("50019") &&  db.execQueryWithSingleColumnResult("select PSTSMTSTATUS  from ISMPST where PSTRFNUM = "+Suborder+ " ;").equalsIgnoreCase("2142"))
		{
			hclient(omsurl, oms.generateInvoiceOrder(Suborder));
			Thread.sleep(2000);
			hclient(omsurl, oms.invoicesentforcollection(Suborder));
			Thread.sleep(2000);
			hclient(omsurl, oms.cbdpaymentrecivied(Suborder));
			Thread.sleep(2000);
			//return "test";
			
		}
		
		 inStatus = db
					.execQueryWithSingleColumnResult("SELECT PSTSMTSTATUS from ISMPST where PSTRFNUM = "
							+ Suborder + " ;");
		
		String[] dest = destination.split("route");
		if (!inStatus.equalsIgnoreCase(dest[0].toString())) {
			if (destination.contains("route")) {
				List<Node> testgraph = testgraph(inStatus, dest[0].toString());
				String snode = "";
				for (Node node : testgraph) {
					snode = snode + "->" + node.state;
				}
				return snode;
			}

			
			
			List<Node> testgraph = testgraph(inStatus, destination);
			for (Node node : testgraph) {

				if (node.label.equalsIgnoreCase("2140")
						|| node.label.equalsIgnoreCase("2136")
						|| node.label.equalsIgnoreCase("2101")
						|| node.label.equalsIgnoreCase("2142")|| node.label.equalsIgnoreCase("2109")) {
					//Commented below line as order api assigned courier  has some issues need to look into .. Currently forcing all order to update courier
					//if(db.execQueryWithSingleColumnResult("select PSTCOURIERNAME  from ISMPST where PSTRFNUM = "+Suborder+ " ;")==null || db.execQueryWithSingleColumnResult("select PSTCOURIERNAME  from ISMPST where PSTRFNUM = "+Suborder+ " ;").length()==0)
					hclient(omsurl, oms.updatesubordercommand(Suborder));
				}

				String state = maporderstatus.getMode(node.state).toString();
				Method method = oms.getClass().getMethod(state, String.class);
				// System.out.println(method.invoke(oms, Suborder).toString());
				String pmt=	db.execQueryWithSingleColumnResult("SELECT POTPMTRFNUM FROM ISMPST join ISMPOT on POTRFNUM= PSTPOTRFNUM WHERE PSTRFNUM = "+Suborder+" ; ") ;
				
				//if((!(pmt.equalsIgnoreCase("50019") &&  node.label.equalsIgnoreCase("2108"))&& !(pmt.equalsIgnoreCase("50019") &&  node.label.equalsIgnoreCase("2101"))))
				//{
						
				System.out.println(hclient(omsurl, method.invoke(oms, Suborder).toString()));
				
				//}

				
				
				
			}
		} else {
			return "Suborder is already in destination state ";
		}

		return "Suborder  changed to  destination state ";
	}

	private CaseDTO fncasedto(Long suborder) {
		CaseDTO caseDTO = new CaseDTO();
		caseDTO.setSuborderNo(suborder);
		caseDTO.setCaseType(CaseType.ORDER_NOT_SHIPPED.getCode());
		caseDTO.setCaseGroup(CaseGroup.DELIVERY_RELATED.getName());
		caseDTO.setCaseDispositionType(CaseDispositionType.CANCEL_ORDER
				.getCode());
		caseDTO.setPreferredCallbackTime(new Date());
		// caseDTO.setCallbackRequired(false);
		caseDTO.setClientType("crm");
		caseDTO.setDnsNumber(4455918L);
		caseDTO.setDnsReason(0L);
		caseDTO.setCaseDescription("Case description");
		caseDTO.setInternalCommunication("test123");
		caseDTO.setCallbackRequired(true);
		caseDTO.setCallerNo("9611437246");// only if client type crm
		caseDTO.setCreatorUserId(2700L);// only if client type crm
		caseDTO.setCommittedDate(new Date());
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setFirstName("Sateesh");
		caseDTO.setCustomerDetails(customerDTO);
		Map<CaseAdditionalKey, Object> caseAdditionalDetails = new HashMap<CaseAdditionalKey, Object>();
		caseAdditionalDetails.put(CaseAdditionalKey.AWB_NO, "123456");
		caseAdditionalDetails
				.put(CaseAdditionalKey.BENEFICIARY_NAME, "Sateesh");
		caseAdditionalDetails.put(CaseAdditionalKey.COURIER_DATE, new Date());
		caseAdditionalDetails.put(CaseAdditionalKey.CALL_BACK_TIME, new Date());
		caseDTO.setCaseAdditionalDetails(caseAdditionalDetails);
		return caseDTO;
	}

	public int hclient(String url, String xml) throws ClientProtocolException,
			IOException, InterruptedException {

		// HttpGet getrequest = new HttpGet(
		// "http://app01.preprod.hs18.lan:11080/casehandling/faces/oms/caseAssignor.jsp?assignmentType=activity");

		System.out.println("CMD URL"+xml);
		HttpClient client = HttpClientBuilder.create().build();
		// HttpResponse response2 = client.execute(getrequest);
		// Thread.sleep(5000);
		@SuppressWarnings("deprecation")
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(url);
		postRequest.addHeader("content-type", "application/xml");
		StringEntity userEntity = new StringEntity(xml);
		postRequest.setEntity(userEntity);
		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				Assert.fail(EntityUtils.toString(entity)
						+ " OMS service not responding contact abhishek.bisht@homeshop18.com");
			}

		}

		// response2 = client.execute(getrequest);
		// Thread.sleep(5000);
		// new OmsCmds().ReversePickupDeliveredSuborderCommand(67823941l,
		// "123123", "500012", new Date(), "FASF", "");

		// System.out.println("Bucket assignment url response" + response2);
		return response.getStatusLine().getStatusCode();
	}

	/*
	 * public void createcase() throws ClientProtocolException, IOException {
	 * DefaultHttpClient httpClient = new DefaultHttpClient(); HttpPost
	 * postRequest = new HttpPost(caseupdate);
	 * postRequest.addHeader("content-type", "application/xml"); StringEntity
	 * userEntity = new StringEntity(caseupdatexml("91435640", "ONRC",
	 * "Closed")); postRequest.setEntity(userEntity); HttpResponse response =
	 * httpClient.execute(postRequest); int statusCode =
	 * response.getStatusLine().getStatusCode(); String caseid = ""; String
	 * squery =
	 * "select CNDDESC from CRMCHT  join ISMCND on CHTCNDCASETYPE = CNDRFNUM AND CHTRFNUM = "
	 * + caseid +
	 * " union select CNDDESC from CRMCHT  join ISMCND on CHTCNDFEEDBACKTYPE = CNDRFNUM AND CHTRFNUM = "
	 * + caseid +
	 * " union 	select CNDDESC from CRMCHT  join ISMCND on CHTCNDDISPTYPE = CNDRFNUM AND CHTRFNUM = "
	 * + caseid +
	 * " union  select CNDDESC from CRMCHT  join ISMCND on  CHTCNDCASEASSIGNTO = CNDRFNUM AND CHTRFNUM = "
	 * + caseid +
	 * " union  select name from   join  CASE_ACTIVITY_TYPE on activity_type_id= CASE_ACTIVITY_TYPE.id and case_id="
	 * + caseid +
	 * " union select name from CASE_ACTIVITY  join  CASE_ACTIVITY_STATUS on activity_status= CASE_ACTIVITY_STATUS.id  and case_id= "
	 * + caseid; // ReadExcel read = new ReadE
	 * 
	 * }
	 */

	@Test
	public void testgraphsearch() throws Exception {

		// 2130 shipped
		// 2154 delivered
		// replacement intiatd - 2175
		// refunded 2134
		// Reverse pckup delived 2183
		// reshipped 2133
		// Reversepickup delivered 2183
		// 2101 Payment Received

		HttpGet getrequest = new HttpGet(
				"http://app01.preprod.hs18.lan:11080/casehandling/faces/oms/caseAssignor.jsp?assignmentType=activity");
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response2 = client.execute(getrequest);
		// statetransfer("67824062", "2133");
		// testgraph("2140", "2154");
		System.out.println("test");
	}

	@Test
	public void reshipped() throws Exception {

		// 2130 shipped
		// 2154 delivered
		// replacement intiatd - 2175
		// refunded 2134
		// Reverse pckup delived 2183
		// reshipped 2133
		// Reversepickup delivered 2183
		// 2101 Payment Received

		// statetransfer("67824234", "2130");
		// statetransfer("67824223", "2183");

		statetransfer("67824547", "2154");
		// statetransfer("67824234", "2154");
		// testgraph("2140", "2154");
		System.out.println("test");

	}

	public List<Node> testgraph(String startstate, String endstate)
			throws Exception {
		// Lets create nodes as given as an example in the article
		DBConnect db = new DBConnect();
		HashMap[] execQueryWithResultHM = db
				.execQueryWithResultHM("SELECT SMTRFNUM  ,SMTNAME FROM ISMSMT");
		g = new Graph();
		Node target = null;
		for (int i = 0; i < execQueryWithResultHM.length; i++) {

			String sobj = execQueryWithResultHM[i].get("SMTRFNUM").toString();

			Node sobjj = new Node(sobj, execQueryWithResultHM[i].get("SMTNAME")
					.toString());
			if (sobjj.label.equals(endstate)) {
				target = sobjj;
				System.out.println("Found target node" + target.state);
			}
			g.addNode(sobjj);
			if (execQueryWithResultHM[i].get("SMTRFNUM").toString()
					.equalsIgnoreCase(startstate)) {
				System.out.println("Found Start node" + sobjj.state);
				g.setRootNode(sobjj);
			}
		}

		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		System.out.println(path);
		System.getProperty("user.dir");
		// ReadExcel read = new ReadExcel(
		// path+"src/test\/resources/statetable.xls");
		// HashMap[] execQueryWithResultHM23= read.getTableToHashMapArray();

		HashMap[] execQueryWithResultHM2 = db
				.execQueryWithResultHM("select SSUSMTFSTATE,SSUSMTTSTATE from ISMSSU where DELETED ='N' and SSUCOMMANDNAME<> 'null' order by  SSUSMTFSTATE asc ");

		for (int i = 0; i < execQueryWithResultHM2.length; i++) {

			g.connectNode(
					getnode(execQueryWithResultHM2[i].get("SSUSMTFSTATE")
							.toString(), g),
					getnode(execQueryWithResultHM2[i].get("SSUSMTTSTATE")
							.toString(), g));
		}

		// Perform the traversal of the graph
		// System.out.println("DFS Traversal of a tree is ------------->");
		return g.dfs2(target);

		// System.out.println("\nBFS Traversal of a tree is ------------->");
		// g.bfs();
		// g.setRootNode(n);
		// Create the graph, add nodes, create edges between nodes

	}

	public Node getnode(String str1, Graph al) {

		for (int i = 0; i < al.nodes.size(); i++) {
			if (al.nodes.get(i).toString().equalsIgnoreCase(str1))
				return (Node) al.nodes.get(i);

		}
		return null;

	}

	@Override
	public String getBasePath() {
		// TODO Auto-generated method stub
		return CASE_TESTS;
	}

}
