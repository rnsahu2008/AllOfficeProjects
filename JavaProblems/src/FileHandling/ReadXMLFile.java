package FileHandling;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class ReadXMLFile {

  public static void main(String argv[]) {

    try {
    	

	File xmlfile = new File("D:\\AAAAAAA\\company.xml");
	DocumentBuilderFactory dcbf= DocumentBuilderFactory.newInstance();
	DocumentBuilder dcb=dcbf.newDocumentBuilder();
	Document doc =dcb.parse(xmlfile);
	//doc.getDocumentElement().normalize();

	NodeList nlist = doc.getElementsByTagName("staff");
	
	//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());


	for (int i=0;i<nlist.getLength();i++)
	{
Node nNode=nlist.item(i);
System.out.println("\nCurrent Element :" + nNode.getNodeName());

	//	if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;

			System.out.println("Staff id : " + eElement.getElementsByTagName("id"));
			System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
			System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
			System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
			System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());

		}
	//}
    } catch (Exception e) {
	e.printStackTrace();
    }
  }

}