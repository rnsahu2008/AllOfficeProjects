package ProgramsPorblems;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FilnotFOund {
	
	public static void main(String[] args) throws FileNotFoundException 
	{
		
		PrintWriter pw = null;
		
			pw = new PrintWriter("abc.txt");
		
		pw.println("hell");
		try
		{
		System.out.println(10/0);
		}
		catch(ArithmeticException e)
		{
			System.out.println("Arithmetic");	
		}
		
	}

}
