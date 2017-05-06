package com.sel.casepanelTests;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hs18.qe.DBConnect;

public class oms_state_update {

	String sdate,sdate2,sdate3;
	DBConnect db;
	
	public oms_state_update(DBConnect db2) throws Exception {
		 this.db=db2;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		sdate = sdf.format(date);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		sdate2 = sdf2.format(date);
		
		SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd-yyyy");
		sdate3 = sdf3.format(date);
	}

	public String verifed(String Suborder) {
			return ("<verifiedSuborderCommand>  <commandId>790b8b65-55b2-409d-aad7-fb221f6f9839</commandId> "
				+ " <commandSource>SUBORDER</commandSource>  <dryRun>false</dryRun>  "
				+ "<userId>1</userId>  "
				+ "<suborderId>"
				+ Suborder
				+ "</suborderId>" + "</verifiedSuborderCommand>");

	}
	
public String generateInvoiceOrder(String Suborder)
{
	
	String pot=	db.execQueryWithSingleColumnResult("SELECT POTRFNUM FROM ISMPST join ISMPOT on POTRFNUM= PSTPOTRFNUM WHERE PSTRFNUM = "+Suborder+" ; ") ;
	
  return		"<generateInvoiceOrderCommand>		  <commandId>1b819eac-9255-42f9-9ab1-85aed2efe7dd</commandId>		  <userId>1</userId>		  <commandSource>ORDER</commandSource>		  <dryRun>false</dryRun>		  <orderId>"+pot+"</orderId>		  <remark>Automated_Order_San</remark>		</generateInvoiceOrderCommand>";

}

public String invoicesentforcollection(String Suborder)
{
	
	String pot=	db.execQueryWithSingleColumnResult("SELECT POTRFNUM FROM ISMPST join ISMPOT on POTRFNUM= PSTPOTRFNUM WHERE PSTRFNUM = "+Suborder+" ; ") ;
		
	return "<invoiceSentForCollectionOrderCommand>	  <commandId>41764624-0463-4cac-8173-62c18eec7d02</commandId>	  <userId>1</userId>	  <commandSource>ORDER</commandSource>	  <dryRun>false</dryRun>	  <orderId>"+pot+"</orderId>	  <shippedDate>"+sdate2+"</shippedDate>	  <awbNumber>5765765</awbNumber>	  <courierId>500012</courierId>	  <remark>santhosh</remark>	</invoiceSentForCollectionOrderCommand>";
	
	//return	"<invoiceSentForCollectionSuborderCommand>		  <commandId>56f2ab9d-a224-4bd7-8142-348028fa47b4</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"+Suborder+"</suborderId>		  <shippedDate>"+sdate+"</shippedDate>		  <awbNumber>946432154</awbNumber>		  <courierId>500012</courierId>		  <remark>Automated_order_San</remark>		</invoiceSentForCollectionSuborderCommand>";
	
}

public String cbdpaymentrecivied(String Suborder)
{
	
	String pot=	db.execQueryWithSingleColumnResult("SELECT POTRFNUM FROM ISMPST join ISMPOT on POTRFNUM= PSTPOTRFNUM WHERE PSTRFNUM = "+Suborder+" ; ") ;
	
		return "<cbdPaymentReceivedOrderCommand> <commandId>5a4e6fcd-53a6-4c45-aacf-f1b2ee13b5ab</commandId> <userId>1</userId>	<commandSource>ORDER</commandSource>		  <dryRun>false</dryRun>		  <orderId>"+pot+"</orderId>		  <remark>Automatedorder_san</remark>		</cbdPaymentReceivedOrderCommand>";		
}

	public String paymentreceived(String Suborder){
		String potrfnum=	db.execQueryWithSingleColumnResult("SELECT PSTPOTRFNUM from ISMPST where PSTRFNUM = "+Suborder+" ; ") ;
		String potnetcartprice =  db.execQueryWithSingleColumnResult("select POTPAYAMT from ISMPOT where POTRFNUM = "+potrfnum+" ; ") ;
		String potpmtrfnum =  db.execQueryWithSingleColumnResult("select POTPMTRFNUM from ISMPOT where POTRFNUM = "+potrfnum+" ; ");
		

		return "<onlinePaymentReceivedOrderCommand>	"
				+ "	  <commandId>cb25388e-73ba-4036-ae4c-2ea59b933b27</commandId>		"
				+ "  <userId>1</userId>		  <commandSource>ORDER</commandSource>		 "
				+ " <dryRun>false</dryRun>		  <orderId>"+potrfnum+"</orderId>		  <paymentTransactionId>2228888888888</paymentTransactionId>		  <paymentDate>"+sdate2+"</paymentDate>		  <paymentAmount>"+potnetcartprice+"</paymentAmount>		  <paymentType>"+potpmtrfnum+"</paymentType>		  <bankName>san_automation</bankName>		  <remark>Automation order</remark>		</onlinePaymentReceivedOrderCommand>";
				}

	public String cargoready(String Suborder) {

		return ("<inStockSuborderCommand> "
				+ "<commandId>790b8b65-55b2-409d-aad7-fb221f6f9839</commandId> 	"
				+ "	  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		"
				+ "  <userId>1</userId>		  <suborderId>" + Suborder
				+ "</suborderId> 	" + "	</inStockSuborderCommand>");
	}

	public String delivered(String Suborder) {

		return ("<deliveredSuborderCommand> <commandId>790b8b65-55b2-409d-aad7-fb221f6f9839</commandId> "
				+ "  <commandSource>SUBORDER</commandSource>  <dryRun>false</dryRun> "
				+ " <userId>1</userId>  <suborderId>"+ Suborder	+ "</suborderId>  "
				+ " <remark>Automation Script </remark>  "
				+ " <receivedBy>test</receivedBy> "
				+ "  <deliveredDate>"+ sdate + " IST</deliveredDate> " + " </deliveredSuborderCommand>");
	}

	public String shipped(String Suborder) throws Exception {

	String courierId=	db.execQueryWithSingleColumnResult("SELECT PSTCOURIERNAME from ISMPST where PSTRFNUM = "+Suborder+" ; ") ;
	String PSTCOURIERNAMETEXT=db.execQueryWithSingleColumnResult("SELECT PSTCOURIERNAMETEXT from ISMPST where PSTRFNUM = "+Suborder+" ; ") ;
	String PSTPSTAWBNUM=db.execQueryWithSingleColumnResult("SELECT PSTAWBNUM from ISMPST where PSTRFNUM = "+Suborder+" ; ") ;
	if(PSTPSTAWBNUM==null)
	{
		PSTPSTAWBNUM="888777333";
	}

		
		return ("<shippedSuborderCommand>		"
				+ "  <commandId>790b8b65-55b2-409d-aad7-fb221f6f9839</commandId>  "
				+ "<commandSource>SUBORDER</commandSource>	"
				+ "  <dryRun>false</dryRun>  <userId>1</userId>	"
				+ "  <suborderId>"
				+ Suborder
				+ "</suborderId>	"
				+ "  <shippedDate>"+ sdate + " IST</shippedDate>	"
				+ "  <awbNumber>"+PSTPSTAWBNUM+"</awbNumber>	  <remark>Automation Script</remark>  "
				+ "<courierId>"+courierId+"</courierId>  <courierName>"+PSTCOURIERNAMETEXT+"</courierName>	" + "</shippedSuborderCommand>");
	}

	public String deliveryatemptfailed(String Suborder)
	{
		return "<deliveryAttemptFailedSuborderCommand>		  <commandId>b12fe6de-965e-466c-a70a-5576fd7c62c5</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"+Suborder+"</suborderId>		  <awbNumber>7777</awbNumber>		  <remark>ttt</remark>		  <reason>DRLK</reason>		  <deliveryAttemptFailedDate>"+sdate2+"</deliveryAttemptFailedDate>		</deliveryAttemptFailedSuborderCommand>";	
	}
	
	public String rto(String Suborder) {
		String scou=db.execQueryWithSingleColumnResult("select PSTCOURIERNAME  from ISMPST where PSTRFNUM = "+Suborder+ " ;");
		
		return ("	<rtoSuborderCommand> 	"
				+ "	  <commandId>bf370ae7-c8b8-437f-83bb-b24bd95a00fa</commandId>	"
				+ "	  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		"
				+ "  <dryRun>false</dryRun>		  <suborderId>"
				+ Suborder
				+ "</suborderId>		"
				+ "  <returnReason>ADIC</returnReason>		  <instructionToAccount>APAP</instructionToAccount>		"
				+ "  <instructionToVendor>HOSH</instructionToVendor>		  <remarks>san</remarks>		"
				+ "  <rtoDate>" + sdate
				+ " UTC</rtoDate>		  <courierId>"+scou+"</courierId>		"
				+ "  <awbNumber>97987987</awbNumber>	" + "	</rtoSuborderCommand>");

	}
	
	public String reversepickuprequiredforreplacement(String suborder) {
		return "<reversePickupRequiredCommand> 	"
				+ "	  <commandId>d768b641-d9fb-4a7c-8adf-e03759629a33</commandId>	"
				+ "	  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>	"
				+ "	  <dryRun>false</dryRun>		  <suborderId>"
				+ suborder
				+ "</suborderId>		"
				+ "  <remark>test</remark>		  <reason>BADQ</reason>		  <flag>REPLACE</flag>	"
				+ "	</reversePickupRequiredCommand> ";
	}

	public String reversepickuprequiredrefund(String suborder) {
		return "<reversePickupRequiredCommand> 	"
				+ "	  <commandId>d768b641-d9fb-4a7c-8adf-e03759629a33</commandId>	"
				+ "	  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>	"
				+ "	  <dryRun>false</dryRun>		  <suborderId>" + suborder
				+ "</suborderId>		"
				+ "  <remark>test</remark>		  <reason>BADQ</reason>		  <flag>REFUND</flag>	"
				+ "	</reversePickupRequiredCommand> ";
	}

	public String reversepickuprequiredreplacement(String suborder) {
		return "<reversePickupRequiredCommand> 	"
				+ "	  <commandId>d768b641-d9fb-4a7c-8adf-e03759629a33</commandId>	"
				+ "	  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>	"
				+ "	  <dryRun>false</dryRun>		  <suborderId>" + suborder
				+ "</suborderId>		"
				+ "  <remark>test</remark>		  <reason>BADQ</reason>		  <flag>REPLACE</flag>	"
				+ "	</reversePickupRequiredCommand> ";
	}
	public String reversepickupdone(String Suborder) {
		String scou=db.execQueryWithSingleColumnResult("select PSTCOURIERNAME  from ISMPST where PSTRFNUM = "+Suborder+ " ;");
		return "<reversePickedUpByCourierSuborderCommand>  <commandId>ec9724b4-d5f3-4a68-b3aa-834130a27304</commandId>  <userId>1</userId>  <commandSource>SUBORDER</commandSource>  <dryRun>false</dryRun>  <suborderId>"+Suborder+"</suborderId>  <remark>Automation</remark>  <courierId>"+scou+"</courierId>  <awbNumber>77777777</awbNumber>  <reversePickupDoneDate>"+sdate+"</reversePickupDoneDate></reversePickedUpByCourierSuborderCommand>";
		}

	public String replacementinitiated(String Suborder) {

		String pstpilrfnum=	db.execQueryWithSingleColumnResult("select PSTPILRFNUM from ISMPST where PSTRFNUM = "+Suborder+" ; ") ;
		
		
		return "<replacementSuborderCommand>   <commandId>9023bef8-deca-4d3b-b9b6-8c3139ab840f</commandId>	  <userId>1</userId>	  <commandSource>SUBORDER</commandSource> 		  <dryRun>false</dryRun> 		  <suborderId>"
				+ Suborder
				+ "</suborderId>		  <itemId>"+pstpilrfnum+"</itemId>		  <replacementType>1</replacementType>		  <address>Automation script</address>		  <addressSecond>Automation script</addressSecond>		  <city>noida</city>		  <country>India</country>		  <email>santhoshhomeshop18@gmail.com</email>		  <emailSecondary>santhoshhomeshop18@gmail.com</emailSecondary>		  <fax>01204242415</fax>		  <firstName>San</firstName>		  <lastName>San</lastName>		  <middleName>l</middleName>		  <mobile>9611437246</mobile>		  <phone>9611437246</phone>		  <zip>201301</zip>		</replacementSuborderCommand>";

	}


	
	
 	public String reversepickupdelivered(String Suborder) {

		return "<reversePickupDeliveredSuborderCommand> 		  <commandId>f025ce65-5af0-4d55-b1c2-df194e21c513</commandId> 		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"				+ Suborder				+ "</suborderId>		  <deliveredDate>"+sdate+"</deliveredDate>		  <recievedBy>santhosh</recievedBy>		  <awbNumber>9999999</awbNumber>		  <courierName>Aramex</courierName>		  <remark>done</remark>		</reversePickupDeliveredSuborderCommand>";	}

	public String lostintransit(String Suborder) {

		return "<lostInTransitSuborderCommand>		  <commandId>fa0572ef-2ef7-4bf7-9293-d443ea5384ee</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"+Suborder+"</suborderId>		  <cancelReason>LDBC</cancelReason>		  <cancelInstructionAccounts>APAP</cancelInstructionAccounts>		  <cancelInstructionCustomer>CWNO</cancelInstructionCustomer>		  <remark>san</remark>		</lostInTransitSuborderCommand>";
	}
	
	public String updatesubordercommand(String Suborder)
	{
	return	"<updateCourierSuborderCommand>		  <commandId>73ed0617-ef42-4b8e-a301-1d3ed87c018a</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"+Suborder+"</suborderId>		  <courierId>500012</courierId>		  <branchCode>897896</branchCode>		  <remark>santhosh</remark>		</updateCourierSuborderCommand>";
	}

	public String returnbycustomer(String Suborder) {

		return "<rbcSuborderCommand> 		  <commandId>a48044d6-4cda-41a5-b942-7c31dcd802f0</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"
				+ Suborder
				+ "</suborderId>		  <returnReason>BADQ</returnReason>		  <remark>1234ASDF</remark>		  <awbNumber>123456789</awbNumber>		  <courierName>Aramex</courierName>		</rbcSuborderCommand>";
	}

	public String requestforreplacement(String Suborder) {

		return "<requestForReplacementSuborderCommand> 		  <commandId>3464791e-af3b-4dc1-a051-bb5c5a1b0133</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"
				+ Suborder
				+ "</suborderId>		  <replacementReason>ADIC</replacementReason>		  <replacementInstruction>REPT</replacementInstruction>		  <remark>done</remark>		</requestForReplacementSuborderCommand>	";

	}

	public String reshipped(String Suborder) {
		return "<reShippedSuborderCommand>		  <commandId>57a7ebf2-fae0-435e-b070-685fde73ee9a</commandId>		"
				+ "  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>	"
				+ "	  <dryRun>false</dryRun>		  <suborderId>"
				+ Suborder
				+ "</suborderId>		  <courierId>500003</courierId>		"
				+ "  <shipDate>"+sdate2+"</shipDate>		  <awbNumber>APRIL201407001</awbNumber>		"
				+ "  <remark>done</remark>		</reShippedSuborderCommand>";
	}

	public String salesreturn(String Suborder) {
		return "<returnSuborderCommand>		  <commandId>2f9fadf7-ccab-4bea-aa98-370ca695e603</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"+Suborder+"</suborderId>		  <salesReturnReason>RIBC</salesReturnReason>		  <instructionToAccount>APAP</instructionToAccount>		  <instructionToVendor>PRSH</instructionToVendor>		  <remarks>sannn</remarks>		</returnSuborderCommand>";
		}

	public String refunded(String Suborder) {
		return "<refundedSuborderCommand> 	  <commandId>120f007b-0379-438e-a618-713577843104</commandId>	  <userId>1</userId>	  <commandSource>"
				+ Suborder
				+ "</commandSource>	  <dryRun>false</dryRun>	  <suborderId>"+Suborder+"</suborderId>	  <refundedReason>APCR</refundedReason>	  <refundInstructionsAccounts>APAP</refundInstructionsAccounts>	  <refundReferenceNum>done</refundReferenceNum>	  <remark>done</remark>	</refundedSuborderCommand>";
	}

	public String smartcancelled(String Suborder) {
		return "<smartCancelledSuborderCommand> 		  <commandId>ff978f53-c828-4c6d-a520-f430ecd44408</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"
				+ Suborder
				+ "</suborderId>		  <reason>APCR</reason>		  <instructionToAccounts>APAP</instructionToAccounts>		  <instructionToCSR>BDPD</instructionToCSR>		  <remark>done</remark>		  <forcedCancellation>false</forcedCancellation>		</smartCancelledSuborderCommand> ";
	}
	public String smartcancelled1(String Suborder) {
		return "<smartCancelledSuborderCommand> 		  <commandId>ff978f53-c828-4c6d-a520-f430ecd44408</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"
				+ Suborder
				+ "</suborderId>		  <reason>APCR</reason>		  <instructionToAccounts>APAP</instructionToAccounts>		  <instructionToCSR>HHDH</instructionToCSR>		  <remark>done</remark>		  <forcedCancellation>false</forcedCancellation>		</smartCancelledSuborderCommand> ";
	}

	public String disputeraised(String Suborder ){
		return "<disputeRaisedSuborderCommand>		  <commandId>11d53354-abd4-49c1-b62d-f9993d5a6fb5</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"+Suborder+"</suborderId>		  <reason>REB</reason>		  <remark>sann</remark>		</disputeRaisedSuborderCommand>";
	}

	public String orderreturnedtovendor(String Suborder){
		
		return "<orderReturnToVendorSuborderCommand>		  <commandId>476a3ac1-e811-420e-bd59-a1c2a8382bd2</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"+Suborder+"</suborderId>		  <ondReason>ACNF</ondReason>		  <instructionToAccount>APAP</instructionToAccount>		  <instructionToVendor>PRSH</instructionToVendor>		  <remarks>Automation Script</remarks>		  <podNumber>87987897</podNumber>		</orderReturnToVendorSuborderCommand>";	}
	

	public String refundchqdispatched(String Suborder)
	{
		return "<refundChequeDispatchedSuborderCommand>		"
				+ "  <commandId>8abc2d7a-d9a8-4789-9d12-76e73bd2ef45</commandId>	"
				+ "	  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>	"
				+ "	  <dryRun>false</dryRun>		  <suborderId>"+Suborder+"</suborderId>		"
						+ "  <awbNumber>87686</awbNumber>		  <shipmentDate>"+sdate+"</shipmentDate>	"
								+ "	  <courierId>500012</courierId>		  <remark>Automation script</remark>	"
								+ "	</refundChequeDispatchedSuborderCommand>";
	}
	
	public String orderreturntovendoradjustment(String Suborder)
	{
		return "<ortvAdjustmentSuborderCommand> "
 + "<commandId>dd6e135b-d4d9-4931-b2e1-9f9fa9c67f74</commandId> "
				+" <userId>1</userId> "
 +"<commandSource>SUBORDER</commandSource> "
				+" <dryRun>false</dryRun> "
 +"<suborderId>"+Suborder+"</suborderId> "
 +" <rtvAdjustmentReason>CRAH</rtvAdjustmentReason> "
 +"<instructionToAccounts>CRCM</instructionToAccounts> "
 +"<deliveredDate>"+sdate+"</deliveredDate> "
 +"<receivedBy>done</receivedBy> "
 +"<remark>55555</remark> "
 +"</ortvAdjustmentSuborderCommand> ";
	}
	
	public String refundchqprepared(String Suborder)
	{
		String PSTPAYABLEPRICE =  db.execQueryWithSingleColumnResult("select PSTPAYABLEPRICE from ISMPST where PSTRFNUM = "+Suborder+" ; ") ;

		return "<refundChequePreparedSuborderCommand>		  <commandId>e959a7b9-3d56-4ce4-8573-96073f9cfbaf</commandId>		  <userId>1</userId>		  <commandSource>SUBORDER</commandSource>		  <dryRun>false</dryRun>		  <suborderId>"+Suborder+"</suborderId>		  <chequeNumber>88877979</chequeNumber>		  <chequeDate>"+sdate+"</chequeDate>		  <chequeAmount>"+PSTPAYABLEPRICE+"</chequeAmount>		  <bankName>Automation Bankname</bankName>		  <remark>Automation remarks</remark>		</refundChequePreparedSuborderCommand>";	
	}

  public String gatepassed(String Suborder)
  {
	return   "<gatePassSuborderCommand>	  <commandId>daec71e9-22f0-4266-a9fd-6e494610d5b1</commandId>	  <userId>1</userId>	  <commandSource>SUBORDER</commandSource>	  <dryRun>false</dryRun>	  <suborderId>"+Suborder+"</suborderId>	</gatePassSuborderCommand>";  }

public String rtodelivered(String Suborder)
{
	String scou=db.execQueryWithSingleColumnResult("select PSTCOURIERNAME  from ISMPST where PSTRFNUM = "+Suborder+ " ;");
	return "<rtoDeliveredSuborderCommand> 	  <commandId>3b1c4b1a-5eb7-4a3d-92f7-85d18fc4da23</commandId>	  <userId>1</userId>	  <commandSource>SUBORDER</commandSource>	  <dryRun>false</dryRun>	  <suborderId>"+Suborder+"</suborderId>	  <receivedBy>Santhosh</receivedBy>	  <rtoDeliveredDate>"+sdate+"</rtoDeliveredDate>	  <awbNumber>8888787</awbNumber>	  <remark>santhosh</remark>	  <courierId>"+scou+"</courierId>	</rtoDeliveredSuborderCommand>";}




public String ChequeReceivedOrderCommand(String Suborder)
{
	String potrfnum=	db.execQueryWithSingleColumnResult("SELECT PSTPOTRFNUM from ISMPST where PSTRFNUM = "+Suborder+" ; ") ;
	String potnetcartprice =  db.execQueryWithSingleColumnResult("select POTNETCARTPRICE from ISMPOT where POTRFNUM = "+potrfnum+" ; ") ;
	
	return  "<chequeReceivedOrderCommand>	  <commandId>50769282-59cb-42a1-bed2-b9becc85149a</commandId>	  <userId>1</userId>	  <commandSource>ORDER</commandSource>	  <dryRun>false</dryRun>	  <orderId>"+potrfnum+"</orderId>	  <chequeNumber>946433554</chequeNumber>	  <chequeDate>"+sdate2+"</chequeDate>	  <chequeAmount>"+potnetcartprice+"</chequeAmount>	  <bankName>946433554</bankName>	  <branchName>946433554</branchName>	  <remark>Automated</remark>	  <desiredState>PAYMENT_RECEIVED</desiredState>	</chequeReceivedOrderCommand>";
}


public String ShortShipmentDistpatchSuborderCommand(String Suborder)
{
	return "<shortShipmentDistpatchSuborderCommand>	  <commandId>9270b85d-472d-4c7d-b368-a6efa6de92ec</commandId>	  <userId>1</userId>	  <commandSource>SUBORDER</commandSource>	  <dryRun>false</dryRun>	  <suborderId>"+Suborder+"</suborderId>	  <shippedDate>"+sdate+"</shippedDate>	  <awbNumber>123123</awbNumber>	  <courierId>500002</courierId>	  <remark>Automated state change</remark>	</shortShipmentDistpatchSuborderCommand>";
}

public String requestForShortShipmentSuborderCommand (String suborder){
return "<requestForShortShipmentSuborderCommand><commandId>57374493-ca8b-4f25-a101-fab24de614d5</commandId><userId>1</userId><commandSource>SUBORDER</commandSource><dryRun>false</dryRun><suborderId>"+suborder+"</suborderId><weight>100.0</weight><remark>san</remark><reason>SSDE</reason></requestForShortShipmentSuborderCommand>";
}
}
