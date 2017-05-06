package ProgramsPorblems;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class VectorExample {
	
	public static void main(String[] args)
	{
		
		Vector<String> vc = new Vector<String>();
		vc.addElement("Test1");
		vc.addElement("Test2");
		vc.addElement("Test3");
		
		System.out.println(vc);
		
		Enumeration en = vc.elements();
		while(en.hasMoreElements())
		{
			System.out.println(en.nextElement());
			
		}
		
		Iterator it =vc.iterator();
		while(it.hasNext())
			
		{
			System.out.println(it.next());
			
		}
		
	}

}
