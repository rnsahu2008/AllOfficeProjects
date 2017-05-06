package PojoCreation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBTest {

	public static void main(String args[]) {
		try {
			JAXBContext ctx=JAXBContext.newInstance(Student.class);
			
			Marshaller marshaller= ctx.createMarshaller();
			
			Student st=new Student("ram", "123", "test", 12, 1, new ArrayList<Subject>());
			
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			
			marshaller.marshal(st, out);
			
			System.out.println(new String(out.toByteArray()));
			
			// create JAXB context
			JAXBContext context = JAXBContext.newInstance();
			// Create Unmarshaller using JAXB context
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Student student = (Student) unmarshaller.unmarshal(new File(
					"D:\\Student.xml"));
			// Process the Student object
			System.out.println("Student name: " + student.getName());
			System.out.println("Student rollNo: " + student.getRollNo());
			for (Subject Subject : student.getSubjects()) {
				System.out.println("Subject Name: " + Subject.getSubjectname());
				System.out.println("Subject Id: " + Subject.getSubjectId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
