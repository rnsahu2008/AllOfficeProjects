package ProgramsPorblems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class iterattor {
	
	public static void main(String[] args) 
	{
		List<String> strList = new ArrayList<>();
		strList.add("Test1");
		strList.add("Test2");
		strList.add("Test3");
		strList.add("Test4");

		//using for-each loop
		for(String obj : strList){
		    System.out.println(obj);
		}

		//using iterator
		Iterator<String> it = strList.iterator();
		while(it.hasNext()){
		    String obj = it.next();
		    System.out.println(obj);
		}

		
	}
	
	

}
