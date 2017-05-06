package FileHandling;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class pojoJaxb {
	
	public void unmarshal() throws JAXBException
	{
		JAXBContext jaxb = JAXBContext.newInstance(Mynames.class);
		Unmarshaller ums = jaxb.createUnmarshaller();
		Mynames p = (Mynames)ums.unmarshal(new File("D:\\AAAAAAA\\Name.xml"));
		System.out.println(p.getnames());
		

}
	public static void main(String[] args) throws JAXBException {
		pojoJaxb pj = new pojoJaxb();
		pj.unmarshal();
		

	}

	
}