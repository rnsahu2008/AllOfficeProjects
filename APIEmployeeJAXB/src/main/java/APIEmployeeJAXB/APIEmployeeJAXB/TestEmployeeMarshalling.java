package APIEmployeeJAXB.APIEmployeeJAXB;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;


public class TestEmployeeMarshalling 
{
	 AllEmployee allemployees = new AllEmployee();

	public  void setAlldata()
	{

		allemployees.setemployees(new ArrayList<Employee>());
		
		Employee emp= new Employee();
		emp.setId(1);
		emp.setFname("Lokesh");
		emp.setLname("Gupta");
		emp.setIncome(100);
					
		Employee emp1= new Employee();
		emp1.setId(1);
		emp1.setFname("Lokesh");
		emp1.setLname("Gupta");
		emp1.setIncome(100);
					
		
		allemployees.getemployees().add(emp1);
		allemployees.getemployees().add(emp);
	}
	
	public static void main(String[] args) throws JAXBException 
	{
		TestEmployeeMarshalling msh = new TestEmployeeMarshalling();
		msh.setAlldata();
		msh.marshalingExample();
		
		//marshalingExample();
		System.out.println("************************************************");
		
	}

	
	private  void marshalingExample() throws JAXBException {
		
		/*JAXBContext class provides the client's entry point to the
		JAXB API. It provides an abstraction for managing the XML/Java 
		binding information necessary to implement the JAXB binding framework operations: unmarshal, marshal and validate.

*/
		
		JAXBContext jaxbContext = JAXBContext.newInstance(AllEmployee.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
 
	//	 jaxbMarshaller.marshal(allemployees, System.out);
		 
		 StringWriter writer = new StringWriter();
			jaxbMarshaller.marshal(allemployees, writer);
			System.out.println(writer);
		/*	for (int i = 0; i < num; i++) {
		//System.out.println(employees);
		jaxbMarshaller.marshal(allemployees, new File("C:\\temp\\employees1.xml"));
				//+ " C:/temp/employees.xml"));
*/	
	}		
	public void postRequestOms(StringWriter writer, int i) throws Exception {
					
		@SuppressWarnings("deprecation")
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost  postrequest = new HttpPost("http://app04.preprod.hs18.lan:7004/api/1/oms/executeOmsCommand");
		postrequest.addHeader("content-type", "application/xml");
		postrequest.addHeader("accept", "application/json");
		//StringEntity is the raw data that you send in the request.
		//200 OK,404The URI requested is invalid or the resource requested,4** means
		//problem in request,Autorizatin issue or Refused to access
		//5** Gateway issue
		//500 something is broken
		StringEntity entity = new StringEntity(writer.getBuffer().toString());
		postrequest.setEntity(entity);
		
		HttpResponse response = httpClient.execute(postrequest);
		int statusCode = response.getStatusLine().getStatusCode();
		ResponseHandler<String> handler = new BasicResponseHandler();
		String output = handler.handleResponse(response);

	}
	}
