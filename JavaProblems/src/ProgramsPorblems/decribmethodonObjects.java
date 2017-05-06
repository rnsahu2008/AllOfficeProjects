package ProgramsPorblems;

import java.awt.List;
import java.util.ArrayList;

import org.seleniumhq.jetty9.security.MappedLoginService.Anonymous;

public class decribmethodonObjects
{
public void call()
{
	
System.out.println("Tets");}


public static void main(String[] args) 
{
	
	decribmethodonObjects b = new decribmethodonObjects(){public void call()	{
			
		System.out.println("Tets1");}

		
	};
	b.call();
	
	ArrayList<Integer> ls = new ArrayList<>();
	
	for(int i=0; i<2;i++)
	{
		
		ls.add(i);
	}
	
	ls.parallelStream().forEach(System.out::println);
}
	
	
}