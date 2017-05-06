package ProgramsPorblems;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.exec.ExecuteException;

public class ThrowVsThrows {
	
	public void ThrowExpetions()
	{
		try
		{
		int i=9;
		int j=4;
		int k=i+j;
		if(k>10)
		{
			throw (new ArithmeticException());
		}
		}
		
		catch(ArithmeticException ex)
		{
			System.out.println("Value Should be Greater Than 10");
			
			
		}
		
	}

	public void ThrowsExpetions() 
	{
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter("abc242.txt");
			pw.println("hell");
		} 
		
		catch (FileNotFoundException e) 
		{
			System.out.println("File Not FOund");
		}
	

	
			}
public static void main(String[] args) throws FileNotFoundException {
	
	ThrowVsThrows obj = new ThrowVsThrows();
	//obj.ThrowExpetions();
	obj.ThrowsExpetions();
	
}
}
