package com.howtodoinjava.jaxb.examples.list;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class TestEmployeeMarshing 
{
	 Employees employees = new Employees();
	public void setdata()
	{
	//	employees.setEmployees(new Employee());
	 	Employee emp= new Employee();
		emp.setId(1);
		emp.setFirstName("Lokesh");
		emp.setLastName("Gupta");
		emp.setIncome(60);
	
		employees.setEmployees(emp);
		
			
		/*
		Employee emp2 = new Employee();
	    emp2.setId(2);
	    emp2.setFirstName("John");
	    emp2.setLastName("Mclane");
	    emp2.setIncome(200.0);

*/		
		}
	
	public static void main(String[] args) throws JAXBException 
	{
		
		TestEmployeeMarshing tm = new TestEmployeeMarshing();
		tm.setdata();
		tm.marshalingExample();
		
		System.out.println("************************************************");
	//	unMarshalingExample();
	}
/*
	private static void unMarshalingExample() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Employees emps = (Employees) jaxbUnmarshaller.unmarshal( new File("c:/temp/employees.xml") );
		
		for(Employee emp : emps.getEmployees())
		{
			System.out.println(emp.getId());
			System.out.println(emp.getFirstName());
		}
	}
*/
	private  void marshalingExample() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
 
		StringWriter writer = new StringWriter();
		jaxbMarshaller.marshal(employees, writer);
		System.out.println(writer);
		// jaxbMarshaller.marshal(employees, System.out);
		//System.out.println(employees);
	//	jaxbMarshaller.marshal(employees, new File("C:\\temp\\employees.xml"));
				//+ " C:/temp/employees.xml"));
		
	}
}
