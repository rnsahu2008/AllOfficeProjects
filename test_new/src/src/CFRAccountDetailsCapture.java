package src;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CFRAccountDetailsCapture extends Base {

	@Test
	public void login() throws IOException, InterruptedException
	{
		type(By.id("form1:login"),"sandy");
		type(By.name("form1:password"), "hsnsandy");
		click(By.id("form1:j_id_p"));	
		Thread.sleep(2000);
	}
	@Test(dataProvider="case")
	public void accountDetails(ArrayList<String> caseno) throws InterruptedException
	{
		for(int i=0;i<=caseno.size();i++)
		{
			driver.get("http://app10.preprod.hs18.lan:8081/faces/callCenter/callReceiveNEFT.xhtml?agentId=25000&caseId="+caseno.get(i)+"&callseqno=55547&callerNo=9990421145&reason=NEFT&route=O&seqNum=55547");
			waitForElementVisible(By.xpath("//div[@id='caseFormDetail:j_id_dx_content']/table/tbody/tr[2]/td[2]"));
			//Check case current status
			if(element(By.xpath("//div[@id='caseFormDetail:j_id_dx_content']/table/tbody/tr[2]/td[2]")).getText().contains("Refund to be Initiated"))
			{
				System.out.println("case already in refund activity");
			}
			else
			{
				if(element(By.xpath("//div[@id='caseFormDetail:j_id_dx_content']/table/tbody/tr[2]/td[2]")).getText().contains("New"))
				{
					click(By.id("caseFormDetail:j_id_dz_label"));
					click(By.xpath("//li[text()='Call Out Required']"));
					type(By.id("caseFormDetail:activityRemark"), "Refund");
					click(By.id("caseFormDetail:neftSubmit"));
					driver.get("http://app10.preprod.hs18.lan:8081/faces/callCenter/callReceiveNEFT.xhtml?agentId=25000&caseId="+caseno.get(i)+"&callseqno=55547&callerNo=9990421145&reason=NEFT&route=O&seqNum=55547");
				}
				
					click(By.id("caseFormDetail:j_id_dz_label"));
					click(By.xpath("//li[text()='Refund to be Initiated']"));
					Thread.sleep(2000);
					click(By.id("caseFormDetail:refundType_label"));
					click(By.xpath("//li[text()='NEFT Transfer']"));
					Thread.sleep(1000);
					waitForElementVisible(By.id("caseFormDetail:activityRemark"));
					type(By.id("caseFormDetail:activityRemark"), "Refund");
					type(By.id("caseFormDetail:accountName"), "Abhishek");
					click(By.id("caseFormDetail:accountType_label"));
					click(By.xpath("//li[text()='Savings Account']"));
					type(By.id("caseFormDetail:ifsc"), "CITI0000004");
					click(By.id("caseFormDetail:validate"));
					click(By.id("caseFormDetail:ivr"));
					soapRequest(caseno.get(i));
					click(By.id("caseFormDetail:refresh"));
					click(By.id("caseFormDetail:j_id_fw"));
					click(By.id("caseFormDetail:neftSubmit"));
			}
		}
		
		
	}
	
	public  void soapRequest(String i) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            javax.xml.soap.SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            String url = "http://srvc04.preprod.hs18.lan:10090/customerNeft";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(i), url);

            // Process the SOAP Response
            printSOAPResponse(soapResponse);

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }
    }

    private static SOAPMessage createSOAPRequest(String i) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        javax.xml.soap.SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://web.cs.homeshop18.com/";

        // SOAP Envelope
        javax.xml.soap.SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("web", serverURI);

        /*
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://web.cs.homeshop18.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <web:updateCustomerNeft>
         <callSeqNum>1</callSeqNum>
         <caseId>120385494</caseId>
         <accountNumber>12346558</accountNumber>
      </web:updateCustomerNeft>
   </soapenv:Body>
</soapenv:Envelope>
         */

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("updateCustomerNeft","web");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("callSeqNum");
        soapBodyElem1.addTextNode("1");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement( "caseId");
        soapBodyElem2.addTextNode(i);
        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement( "accountNumber");
        soapBodyElem3.addTextNode("12345678");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + "updateCustomerNeft");

        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }

    /**
     * Method used to print the SOAP Response
     */
    private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
      //  System.out.print("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }
	
    @DataProvider(name = "case")                                   // Data provider for log in 
	public Object[][] dataProviderLogin() throws IOException
	{
		testDataFile(File);
		Object[][] data = null;
		
		 data= new Object[][]{{dataProviderByRow(2, 0, 28, 0)}};
		
		closeFile();
		return data;
		
	}
}
