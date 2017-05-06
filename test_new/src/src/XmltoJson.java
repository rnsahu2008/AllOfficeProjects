package src;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class XmltoJson {

	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
		XmltoJson json = new XmltoJson();
		json.xmljson();

	}

	public void xmljson() throws JSONException
	{
		String xml ="<com.hs18.oms.commons.commands.suborder.VerifiedSuborderCommand>"
 + "<commandId>ba5e1a9e-6854-4360-9323-7bdada0469bc</commandId>"
  +"<userId>1</userId>"
  +"<commandSource>SUBORDER</commandSource>"
 +" <dryRun>false</dryRun>"
  +"<suborderId>41357325115</suborderId>"
  +"<vendorInstruction>PRSH</vendorInstruction>"
  +"<remark>test</remark>"
+"</com.hs18.oms.commons.commands.suborder.VerifiedSuborderCommand>";
		
		JSONObject soapDatainJsonObject = XML.toJSONObject(xml);
		
		JSONObject jsonObj =soapDatainJsonObject.getJSONObject("com.hs18.oms.commons.commands.suborder.VerifiedSuborderCommand");

		System.out.println(soapDatainJsonObject);
		String suborderId = (String) jsonObj.get("suborderId");
		System.out.println(suborderId);
	}
	
}
